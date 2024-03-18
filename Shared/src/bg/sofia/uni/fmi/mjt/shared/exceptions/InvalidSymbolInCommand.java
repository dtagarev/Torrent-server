package bg.sofia.uni.fmi.mjt.shared.exceptions;

public class InvalidSymbolInCommand extends RuntimeException {
    public InvalidSymbolInCommand(String message) {
        super(message);
    }
}
