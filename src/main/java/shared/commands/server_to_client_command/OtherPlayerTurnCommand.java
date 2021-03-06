package shared.commands.server_to_client_command;

import shared.utils.ControllerClientInterface;

public class OtherPlayerTurnCommand extends ServerToClientCommand {
    private String username;

    /**
     * Notifies all the other users that is one player's turn
     * @param username player's username
     */
    public OtherPlayerTurnCommand(String username) {
        this.username = username;
    }

    @Override
    public void visit(ControllerClientInterface clientController) {
        clientController.applyCommand(this);
    }

    public String getUsername() {
        return username;
    }
}
