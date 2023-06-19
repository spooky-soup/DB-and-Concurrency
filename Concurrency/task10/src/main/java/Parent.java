public class Parent extends Thread{
    String[] arg;
    final Object lock = new Object();
    public Parent(String[] arg) {
        this.arg = arg;
    }

    public void run() {
        Child child = new Child(arg, this);
        child.start();
        for (int i = 0; i < arg.length; i++) {
            synchronized(lock) {
                try {
                    if (i != 0) {
                        lock.wait();
                    }
                    System.out.println("Parent prints " + arg[i]);
                    lock.notify();
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
