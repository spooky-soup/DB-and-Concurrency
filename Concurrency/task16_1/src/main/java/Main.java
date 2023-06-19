import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner inScanner = new Scanner(System.in);
        URL url = null;
        while (url == null) {
            try {
                url = new URL(inScanner.nextLine());
            } catch (MalformedURLException e) {
                System.out.println("URL not found. Try again.");
                url = null;
            }
        }
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        final LinkedBlockingQueue<String> getLines = new LinkedBlockingQueue<>();

        new Thread(() -> {
            String nextLine;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if ((nextLine = reader.readLine()) == null)
                        break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                getLines.add(nextLine);
            }
        }).start();

        sleep(100);
        String currResponse = getLines.poll();

        int i = 0;
        while (currResponse != null) {
            if (i < 25) {
                System.out.println(currResponse);
                i++;
            }
            else {
                i = 0;
                System.out.println("Press enter to scroll...");
                String inKey = inScanner.nextLine();
                if(!(inKey.equals(" ")))
                    break;
            }
            currResponse = getLines.poll();
        }

    }
}
