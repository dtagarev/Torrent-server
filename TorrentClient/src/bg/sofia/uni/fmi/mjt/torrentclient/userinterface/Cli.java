package bg.sofia.uni.fmi.mjt.torrentclient.userinterface;

public class Cli implements UserInterface {

    @Override
    public void displayNamePrompt() {
        System.out.print("Enter name: ");
    }

    @Override
    public void displayMessagePrompt() {
        System.out.print("Enter message: ");
    }

    @Override
    public void displayReply(String reply) {
        System.out.println(reply);
    }

    @Override
    public void displayErrorMessage(String message) {
        System.out.println("Error: " + message);
    }

}
