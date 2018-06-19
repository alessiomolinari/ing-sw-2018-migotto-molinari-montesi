package it.polimi.se2018.commands.server_to_client_command;

import it.polimi.se2018.utils.ControllerClientInterface;

import java.util.ArrayList;

public class RefreshWpcCommand extends ServerToClientCommand {


    private ArrayList<String> personalWpc; //Dice in the format: colorNumber/empty

    private ArrayList<ArrayList<String>> otherPlayersWpcs; //Dice in the format colorNumber/empty or restrictionColor or restrictionValue

    public ArrayList<String> getPersonalWpc() {
        return personalWpc;
    }

    public ArrayList<ArrayList<String>> getOtherPlayersWpcs() {
        return otherPlayersWpcs;
    }

    public RefreshWpcCommand(ArrayList<String> personalWpc, ArrayList<ArrayList<String>> otherPlayersWpcs) {

        this.personalWpc = personalWpc;
        this.otherPlayersWpcs = otherPlayersWpcs;
    }

    /**
     * Visitor methods, it calls the clientController to perform a move using dynamic binding
     * @param clientController the parameters who calls the dynamic method
     */
    public void visit(ControllerClientInterface clientController) {
        clientController.applyCommand(this);
    }

}