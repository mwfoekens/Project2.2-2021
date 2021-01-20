import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


class Worker implements Runnable {
    private final Socket connection;
    private final BufferedReader bufferedReader;
    private final Unmarshaller jaxbUnmarshaller;
    //    private DBThread dbThread;
    private DataSaver dataSaver;

    public Worker(Socket connection, DataSaver dataSaver) throws JAXBException, IOException {
        this.connection = connection;
        this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JAXBContext jaxbContext = JAXBContext.newInstance(WeatherData.class);
        this.jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        this.dataSaver = dataSaver;
    }

    @Override
    public void run() {
        try {
            //System.err.println("New worker thread started");

            //check if maximum number of connections reached
            Main.mijnSemafoor.probeer();

            StringBuilder builder = null;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("<?xml")) {
                    if (builder != null) {
                        WeatherData q = getWeatherData(builder);
                        addToQueue(q);
                    }
                    builder = new StringBuilder();
                }
                builder.append(line);
            }
            WeatherData q = getWeatherData(builder);
            addToQueue(q);

            // now close the socket connection
            connection.close();
            //System.err.println("Connection closed: workerthread ending");
            // upping the semaphore.. since the connnection is gone....
            Main.mijnSemafoor.verhoog();
        } catch (IOException | InterruptedException | JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * compares entirety of the weatherdata unit, adds to the queue of DataSaver.java
     * @param q an entire weatherdata unit
     * @throws IOException had to add otherwise IntelliJ cries
     */
    private void addToQueue(WeatherData q) throws IOException {
        for (int i = 0; i < q.getMeasurements().size(); i++) {
            Measurement item = q.getMeasurements().get(i);
            if (compareData(item)){
                dataSaver.queue.add(item);
            }
        }
    }

    private WeatherData getWeatherData(StringBuilder builder) throws JAXBException {
        return (WeatherData) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(builder.toString().getBytes()));
    }

    /**
     * Checks if any measurement fields are empty.
     * @param measurement measurement that needs to be checked
     * @deprecated replaced by compareData()
     */
    private void correctIfEmpty(Measurement measurement) {
        if (measurement.getFrshtt().equals("")) {
            measurement.setFrshtt("0");
        }
    }

    /**
     * Data correcting method.
     * @param measurement measurement that needs to be checked.
     * @return returns whether measurement should be added. (Is only false if it's the first measurement of that station and not within proper ranges).
     * @throws IOException had to add otherwise IntelliJ cries
     */
    private boolean compareData(Measurement measurement) throws IOException {

        DataRetriever dataRetriever = new DataRetriever(Path.of("D:\\Programmershit\\Project2.2-2021\\Data"));

        // if the directory exists, the Measurements.csv file also exists.
        if (!dataRetriever.dirExists(Path.of(String.valueOf(measurement.getStn())))) {
            return checkUltimateValues(measurement);
        } else {
            List<Float> temp = dataRetriever.retrieveTemp(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).collect(Collectors.toList());
            measurement.setTemp(repairField(temp, measurement.getTemp()));
            List<Float> dewp = dataRetriever.retrieveDewp(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).collect(Collectors.toList());
            measurement.setDewp(repairField(dewp, measurement.getDewp()));
            List<Float> stp = dataRetriever.retrieveStp(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).collect(Collectors.toList());
            measurement.setStp(repairField(stp, measurement.getStp()));
            List<Float> slp = dataRetriever.retrieveSlp(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).collect(Collectors.toList());
            measurement.setSlp(repairField(slp, measurement.getSlp()));
            List<Float> visib = dataRetriever.retrieveVisib(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).collect(Collectors.toList());
            measurement.setVisib(repairField(visib, measurement.getVisib()));
            List<Float> wdsp = dataRetriever.retrieveWdsp(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).collect(Collectors.toList());
            measurement.setWdsp(repairField(wdsp, measurement.getWdsp()));
            List<Float> prcp = dataRetriever.retrievePrcp(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).collect(Collectors.toList());
            measurement.setPrcp(repairField(prcp, measurement.getPrcp()));
            List<Float> sndp = dataRetriever.retrieveSndp(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).collect(Collectors.toList());
            measurement.setSndp(repairField(sndp, measurement.getSndp()));
            List<Float> cldc = dataRetriever.retrieveCldc(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).collect(Collectors.toList());
            measurement.setCldc(repairField(cldc, measurement.getCldc()));
            List<Float> wnddir = dataRetriever.retrieveWnddir(Path.of(String.valueOf(measurement.getStn()))).stream().limit(10).map(Integer::floatValue).collect(Collectors.toList());
            measurement.setWnddir((int) repairField(wnddir, measurement.getWnddir()));
            return true;
        }
    }

    /**
     * Compares the param field against the rest of the data. Field has to be within a 20% margin of the average.
     * @param data list of floats containing comparing data
     * @param field the field that needs to be checked
     * @return returns the average value if false, returns the input value if true
     */
    private float repairField(List<Float> data, float field) {
        float sum = 0;
        for (Float datum : data) {
            sum += datum;
        }
        float avg = sum / data.size();
        float fieldMax = (float) (avg * 1.20);
        float fieldMin = (float) (avg * 0.80);

        if (field > fieldMax || field < fieldMin) {
            return field;
        } else {
            return avg;
        }
    }

    /**
     * Checks if the data is within possible range.
     * @param measurement measurement unit
     * @return returns true if ALL are true. If one is false, something is wrong.
     */
    private boolean checkUltimateValues(Measurement measurement) {
        //TEMP -100, 70
        //DEWP -100, 50
        //SLP 700, 1200
        //WNDDIR 0, 360
        //WDSP 0, 407
        //CLDC 0, 99.9
        //SNDP 0, 193
        //PRCP 0, 187

        return isBetween(-100, 70, measurement.getTemp()) && isBetween(-100, 50, measurement.getDewp())
                && isBetween(700, 1200, measurement.getSlp()) && isBetween(0, 360, measurement.getWnddir())
                && isBetween(0, 407, measurement.getWdsp()) && isBetween(0, (float) 99.9, measurement.getCldc()) &&
                isBetween(0, 193, measurement.getSndp()) && isBetween(0, 187, measurement.getPrcp());
    }

    /**
     * Method to check if value within range.
     * @param min minimum possible value
     * @param max maximum possible value
     * @param value value of the data.
     * @return returns true if within range, false if outside range
     */
    private static boolean isBetween(float min, float max, float value) {
        return value > min && value < max;
    }
}

