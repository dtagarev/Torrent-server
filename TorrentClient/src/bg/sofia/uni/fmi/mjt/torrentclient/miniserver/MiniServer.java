package bg.sofia.uni.fmi.mjt.torrentclient.miniserver;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;

public class MiniServer implements Runnable {

    private final String host;
    private final int port;
    private static final int BUFFER_SIZE = 1024;
    private ByteBuffer buffer;
    private Selector selector;

    private ExecutorService executor;

    private UserDirectory userDirectory;
    private final ErrorHandler errorHandler;

    public MiniServer(String host, int port, UserDirectory userDirectory, ErrorHandler errorHandler) {
        this.host = host;
        this.port = port;

        this.userDirectory = userDirectory;
        this.errorHandler = errorHandler;

        executor = newVirtualThreadPerTaskExecutor();
    }

    public void run() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int readyChannels = selector.select();
                    System.out.println("Miniserver: Thread left select");
                    //Todo: Test shutdown while a reading writing or accepting operation is happening
                    // estimating that the operation should throw an exception and the thread should be interrupted
                    // inside the catch clause by closed by interrupt exception
                    if (readyChannels == 0) {
                        continue;
                    }

                    handleClientRequest();

                } catch (ClosedByInterruptException ignored) {
                    shutDown();
                } catch (IOException e) {
                    errorHandler.writeToLogFile(e);
                }
            }

            shutDown();
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }

        System.out.println("Miniserver: Shutdown complete");
    }

    private void handleClientRequest() throws ClosedByInterruptException, IOException {
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

        System.out.println("Miniserver: Processing ready channels");
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            System.out.println("Miniserver: iterating the channels");

            if (key.isReadable()) {

                SocketChannel clientChannel = (SocketChannel) key.channel();
                String clientInput = getClientInput(clientChannel);
                writeClientOutput(clientChannel, clientInput);

            } else if (key.isAcceptable()) {
                acceptClient(selector, key);
            }

            keyIterator.remove();

        }
    }

    public void shutDown() {
        executor.shutdownNow();
    }

    private void configureServerSocketChannel(ServerSocketChannel channel) throws IOException {
        System.out.println("Miniserver: Configuring server socket channel");
        channel.bind(new InetSocketAddress(host, port));
        channel.configureBlocking(false);
        System.out.println("Miniserver: Resitering server socket channel with selector");
        channel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Miniserver: Done configuring server socket channel");
    }

    private String getClientInput(SocketChannel clientChannel) throws ClosedByInterruptException,IOException {
        buffer.clear();
        System.out.println("Miniserver: Reading from client");
        int readBytes = clientChannel.read(buffer);
        System.out.println("Miniserver: Done reading ");
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws ClosedByInterruptException, IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        System.out.println("Miniserver: Writing to client");
        clientChannel.write(buffer);
        System.out.println("Miniserver: Done writing");
    }

    private void sendFileToClient(SocketChannel clientChannel, String fileName) throws IOException {

    }

    private void acceptClient(Selector selector, SelectionKey key) throws ClosedByInterruptException, IOException {
        System.out.println("Miniserver: Accepting client connection");
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        System.out.println("Miniserver: Created sockChannel: " + sockChannel.toString());
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
        System.out.println("Miniserver: New Client connected");
    }
}