package bg.sofia.uni.fmi.mjt.torrentserver.server;

import bg.sofia.uni.fmi.mjt.shared.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;
import bg.sofia.uni.fmi.mjt.torrentserver.command.HelpCommand;
import bg.sofia.uni.fmi.mjt.torrentserver.command.ListFilesCommand;
import bg.sofia.uni.fmi.mjt.torrentserver.command.RegisterCommand;
import bg.sofia.uni.fmi.mjt.torrentserver.command.UnregisterCommand;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ServerThread implements Runnable {

    private final int port;
    private final String host;
    private final int bufferSize;
    private boolean isServerWorking;
    private ByteBuffer buffer;
    private Selector selector;

    private ServerStorage storage;
    private Map<SocketChannel, String> socketToNameStorage;
    private CommandExecutor commandExecutor;

    private ErrorHandler errorHandler;



    public ServerThread(int port, String host, int bufferSize) {
        this.port = port;
        this.host = host;

        this.bufferSize = bufferSize;
        storage = new ServerStorage();

        Path logFilePath = Path.of(System.getProperty("user.dir") + File.separator + "serverLogs.txt");

        errorHandler = new ErrorHandler(logFilePath);

        commandExecutor = new CommandExecutor(Set.of(
                new RegisterCommand(storage),
                new UnregisterCommand(storage, errorHandler),
                new ListFilesCommand(storage),
                new HelpCommand(storage))
        );

        socketToNameStorage = new HashMap<>();
    }

    public void run() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(bufferSize);
            isServerWorking = true;

            System.out.println("Server is started and waiting for connections");

            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isReadable()) {

                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String clientInput = getClientInput(clientChannel);


                            System.out.println("Client said  -> " + clientInput );
                            if (clientInput == null) {
                                continue;
                            }

                            if(socketToNameStorage.get(clientChannel) == null) {
                                socketToNameStorage.put(clientChannel, clientInput);
                                storage.register(clientInput, Collections.emptyList());
                                writeClientOutput(clientChannel, "Welcome " + clientInput);
                            } else {
                                String result = executeCommand(clientInput, socketToNameStorage.get(clientChannel));

                                writeClientOutput(clientChannel, result);
                            }

                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }


                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    private String executeCommand(String clientInput, String clientName) {
        try {
            return commandExecutor.execute(clientInput);
        } catch (EmptyCommand e) {
            String errorMessage = "Client "
                + clientName
                + " has entered an invalid command: " + clientInput;

            errorHandler.writeToLogFile(e, errorMessage);
            return "Empty command! Please enter a valid command.";
        } catch (InvalidSymbolInCommand e) {
            String errorMessage = "Client "
                + clientName
                + " has entered an invalid command: " + clientInput;

            errorHandler.writeToLogFile(e, errorMessage);
            return "Invalid symbol in command! Please enter a valid command.";
        } catch (InvalidCommand e) {
            String errorMessage = "Client "
                + clientName
                + " has entered an invalid command: " + clientInput;

            errorHandler.writeToLogFile(e, errorMessage);
            return "Invalid command! Please enter a valid command.";
        }
    }

    public void shutDown() {
        System.out.println("Server is stopping");
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(host, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            System.out.println("Client has closed the connection");
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);

        socketToNameStorage.put(accept, null);

        //I want to see which client
        System.out.println("Client has connected");
    }
}