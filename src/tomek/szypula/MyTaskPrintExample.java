package tomek.szypula;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MyTaskPrintExample implements Callable<Double> {
    private int name;

    public MyTaskPrintExample(int name) {
        this.name = name;
    }

    @Override
    public Double call() {
        try {
            TimeUnit.MILLISECONDS.sleep(200);
            System.out.println("Task " + name + " is running.");
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Double.valueOf(0);

    }
}
