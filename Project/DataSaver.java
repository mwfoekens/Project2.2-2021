import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Creates new directories to save data in. Has a queue of Measurements, and writes to csv files.
 *
 * @author Merel Foekens
 * @version 1.0
 */
public class DataSaver implements Runnable {
    private final DataAccessFactory dataAccessFactory;
    public final BlockingQueue<Measurement> queue = new LinkedBlockingQueue<>();

    /**
     * Constructor goes to desired path, checks if the desired Directory exists. If not, it creates a new one.
     *
     * @param dataAccessFactory DataAccessFactory that does things.
     */
    public DataSaver(DataAccessFactory dataAccessFactory) {
        this.dataAccessFactory = dataAccessFactory;
    }

    /**
     * !!! Might have to change this method a bit more considering it's supposed to run on a separate VM. !!!
     * <p>
     * Function waits until it can take a set of values from the queue, then creates a new dir for the station number
     * if it doesn't already exist. It then writes/appends to a csv file.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Measurement values = queue.take();
                DataAccess dataAccess = dataAccessFactory.getForStation(values.getStn());
                dataAccess.writeRow(values);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
