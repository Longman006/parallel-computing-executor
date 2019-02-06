package tomek.szypula;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutorTester {


    public long testMyExecutor(int numberOfTasks){
        int numberOfThreads = Utils.getNumberOfProcessors();
        MyExecutor myExecutor = new MyExecutor(numberOfThreads);
        List<MyTaskSum> tasks = new ArrayList<>();
        Utils.measureTimeStart();
        for (int i = 0; i < numberOfTasks; i++) {
            MyTaskSum task = new MyTaskSum(i,2000);
            tasks.add(task);
        }
        for (MyTaskSum task :
                tasks) {
            myExecutor.submit(task);
        }
        myExecutor.exit();
        myExecutor.join();
        long t1 = Utils.measureTimeStop();

        return t1;
    }
    public long testCompletionService(int numberOfTasks){
        int numberOfThreads = Utils.getNumberOfProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CompletionService<Double> service = new ExecutorCompletionService<>(executorService);
        Utils.measureTimeStart();
        List<MyTaskSum> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            MyTaskSum task = new MyTaskSum(i,2000);
            tasks.add(task);
        }
        for (MyTaskSum task :
                tasks) {
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
        executorService.shutdown();
        executorService.shutdownNow();
        long t2 = Utils.measureTimeStop();
        return t2;
    }

    
}
