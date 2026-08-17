package net.ada;

import net.ada.api.impl.ClientAPI;

public class Client {
    public static Client INSTANCE;

    ClientAPI clientAPI;
    public Client() {
        if (INSTANCE == null) {
            INSTANCE = this;
        }
        else {
            // TODO logger error!!!
        }
        clientAPI = new ClientAPI();
    }

    public ClientAPI getClientAPI() {
        return clientAPI;
    }
}
