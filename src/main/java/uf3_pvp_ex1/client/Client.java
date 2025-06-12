package uf3_pvp_ex1.client;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name = "";
    private String message = "";
    private int nReserves = 0;
    private int requestType = 0;

    public Client(String name) {
        this.name = name;
    }

    private void connect() throws IOException {
        socket = new Socket("127.0.0.1", 65000);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private String read() throws IOException {
        return reader.readLine();
    }

    private void send(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    private void disconnect() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }

    public void sendMessage(boolean reservar) {
        sendMessage(reservar, 0);
    }

    public void sendMessage(boolean reservar, int nReserves) {
        requestType = reservar ? 1 : 2;
        StringBuilder messageBuilder = new StringBuilder().append(requestType);
        if (reservar) {
            messageBuilder.append(nReserves > 9 ? 2 : 1).append(nReserves);
            this.nReserves = nReserves;
        }

        messageBuilder.append(name.length()).append(name);

        this.message = messageBuilder.toString();
        new Thread(this).start();
    }

    private void handleResponse(String response) {
        String error = response.substring(0, 1);
        if (error.equals("-")) {
            handleErrorResponse(response);
            return;
        }

        String nReservesRestants = response.substring(1);
        if (requestType == 1) {
            System.out.println(name + " " + nReserves + " reservades, restants: " + nReservesRestants);
        } else if (requestType == 2) {
            System.out.println(name + " consulta, restants " + nReservesRestants);
        }

    }

    private void handleErrorResponse(String errorResponse) {
        try {
            int errorCode = Integer.parseInt(errorResponse.substring(1, 2));
            String nReservesRestants = errorResponse.substring(2);
            if (errorCode == 1) {
                System.out.println(name + " " + nReserves + " rebutjades, restants: " + nReservesRestants);
            } else if (errorCode == 2) {
                System.out.println(name + " Hi ha hagut algun error inesperat");
            }
        } catch (NumberFormatException e) {
            System.out.println("There's been an error while reading the error status code");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            connect();
            send(message);
            String responseMessage = read();

            handleResponse(responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }
}