package bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.send;

import java.net.Socket;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import java.io.IOException;

public class FileSenderJob implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private Path filePath;

    public FileSenderJob(ServerSocketChannel serverSocketChannel, Path filePath) {
        this.serverSocketChannel = serverSocketChannel;
        this.filePath = filePath;
    }

    //@Override
    //public Boolean call() {
    //    Path filePath = Path.of("src/bg/sofia/uni/fmi/mjt/torrentclient/miniserver/MiniServer.java");

    //    //send message to client
    //    try (InputStream fileInputStream = Files.newInputStream(filePath);
    //         OutputStream outputStream = clientChannel.socket().getOutputStream()) {
    //        byte[] buffer = new byte[1024];
    //        int bytesRead;
    //        MessageDigest md = MessageDigest.getInstance("MD5");

    //        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
    //            outputStream.write(buffer, 0, bytesRead);
    //            md.update(buffer, 0, bytesRead);
    //            if(Thread.currentThread().isInterrupted()) {
    //                byte[] checksum = md.digest();
    //                outputStream.write(checksum); // Send checksum after file
    //                return false;
    //            }
    //        }

    //        byte[] checksum = md.digest();
    //        outputStream.write(checksum); // Send checksum after file

    //        System.out.println("File sent successfully.");

    //    } catch (NoSuchAlgorithmException e) {
    //        System.err.println("MD5 algorithm is not available.");
    //        throw new RuntimeException(e);

    //    } catch (IOException e) {
    //        System.err.println("There was a problem with the file transfer.");
    //        throw new RuntimeException(e);
    //    }

    //    return true;
    //}

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
