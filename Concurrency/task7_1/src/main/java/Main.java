public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter(Integer.parseInt(args[0]));
        Thread counterThread = new Thread(counter);
        counterThread.start();
        System.out.println("Result with " + args[0] + " workers after 1000 iterations: " + counter.getRes());
    }
}
