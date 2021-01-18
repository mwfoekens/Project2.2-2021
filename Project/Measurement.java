import javax.xml.bind.annotation.XmlElement;

public class Measurement {
    private String stn;
    private String date;
    private String time;
    private String temp;
    private String dewp;
    private String stp;
    private String slp;
    private String visib;
    private String wdsp;
    private String prcp;
    private String sndp;
    private String frshtt;
    private String cldc;
    private String wnddir;

    public Measurement(String stn, String date, String time, String temp, String dewp, String stp, String slp, String visib, String wdsp, String prcp, String sndp, String frshtt, String cldc, String wnddir) {
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
    public String getStn() {
        return stn;
    }

    public void setStn(String stn) {
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
    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    @XmlElement(name = "DEWP")
    public String getDewp() {
        return dewp;
    }

    public void setDewp(String dewp) {
        this.dewp = dewp;
    }

    @XmlElement(name = "STP")
    public String getStp() {
        return stp;
    }

    public void setStp(String stp) {
        this.stp = stp;
    }

    @XmlElement(name = "SLP")
    public String getSlp() {
        return slp;
    }

    public void setSlp(String slp) {
        this.slp = slp;
    }

    @XmlElement(name = "VISIB")
    public String getVisib() {
        return visib;
    }

    public void setVisib(String visib) {
        this.visib = visib;
    }

    @XmlElement(name = "WDSP")
    public String getWdsp() {
        return wdsp;
    }

    public void setWdsp(String wdsp) {
        this.wdsp = wdsp;
    }

    @XmlElement(name = "PRCP")
    public String getPrcp() {
        return prcp;
    }

    public void setPrcp(String prcp) {
        this.prcp = prcp;
    }

    @XmlElement(name = "SNDP")
    public String getSndp() {
        return sndp;
    }

    public void setSndp(String sndp) {
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
    public String getCldc() {
        return cldc;
    }

    public void setCldc(String cldc) {
        this.cldc = cldc;
    }

    @XmlElement(name = "WNDDIR")
    public String getWnddir() {
        return wnddir;
    }

    public void setWnddir(String wnddir) {
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
