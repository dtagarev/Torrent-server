package bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.send;

import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import java.io.IOException;

public class FileSenderJob implements Callable<Boolean> {

    private SocketChannel clientChannel;
    private Path filePath;

    public FileSenderJob(SocketChannel socketChannel, Path filePath) {
        this.clientChannel = socketChannel;
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
    public Boolean call() {
        try(FileChannel fileChannel = FileChannel.open(filePath)) {

            //How to use correctly
            fileChannel.transferTo(0, fileChannel.size(), clientChannel);

        } catch(AsynchronousCloseException e) {
            return false;
        } catch (IOException e) {
            //error handler . write to log file
            System.err.println("There was a problem with the file transfer.");
            return false;
        }

        return true;
    }
}
