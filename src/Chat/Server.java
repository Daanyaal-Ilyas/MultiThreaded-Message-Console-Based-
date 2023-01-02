package Chat;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {
    private static Map<String, ServerThread> channels = new HashMap<>();
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);
        while (true) {
            LoginMessage loginMessage = new LoginMessage("username", "channel");
            Socket clientSocket = serverSocket.accept();
            System.out.println("NEW Client Connected ");
            ClientThread clientThread = new ClientThread(clientSocket, loginMessage.getChannel());
            clientThread.start();

        }
    }
}

class ServerThread extends Thread {
    private Socket clientSocket;
    private String channel;
    private PrintWriter out;
    private static Map<String, ServerThread> channels = new HashMap<>();
    public ServerThread(Socket clientSocket, String channel) {
        this.clientSocket = clientSocket;
        this.channel = channel;
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        channels.put(channel, this);
    }
}