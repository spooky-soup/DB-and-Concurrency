public class Child extends Thread{
    private final String txt;
    public Child(String in) {
        this.txt = in;
    }
    public void run() {
        System.out.println(txt);
    }
}
