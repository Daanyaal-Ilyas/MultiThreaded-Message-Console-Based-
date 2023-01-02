package Chat;

class Message {
    private final String type;
    private final String sender;
    private final String message;
    private final String channel;

    public Message(String type, String sender, String message, String channel) {
        this.type = type;
        this.sender = sender;
        this.message = message;
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getChannel() {
        return channel;
    }
}

class LoginMessage {
    private String type = "login";
    private String username;
    private String channel;

    public LoginMessage(String username, String channel) {
        this.username = username;
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getChannel() {
        return channel;
    }

}

class LoginSuccessMessage {

}





