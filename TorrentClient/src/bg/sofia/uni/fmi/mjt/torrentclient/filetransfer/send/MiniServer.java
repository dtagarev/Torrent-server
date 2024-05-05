package bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.send;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.receive.FileReceiver;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

public class MiniServer implements Runnable {

    private final ServerSocketChannel serverSocketChannel;
    private static final int BUFFER_SIZE = 1024;
    private ByteBuffer buffer;
    private Selector selector;

    private final ExecutorService executor;

    private final UserDirectory userDirectory;
    private final ErrorHandler errorHandler;

    private final static String INVALID_FILE_RESPONSE = "ERROR";

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
                    System.out.println("Miniserver: Going select");
                    int readyChannels = selector.select();
                    System.out.println("Miniserver: Thread left select");

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
                if(clientInput == null) {
                    continue;
                }
                System.out.println("Miniserver: Client input = " + clientInput);
                String reply = INVALID_FILE_RESPONSE;

                if(UserDirectory.isAFile(clientInput) && userDirectory.containsFilePath(clientInput)) {
                    Integer port = createFileSenderJob(clientChannel, clientInput);
                    if(port != -1) {
                        reply = port.toString();
                    }
                }

                System.out.println("Miniserver: Created File sender job \n with port " +
                        reply + " for file " + clientInput);

                writeClientOutput(clientChannel, reply);

            } else if (key.isAcceptable()) {
                acceptClient(selector, key);
            }

            keyIterator.remove();
        }
    }

    private Integer createFileSenderJob(SocketChannel clientChannel, String clientInput) {
        try {
            String thisHost = ((InetSocketAddress) serverSocketChannel.getLocalAddress()).getHostString();
            InetSocketAddress fileSenderJobAddress = new InetSocketAddress(thisHost, 0);
            ServerSocketChannel fileSenderJobSocketChannel = ServerSocketChannel.open();
            fileSenderJobSocketChannel.bind(fileSenderJobAddress);

            Thread fileSenderJobThread = new Thread(new FileSenderJob(fileSenderJobSocketChannel, Path.of(clientInput)));
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
        System.out.println("Miniserver: Configuring server socket channel");
        serverSocketChannel.configureBlocking(false);
        System.out.println("Miniserver: Resitering server socket channel with selector");
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
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