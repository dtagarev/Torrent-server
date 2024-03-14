package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.io.File;
import java.nio.file.Path;

public interface UserDirectory {
    //??? getSeedingFiles() -- send ( get in usable format) file info of all available files -- synchronised ?
    //File getFile(String filename) -- synchronised ?
    //void addFile() -- synchronised ?
    //void removeFile() -- synchronised ?
    //void getFiles() -- synchronised ?
    //write me all the functions that the user directory should have

    Path getFile(String filename); //-- synchronised ?
}
