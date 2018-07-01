package it.polimi.se2018.commands.server_to_client_command;

import it.polimi.se2018.commands.server_to_client_command.ServerToClientCommand;
import it.polimi.se2018.utils.ControllerClientInterface;

public class AskDieValue extends ServerToClientCommand {

    public void visit(ControllerClientInterface clientController) {
        clientController.applyCommand(this);
    }

}