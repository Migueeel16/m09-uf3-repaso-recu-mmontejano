package uf3_pvp_ex1.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.inicia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
