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
 * @version 1
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

    /**
     * Checks if .csv file in targetDir exists. (Necessity explained in compareData() in Worker class)
     *
     * @param targetDir targetDir should be a station number
     * @return returns true if .csv file exists, false if it doesn't.
     */
    boolean csvExists(Path targetDir){
        Path path = pathToDataDir.resolve(targetDir).resolve("Measurements.csv");
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
     * Retrieve temperature
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of temperatures
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveTemp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[3]));
    }

    /**
     * Retrieve humidity
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of humidity
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveDewp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[4]));
    }


    /**
     * Retrieve station pressure
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of station pressure
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveStp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[5]));
    }

    /**
     * Retrieve sea level pressure
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of sea level pressure
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveSlp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[6]));
    }

    /**
     * Retrieve visibility
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of visibilties
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveVisib(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[7]));
    }

    /**
     * Retrieve wind speed
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of wind speed
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveWdsp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[8]));
    }

    /**
     * Retrieve precipitation
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of precipitation
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrievePrcp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[9]));
    }

    /**
     * Retrieve snow fall
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of snow fall
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveSndp(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[10]));
    }

    ArrayList<String> retrieveFrshtt(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> row[11]);
    }

    /**
     * Retrieve cloudiness
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of cloudiness
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Float> retrieveCldc(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Float.parseFloat(row[12]));
    }

    /**
     * Retrieve wind direction.
     *
     * @param targetDir targetDir should be a station number
     * @return returns a list of wind direction
     * @throws IOException had to add otherwise intelliJ cries
     */
    ArrayList<Integer> retrieveWnddir(Path targetDir) throws IOException {
        return retrieveColumn(targetDir, row -> Integer.parseInt(row[13]));
    }

    private <R> ArrayList<R> retrieveColumn(Path targetDir, Function<String[], R> get) throws IOException {
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
