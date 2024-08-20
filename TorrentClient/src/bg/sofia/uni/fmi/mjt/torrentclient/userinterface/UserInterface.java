package bg.sofia.uni.fmi.mjt.torrentclient.userinterface;

public interface UserInterface {
    void displayNamePrompt();

    void displayMessagePrompt();

    void displayReply(String reply);

    void displayErrorMessage(String message);
}
