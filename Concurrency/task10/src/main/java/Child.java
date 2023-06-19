public class Child extends Thread {
    String[] arg;
    Parent parent;
    public Child(String[] arg, Parent parent) {
        this.arg = arg;
        this.parent = parent;
    }
    public void run() {
        synchronized (parent.lock) {
            for (int i = 0; i < arg.length; i++) {
                synchronized(parent.lock) {
                    try {
                        if (i != 0) {
                            parent.lock.wait();
                        }
                        System.out.println("Child prints " + arg[i]);
                        parent.lock.notify();
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }
}