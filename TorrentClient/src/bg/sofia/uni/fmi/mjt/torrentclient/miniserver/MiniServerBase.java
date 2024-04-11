package bg.sofia.uni.fmi.mjt.torrentclient.miniserver;

import main.java.application.types.QueuedCommand;
import main.java.commands.Command;
import main.java.commands.RemoteChannelCommand;
import main.java.commands.OriginChannelCommand;
import main.java.commands.incoming.IncomingCommandProcessor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class MiniServerBase {

    public final String username;
    protected Queue<OriginChannelCommand> originActions;
    protected Queue<RemoteChannelCommand> clientActions;
    /**
     * Reference to the origin server (where we pull torrent/user metadata from)
     */
    protected SocketChannel originChannel;
    /**
     * The applicationChannel is basically the local client server that receives and
     * transmits data to other client servers (peers).
     */
    protected ServerSocketChannel applicationChannel;
    protected Selector selector;
    /**
     * The last channel that had been ready
     */
    protected SocketChannel selectedChannel;
    protected final Map<SocketChannel, QueuedCommand> responseQueue;
    protected final IncomingCommandProcessor commandProcessor;
    private final boolean isBlocking;

    protected MiniServerBase(String username, boolean isBlocking, InetSocketAddress originAddress) throws IOException {
        this.responseQueue = new HashMap<>();
        this.originActions = new ArrayBlockingQueue<>(20);
        this.clientActions = new ArrayBlockingQueue<>(20);
        this.commandProcessor = new IncomingCommandProcessor(this);
        this.username = username;
        this.isBlocking = isBlocking;
        this.initOriginServer(originAddress);
        this.initApplicationServer();
    }

    private void initOriginServer(InetSocketAddress address) throws IOException {
        this.originChannel = SocketChannel.open(address);
        this.originChannel.configureBlocking(isBlocking);
        System.out.println("Connecting to " + address);
    }

    private void initApplicationServer() throws IOException {
        this.applicationChannel = ServerSocketChannel.open();
        this.applicationChannel.configureBlocking(isBlocking);
    }

    public boolean setCommand(Command command) {
        //TODO origin/client type check
        if (command instanceof OriginChannelCommand) {
            return this.originActions.offer((OriginChannelCommand) command);
        }
        if (command instanceof RemoteChannelCommand) {
            return this.clientActions.offer((RemoteChannelCommand) command);
        }
        throw new UnsupportedOperationException("Command type is not supported: " + command.getClass().getName());
    }

    public SocketChannel getOriginChannel() {
        return this.originChannel;
    }

    public abstract void start();

    public SocketChannel connectToClient(InetSocketAddress address) throws IOException {
        final var s = SocketChannel.open();
        s.configureBlocking(false);

        s.connect(address);
        s.register(selector, SelectionKey.OP_CONNECT);
        return s;
    }
}
