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
        //while(!Thread.currentThread().isInterrupted()) {
        while(cnt < iter) {
            partialRes += (counter.workersCnt * cnt + num);
            cnt += 1;
        }
    }


    public float getPartialRes() {
        return this.partialRes;
    }
}
