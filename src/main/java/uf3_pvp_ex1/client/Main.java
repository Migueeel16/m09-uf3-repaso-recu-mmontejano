package uf3_pvp_ex1.client;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Client client1 = new Client("Client 1");
        Client client2 = new Client("Client 2");
        Client client3 = new Client("Client 3");
        Client client4 = new Client("Client 4");
        Client client5 = new Client("Client 5");
        Client client6 = new Client("Client 6");
        Client client7 = new Client("Client 7");
        Client client8 = new Client("Client 8");
        Client client9 = new Client("Client 9");
        Client client10 = new Client("Client 10");
        client1.sendMessage(true, randomNumber());
        client2.sendMessage(false);
        client3.sendMessage(true, randomNumber());
        client4.sendMessage(false);
        client5.sendMessage(true, randomNumber());
        client6.sendMessage(true, randomNumber());
        client7.sendMessage(true, randomNumber());
        client8.sendMessage(true, randomNumber());
        client9.sendMessage(true, randomNumber());
        client10.sendMessage(true, randomNumber());

    }

    public static int randomNumber () {
        Random random = new Random();
        return random.nextInt(5 - 1 + 1) + 1;
    }
}
