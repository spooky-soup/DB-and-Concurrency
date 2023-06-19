public class Child extends Thread{
    private final String txt;

    public Child(String in) {
        this.txt = in;
    }

    public void run() {
        while(!isInterrupted()) {
            System.out.println(txt);
        }

        System.out.println("Child thread dying...");
    }
}
