package DataRetriever;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * Woof Woof.
 * Retrieves data from .csv files
 *
 * @author Merel Foekens
 * @author https://github.com/mwfoekens
 * @version 0.8
 */
public class DataRetriever {
    private final Path pathToDataDir;

    public DataRetriever(Path pathToDataDir) {
        this.pathToDataDir = pathToDataDir;
        if (!Files.exists(pathToDataDir)) {
            System.err.println("Directory does not exist. Path: " + pathToDataDir);
        }
    }

    /**
     * Checks if target directory exists
     *
     * @param targetDir targetDir should be a station number
     * @return returns whether the station number directory exists
     */
    boolean dirExists(Path targetDir) {
        Path path = pathToDataDir.resolve(targetDir);
        return Files.exists(path);
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

    /**
     * Retrieve station pressure
     * @param targetDir targetDir should be a station number
     * @return returns a list of station pressure
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveStp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[5]));
    }

    /**
     * Retrieve sea level pressure
     * @param targetDir targetDir should be a station number
     * @return returns a list of sea level pressure
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveSlp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[6]));
    }

    /**
     * Retrieve wind speed
     * @param targetDir targetDir should be a station number
     * @return returns a list of wind speed
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveWdsp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[8]));
    }

    /**
     * Retrieve wind direction.
     * @param targetDir targetDir should be a station number
     * @return returns a list of wind direction
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Integer> retrieveWnddir(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Integer.parseInt(row[13]));
    }

    <R> ArrayList<R> retrieveColumn(Path targetDir, Function<String[], R> get) throws IOException {
        if (!dirExists(targetDir)) {
            throw new IOException("Target directory " + targetDir + " does not exist");
        } else {
            try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(String.valueOf(pathToDataDir.resolve(targetDir).resolve("Measurements.csv"))))) {
                String line;
                String[] data;
                ArrayList<R> results = new ArrayList<>();
                while ((line = bufferedReader.readLine()) != null) {
                    data = line.split(",");
                    R cell = get.apply(data);
                    results.add(cell);
                }
                return results;
            }
        }
    }
}
