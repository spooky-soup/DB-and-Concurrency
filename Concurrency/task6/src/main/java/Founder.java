import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public final class Founder {
    private final List<Worker> workers;
    final Company company;
    CyclicBarrier barrier;

    public Founder(final Company company) {
        this.company = company;
        this.workers = new ArrayList<>(company.getDepartmentsCount());
        for (int i = 0; i < company.getDepartmentsCount(); i++) {
            workers.add(i, new Worker(company, i, this));
        }
        barrier = new CyclicBarrier(company.getDepartmentsCount(), company::showCollaborativeResult);
    }
    public void start() throws InterruptedException {
        for (final Runnable worker : workers) {
            Thread workerThread = new Thread(worker);
            workerThread.start();
        }
    }
}
