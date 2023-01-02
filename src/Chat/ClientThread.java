package Chat;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class ClientThread extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private String channel;
    private static Gson json = new Gson();
    private static List<ClientThread> clients = new ArrayList<>();

    public ClientThread(Socket clientSocket, String channel) {
        this.clientSocket = clientSocket;
        this.channel = channel;
        clients.add(this);
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            while (true) {
                try {
                    String messageJson = in.readLine();
                    Message message = json.fromJson(messageJson, Message.class);
                    if (message.getType().equals("login")) {
                        // Handle login message
                        LoginMessage loginMessage = json.fromJson(messageJson, LoginMessage.class);
                        username = loginMessage.getUsername();
                        setChannel(loginMessage.getChannel());
                        out.println(json.toJson(new LoginSuccessMessage()));
                    } else if (message.getType().equals("message")) {
                        // Handle regular message
                        System.out.println(message.getSender() + ": " + message.getMessage());
                        // Send message to all clients in the same channel
                        for (ClientThread client : clients) {
                            if (client.channel.equals(channel)) {
                                client.out.println(messageJson);
                            }
                        }
                    }
                }
                catch (SocketException e){
                    clientSocket.close();
                    System.out.println("Client Disconnected ");
                    in.close();
                    // Stop the client thread from running
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

