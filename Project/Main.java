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
    public static int PORT;
    private static final int MAXNROF_CONNECTIONS = 800;
    public static CountSemaphore semaphore = new CountSemaphore(MAXNROF_CONNECTIONS);

    public static void main(String[] args) throws JAXBException, IOException {
        Socket connection;
        DataAccessFactory dataAccessFactory = new DataAccessFactory(Path.of(args[0]), Integer.parseInt(args[1]));
        Executor executor = Executors.newCachedThreadPool();

        PORT = Integer.parseInt(args[2]);
        ServerSocket server = new ServerSocket(PORT);
        System.err.println("Server started.\nMaximum amount of threads: " + MAXNROF_CONNECTIONS +
                "\nListening on port: " + PORT + "\nCache size: " + args[1]);

        while (true) {
            connection = server.accept();
            Thread worker = new Thread(new Worker(connection, dataAccessFactory, executor));
            worker.start();
        }
    }
}
