package tomek.szypula;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

public class MyExecutor {
    private int numberOfThreads;
    private boolean isStopped ;
    private MyWorker[] workerThreads;
    private LinkedBlockingQueue<Callable<Double>> queue;
    private volatile boolean exit = false;
    private List<FutureTask<Double>> tasks;

    public MyExecutor(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        workerThreads = new MyWorker[numberOfThreads];
        queue = new LinkedBlockingQueue<>();
        isStopped = Boolean.FALSE;
        tasks = new ArrayList<>();
        createPool();
    }

    private void createPool() {
        for (int i = 0; i < numberOfThreads; i++) {
            workerThreads[i] = new MyWorker();
            workerThreads[i].start();
        }
    }

    public FutureTask<Double> submit(Callable<Double> task){
        synchronized (queue) {
            queue.add(task);
            queue.notify();
            FutureTask<Double> futureTask = new FutureTask<Double>(task);
            return futureTask;
        }
    }
    public boolean isDone(){
        for (FutureTask<Double> futureTask:
            tasks   ) {
            if(!futureTask.isDone())
                return false;
        }
        return true;
    }
    public synchronized void stop(){
        this.exit = true;
    }

    private class MyWorker extends Thread {

        @Override
        public void run() {
            Callable<Double> task;
            while(!exit) {
                while (queue.isEmpty()) {
                    try {
                        synchronized (queue) {
                            queue.wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
                task = queue.poll();
                try {
                    if (task != null)
                        task.call();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
