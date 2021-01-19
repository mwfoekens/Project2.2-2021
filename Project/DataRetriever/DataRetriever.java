package DataRetriever;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Woof Woof.
 * Retrieves data from .csv files
 *
 * @author Merel Foekens
 * @author https://github.com/mwfoekens
 * @version 0.5
 */
public class DataRetriever {
    private final String pathToDataDir;

    public DataRetriever(String pathToDataDir) {
        this.pathToDataDir = pathToDataDir;
        if (!Files.exists(Path.of(pathToDataDir))) {
            System.err.println("Directory does not exist. Path: " + pathToDataDir);
        }
    }

    /**
     * Checks if target directory exists
     *
     * @param targetDir targetDir should be a station number
     * @return returns whether the station number exists
     */
    boolean dirExists(String targetDir) {
        String path = pathToDataDir + "\\" + targetDir;
        return Files.exists(Path.of(path));
    }

    /**
     * Retrieves all data from a specific station number.
     *
     * @param targetDir targetDir should be a station number
     * @throws IOException had to add this exception otherwise IntelliJ cries.
     */

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
    void retrieveData(String targetDir) throws IOException {
        if (dirExists(targetDir)) {
            String path = pathToDataDir + "\\" + targetDir;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path + "\\Measurements.csv"))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    // what to do with each line
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Target directory does not exist");
        }
    }

    ArrayList<Float> retrieveTopTenStationPressure(String targetDir) throws IOException {
        if (dirExists(targetDir)) {
            String path = pathToDataDir + "\\" + targetDir;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path + "\\Measurements.csv"))) {
                String line;
                String[] data;
                ArrayList<Float> stationPressure = new ArrayList<>();
                while ((line = bufferedReader.readLine()) != null) {
                    data = line.split(",");
                    float stp = Float.parseFloat(data[5]);
                    stationPressure.add(stp);
                }
                Collections.reverse(stationPressure);
                return stationPressure;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Target directory does not exist");
        }
        return null;
    }

    ArrayList<Float> retrieveTopTenSeaLevelPressure(String targetDir) throws IOException {
        if (dirExists(targetDir)) {
            String path = pathToDataDir + "\\" + targetDir;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path + "\\Measurements.csv"))) {
                String line;
                String[] data;
                ArrayList<Float> seaLevelPressure = new ArrayList<>();
                while ((line = bufferedReader.readLine()) != null) {
                    data = line.split(",");
                    float stp = Float.parseFloat(data[6]);
                    seaLevelPressure.add(stp);
                }
                Collections.reverse(seaLevelPressure);
                return seaLevelPressure;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Target directory does not exist");
        }
        return null;
    }

    ArrayList<Integer> retrieveWindDirection(String targetDir) throws IOException {
        if (dirExists(targetDir)) {
            String path = pathToDataDir + "\\" + targetDir;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path + "\\Measurements.csv"))) {
                String line;
                String[] data;
                ArrayList<Integer> windDirection = new ArrayList<>();
                while ((line = bufferedReader.readLine()) != null) {
                    data = line.split(",");
                    int wnddir = Integer.parseInt(data[13]);
                    windDirection.add(wnddir);
                }
                Collections.reverse(windDirection);
                return windDirection;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Target directory does not exist");
        }
        return null;
    }

    ArrayList<Float> retrieveWindSpeed(String targetDir) throws IOException {
        if (dirExists(targetDir)) {
            String path = pathToDataDir + "\\" + targetDir;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path + "\\Measurements.csv"))) {
                String line;
                String[] data;
                ArrayList<Float> windDirection = new ArrayList<>();
                while ((line = bufferedReader.readLine()) != null) {
                    data = line.split(",");
                    float wdsp = Float.parseFloat(data[8]);
                    windDirection.add(wdsp);
                }
                Collections.reverse(windDirection);
                return windDirection;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Target directory does not exist");
        }
        return null;
    }
}
