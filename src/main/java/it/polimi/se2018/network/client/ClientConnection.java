package it.polimi.se2018.network.client;


import it.polimi.se2018.server_to_client_command.ServerToClientCommand;

public interface ClientConnection {
    void notifyClient(ServerToClientCommand command);
}
