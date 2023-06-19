import java.util.*;

public class Main {
    static LinkedList<String> list = new LinkedList<>();

    public static void main(String[] args) {

        Runnable sortRunnable = () -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sortList();
            }
        };

        Scanner in = new Scanner(System.in);
        Thread sorting = new Thread(sortRunnable);
        Thread addingLines = new Thread(() -> {
            sorting.start();
            while (!Thread.currentThread().isInterrupted()) {
                String s = in.nextLine();
                add(s);
            }
        });
        addingLines.start();

    }

    private static void add(String msg) {
        String[] msgList = split(msg);
        if (msgList.length == 0) printList();
        if (list.isEmpty()) {
            list.add(msgList[0]);
            msgList = Arrays.copyOfRange(msgList, 1, msgList.length);
        }
        if (msgList.length == 0) return;
        synchronized(Objects.requireNonNull(list.peekFirst())) {
            list.addAll(Arrays.asList(msgList));
            System.out.println("Added " + Arrays.asList(msgList));
        }
    }

    private static void printList() {
        synchronized(Objects.requireNonNull(list.peekFirst())) {
            System.out.println(list);
        }
    }

    private static String[] split(String msg) {
        if (msg.isEmpty()) return new String[]{};
        if (msg.length() <= 80) return new String[]{msg};

        int endpoint = (msg.length() / 80);
        endpoint = msg.length() % 80 != 0 ? endpoint + 1 : endpoint;

        String[] res = new String[(msg.length() - 1) / 80 + 1];
        for (int i = 0; i < endpoint; i++) {
            if ((i+1)*80 < msg.length())
                res[i] = msg.substring(i*80, (i+1)*80);
            else
                res[i] = msg.substring(i*80);
        }
        return res;
    }

    private static void sortList() {
        if (list.isEmpty()) return;
        synchronized (Objects.requireNonNull(list.peekFirst())) {
            LinkedList<String> copyList = new LinkedList<>(list);
            Collections.copy(copyList, list);
            copyList.sort(String::compareTo);
            list = copyList;
            System.out.println("Sorted successfully");
        }
    }

}
