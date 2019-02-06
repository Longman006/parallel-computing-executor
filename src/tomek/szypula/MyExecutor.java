package tomek.szypula;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

public class MyExecutor {
    private int numberOfThreads;
    private boolean isStopped ;
    private volatile MyWorker[] workerThreads;
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
            FutureTask<Double> futureTask = new FutureTask<Double>(task);
            tasks.add(futureTask);
            queue.add(task);
            queue.notify();
            return futureTask;
        }
    }
    public boolean finish(){
        for (FutureTask<Double> futureTask:
            tasks   ) {
            try {
                futureTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public boolean isDone(){
        for (FutureTask<Double> task :
                tasks) {

            if (!task.isDone())
                return false;
        }
        return true;
    }
    public void shutdown(){
        exit();
        for (Thread thread :
                workerThreads) {
            workerThreads = null;
        }

    }
    public void exit(){
        this.exit = !this.exit;
    }

    /**
     * Deprecated
     */
    public void join(){
        for (Thread thread :
                workerThreads)
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    private class MyWorker extends Thread {

        @Override
        public void run() {
            while(true) {
                Callable<Double> task;
                while (queue.isEmpty() && !exit) {
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
                } catch (Exception e) {
                }
                if (exit && queue.isEmpty()) return;
            }

        }
    }
}
