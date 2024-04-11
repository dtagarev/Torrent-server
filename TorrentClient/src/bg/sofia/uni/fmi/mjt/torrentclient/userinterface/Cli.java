package bg.sofia.uni.fmi.mjt.torrentclient.userinterface;

public class Cli implements UserInterface {

    @Override
    public void displayNamePrompt() {
        System.out.print("Enter name: ");
    }

    @Override
    public void displayWelcomeMessage(String username) {
        System.out.println("Welcome, " + username + "!");
    }

    @Override
    public void displayMessagePrompt() {
        System.out.print("Enter message: ");
    }

    @Override
    public void displaySendingMessage(String message) {
        System.out.println("Sending message <" + message + "> to the server...");
    }

    @Override
    public void displayConnectedToServer() {
        System.out.println("Connected to the server.");
    }

    @Override
    public void displayReply(String reply) {
        System.out.println(reply);
    }

    @Override
    public void displayErrorMessage(String message) {
        System.out.println("Error: " + message);
    }

    @Override
    public void displayCommandReply(String reply) {
        System.out.println(reply);
    }
}
