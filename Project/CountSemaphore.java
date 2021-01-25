/**
 * Keeps track of threads
 *
 * @author Hanzehogeschool
 * Version 1.0
 */
public class CountSemaphore {
    // all honours to Edsgar Dijkstra

    private int max = 0;
    private int waarde = 0;

    public CountSemaphore(int Getal) {
        this.max = Getal;
        this.waarde = Getal;
    }

    public synchronized void probeer() throws InterruptedException {
        while (this.waarde <= 0) wait();
        this.waarde--;
        this.notify();
    }

    public synchronized void verhoog() throws InterruptedException {
        while (this.waarde >= max) wait();
        this.waarde++;
        this.notify();
    }
}
