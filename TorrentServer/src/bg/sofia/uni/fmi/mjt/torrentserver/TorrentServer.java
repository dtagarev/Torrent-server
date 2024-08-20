        package bg.sofia.uni.fmi.mjt.torrentserver;

import bg.sofia.uni.fmi.mjt.torrentserver.server.ServerThread;

import java.util.Scanner;

public class TorrentServer {

    public static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        ServerThread server = new ServerThread(SERVER_PORT, SERVER_HOST, BUFFER_SIZE);
        Thread serverThread = new Thread(server);

        System.out.println("Starting server");
        System.out.println("You can stop it typing \"stop\"");

        serverThread.start();
        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = sc.nextLine();

            if (input.equals("stop")) {
                server.shutDown();
                try {
                    serverThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Server was stopped successfully");
                return;
            }
        }

    }
}
