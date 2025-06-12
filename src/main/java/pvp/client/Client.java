package pvp.client;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String message = "";

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

    public void sendMessage(String message) {
        this.message = message;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            connect();
            send(message);
            String responseMessage = read();
            System.out.println(message + ": " + responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }
}