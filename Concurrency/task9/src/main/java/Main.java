public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();
        Thread contThread = new Thread(controller);
        contThread.start();
    }
}
