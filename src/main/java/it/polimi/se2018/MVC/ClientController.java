package it.polimi.se2018.MVC;

import com.sun.deploy.util.SessionState;
import it.polimi.se2018.network.SocketClientConnecter;

public class ClientController {

    /**
     * Il controller che viene in contatto con la connessione (Socket o RMI)
     *
     * Ha un duplice scopo:
     * Da una parte, presenta tutti i metodi che chiamano apply ( ServerToClientCommand ...)
     * per binding dinamico, verranno acceduti i metodi corretti.
     *
     * Dall'altra, si occupa di inviare i comandi del tipo ClientToServerCommand alla connessione
     *
     *
     */

    // TODO CALLED: CLientCommandHandler

    private View userInterface;

    /** The network interface. */
    private ClientConnecter clientConnecterInterface;



    // Main method for sending commands to Server

    public void sendCommand(ClientToServerCommand command) {
        try {
            clientConnecterInterface.sendCommand(command);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    /**
     * Second role of the ClientController is to receive commands from Server and apply them (to Client)
     *
     * I have multiple methods called applyCommand with a different command as parameter
     * Through Binding, java will execute the correct one
     * Those methods interact with userInterface (the View)
     * @param invalidCommand
     */

    public void applyCommand(InvalidCommand invalidCommand) {
        userInterface.commandNotValid();

    }

    /**
     * Apply command.
     *
     * @param startTurnCommand the start turn command
     */
    public void applyCommand(StartTurnCommand startTurnCommand) {
        userInterface.startTurn();

    }

    /**
     * Apply command.
     *
     * @param closeConnectionCommand the close connection command
     */
    public void applyCommand(CloseClientCommand closeConnectionCommand) {
        userInterface.notifyServerClosed();
        networkInterface.closeConnection();

    }

    /**
     * Apply command.
     *
     * @param askPrivilegeChoiceCommand the ask privilege choice command
     */
    public void applyCommand(ChosenToolCardCommand askPrivilegeChoiceCommand) {
        userInterface.AskPrivilegeChoice(askPrivilegeChoiceCommand.getNumberOfPrivilege(), askPrivilegeChoiceCommand.getPrivilegeResources());

    }

    /**
     * Apply command.
     *
     * @param initializeMatchCommand the initialize match command
     */
    public void applyCommand(InitializeMatchCommand initializeMatchCommand) {
        userInterface.initializeMatch(initializeMatchCommand.getNumPlayers());
    }

    /**
     * Apply command.
     *
     * @param winCommand the win command
     */
    public void applyCommand(WinCommand winCommand) {
        userInterface.win();

    }

    /**
     * Apply command.
     *
     * @param loseCommand the lose command
     */
    public void applyCommand(LoseCommand loseCommand) {
        userInterface.lose();

    }


}
