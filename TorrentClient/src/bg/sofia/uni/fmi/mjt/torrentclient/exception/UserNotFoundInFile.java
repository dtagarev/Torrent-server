package bg.sofia.uni.fmi.mjt.torrentclient.exception;

public class UserNotFoundInFile extends RuntimeException {
    public UserNotFoundInFile(String message) {
        super(message);
    }
}
