package it.polimi.se2018.network.server;


import it.polimi.se2018.commands.client_to_server_command.ClientToServerCommand;
import it.polimi.se2018.commands.server_to_client_command.NewConnectedPlayerNotification;
import it.polimi.se2018.commands.server_to_client_command.PingConnectionTester;
import it.polimi.se2018.network.client.ClientConnection;
import it.polimi.se2018.network.client.rmi.RMIClientInterface;
import it.polimi.se2018.network.server.rmi.RMIServer;
import it.polimi.se2018.network.server.rmi.RMIVirtualClient;
import it.polimi.se2018.network.server.socket.SocketServer;
import it.polimi.se2018.network.server.socket.SocketVirtualClient;
import it.polimi.se2018.commands.server_to_client_command.AuthenticatedCorrectlyCommand;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Server {

    /**
     * Clients that are waiting for a game to start and saved by username
     */
    private static ArrayList<String> waitingClients = new ArrayList<>();
    /**
     * Clients connected with their own username and ClientConnection:
     * ClientConnection is the reference that the server has to contact them
     * These clients could both be in a game or be waiting for a game to start
     */
    private static HashMap<String, ClientConnection> connectedClients = new HashMap<>();

    /**
     * Clients that were in a game andd then got disconnected
     * These clients could be reinserted in a paused game when they reconnect to the server
     * Clients are saved by their unique username
     */
    private static ArrayList<String> disconnectedClients = new ArrayList<>();
    /**
     * List of active games (one Thread for each game)
     */
    private static ArrayList<Controller> activeGames = new ArrayList<>();
    /**
     * Map used to pass a command coming from the network to the right controller (the right game) to manage it
     */
    private static HashMap<String, VirtualView> userMap = new HashMap<>();

    private static Timer timer;

    public static void main(String[] args) {

        boolean activeServer = true;
        //pubblica RMI impl server side
        new RMIServer().RMIStartListening();
        System.out.println("Listening RMI");
        //listen socket connections
        new SocketServer().socketStartListening();
        System.out.println("Listening Socket");


        //TODO CHECK
        new Thread(() -> {
            while (activeServer) {
                for (String user : connectedClients.keySet()) {
                    connectedClients.get(user).notifyClient(new PingConnectionTester());  //Checking if still connected(for RMI)
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();

    }
    public static void handle(ClientToServerCommand command){
        String username = command.getUsername();
        userMap.get(username).notify(command);
    }


    /**
     * New connections are automatically added to waitingClients because they haven't chosen a username yet.
     * They are later moved from waitingClients to connnectedClients when they are connected with their user.
     * @param client
     */
    public static void addClientInterface(RMIClientInterface client, String username){

        //Create reference to RMIClient
        RMIVirtualClient vc = new RMIVirtualClient(client);

        /*
        Three cases:
        1) Attempt to reconnect after disconnection
        2) Correct login request. view is added to waitingClients (waiting for a game to start)
        3) Attempt to connect with a user already taken, the server choose a similar valid one and notifies it to the view
        Every time, a response is sent back to notify success or failure
         */
        if (disconnectedClients.contains(username)){
            disconnectedClients.remove(username);
            connectedClients.put(username, vc);
            vc.notifyClient(new AuthenticatedCorrectlyCommand(username));
        } else if(!connectedClients.containsKey(username)){
            connectedClients.put(username, vc);
            addToWaitingClients(username);
            vc.notifyClient(new AuthenticatedCorrectlyCommand(username));
        } else {
            Integer i = 1;
            while (true){
                String newUser = username + i.toString();
                if(!connectedClients.containsKey(newUser)){
                    connectedClients.put(newUser, vc);
                    addToWaitingClients(newUser);
                    vc.notifyClient(new AuthenticatedCorrectlyCommand(newUser));
                    return;
                }
                i++;
            }
        }

    }

    private static void notifyNewConnectedPlayer(String username){
        for (String connectionReference : waitingClients){
            if (!waitingClients.equals(username)) {
                getConnectedClients().get(connectionReference).notifyClient(new NewConnectedPlayerNotification(username));
            }
        }

    }

    public static void addClientInterface(Socket socket, ObjectInputStream input, ObjectOutputStream output, String username){

        //Create reference to Socket view

        SocketVirtualClient vc = new SocketVirtualClient(socket, input, output);

        /*
        Three cases:
        1) Attempt to reconnect after disconnection
        2) Correct login request. view is added to waitingClients (waiting for a game to start)
        3) Attempt to connect with a user already taken, the server choose a similar valid one and notifies it to the view
        Every time, a response is sent back to notify success or failure
         */
        System.out.println("Entro in addClientSocket");
        if (disconnectedClients.contains(username)){
            disconnectedClients.remove(username);
            connectedClients.put(username, vc);
            vc.start();
            vc.notifyClient(new AuthenticatedCorrectlyCommand(username));
        }  else if (!connectedClients.containsKey(username)){
            connectedClients.put(username, vc);
            System.out.println("prima di inviare AuthCommand");
            vc.notifyClient(new AuthenticatedCorrectlyCommand(username));
            vc.start();
            addToWaitingClients(username);
        } else {
            Integer i = 1;
            while (true){
                String newUser = username + i.toString();
                if(!connectedClients.containsKey(newUser)){
                    connectedClients.put(newUser, vc);
                    vc.notifyClient(new AuthenticatedCorrectlyCommand(newUser));
                    vc.start();
                    addToWaitingClients(newUser);
                    return;
                }
                i++;
            }
        }

    }

    /**
     * Remove any reference of a given player from the server
     * @param username
     */
    public static void removeClient(String username){
        if (connectedClients.containsKey(username)){
            connectedClients.remove(username);
        }
        if (waitingClients.contains(username)){
            waitingClients.remove(username);
        }
        if (disconnectedClients.contains(username)){
            disconnectedClients.remove(username);
        }
    }

    /**
     * Disconnect a player from the server: his username is saved in disconnectedClients in case he will reconnect
     * @param username
     */
    public static void disconnectClient(String username){
        if (waitingClients.contains(username)) { //Covers the case in which a player is connected but isn't in a started game
            removeClient(username);
            if (waitingClients.size() <= 1) {
                timer.cancel();
            }
            return;
        }
        if(connectedClients.containsKey(username)){
            connectedClients.remove(username);
            disconnectedClients.add(username);
            //TODO Ale: controllo che ci siano abbastanza giocatori nella partita
            // 1)- Cerco il controller associato all'username del view disconnesso
            // 2) una volta trovato, controllo se ha almeno 2 giocatori nell'Hashmap dei ConnectedClients
            // 3) Se ne ha almeno 2, no action
            // 4) se ne ha uno soltato, proclamo vincitore l'ultimo rimasto

            System.out.println("client " + username + " disconnected");
        } else {
            System.out.println("Could not found such view");
        }
    }

    public synchronized static void addToWaitingClients(String username){ //TODO Gestire la concorrenza: se vengono addati insieme 6 view, faccio partire un timer e l'altro per i 2 player rimanenti?
        waitingClients.add(username);
        System.out.println("Addato "+ username);
        notifyNewConnectedPlayer(username);
        if (waitingClients.size() == 2){
            System.out.println("Starting timer");
            timer = new Timer(); //TODO gestire il caso di più partite in attesa: devo avere degli ARRAY di timer in quel caso! e sapere quali sono attivi
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            // Inutile -> itsTimeToStart = true;  //TODO a cosa serve questa variabile? startNewGame() deve essere controllato da questa variabile? deve essere messo in un thread quindi?
                            System.out.println("Time expired");
                            //TODO: check all players from RMI are connected -> Ping
                            //wait for 1s (time to the ping to be sent)

                            //if arrives here, the client connected are more than 1 (if not, disconnectPlayers can cancel the timer
                            startNewGame(); //DA TOGLIERE, l'ho utilizzato solo come prova. il metodo deve essere contorllato dalla variabile itsTimeToStart
                        }
                    },
                    6000 //TODO import from file
            );
        }
        if (waitingClients.size()==4){
            startNewGame();
        }
    }

    public static synchronized void startNewGame(){
        //when a game starts, timer is cancelled
        timer.cancel();
        ArrayList<String> players = new ArrayList<>();

        for(int i = 0; i < waitingClients.size() || i < 4; i ++){
            try {
                players.add(waitingClients.get(i));
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        for(String user : players){
            waitingClients.remove(user);
        }

        Controller controller = new Controller(players);
        activeGames.add(controller);
    }

    public static void requestRefreshBoard(String username){
        for (Controller game : activeGames){
            for (String user : game.getUserViewMap().keySet()){
                if (username.equals(user)){
                    game.getModel().notifyRefreshBoard(); //TODO forse possibile indirizzarla ad solo a un player, che la richiede
                    return;
                }
            }
        }
    }

    public static void updateDisconnectedUser(String username){
        for (Controller game : activeGames){
            for (String user : game.getUserViewMap().keySet()){
                if (username.equals(user)){ //found the right controller
                    for (String usernameToNotify : game.getUserViewMap().keySet())
                        if (!username.equals(usernameToNotify))
                            game.getUserViewMap().get(user).playerDisconnection(username);
                    return;
                }
            }
        }
    }

    public static ArrayList<String> getWaitingClients(){
        return waitingClients;
    }

    public static HashMap<String, ClientConnection> getConnectedClients() {
        return connectedClients;
    }

    public static ArrayList<String> getDisconnectedClients() {
        return disconnectedClients;
    }

    public static ArrayList<Controller> getActiveGames() {
        return activeGames;
    }

    public static HashMap<String, VirtualView> getUserMap() {
        return userMap;
    }

}