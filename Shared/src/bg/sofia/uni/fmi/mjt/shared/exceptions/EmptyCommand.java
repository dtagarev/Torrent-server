package bg.sofia.uni.fmi.mjt.shared.exceptions;

public class EmptyCommand extends RuntimeException {
    public EmptyCommand(String message) {
        super(message);
    }
}
