package bg.sofia.uni.fmi.mjt.torrentclient.command.transfer;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.BaseCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerConnection;
import bg.sofia.uni.fmi.mjt.torrentclient.exceptions.DownloadCommandConnectionException;
import bg.sofia.uni.fmi.mjt.torrentclient.exceptions.FileDoesNotExistException;
import bg.sofia.uni.fmi.mjt.torrentclient.exceptions.ServerConnectionException;
import bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.receive.FileRequest;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UsersFileManager;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class DownloadCommand extends BaseCommand implements Command {
    private static final int COMMAND_ARGUMENTS_COUNT = 3;
    private static final String CORRECT_FORMAT = "download <user> <path to file on user> <path to save>";
    private final UsersFileManager usersFileManager;

    private static final String INVALID_FILE_RESPONSE = "ERROR";
    private final BlockingQueue<FileRequest> downloadQueue;

    private static final int SERVER_CONNECTION_BUFFER_SIZE = 1024;

    public DownloadCommand(UsersFileManager usersFileManager, BlockingQueue<FileRequest> downloadQueue) {
        this.usersFileManager = usersFileManager;
        this.downloadQueue = downloadQueue;
    }

    private  List<String> getSenderHostAndPort(String username) {

        try {
            String senderData = usersFileManager.getUserData(username);
            String[] hostAndPort = senderData.split("-")[1].split(":");
            return List.of(hostAndPort[0].substring(1), hostAndPort[1]);
        } catch (IOException e) {
            throw new InvalidCommand(e.toString());
        }
    }

    private void checkPathToSave(String pathToSave) {
        try {
            Path path = Paths.get(pathToSave);
            Path parent = path.getParent();

            if (parent == null || !Files.exists(parent)) {
                throw new InvalidCommand("Invalid path to save. Path should exist");
            }

            if (Files.isDirectory(path)) {
                throw new InvalidCommand("Invalid path to save. " +
                        "Path should have a filename");
            }

            if (Files.isRegularFile(path)) {
                throw new InvalidCommand("Invalid path to save. " +
                        "Path should not be an existing file");
            }
        } catch (InvalidPathException e) {
            throw new InvalidCommand("Invalid path to save");
        }
    }

    private String getSenderFilePort(List<String> hostAndPort, String pathToFileOnUser)
        throws ServerConnectionException, IOException {

        ServerConnection serverConnection = new ServerConnection(hostAndPort.getFirst(),
            Integer.parseInt(hostAndPort.getLast()),
            SERVER_CONNECTION_BUFFER_SIZE);

        String portReply = serverConnection.communicateWithServer(pathToFileOnUser);

        serverConnection.closeConnection();
        return portReply;
    }

    @Override
    public String execute(List<String> list) {
        checkCharacters(list);
        checkNumberOfArguments(list, COMMAND_ARGUMENTS_COUNT, CORRECT_FORMAT);

        final String username = list.get(0);
        final String pathToFileOnUser = list.get(1);
        final String pathToSave = list.get(2);

        checkUsername(username);
        checkPathToSave(pathToSave);

        List<String> hostAndPort = getSenderHostAndPort(username);

        try {
            String senderFilePort = getSenderFilePort(hostAndPort, pathToFileOnUser);

            if (senderFilePort.equals(INVALID_FILE_RESPONSE)) {
                throw new FileDoesNotExistException("File cannot be downloaded. File does not exist");
            }

            downloadQueue.offer(new FileRequest(Path.of(pathToFileOnUser),
                                                Path.of(pathToSave),
                                                hostAndPort.getFirst(),
                                                Integer.parseInt(senderFilePort)));
        } catch (ServerConnectionException | IOException e) {
            throw new DownloadCommandConnectionException("File cannot be downloaded. Can't connect to user");
        }

        return "Added new download request for file " + pathToFileOnUser;
    }

    @Override
    public String toString() {
        return "download";
    }
}
