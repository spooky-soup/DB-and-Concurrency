import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller implements Runnable{
    Philosopher[] philosophers = new Philosopher[5];
    Boolean[] forks = new Boolean[5];
    Semaphore[] forks_sem = new Semaphore [] {
            new Semaphore(1),
            new Semaphore(1),
            new Semaphore(1),
            new Semaphore(1),
            new Semaphore(1)
    };
    AtomicBoolean[] atomicForks = new AtomicBoolean[5];
    Object waiter = new Object();
    public Controller() {
        Arrays.fill(forks, true);
        Arrays.fill(atomicForks, new AtomicBoolean(true));
        for(int i = 0; i < philosophers.length; i++) {
            //philosophers[i] = new Philosopher(i, forks[i], forks[(i+1) % philosophers.length], waiter);
            philosophers[i] = new Philosopher(i, forks_sem[i], forks_sem[(i+1) % philosophers.length], waiter);
        }
    }

    @Override
    public void run() {
        for (Philosopher p : philosophers) {
            Thread philThread = new Thread(p);
            philThread.start();
        }
    }
}
