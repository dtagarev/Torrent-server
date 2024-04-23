package bg.sofia.uni.fmi.mjt.torrentclient.connection;

import bg.sofia.uni.fmi.mjt.torrentclient.exception.ServerConnectionException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ServerConnection {
    private final int SERVER_PORT;
    private final String SERVER_HOST;
    private final ByteBuffer buffer;

    SocketChannel socketChannel;

    public ServerConnection(String SERVER_HOST, int SERVER_PORT, int BUFFER_SIZE) throws ServerConnectionException {
        this.SERVER_HOST = SERVER_HOST;
        this.SERVER_PORT = SERVER_PORT;
        this.buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        try {
            this.socketChannel = connectToServer();
        } catch (IOException e) {
            throw new ServerConnectionException("There is a problem with the network communication");
        }

    }

    private SocketChannel connectToServer() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

        return socketChannel;
    }

    private void writeToServer(String message) throws IOException {
        buffer.clear(); // switch to writing mode
        buffer.put(message.getBytes()); // buffer fill
        buffer.flip(); // switch to reading mode
        socketChannel.write(buffer); // buffer drain
    }

    private String readFromServer() throws IOException {
        buffer.clear(); // switch to writing mode
        socketChannel.read(buffer); // buffer fill
        buffer.flip(); // switch to reading mode

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        return new String(byteArray, StandardCharsets.UTF_8); // buffer drain
    }

    public void closeConnection() throws IOException {
        socketChannel.close();
    }

    public synchronized String communicateWithServer(String message) throws IOException {
        writeToServer(message);
        return readFromServer();
    }
}
