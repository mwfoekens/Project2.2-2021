import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DataAccess {
    private final int cacheSize;
    private final Path filepath;
    private final Deque<Measurement> cache;


    public DataAccess(Path pathToDataDir, int stnId, int cacheSize) throws IOException {
        this.cacheSize = cacheSize;
        cache = new LinkedList<>();

        if (!Files.exists(pathToDataDir)) {
            throw new NoSuchFileException(pathToDataDir + " does not exist.");
        }
        Path stnPath = pathToDataDir.resolve(Integer.toString(stnId));
        if (!Files.exists(stnPath)) {
            Files.createDirectory(stnPath);
        }

        filepath = stnPath;
        List<Measurement> allRows = readAll();
        Collections.reverse(allRows);
        cache.addAll(allRows.stream().limit(cacheSize).collect(Collectors.toList()));
    }

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

    synchronized List<Measurement> readCache() {
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

    synchronized boolean hasMeasurements(){
        return !cache.isEmpty();
    }

    //    Windspeed and wind direction of all stations within 1500 km range of Nairobi (so also at sea)
//    Top 10 air pressure of all stations in Kenya and Djibouti
    //KENYA STATION NR
    //636950
    //637140
    //637090
    //637080
    //637230
    //637200
    //637400 NAIROBI
    //637660
    //637930
    //638200
    //636120
    //636410
    //636190
    //636240
    //636610
    //636860
    //636710
    //DJIBOUTI STATION NR
    //631260
    //631250

}
