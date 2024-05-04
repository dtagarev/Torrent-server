import bg.sofia.uni.fmi.mjt.torrentclient.client.ClientHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.Cli;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        new ClientHandler().start();
    }
}