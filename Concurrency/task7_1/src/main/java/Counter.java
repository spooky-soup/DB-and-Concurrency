import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Counter implements Runnable{
    final int workersCnt;
    private final List<Worker> workers;
    CyclicBarrier barrier;
    private float res = 0;
    int iterations = 1000;

    public Counter(int workersCnt) {
        this.workersCnt = workersCnt;
        this.workers = new ArrayList<>(workersCnt);
        for (int i = 0; i < workersCnt; i++) {
            workers.add(i, new Worker(i, this, iterations / workersCnt));
        }
        barrier = new CyclicBarrier(workersCnt, this::countRes);
    }

    @Override
    public void run() {
        for (final Runnable worker : workers) {
            Thread workerThread = new Thread(worker);
            workerThread.start();
        }
    }

    private void countRes() {
        for(Worker w : workers) {
            res += w.getPartialRes();
        }
    }

    public float getRes() {
        return res;
    }
}
