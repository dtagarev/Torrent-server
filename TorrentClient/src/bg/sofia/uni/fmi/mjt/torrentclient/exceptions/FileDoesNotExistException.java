package bg.sofia.uni.fmi.mjt.torrentclient.exceptions;

public class FileDoesNotExistException extends RuntimeException {
    public FileDoesNotExistException(String message) {
        super(message);
    }
}
