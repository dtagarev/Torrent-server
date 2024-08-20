package bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.send;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;

public class MiniServer implements Runnable {

    private final ServerSocketChannel serverSocketChannel;
    private static final int BUFFER_SIZE = 1024;
    private ByteBuffer buffer;
    private Selector selector;

    private final ExecutorService executor;

    private final UserDirectory userDirectory;
    private final ErrorHandler errorHandler;

    private static final String INVALID_FILE_RESPONSE = "ERROR";

    public MiniServer(ServerSocketChannel serverSocketChannel, UserDirectory userDirectory, ErrorHandler errorHandler) {

        this.serverSocketChannel = serverSocketChannel;
        this.userDirectory = userDirectory;
        this.errorHandler = errorHandler;

        executor = newVirtualThreadPerTaskExecutor();
    }

    public void run() {
        try {
            selector = Selector.open();
            configureServerSocketChannel();
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int readyChannels = selector.select();

                    if (readyChannels == 0) {
                        continue;
                    }

                    handleClientRequest();

                } catch (ClosedByInterruptException ignored) {
                    this.shutDown();
                } catch (IOException e) {
                    errorHandler.writeToLogFile(e);
                }
            }

            shutDown();
        } catch (IOException e) {
            System.err.println("failed to start server: " + e);
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    private void handleClientRequest() throws ClosedByInterruptException, IOException {
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            if (key.isReadable()) {

                SocketChannel clientChannel = (SocketChannel) key.channel();
                String clientInput = getClientInput(clientChannel);
                if (clientInput == null) {
                    continue;
                }
                String reply = INVALID_FILE_RESPONSE;

                if (UserDirectory.isAFile(clientInput) && userDirectory.containsFilePath(clientInput)) {
                    Integer port = createFileSenderJob(clientInput);
                    if (port != -1) {
                        reply = port.toString();
                    }
                }
                writeClientOutput(clientChannel, reply);

            } else if (key.isAcceptable()) {
                acceptClient(selector, key);
            }

            keyIterator.remove();
        }
    }

    private Integer createFileSenderJob(String clientInput) {
        try {
            String thisHost = ((InetSocketAddress) serverSocketChannel.getLocalAddress()).getHostString();
            InetSocketAddress fileSenderJobAddress = new InetSocketAddress(thisHost, 0);
            ServerSocketChannel fileSenderJobSocketChannel = ServerSocketChannel.open();
            fileSenderJobSocketChannel.bind(fileSenderJobAddress);

            Thread fileSenderJobThread = new Thread(
                new FileSenderJob(fileSenderJobSocketChannel, Path.of(clientInput)));
            fileSenderJobThread.setDaemon(true);

            executor.submit(fileSenderJobThread);

            return fileSenderJobSocketChannel.socket().getLocalPort();

        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            return -1;
        }
    }

    public void shutDown() throws IOException {
        serverSocketChannel.close();
        executor.shutdownNow();
    }

    private void configureServerSocketChannel() throws IOException {
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();
        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output)
        throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void acceptClient(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}