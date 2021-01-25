import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * Exists for each station number. Both retrieves & saves data from CSV file.
 *
 * @author Merel Foekens
 * @version 1.0
 */
public class DataAccess {
    private final int cacheSize;
    private final Path filepath;
    private final Deque<Measurement> cache;

    /**
     * @param pathToDataDir Directory where all station numbers should be stored
     * @param stn           Station number
     * @param cacheSize     size of the cache
     * @throws IOException IOException to make IntelliJ & Java happy
     */
    public DataAccess(Path pathToDataDir, int stn, int cacheSize) throws IOException {
        this.cacheSize = cacheSize;
        cache = new ConcurrentLinkedDeque<>();

        if (!Files.exists(pathToDataDir)) {
            throw new NoSuchFileException(pathToDataDir + " does not exist.");
        }

        Path stnPath = pathToDataDir.resolve(Integer.toString(stn));

        if (!Files.exists(stnPath)) {
            Files.createDirectory(stnPath);
        }

        filepath = stnPath;

        // read all data and load it into cache.
        List<Measurement> allRows = readAll();
        Collections.reverse(allRows);
        cache.addAll(allRows.stream().limit(cacheSize).collect(Collectors.toList()));
    }

    /**
     * Updates cache, and writes to the CSV file.
     *
     * @param values a Measurement.
     */
    synchronized void writeRow(Measurement values) {
        cache.addLast(values);

        if (cache.size() > cacheSize) {
            cache.removeFirst();
        }

        // Writes to .csv file. Doesn't need to be flushed/closed, because try-with-resources takes care of that.
        // Creates/appends to the .csv file.
        try (FileWriter fileWriter = new FileWriter(String.valueOf(filepath.resolve("Measurements.csv")), true)) {

            writeCell(fileWriter, Integer.toString(values.getStn()));
            writeCell(fileWriter, values.getDate());
            writeCell(fileWriter, values.getTime());
            writeCell(fileWriter, Float.toString(values.getTemp()));
            writeCell(fileWriter, Float.toString(values.getDewp()));
            writeCell(fileWriter, Float.toString(values.getStp()));
            writeCell(fileWriter, Float.toString(values.getSlp()));
            writeCell(fileWriter, Float.toString(values.getVisib()));
            writeCell(fileWriter, Float.toString(values.getWdsp()));
            writeCell(fileWriter, Float.toString(values.getPrcp()));
            writeCell(fileWriter, Float.toString(values.getSndp()));
            writeCell(fileWriter, values.getFrshtt());
            writeCell(fileWriter, Float.toString(values.getCldc()));
            writeCellNl(fileWriter, Integer.toString(values.getWnddir()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all lines in a CSV file. Transforms each line to a Measurement Object
     *
     * @return Returns a list of all rows.
     * @throws IOException IOException to make IntelliJ & Java happy
     */
    synchronized List<Measurement> readAll() throws IOException {
        try {
            List<Measurement> allRows = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(String.valueOf(filepath.resolve("Measurements.csv"))));
            String line;
            List<String> data;

            while ((line = bufferedReader.readLine()) != null) {
                data = Arrays.asList(line.split(","));
                allRows.add(new Measurement(data));
            }
            return allRows;
        } catch (FileNotFoundException e) {
            return List.of();
        }
    }

    /**
     * Prints cache
     *
     * @return Returns the cache in an ArrayList
     */
    List<Measurement> readCache() {
        return new ArrayList<>(cache);
    }

    /**
     * Helper function to make code more readable. Adds comma after data.
     *
     * @param fileWriter file writer that writes to .csv
     * @param input      what to add to the .csv
     * @throws IOException had to add otherwise intelliJ cries
     */
    private void writeCell(FileWriter fileWriter, String input) throws IOException {
        fileWriter.append(input).append(",");
    }

    /**
     * Helper function to make code more readable. Adds a new line after data.
     *
     * @param fileWriter file writer that writes to .csv
     * @param input      input what to add to the .csv
     * @throws IOException had to add otherwise intelliJ cries
     */
    private void writeCellNl(FileWriter fileWriter, String input) throws IOException {
        fileWriter.append(input).append("\n");
    }

    /**
     * Checks if there's measurements available.
     *
     * @return Returns false if cache is empty, returns true if there's object in cache
     */
    boolean hasMeasurements() {
        return !cache.isEmpty();
    }
}
