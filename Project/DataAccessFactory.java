import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataAccessFactory {
    private final Map<Integer, DataAccess> instances = new HashMap<>();
    private final int cacheSize;
    private final Path pathToDataDir;

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

    DataAccess getForStation(int stn){
        return Optional.ofNullable(instances.get(stn)).orElseGet(()-> createForStation(stn));
    }
}
