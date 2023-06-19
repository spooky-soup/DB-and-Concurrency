import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) throws InterruptedException { //0-й аргумент - количество воркеров, 1-й - общее число итераций
        Scanner in = new Scanner(System.in);
        System.out.println("Enter threads number: ");
        int n = parseInt(in.nextLine());
        System.out.println("Enter number of iterations: ");
        int i = parseInt(in.nextLine());

        Counter counter = new Counter(n, i);
        Thread counterThread = new Thread(counter);
        counterThread.start();
        counterThread.join();
        //double res = counter.getRes();
        //System.out.println("Result with " + n + " workers after " + i + " iterations: " + counter.getRes());
    }
}
