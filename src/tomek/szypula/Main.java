package tomek.szypula;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        int numberOfThreads = Utils.getNumberOfProcessors();
        MyExecutor myExecutor = new MyExecutor(numberOfThreads);
        Utils.measureTimeStart();
        for (int i = 0; i < 20; i++) {
            MyTaskSum task = new MyTaskSum(i,2000);
            myExecutor.submit(task);
        }
        while (!myExecutor.isDone()){}
        long t1 = Utils.measureTimeStop();
        System.out.println("Time myExecutor: "+ t1);

        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CompletionService<Double> service = new ExecutorCompletionService<>(Executors.newFixedThreadPool(numberOfThreads));
        Utils.measureTimeStart();
        List<Callable<Double>> tasks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyTaskSum task = new MyTaskSum(i,2000);
            tasks.add(task);
            service.submit(task);
        }
        for (MyTaskSum task :
                tasks) {
            try {
                service.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long t2 = Utils.measureTimeStop();
        System.out.println("Time Executor: "+ t2);

    }
}
