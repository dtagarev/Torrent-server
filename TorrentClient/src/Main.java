import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        InetSocketAddress miniServerAddress = new InetSocketAddress("localhost", 0);
        InetSocketAddress miniServerAddress2 = new InetSocketAddress("localhost", 0);
        try (ServerSocketChannel socketChannel  = ServerSocketChannel.open();
             ServerSocketChannel socketChannel2 = ServerSocketChannel.open()){

            socketChannel.bind(miniServerAddress);
            socketChannel2.bind(miniServerAddress2);

            System.out.println("miniServerAddress port = " + socketChannel.socket().getLocalPort());
            System.out.println("miniServerAddress2 port = " + socketChannel2.socket().getLocalPort());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}