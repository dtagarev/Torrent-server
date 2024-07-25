# Torrent Server: A P2P, Multithreaded Java Application
This project implements a peer-to-peer (P2P) torrent server using Java's multithreading capabilities.
The server facilitates the sharing and downloading of files across a distributed network of peers.
## Usage Instructions for Torrent Server
### Client Commands

| Command      | Arguments                                    | Description                                                                                   |
|--------------|----------------------------------------------|-----------------------------------------------------------------------------------------------|
| `register`   | `<user> <file1, file2, file3, ..., fileN>`   | Announce which files are available for download from the client. `user` specifies a unique username. |
| `unregister` | `<user> <file1, file2, file3, ..., fileN>`   | Notify the server that certain files are no longer available for download from the client.      |
| `list-files` | N/A                                          | Return a list of available files along with the users from whom they can be downloaded.         |
| `download`   | `<user> <path to file on user> <path to save>` | Download a specified file from the given user. The client retrieves the file from the user's mini-server and saves it locally. |

## License
This project is licensed under the MIT License. See the [LICENSE](https://github.com/dtagarev/Torrent-server/blob/master/LICENSE) file for more details.
