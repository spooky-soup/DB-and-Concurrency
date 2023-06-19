import java.util.Random;

public class Philosopher implements Runnable{
    private int place;
    private final Object left_fork;
    private final Object right_fork;
    final Random random = new Random();
    private boolean isEating;
    Controller controller;

    public Philosopher(int place, Object left_fork, Object right_fork, Controller controller) {
        this.place = place;
        this.left_fork = left_fork;
        this.right_fork = right_fork;
        this.controller = controller;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int n) {
        this.place = n;
    }

    private void eat() throws InterruptedException {
        //System.out.println("Philosopher " + place + " is taking left fork");
        synchronized (left_fork) {
            System.out.println("Philosopher " + place + " took the left fork");
            Thread.sleep(2);
            synchronized(right_fork) {
                System.out.println("Philosopher " + place + " took the right fork");
                System.out.println("Philosopher " + place + " is eating...");
                int eatTime = random.nextInt(10) + 1;
                Thread.sleep(eatTime);
                System.out.println("Philosopher " + place + " finished eating after " + eatTime/1000.0 + " seconds.");
            }
        }
        isEating = false;
    }

    private void think() throws InterruptedException {
        Thread.sleep(random.nextInt(10) + 1);
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            //Для решения проблемы дедлока проверяем, едят ли соседние философы и ждем когда они освободят вилки
            while(controller.philosophers[(place - 1 + 5) % 5].isEating() || controller.philosophers[(place + 6) % 5].isEating()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                isEating = true;
                think();
                eat();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isEating() {
        return this.isEating;
    }
}
