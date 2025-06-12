package uf3_pvp_ex1.server;

import java.io.*;
import java.net.Socket;

@SuppressWarnings("ALL")
public class Manager implements Runnable {
    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Manager(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        System.out.println("S'ha conectat un client nou! Port: " + socket.getPort());
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private String read() throws IOException {
        return reader.readLine();
    }

    private void sendResponse(String remainingSeats) throws IOException {
        String response = new StringBuilder().append(1).append(remainingSeats).toString();
        try {
            writer.write(response);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("There's been an error sending the message... :( ");
            e.printStackTrace();
            sendErrorResponse();
        }
    }

    private void sendErrorResponse() {
        int remainingSeats = this.server.checkSeats();
        String response = new StringBuilder().append(-2).append(remainingSeats).toString();
        try {
            writer.write(response);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("There's been an error sending the error message... :( ");
            e.printStackTrace();
        }
    }

    private void sendErrorResponseNoSpaces(int remainingSeats) {
        String response = new StringBuilder().append(-1).append(remainingSeats).toString();
        try {
            writer.write(response);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("There's been an error sending the error message... :( ");
            e.printStackTrace();
        }
    }

    private void handleRequest(String message) {
        try {
            // ACTION
            // 1 -> add seats
            // 2 -> read seats
            int action = Integer.parseInt(message.substring(0, 1));

            if (action == 1) {
                int currentOffset = 0;
                int numberSeatsCharactersOffset = Integer.parseInt(message.substring(1, 2));
                currentOffset += 2;

                int numberSeats = Integer.parseInt(message.substring(currentOffset, currentOffset + numberSeatsCharactersOffset));
                currentOffset += numberSeatsCharactersOffset;

                int remainingSeats = 0;

                int numberNameCharactersOffset = Integer.parseInt(message.substring(currentOffset, currentOffset + 1));
                currentOffset += 1;

                String clientName = message.substring(currentOffset);

                try {
                    remainingSeats = this.server.addReservation(numberSeats);
                } catch (ReservationException e) {
                    System.out.println(clientName + " " + numberSeats + " rebutjades, restants: " + e.getRemainingSeats());
                    sendErrorResponseNoSpaces(e.getRemainingSeats());
                    return;
                }
                String seats = Integer.toString(remainingSeats);

                sendResponse(seats);
                System.out.println(clientName + " " + numberSeats + " reservades, restants: " + remainingSeats);
            } else if (action == 2) {
                int remainingSeats = this.server.checkSeats();

                String clientName = message.substring(2);

                sendResponse(Integer.toString(remainingSeats));
                System.out.println(clientName + " consulta, restants: " + remainingSeats);
            } else {
                sendErrorResponse();
            }
        } catch (NumberFormatException e) {
            System.out.println("Request code does not exist");
            e.printStackTrace();
            sendErrorResponse();
        } catch (IOException e) {
            System.out.println("There's been an error sending the response...");
            sendErrorResponse();
        }
    }

    @Override
    public void run() {
        try {
            String clientMessage = this.read();
            if (clientMessage == null) {
                return;
            }

            handleRequest(clientMessage);

        } catch (IOException e) {
            System.out.println("There's been an error: ");
            e.printStackTrace();
            sendErrorResponse();
        } finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
