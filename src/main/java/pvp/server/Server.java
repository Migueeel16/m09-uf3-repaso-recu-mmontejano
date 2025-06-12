package pvp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private final int PORT = 65000;
    private String messagesChain = "";
    private boolean isProcessing = false;

    public void inicia() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Iniciat servidor al port " + serverSocket.getLocalPort());

        while (true) {
            Socket socket = serverSocket.accept();
            try {
                Manager manager = new Manager(socket, this);
                new Thread(manager).start();
            }catch (IOException e){
                System.out.println("Error creant gestor pel client ");
            }
        }
    }

    public synchronized void addStringToMessageChain(String message) {
        while (isProcessing) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        isProcessing = true;

        System.out.println("Adding new message: " + message);
        messagesChain += "|" + message;

        isProcessing = false;
        this.notifyAll();
    }

    public synchronized String getMessageChain() {
        while (isProcessing) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return messagesChain;
            }
        }

        isProcessing = true;

        String chain = messagesChain;

        isProcessing = false;
        notifyAll();

        return chain;
    }
}
