package bg.sofia.uni.fmi.mjt.torrentclient.connection;

import bg.sofia.uni.fmi.mjt.torrentclient.exceptions.ServerConnectionException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ServerConnection implements ServerCommunicator {
    private final int serverPort;
    private final String serverHost;
    private final ByteBuffer buffer;

    private final SocketChannel socketChannel;

    public ServerConnection(String serverHost, int serverPort, int bufferSize) throws ServerConnectionException {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.buffer = ByteBuffer.allocateDirect(bufferSize);

        try {
            this.socketChannel = connectToServer();
        } catch (IOException e) {
            throw new ServerConnectionException("There is a problem with the network communication");
        }

    }

    private SocketChannel connectToServer() throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(serverHost, serverPort));

        return socketChannel;
    }

    public void writeToServer(String message) throws IOException {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
    }

    public String readFromServer() throws IOException {
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        return new String(byteArray, StandardCharsets.UTF_8);
    }

    public void closeConnection() throws IOException {
        socketChannel.close();
    }

    @Override
    public synchronized String communicateWithServer(String message) throws IOException {
        writeToServer(message);
        return readFromServer();
    }

    public String getClientHost() throws IOException {
        return ((InetSocketAddress) socketChannel.getLocalAddress()).getHostString();
    }
}
