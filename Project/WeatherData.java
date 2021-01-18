import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "WEATHERDATA")
public class WeatherData {
    private List<Measurement> measurements;

    public WeatherData(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public WeatherData() {
    }

    @XmlElement(name = "MEASUREMENT")
    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "measurements=" + measurements +
                '}';
    }
}
