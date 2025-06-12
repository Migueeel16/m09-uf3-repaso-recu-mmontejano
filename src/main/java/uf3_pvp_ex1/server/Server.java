package uf3_pvp_ex1.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private final int PORT = 65000;
    private int remainingSeats = 20;
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

    public synchronized int addReservation(int numberSeats) throws ReservationException {
        while (isProcessing) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return remainingSeats;
            }
        }

        if (remainingSeats == 0 || remainingSeats - numberSeats < 0) throw new ReservationException("No space for seats", remainingSeats);

        isProcessing = true;

        remainingSeats -= numberSeats;

        int currentSeats = remainingSeats;
        isProcessing = false;
        this.notifyAll();

        return currentSeats;
    }

    public synchronized int checkSeats() {
        while (isProcessing) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return remainingSeats;
            }
        }

        isProcessing = true;

        int currentSeats = remainingSeats;

        isProcessing = false;
        notifyAll();

        return currentSeats;
    }
}
