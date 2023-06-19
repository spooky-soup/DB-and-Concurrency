public class Main {
    public static void main(String[] args) {
        String[] arg = new String[10];
        for (int i = 0; i < arg.length; i++) {
            arg[i] = "String #" + i;
        }
        Parent parent = new Parent(arg);
        parent.start();
    }
}
