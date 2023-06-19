import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int workersCnt = Integer.parseInt(scan.nextLine());
        Counter counter = new Counter(workersCnt);
        Thread counterThread = new Thread(counter);
        var shutdownListener = new Thread(){
            public void run(){
                try {
                    System.out.println("\nSIGINT CATCHED");
                    System.out.println("Result: " + counter.getRes());
                } catch (InterruptedException ignored) {
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownListener);
        counterThread.start();
    }
}
