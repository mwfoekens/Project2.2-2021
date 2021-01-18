import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DBThread implements Runnable {
    public final BlockingQueue<Measurement> queue = new LinkedBlockingQueue<>();
    private Connection connection;
    public boolean running = true;

    public DBThread() {
        String url = "jdbc:postgresql://localhost/unwdmi";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "hoi");
        props.setProperty("ssl", "false");
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, props);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.err.println("\nOpened database successfully.");
    }

    @Override
    public void run() {
        try {
            Statement statement = connection.createStatement();

            while (running) {
                Measurement values = queue.take();
                statement.executeUpdate("insert into measurements " +
                        "(stn, \"date\", \"time\", \"temp\", dewp, stp, slp, visib, wdsp, prcp, sndp, frshtt, cldc, wnddir) " +
                        "values (" + values.getStn() + ", '" + values.getDate() + "', '" + values.getTime() + "', " + values.getTemp() + ", "
                        + values.getDewp() + ", " + values.getStp() + ", " + values.getSlp() + ", " + values.getVisib() + ", "
                        + values.getWdsp() + ", " + values.getPrcp() + ", " + values.getSndp() + ", '" + values.getFrshtt()
                        + "', " + values.getCldc() + ", " + values.getWnddir() + ");");
            }
        } catch (SQLException | InterruptedException throwables) {
            throwables.printStackTrace();
        }
    }
}
