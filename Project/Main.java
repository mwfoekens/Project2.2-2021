import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Main class
 *
 * @author Hanzehogeschool
 * Heavily edited by Merel Foekens
 * @version 2.0
 */
public class Main {
    public static final int PORT = 2500;
    private static final int maxnrofConnections = 800;
    public static CountSemaphore semaphore = new CountSemaphore(maxnrofConnections);

    public static void main(String[] args) throws JAXBException, IOException {
        Socket connection;
        // TODO CHANGE dirPATH TO SHARED FILESYSTEM!!!
        DataAccessFactory dataAccessFactory = new DataAccessFactory(Path.of("D:\\Programmershit\\Project2.2-2021\\Data"), 20);
        Executor executor = Executors.newCachedThreadPool();

        ServerSocket server = new ServerSocket(PORT);
        System.err.println("Server started. Maximum amount of threads: " + maxnrofConnections);

        while (true) {
            connection = server.accept();
            Thread worker = new Thread(new Worker(connection, dataAccessFactory, executor));
            worker.start();
        }
    }
}
