package bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.send;

import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

import java.io.IOException;

public class FileSenderJob implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private Path filePath;

    public FileSenderJob(ServerSocketChannel serverSocketChannel, Path filePath) {
        this.serverSocketChannel = serverSocketChannel;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        System.out.println("FileSenderJob: Thread entered run");
        try (SocketChannel clientSocket = serverSocketChannel.accept();
            FileChannel fileChannel = FileChannel.open(filePath)) {

            fileChannel.transferTo(0, fileChannel.size(), clientSocket);
            System.out.println("FileSenderJob: File send");

        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("FileSenderJob: Shutting down thread");

    }
}
