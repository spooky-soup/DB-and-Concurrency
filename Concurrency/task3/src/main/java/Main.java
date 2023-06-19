public class Main {
    static String[] arg1 = {"1", "2", "3"};
    static String[] arg2 = {"one", "two", "three", "four"};
    static String[] arg3 = {"uno", "dos", "tres"};
    static String[] arg4 = {"один", "два", "три"};
    public static void main(String[] args) {
        Printer printer1 = new Printer(arg1);
        Printer printer2 = new Printer(arg2);
        Printer printer3 = new Printer(arg3);
        Printer printer4 = new Printer(arg4);
        printer1.start();
        printer2.start();
        printer3.start();
        printer4.start();
    }
}
