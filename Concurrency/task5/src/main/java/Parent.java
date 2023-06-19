public class Parent extends Thread {

    public void run() {
        Child child = new Child("this is text");
        child.start();

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        child.interrupt();
    }
}
