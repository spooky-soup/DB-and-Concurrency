import java.util.ArrayList;
import java.util.List;

public class Counter implements Runnable{
    final int workersCnt;
    private final List<Worker> workers;
    private float res = 0;
    boolean finishing = false;
    int maxCnt = 0;
    public final Object lock = new Object();

    public Counter(int workersCnt) {
        this.workersCnt = workersCnt;
        this.workers = new ArrayList<>(workersCnt);
        for (int i = 0; i < workersCnt; i++) {
            workers.add(i, new Worker(i, this));
        }
        /*barrier = new CyclicBarrier(workersCnt, () -> {
            try {
                countRes();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });*/
    }

    @Override
    public void run() {
        for (final Runnable worker : workers) {
            Thread workerThread = new Thread(worker);
            workerThread.start();
        }
    }

    private void finishWork() {
        //int maxCnt = 0;
        synchronized(lock) {
            finishing = true;
            for (Worker w : workers) {
                System.out.println("worker " + w.num + " stopped at " + w.getWorkerCnt());
                if (w.getWorkerCnt() > maxCnt) maxCnt = w.getWorkerCnt();
            }
            for (Worker w : workers) {
                w.setIterationNumber(maxCnt);
            }
            finishing = false;
            lock.notifyAll();
        }
    }

    private void countRes() {
        finishWork();
        for(Worker w : workers) {
            System.out.println(w.getPartialRes());
            res += w.getPartialRes();
        }
    }

    public float getRes() throws InterruptedException {
        this.countRes();
        System.out.println(workersCnt + " workers");
        System.out.println(maxCnt + " iterations per worker");
        return res*4;
    }

    public boolean isFinishing() {
        return finishing;
    }
}
