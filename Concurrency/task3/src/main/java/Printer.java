public class Printer extends Thread{
    private final String[] msg;
    public Printer(String[] in) {
        this.msg = in;
    }

    public void run(){
        for (String s : msg) {
            System.out.println(s);
        }
    }
}
