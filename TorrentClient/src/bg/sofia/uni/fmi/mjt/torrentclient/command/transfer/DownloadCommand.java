package bg.sofia.uni.fmi.mjt.torrentclient.command.transfer;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.BaseCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerConnection;
import bg.sofia.uni.fmi.mjt.torrentclient.exceptions.ServerConnectionException;
import bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.receive.FileRequest;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UsersFileManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class DownloadCommand extends BaseCommand implements Command {
    private static final int COMMAND_ARGUMENTS_COUNT = 3;
    private static final String correctFormat = "download <user> <path to file on user> <path to save>";
    private final UsersFileManager usersFileManager;

    private static final String INVALID_FILE_RESPONSE = "ERROR";
    private final BlockingQueue<FileRequest> downloadQueue;

    public DownloadCommand(UsersFileManager usersFileManager, BlockingQueue<FileRequest> downloadQueue) {
        this.usersFileManager = usersFileManager;
        this.downloadQueue = downloadQueue;
    }

    private  List<String> getUserHostAndPort(String username) {

        try {
            String userData = usersFileManager.getUserData(username);
            String[] hostAndPort = userData.split("-")[1].split(":");
            return List.of(hostAndPort[0].substring(1), hostAndPort[1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkPathToSave(String pathToSave) {
        try {
            Path path = Paths.get(pathToSave);
            Path parent = path.getParent();

            if (parent == null || !Files.exists(parent)) {
                throw new InvalidCommand("Invalid path to save. Path should exist");
            }

            if(Files.isDirectory(path)) {
                throw new InvalidCommand("Invalid path to save. " +
                        "Path should have a filename");
            }

            if(Files.isRegularFile(path)) {
                throw new InvalidCommand("Invalid path to save. " +
                        "Path should not be an existing file");
            }
        } catch(InvalidPathException e) {
            throw new InvalidCommand("Invalid path to save");
        }
    }

    private ServerConnection connectToUser(String username) throws ServerConnectionException {
        List<String> hostAndPort = getUserHostAndPort(username);

        return new ServerConnection(hostAndPort.getFirst(),
                Integer.parseInt(hostAndPort.getLast()),
                1024);
    }

    private SocketChannel connectToUserOld(String username) {

        //String[] hostAndPort = getUserHostAndPort(username).split(":");

        SocketChannel socketChannel = null;

        // USER MAY NOT EXIST
        try {
            socketChannel = SocketChannel.open();
            //final var success = socketChannel.connect(new InetSocketAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
            ////TODO: change exceptions to correct exceptions
            //if (!success) throw new InvalidCommand("File cannot be downloaded. Can't connect to user");

        } catch (IOException e) {
            throw new InvalidCommand(e.getMessage());
        }

        return socketChannel;
    }

    @Override
    public String execute(List<String> list) {
        checkCharacters(list);
        checkNumberOfArguments(list, COMMAND_ARGUMENTS_COUNT, correctFormat);

        final String username = list.get(0);
        final String pathToFileOnUser = list.get(1);
        final String pathToSave = list.get(2);

        checkUsername(username);
        checkPathToSave(pathToSave);

        List<String> hostAndPort = getUserHostAndPort(username);

        //TODO: handle exceptions correctly
        try {
            ServerConnection serverConnection = new ServerConnection(hostAndPort.getFirst(),
                    Integer.parseInt(hostAndPort.getLast()),
                    1024);

            String portReply = serverConnection.communicateWithServer(pathToFileOnUser);

            serverConnection.closeConnection();

            if(portReply.equals(INVALID_FILE_RESPONSE)) {
                throw new InvalidCommand("File cannot be downloaded. File does not exist");
            }

            boolean isAdded = downloadQueue.offer(
                    new FileRequest(Path.of(pathToFileOnUser),
                            Path.of(pathToSave),
                            hostAndPort.getFirst(),
                            Integer.parseInt(portReply)));

            System.out.println("FileReceiver: added request with port " + portReply + " for file " + pathToFileOnUser);
            if(!isAdded) {
                throw new InvalidCommand("File cannot be downloaded. Too many download requests");
            }


        } catch (ServerConnectionException | IOException e) {
            throw new InvalidCommand("File cannot be downloaded. Can't connect to user");
        }

        return "Added new download request for file " + pathToFileOnUser;
    }

    @Override
    public String toString() {
        return "download";
    }
}
