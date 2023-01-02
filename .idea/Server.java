import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.List;
import com.google.gson.Gson;

public class Server {
    private static final int PORT = 1234;
    private static Map<String, ClientThread> clientThreads = new HashMap<>();
    private static List<Message> messages = new ArrayList<>();
    private static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server listening on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            System.out.println("New client connected");
            ClientThread clientThread = new ClientThread(clientSocket);
            clientThread.start();
        }
    }
    public void saveMessages() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("messages.txt"))) {
            for (Message message : messages) {
                writer.write(message.getSender() + ": " + message.getMessage());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void broadcastMessage(String sender, String message) {
        messages.add(gson.fromJson(message, Message.class));
        for (ClientThread clientThread : clientThreads.values()) {
            if (sender == null) {
                LoginSuccessMessages loginSuccessMessage = gson.fromJson(message, LoginSuccessMessages.class);
                String loginSuccessJson = gson.toJson(loginSuccessMessage);
                clientThread.sendMessage(loginSuccessMessage.getSender(), loginSuccessJson);
            } else {
                Message msg = gson.fromJson(message, Message.class);
                clientThread.sendMessage(sender, message);
            }
        }
    }


    public static void addClient(String username, ClientThread clientThread) {
        clientThreads.put(username, clientThread);
    }

    public static void removeClient(String username) {
        clientThreads.remove(username);
    }
}