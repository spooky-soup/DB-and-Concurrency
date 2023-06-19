public class Main {
    public static void main(String[] args) throws InterruptedException {
        Company company = new Company(5);
        Founder founder = new Founder(company);
        founder.start();
    }
}
