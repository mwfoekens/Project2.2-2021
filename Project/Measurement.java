import javax.xml.bind.annotation.XmlElement;

/**
 * Class that holds measurement data
 * @author Merel Foekens
 * @version 1.0
 */
public class Measurement {
    private int stn;
    private String date;
    private String time;
    private float temp;
    private float dewp;
    private float stp;
    private float slp;
    private float visib;
    private float wdsp;
    private float prcp;
    private float sndp;
    private String frshtt;
    private float cldc;
    private int wnddir;

    public Measurement(int stn, String date, String time, float temp, float dewp, float stp, float slp, float visib, float wdsp, float prcp, float sndp, String frshtt, float cldc, int wnddir) {
        this.stn = stn;
        this.date = date;
        this.time = time;
        this.temp = temp;
        this.dewp = dewp;
        this.stp = stp;
        this.slp = slp;
        this.visib = visib;
        this.wdsp = wdsp;
        this.prcp = prcp;
        this.sndp = sndp;
        this.frshtt = frshtt;
        this.cldc = cldc;
        this.wnddir = wnddir;
    }

    public Measurement() {

    }

    @XmlElement(name = "STN")
    public int getStn() {
        return stn;
    }

    public void setStn(int stn) {
        this.stn = stn;
    }

    @XmlElement(name = "DATE")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @XmlElement(name = "TIME")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @XmlElement(name = "TEMP")
    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    @XmlElement(name = "DEWP")
    public float getDewp() {
        return dewp;
    }

    public void setDewp(float dewp) {
        this.dewp = dewp;
    }

    @XmlElement(name = "STP")
    public float getStp() {
        return stp;
    }

    public void setStp(float stp) {
        this.stp = stp;
    }

    @XmlElement(name = "SLP")
    public float getSlp() {
        return slp;
    }

    public void setSlp(float slp) {
        this.slp = slp;
    }

    @XmlElement(name = "VISIB")
    public float getVisib() {
        return visib;
    }

    public void setVisib(float visib) {
        this.visib = visib;
    }

    @XmlElement(name = "WDSP")
    public float getWdsp() {
        return wdsp;
    }

    public void setWdsp(float wdsp) {
        this.wdsp = wdsp;
    }

    @XmlElement(name = "PRCP")
    public float getPrcp() {
        return prcp;
    }

    public void setPrcp(float prcp) {
        this.prcp = prcp;
    }

    @XmlElement(name = "SNDP")
    public float getSndp() {
        return sndp;
    }

    public void setSndp(float sndp) {
        this.sndp = sndp;
    }

    @XmlElement(name = "FRSHTT")
    public String getFrshtt() {
        return frshtt;
    }

    public void setFrshtt(String frshtt) {
        this.frshtt = frshtt;
    }

    @XmlElement(name = "CLDC")
    public float getCldc() {
        return cldc;
    }

    public void setCldc(float cldc) {
        this.cldc = cldc;
    }

    @XmlElement(name = "WNDDIR")
    public int getWnddir() {
        return wnddir;
    }

    public void setWnddir(int wnddir) {
        this.wnddir = wnddir;
    }


    @Override
    public String toString() {
        return "Measurement{" +
                "stn='" + stn + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", temp='" + temp + '\'' +
                ", dewp='" + dewp + '\'' +
                ", stp='" + stp + '\'' +
                ", slp='" + slp + '\'' +
                ", visib='" + visib + '\'' +
                ", wdsp='" + wdsp + '\'' +
                ", prcp='" + prcp + '\'' +
                ", sndp='" + sndp + '\'' +
                ", frshtt='" + frshtt + '\'' +
                ", cldc='" + cldc + '\'' +
                ", wnddir='" + wnddir + '\'' +
                '}';
    }
}
