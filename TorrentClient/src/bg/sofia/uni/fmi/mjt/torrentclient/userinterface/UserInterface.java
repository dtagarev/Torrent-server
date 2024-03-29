package bg.sofia.uni.fmi.mjt.torrentclient.userinterface;

public interface UserInterface {
    void displayNamePrompt();
    void displayMessagePrompt();
    void displaySendingMessage(String message);
    void displayConnectedToServer();

    void displayServerReply(String reply);

    void displayErrorMessage(String message);
    void displayCommandReply(String reply);
}
