import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

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
        DataSaver dataSaver = new DataSaver(dataAccessFactory);
        Thread dataThread = new Thread(dataSaver);
        dataThread.start();
        Thread dataThread2 = new Thread(dataSaver);
        dataThread2.start();

        ServerSocket server = new ServerSocket(PORT);
        System.err.println("Server started. Maximum amount of threads: " + maxnrofConnections);

        while (true) {
            connection = server.accept();
            //System.err.println("New connection accepted..handing it over to worker thread");
            Thread worker = new Thread(new Worker(connection, dataSaver, dataAccessFactory));
            worker.start();
        }
    }
}
