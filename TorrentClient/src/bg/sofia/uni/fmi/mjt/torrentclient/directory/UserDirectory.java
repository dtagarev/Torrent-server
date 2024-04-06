package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.nio.file.Path;
import java.util.List;

public interface UserDirectory {
    //void addFile() -- synchronised ?
    //void removeFile() -- synchronised ?

    List<String> getSeedingFiles(); //-- send (get in usable format) file info of all available files -- synchronised ?
    Path getFile(String filename); //-- synchronised ?
    boolean containsFile(String filename);
}
