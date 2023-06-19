import java.util.concurrent.Semaphore;

public class Main {
    private static final int NUM = 10;
    static Semaphore sem1 = new Semaphore(1);
    static Semaphore sem2 = new Semaphore(0);
    public static void printLines(String threadName, Semaphore aqSem, Semaphore reSem) throws InterruptedException {
        for (int i = 0; i < NUM; i++) {
            aqSem.acquire();
            System.out.println(threadName + " prints " + i);
            reSem.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread child = new Thread(() -> {
            try {
                printLines("child", sem1, sem2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        child.start();
        printLines("parent", sem2, sem1);
    }
}
