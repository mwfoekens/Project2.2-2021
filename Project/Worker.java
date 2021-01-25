import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Parses & corrects data and adds data to the queue of an Executor
 *
 * @author Hanzehogeschool
 * Heavily edited by Merel Foekens
 * @version 2.0
 */

class Worker implements Runnable {
    private final Socket connection;
    private final BufferedReader bufferedReader;
    private final Unmarshaller jaxbUnmarshaller;
    private boolean running = true;
    private final DataAccessFactory dataAccessFactory;
    private final Executor executor;

    /**
     * Constructor
     *
     * @param connection        Socket Object
     * @param dataAccessFactory DataAccessFactory Object
     * @param executor          Executor Object
     * @throws JAXBException JAXBException
     * @throws IOException   IOException
     */
    public Worker(Socket connection, DataAccessFactory dataAccessFactory, Executor executor) throws JAXBException, IOException {
        this.connection = connection;
        this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JAXBContext jaxbContext = JAXBContext.newInstance(WeatherData.class);
        this.jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        this.dataAccessFactory = dataAccessFactory;
        this.executor = executor;
    }

    /**
     * Run method. Keeps connection open while there's still data to be read.
     */
    @Override
    public void run() {
        try {
            //check if maximum number of connections reached
            Main.semaphore.probeer();

            // keeps connection open while theres still input, closes if builder outside of while loop is still null
            while (running) {
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

                if (builder == null) {
                    running = false;
                } else {
                    WeatherData q = getWeatherData(builder);
                    addToQueue(q);
                }
            }
        } catch (IOException | InterruptedException | JAXBException | NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close socket
                connection.close();
                // Up semaphore
                Main.semaphore.verhoog();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Compares entirety of the WeatherData unit, adds to the executor service
     *
     * @param q an entire weather data unit
     * @throws IOException had to add otherwise IntelliJ cries
     */
    private void addToQueue(WeatherData q) throws IOException, InterruptedException, NoSuchFieldException {
        int length = q.getMeasurements().size();

        for (int i = 0; i < length; i++) {
            Measurement item = q.getMeasurements().get(i);

            if (repairData(item)) {
                executor.execute(() -> dataAccessFactory.getForStation(item.getStn()).writeRow(item));
            }
        }
    }

    /**
     * Parses data from XML files.
     *
     * @param builder the builder
     * @return returns WeatherData
     * @throws JAXBException JAXBexception
     */
    private WeatherData getWeatherData(StringBuilder builder) throws JAXBException {
        return (WeatherData) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(builder.toString().getBytes()));
    }

    /**
     * Data correcting method.
     *
     * @param measurement measurement that needs to be checked.
     * @return returns whether measurement should be added. (Only false if it's the first measurement of that station and not within acceptable ranges).
     */
    private boolean repairData(Measurement measurement) {
        // if the directory exists, the Measurements.csv file also exists.
        if (dataAccessFactory.getForStation(measurement.getStn()).hasMeasurements()) {
            return checkUltimateValues(measurement);

        } else {
            // Grabs the cache for the station number
            List<Measurement> measurements = dataAccessFactory.getForStation(measurement.getStn()).readCache();

            // Puts the cahce in a list that can be used by the repairField() method.
            List<Float> temp = measurements.stream().map(Measurement::getTemp).collect(Collectors.toList());
            measurement.setTemp(repairField(temp, measurement.getTemp()));

            List<Float> dewp = measurements.stream().map(Measurement::getDewp).collect(Collectors.toList());
            measurement.setDewp(repairField(dewp, measurement.getDewp()));

            List<Float> stp = measurements.stream().map(Measurement::getStp).collect(Collectors.toList());
            measurement.setStp(repairField(stp, measurement.getStp()));

            List<Float> slp = measurements.stream().map(Measurement::getSlp).collect(Collectors.toList());
            measurement.setSlp(repairField(slp, measurement.getSlp()));

            List<Float> visib = measurements.stream().map(Measurement::getVisib).collect(Collectors.toList());
            measurement.setVisib(repairField(visib, measurement.getVisib()));

            List<Float> wdsp = measurements.stream().map(Measurement::getWdsp).collect(Collectors.toList());
            measurement.setWdsp(repairField(wdsp, measurement.getWdsp()));

            List<Float> prcp = measurements.stream().map(Measurement::getPrcp).collect(Collectors.toList());
            measurement.setPrcp(repairField(prcp, measurement.getPrcp()));

            List<Float> sndp = measurements.stream().map(Measurement::getSndp).collect(Collectors.toList());
            measurement.setSndp(repairField(sndp, measurement.getSndp()));

            // Different method because Frshtt is pretty irregular regarding the rest of the values.
            measurement.setFrshtt(repairFrshtt(measurement.getFrshtt()));

            List<Float> cldc = measurements.stream().map(Measurement::getCldc).collect(Collectors.toList());
            measurement.setCldc(repairField(cldc, measurement.getCldc()));

            List<Float> wnddir = measurements.stream().map(Measurement::getWnddir).map(Integer::floatValue).collect(Collectors.toList());
            measurement.setWnddir(Math.round(repairField(wnddir, measurement.getWnddir())));

            return true;
        }
    }

    /**
     * Calculates the standard deviation from all previous values, and compares the param field to the calculated deviation.
     * Field has to be within 2 times the standard deviation. If the standard deviation is 0, the field gets returned as is.
     *
     * @param data  list of floats containing comparing data
     * @param field the field that needs to be checked
     * @return returns the average value if an outlier, returns the input value if within standard deviation range/
     */
    private static float repairField(List<Float> data, float field) {
        float sum = 0;
        float sqDiff = 0;
        float avg;
        double deviation;
        int listSize = data.size();

        for (Float datum : data) {
            sum += datum;
        }

        avg = sum / listSize;

        for (Float datum : data) sqDiff += ((datum - avg) * (datum - avg));

        deviation = Math.sqrt(sqDiff / (listSize - 1));

        // if deviation is 0, all entry points are the same value, which means there is no deviation.
        if (deviation == 0) {
            return field;
        } else {
            if (field > (avg + 2 * deviation) || field < (avg - 2 * deviation)) {
                // shenaningans to avoid too many decimals
                return (float) Math.round(avg * 100) / 100;
            } else {
                return field;
            }
        }
    }

    /**
     * Checks if the fields of the first entry of a station are within acceptable ranges.
     *
     * @param measurement measurement unit
     * @return returns true if ALL are true. If one is false, something is wrong.
     */
    private static boolean checkUltimateValues(Measurement measurement) {
        // Acceptable ranges for all relevant measurement fields
        //TEMP -100, 70
        //DEWP -100, 50
        //SLP 700, 1200
        //WNDDIR 0, 360
        //WDSP 0, 407
        //CLDC 0, 99.9
        //SNDP 0, 193
        //PRCP 0, 187

        return isBetween(-100, 70, measurement.getTemp()) &&
                isBetween(-100, 50, measurement.getDewp()) &&
                isBetween(700, 1200, measurement.getSlp()) &&
                isBetween(0, 360, measurement.getWnddir()) &&
                isBetween(0, 407, measurement.getWdsp()) &&
                isBetween(0, (float) 99.9, measurement.getCldc()) &&
                isBetween(0, 193, measurement.getSndp()) &&
                isBetween(0, 187, measurement.getPrcp());
    }

    /**
     * Method to check if singular field is within acceptable range.
     *
     * @param min   minimum possible value
     * @param max   maximum possible value
     * @param value value of the data.
     * @return returns true if within range, false if outside range
     */
    private static boolean isBetween(float min, float max, float value) {
        return value >= min && value <= max;
    }

    /**
     * Seperate repair function for the Frshtt field it is irregular compared to the other fields.
     *
     * @param field field that needs to be corrected
     * @return returns "000000" if empty, returns field if not empty.
     */
    private static String repairFrshtt(String field) {
        if (field.equals("")) {
            return "000000";
        } else {
            return field;
        }
    }
}

