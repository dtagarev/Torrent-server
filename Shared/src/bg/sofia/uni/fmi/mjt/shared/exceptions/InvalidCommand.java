package bg.sofia.uni.fmi.mjt.shared.exceptions;

public class InvalidCommand extends RuntimeException {
    public InvalidCommand(String message) {
        super(message);
    }
}
