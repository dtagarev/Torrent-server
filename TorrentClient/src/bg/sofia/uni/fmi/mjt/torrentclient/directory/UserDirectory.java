package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

public interface UserDirectory {
    void addFile(Path file); //-- synchronised ?
    //void removeFile() -- synchronised ?

    List<Path> getSeedingFiles(); //-- send (get in usable format) file info of all available files -- synchronised ?
    Path getFile(String filename) throws FileNotFoundException; //-- synchronised ?
    boolean containsFile(String filename);
}
