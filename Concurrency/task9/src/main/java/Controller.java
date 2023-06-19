public class Controller implements Runnable{
    Philosopher[] philosophers = new Philosopher[5];
    Object[] forks = new Object[5];

    public Controller() {
        for (int i = 0; i < philosophers.length; i++) {
            forks[i] = new Object();
        }

        for(int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i+1) % philosophers.length], this);
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
