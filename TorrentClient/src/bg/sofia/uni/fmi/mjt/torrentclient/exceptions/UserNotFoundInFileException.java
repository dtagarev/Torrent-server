package bg.sofia.uni.fmi.mjt.torrentclient.exceptions;

public class UserNotFoundInFileException extends RuntimeException {
    public UserNotFoundInFileException(String message) {
        super(message);
    }
}
