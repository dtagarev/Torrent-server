package bg.sofia.uni.fmi.mjt.torrentclient.miniserver;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import bg.sofia.uni.fmi.mjt.torrentclient.exception.UserNotFoundInFile;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UsersFileManager;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

public class DownloadCommand {

    private ExecutorService executorService;
    private UsersFileManager userConnectionData;
    private ErrorHandler errorHandler;
    private UserInterface ui;

    //sinhronized queue trqbva da e koqto se podava na fileReceivera
    private Queue<String> downloadQueue;

    private static final int DOWNLOAD_COMMAND_NAME_IDX = 0;
    private static final int DOWNLOAD_COMMAND_USER_IDX = 1;
    private static final int DOWNLOAD_COMMAND_FILES_IDX = 2;
    private static final int DOWNLOAD_COMMAND_PATH_IDX = 3;

    public DownloadCommand(ErrorHandler errorHandler, UsersFileManager usersFileManager, UserInterface userInterface) {
        this.executorService = newVirtualThreadPerTaskExecutor();
        this.errorHandler = errorHandler;
        this.userConnectionData = usersFileManager;
        this.ui = userInterface;
    }

    private void checkPathIsDirectory(Path filePath) {
        if(!Files.isDirectory(filePath)) {
            throw new IllegalArgumentException("The path must be a working directory");
        }
    }

    private void checkPathIsNonexistentFileName(Path path) {
        if (Files.exists(path)) {
            throw new IllegalArgumentException("The path must not be an existing file or directory");
        }
    }

    private List<String> takeNamePathAndFiles(String command) {
        String[] cmdParts = command.split(" ");
        String[] files = cmdParts[DOWNLOAD_COMMAND_FILES_IDX].split(",");

        List<String> cmdList = List.of(cmdParts[DOWNLOAD_COMMAND_USER_IDX], cmdParts[DOWNLOAD_COMMAND_PATH_IDX]);
        cmdList.addAll(List.of(files));
        return cmdList;
    }
    private List<String> getUserHostAndPort(String userName) throws FileNotFoundException, UserNotFoundInFile, IOException {
        String userData = userConnectionData.getUserData(userName);
        //TODO: do i need the / inside the host? Now i remove it with the + 1
        String userHostAndPort = userData.substring(userData.indexOf('-') + 1);

        return List.of(userHostAndPort.split(":"));
    }

    public void initiateDownload(String command) {

        List<String> cmdList = takeNamePathAndFiles(command);

        try {
            List<String> userHostAndPort = getUserHostAndPort(cmdList.get(DOWNLOAD_COMMAND_USER_IDX));

            Path filePath = Path.of(cmdList.get(1));
            List<String> files = new ArrayList<>(cmdList.subList(2, cmdList.size()));

            //TODO: downloan command and the fileReceiverJob should be like the lab with the pictures and the queue
            if(files.size() > 1) {
                //TODO: process multible files
            } else {
                //TODO: process one file
            }
        } catch (FileNotFoundException e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("Cannot find necessary user information to continue the download");
        } catch (UserNotFoundInFile e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("User not found");
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("An error occurred while trying to read the user data");
        }
    }
}
