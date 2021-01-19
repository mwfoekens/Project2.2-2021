import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

public class Main {
    public static final int PORT = 2500;
    private static final int maxnrofConnections = 1;
    public static TelSemafoor mijnSemafoor = new TelSemafoor(maxnrofConnections);

    public static void main(String[] args) throws JAXBException, IOException {
        Socket connection;
        // TODO CHANGE dirPATH TO SHARED FILESYSTEM!!!
        DataSaver dataSaver = new DataSaver(Path.of("D:\\Programmershit\\Project2.2-2021"), "Data");
        Thread dataThread = new Thread(dataSaver);
        dataThread.start();
        ServerSocket server = new ServerSocket(PORT);
        System.err.println("MT Server started. Maximum amount of threads: " + maxnrofConnections);

        while (true) {
            connection = server.accept();
            //System.err.println("New connection accepted..handing it over to worker thread");
            Thread worker = new Thread(new Worker(connection, dataSaver));
            worker.start();
        }
    }
}
