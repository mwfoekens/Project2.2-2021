import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Creates new directories to save data in. Has a queue of Measurements, and writes to csv files.
 *
 * @author Merel Foekens
 * @author https://github.com/mwfoekens
 * @version 1.0
 */
public class DataSaver implements Runnable {
    private final String dirPath;
    private final String dataDir;
    public final BlockingQueue<Measurement> queue = new LinkedBlockingQueue<>();

    /**
     * Constructor goes to desired path, checks if the desired Directory exists. If not, it creates a new one.
     *
     * @param dirPath Path where dataDir directory should be saved
     * @param dataDir Directory where the data is going to be saved
     * @throws IOException had to add this exception otherwise IntelliJ cries.
     */
    public DataSaver(String dirPath, String dataDir) throws IOException {
        this.dirPath = dirPath;
        this.dataDir = dataDir;

        if (!Files.exists(Path.of(dataDir))) {
            Files.createDirectory(Path.of(dataDir));
            System.out.println("Directory successfully created.");
        } else {
            System.err.println("Directory with name \"" + dataDir + "\" already exists. If existing directory is not" +
                    " correct, QUIT application immediately.");
        }
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
                // Grabs a new value from the queue and makes a new directory for the station if it doesn't already exist
                Measurement values = queue.take();
                String stnPath = dirPath + "\\" + dataDir + "\\" + values.getStn();
                if (!Files.exists(Path.of(stnPath))) {
                    Files.createDirectory(Path.of(stnPath));
                }

                // Writes to .csv file. Doesn't need to be closed, because try-with-resources takes care of that.
                try (FileWriter fileWriter = new FileWriter(String.valueOf(Path.of(stnPath, "Measurements.csv")), true)) {

                    writeCell(fileWriter, values.getStn());
                    writeCell(fileWriter, values.getDate());
                    writeCell(fileWriter, values.getTime());
                    writeCell(fileWriter, values.getTemp());
                    writeCell(fileWriter, values.getDewp());
                    writeCell(fileWriter, values.getStp());
                    writeCell(fileWriter, values.getSlp());
                    writeCell(fileWriter, values.getVisib());
                    writeCell(fileWriter, values.getWdsp());
                    writeCell(fileWriter, values.getPrcp());
                    writeCell(fileWriter, values.getSndp());
                    writeCell(fileWriter, values.getFrshtt());
                    writeCell(fileWriter, values.getCldc());
                    writeCellnl(fileWriter, values.getWnddir());

                    // Flush for good practice
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Helper function to make code more readable. Adds comma after data.
     *
     * @param fileWriter filewriter that writes to .csv
     * @param input what to add to the .csv
     * @throws IOException had to add otherwise intelliJ cries
     */
    private void writeCell(FileWriter fileWriter, String input) throws IOException {
        fileWriter.append(input).append(",");
    }

    /**
     * Helper function to make code more readable. Adds newline after data.
     *
     * @param fileWriter filewriter that writes to .csv
     * @param input input what to add to the .csv
     * @throws IOException had to add otherwise intelliJ cries
     */
    private void writeCellnl(FileWriter fileWriter, String input) throws IOException {
        fileWriter.append(input).append("\n");
    }
}
