import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Philosopher implements Runnable{
    private int place;
    //private Boolean left_fork;
    //private Boolean right_fork;

    private final Semaphore left_fork;
    private final Semaphore right_fork;
    private final Object waiter;
    boolean tookForks = false;
    final Random random = new Random();

    public Philosopher(int place, Semaphore left_fork, Semaphore right_fork, Object waiter) {
        this.place = place;
        this.left_fork = left_fork;
        this.right_fork = right_fork;
        this.waiter = waiter;
    }
    //Создать класс вилки с одним полем - boolean available и передавать их 
    private void eat() throws InterruptedException {
        //System.out.println("Philosopher " + place + " is taking left fork");
        synchronized (waiter) {
            if (left_fork.tryAcquire()) {
                if (right_fork.tryAcquire()) {
                    tookForks = true;
                } else {
                    //left_fork = true;
                    left_fork.release();
                }
            }
        }
        if (tookForks) {
            System.out.println("Philosopher " + place + " is eating...");
            int eatTime = random.nextInt(2000) + 1000;
            Thread.sleep(eatTime);
            System.out.println("Philosopher " + place + " finished eating after " + eatTime / 1000.0 + " seconds.");
            tookForks = false;
            right_fork.release();
            left_fork.release();
        }
    }

    private void think() throws InterruptedException {
        //System.out.println("Philosopher " + place + " is thinking...");
        Thread.sleep(random.nextInt(2000) + 1000);
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                think();
                eat();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
