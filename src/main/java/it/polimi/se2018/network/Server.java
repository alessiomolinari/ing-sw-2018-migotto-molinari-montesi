package it.polimi.se2018.network;

import it.polimi.se2018.MVC.Controller;
import it.polimi.se2018.Model;
import it.polimi.se2018.network.rmi.RMIListener;
import it.polimi.se2018.network.socket.SocketListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {

    /**
     * Class for the server.
     * It is the core of the network and contain all the links for ServerSocket / ServerRmi accepters
     */

    ArrayList<ServerConnection> waitingServerConnections = new ArrayList<>();

    /**
     * Connections that has incorrect usernames (not in the db/ not uniques ) can't play
     */
    ArrayList<ServerConnection> incorrectConnections = new ArrayList<>();



    Controller controller;

    private int port;

    private SocketListener socketListener;
    private RMIListener rmiListener;

    private static Server instance = null;

    private BufferedReader bufferedReader;

    //True if server is operative
    private boolean operative;

    private Server(int port) {
        this.port = port;
        socketListener = new SocketListener(this, 1111);
    }

    /**
     * Gets the single instance of Server.
     *
     * @return single instance of Server
     */
    public static synchronized Server getInstance() {
        if ( instance == null) {
            return new Server(1111); //TODO cambia
        } else
            return instance;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        startServer();
    }

    /**
     * Start server.
     */
    private void startServer() {

        socketListener = new SocketListener(this, port);
        socketListener.run();
        //rmiListener = new ServerRMIListener(this);

        while (operative) {
            String input = null;
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
            }

            if ("M".equals(input) || "m".equals(input)) {
                operative = true;
            }
        }

    }

    public synchronized void addConnection(ServerConnection connection) {

        // Iniziale per capire come va implementato il controllo username
        incorrectConnections.add(connection);

         if (waitingServerConnections.size()==2){
             ;//runMatch();
         }

    }

    public static void main(String args[]) {
        Server s = new Server(1000);

    }
}
/*
    private boolean disconnectedClientInMatch() {
        for (MatchHandler match : createdMatches) {
            if (match.hasDisconnectedPlayer()){
                return true;
            }
        }

        return false;
    }

    public synchronized boolean checkWaitingList() {
        return waitingClients.size() >= NetworkConstants.MINPLAYERS;

    }

    public synchronized void timerExpired() {
        if (checkWaitingList())
            createMatch();
    }

    private void startInitialTimer() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FileConstants.INITIAL_TIME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int timeMillis = 0;
        try {
            timeMillis = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer = new Thread(new InitialTimer(this, timeMillis));
        timer.start();
    }




}
*/
