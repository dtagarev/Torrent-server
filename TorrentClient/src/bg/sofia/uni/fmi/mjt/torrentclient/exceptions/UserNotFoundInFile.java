package bg.sofia.uni.fmi.mjt.torrentclient.exceptions;

public class UserNotFoundInFile extends RuntimeException {
    public UserNotFoundInFile(String message) {
        super(message);
    }
}
