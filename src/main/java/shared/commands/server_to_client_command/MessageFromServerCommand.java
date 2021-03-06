package shared.commands.server_to_client_command;

import shared.utils.ControllerClientInterface;

public class MessageFromServerCommand extends ServerToClientCommand {

    /**
     * Contains a generic message from the controller
     * @param message message to show
     */
    public MessageFromServerCommand(String message) {
        this.message=message;
    }

    public void visit(ControllerClientInterface clientController) {
        clientController.applyCommand(this);
    }
}
