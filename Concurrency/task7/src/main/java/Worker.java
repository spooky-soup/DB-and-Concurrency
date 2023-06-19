import java.util.concurrent.BrokenBarrierException;

public class Worker implements Runnable{
    final int num;
    private float partialRes = 0;
    Counter counter;
    int iter;

    public Worker(int num, Counter counter, int workerIterations) {
        this.num = num;
        this.counter = counter;
        this.iter = workerIterations;
    }


    @Override
    public void run() {
        int cnt = 0;
        double stepRes;
        int idx;
        while(cnt < iter) {
            idx = ((counter.workersCnt * cnt) + num);
            System.out.println("Worker " + num + " idx " + idx);
            stepRes = (1.0 / ((idx * 2) + 1));
            stepRes = (idx % 2 == 0) ? stepRes : stepRes * (-1);
            partialRes += stepRes;
            cnt += 1;
        }
        try {
            counter.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


    public float getPartialRes() {
        return this.partialRes;
    }
}
