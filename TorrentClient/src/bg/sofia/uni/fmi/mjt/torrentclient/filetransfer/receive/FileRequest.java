package bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.receive;

import java.nio.file.Path;

public record FileRequest(Path from, Path to, String host, int port) {
}
