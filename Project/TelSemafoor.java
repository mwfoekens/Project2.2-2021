/**
 * Keeps track of threads
 *
 * @author Hanzehogeschool
 * Version 1.0
 */
public class TelSemafoor {
    // all honours to Edsgar Dijkstra

    private int max = 0;
    private int waarde = 0;

    public TelSemafoor(int Getal) {
        this.max = Getal;
        this.waarde = Getal;
    }

    public synchronized void probeer() throws InterruptedException {
        //System.err.println("Probeer aangeroepen...");
        while (this.waarde <= 0) wait();
        this.waarde--;
        //System.err.println("Probeer gelukt...");
        this.notify();
    }

    public synchronized void verhoog() throws InterruptedException {
        //System.err.println("Verhoog aangeroepen...");
        while (this.waarde >= max) wait();
        this.waarde++;
        //System.err.println("Verhoog gelukt...");
        this.notify();
    }
}
