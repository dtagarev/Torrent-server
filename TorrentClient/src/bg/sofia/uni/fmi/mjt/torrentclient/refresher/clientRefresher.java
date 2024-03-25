package bg.sofia.uni.fmi.mjt.torrentclient.refresher;

import java.nio.ByteBuffer;

public class clientRefresher implements Runnable {

    private final int SERVER_PORT;
    private final String SERVER_HOST;
    private static final int BUFFER_SIZE = 4096;

    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public clientRefresher(int SERVER_PORT, String SERVER_HOST) {
        this.SERVER_PORT = SERVER_PORT;
        this.SERVER_HOST = SERVER_HOST;
    }

    @Override
    public void run() {

    }
}
