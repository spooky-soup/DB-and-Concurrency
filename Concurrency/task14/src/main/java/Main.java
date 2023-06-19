import java.util.concurrent.Semaphore;

public class Main {

    static int a_cnt = 0;
    static int b_cnt = 0;
    static int c_cnt = 0;
    static Semaphore semA = new Semaphore(0);
    static Semaphore semB = new Semaphore(0);
    static Semaphore semC = new Semaphore(0);


    static Runnable produceA = () -> {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                //semA.acquire();
                Thread.sleep(1000);
                a_cnt++;
                semA.release();
                System.out.println("Produced detail A");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };
    static Runnable produceB = () -> {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                //System.out.println("Producing detail B...");
                //semB.acquire();
                Thread.sleep(2000);
                b_cnt++;
                semB.release();
                System.out.println("Produced detail B...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };
    static Runnable produceC = () -> {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                //System.out.println("Producing detail C...");
                Thread.sleep(3000);
                c_cnt++;
                semC.release();
                System.out.println("Produced detail C...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(produceA);
        Thread b = new Thread(produceB);
        Thread c = new Thread(produceC);
        a.start();
        b.start();
        c.start();

        while(!Thread.currentThread().isInterrupted()) {
            semA.acquire();
            semB.acquire();
            semC.acquire();
            System.out.println("Widget done");
        }
    }
}
