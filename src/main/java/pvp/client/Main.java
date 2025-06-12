package pvp.client;

public class Main {
    public static void main(String[] args) {
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        Client client4 = new Client();
        client1.sendMessage("Client1");
        client2.sendMessage("Client2");
        client3.sendMessage("Client3");
        client4.sendMessage("Client4");

    }
}
