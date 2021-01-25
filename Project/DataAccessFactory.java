import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DataAccessFactory is responsible for giving each station number a DataAccess Object
 *
 * @author Merel Foekens
 * @version 1.0
 */
public class DataAccessFactory {
    private final Map<Integer, DataAccess> instances = new HashMap<>();
    private final int cacheSize;
    private final Path pathToDataDir;

    /**
     * Constructor for DataAccessFactory
     *
     * @param pathToDataDir Directory where all station numbers should be stored
     * @param cacheSize How big the cache should be
     * @throws IOException IOException to make IntelliJ & Java happy
     */
    public DataAccessFactory(Path pathToDataDir, int cacheSize) throws IOException {
        this.cacheSize = cacheSize;
        this.pathToDataDir = pathToDataDir;
        if (!Files.exists(pathToDataDir)) {
            Files.createDirectory(pathToDataDir);
            System.out.println("Directory " + pathToDataDir + " created.");
        } else{
            System.err.println("Directory " + pathToDataDir + " already exists.");
        }
    }

    /**
     * Creates a DataAccess Object for a station.
     *
     * @param stn Station number for which a DataAccess Object should be created/retrieved
     * @return Returns a new DataAccess Object if it doesn't yet exist, returns the existing one if possible
     */
    private synchronized DataAccess createForStation(int stn) {
        try {
            if (instances.containsKey(stn)) {
                return instances.get(stn);
            } else {
                DataAccess dataAccess = new DataAccess(pathToDataDir, stn, cacheSize);
                instances.put(stn, dataAccess);
                return dataAccess;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves DataAccess Object for the station, creates a new one if not found
     *
     * @param stn Station number for which a DataAccess Object should be created/retrieved
     * @return Returns a DataAccess Object.
     */
    DataAccess getForStation(int stn){
        return Optional.ofNullable(instances.get(stn)).orElseGet(()-> createForStation(stn));
    }
}
