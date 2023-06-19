public class Parent extends Thread{
    public void run() {
        Child child = new Child();
        child.start();
        for (int i = 0; i< 10; i++) {
            System.out.println("Parent prints " + i);
        }
    }
}
