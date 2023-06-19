public class Worker implements Runnable{
    final int num;
    private float partialRes = 0;
    Counter counter;
    private int cnt = 0;
    private int iter = -1;

    public Worker(int num, Counter counter) {
        this.num = num;
        this.counter = counter;
    }


    @Override
    public void run() {
        while(iter == -1 || cnt < iter) {
            synchronized(counter.lock) {
                while (counter.isFinishing()) {
                    try {
                        counter.lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            int idx = counter.workersCnt * cnt + num;
            double current = 1.0 / ((2*idx) + 1);
            if (idx % 2 != 0) current = (-1) * current;
            partialRes += current;
            cnt += 1;
        }
    }

    public int getWorkerCnt() {
        //System.out.println(num + " worker paused on " + cnt);
        return cnt;
    }

    public void setIterationNumber(int iterationNumber) {
        System.out.println("worker " + num + " setting new iter " + iterationNumber);
        this.iter = iterationNumber;
    }


    public float getPartialRes() {
        return this.partialRes;
    }
}
