package Chat;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import com.google.gson.Gson;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;
    private static Gson json = new Gson();
    private static String username;
    private static String channel;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        // Get username from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.next();
        System.out.print("Enter your channel: ");
        String channel = scanner.next();
        // Send login message to server
        LoginMessage loginMessage = new LoginMessage(username, channel);
        String loginJson = json.toJson(loginMessage);
        out.println(loginJson);

        // Wait for login success message from server
        String loginSuccessJson = in.readLine();
        LoginMessage loginSuccessMessage = json.fromJson(loginSuccessJson, LoginMessage.class);

        // Start listening for messages from server in a separate thread
        Thread messageListenerThread = new Thread(new MessageListener(in, channel));
        messageListenerThread.start();

        // Read messages from user and send to server
        while (true) {
            String message = scanner.nextLine();
            Message msg = new Message("message", username, message, channel);
            String messageJson = json.toJson(msg);
            out.println(messageJson);

            if (message.equals("/quit")) {
                System.exit(0);
            }
        }
    }
}

class MessageListener implements Runnable {
    private BufferedReader in;
    private File messagesFile;
    private BufferedWriter writer;
    private String channel;

    public MessageListener(BufferedReader in, String channel) {
        this.in = in;
        this.channel = channel;
        this.messagesFile = new File("messages-" + channel + ".txt");
        if (!messagesFile.exists()) {
            try {
                messagesFile.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file");
                e.printStackTrace();
            }
        }
        try {
            Scanner reader = new Scanner(messagesFile);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                System.out.println(data);
            }
            reader.close();
            try {
                writer = new BufferedWriter(new FileWriter(messagesFile, true));
            } catch (IOException e) {
                System.err.println("Error opening file for writing");
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }
    }


    private static Gson json = new Gson();

    @Override
    public void run() {
        try {
            writer = new BufferedWriter(new FileWriter(messagesFile));
        } catch (IOException e) {
            System.err.println("Error opening file for writing");
            e.printStackTrace();
        }
        while (true) {
            try {
                String messageJson = in.readLine();
                Message message = json.fromJson(messageJson, Message.class);
                if (message.getChannel().equals(channel)) {
                    // Print just the sender, message, and channel
                    if (message.getType().equals("join")) {
                        // Print join message to console and write to file
                        System.out.println(message.getSender() + ": " + message.getMessage());
                        writer.write(message.getSender() + " " + message.getMessage() + "\n");
                        writer.flush();
                    }
                    else if (message.getType().equals("message")) {
                        // Print message to console and write to file as before
                        System.out.println(message.getSender() + ": " + message.getMessage());
                        writer.write(message.getSender() + ": " + message.getMessage() + "\n");
                        writer.flush();
                    }
                    else if (message.getType().equals("leave")) {
                        // Print leave message to console and write to file
                        System.out.println(message.getSender() + ": " + message.getMessage());
                        writer.write(message.getSender() + " " + message.getMessage() + "\n");
                        writer.flush();
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}