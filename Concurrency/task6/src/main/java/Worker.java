import java.util.concurrent.BrokenBarrierException;

public class Worker implements Runnable{
    final int departmentID;
    Department department;
    final Company company;
    final Founder founder;

    public Worker(Company company, int departmentID, Founder founder) {
        this.departmentID = departmentID;
        this.company = company;
        this.founder = founder;
    }

    @Override
    public void run() {
        department = company.getFreeDepartment(departmentID);
        department.performCalculations();
        try {
            founder.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
