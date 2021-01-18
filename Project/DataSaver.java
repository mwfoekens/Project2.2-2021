import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataSaver implements Runnable {
    private final String dirPath;
    private final String dataDir;
    private FileWriter fileWriter;
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
        Path path = Paths.get(dirPath);

        if (!Files.exists(Path.of(dataDir))) {
            Files.createDirectory(Path.of(dataDir));
            System.out.println("Directory successfully created.");
        } else {
            System.err.println("Directory with name \"" + dataDir + "\" already exists. If existing directory is not" +
                    " correct, QUIT application immediately.");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                Measurement values = queue.take();
                String stnPath = dirPath + "\\" + dataDir + "\\" + values.getStn();
                if (!Files.exists(Path.of(stnPath))) {
                    Files.createDirectory(Path.of(stnPath));
                }

                fileWriter = new FileWriter(String.valueOf(Path.of(stnPath, "Measurements.csv")), true);
                writeCell(values.getStn());
                writeCell(values.getDate());
                writeCell(values.getTime());
                writeCell(values.getTemp());
                writeCell(values.getDewp());
                writeCell(values.getStp());
                writeCell(values.getSlp());
                writeCell(values.getVisib());
                writeCell(values.getWdsp());
                writeCell(values.getPrcp());
                writeCell(values.getSndp());
                writeCell(values.getFrshtt());
                writeCell(values.getCldc());
                fileWriter.append(values.getWnddir()).append("\n");
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function
     *
     * @param input what to add to the csv
     * @throws IOException
     */
    private void writeCell(String input) throws IOException {
        fileWriter.append(input + ",");
    }
}
