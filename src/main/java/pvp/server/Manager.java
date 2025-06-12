package pvp.server;

import java.io.*;
import java.net.Socket;

public class Manager implements Runnable {
    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Manager (Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        System.out.println("Creat un nou client");
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private String read() throws IOException {
        return reader.readLine();
    }

    private void write(String currentChain) throws IOException {
        writer.write(currentChain);
        writer.newLine();
        writer.flush();
    }

    @Override
    public void run() {
        try {
            String clientMessage = this.read();
            if (clientMessage == null) {
                return;
            }

            this.server.addStringToMessageChain(clientMessage);
            String currentChain = this.server.getMessageChain();
            this.write(currentChain);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                System.err.println("Error closing reader: " + e.getMessage());
            }
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                System.err.println("Error closing writer: " + e.getMessage());
            }
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
