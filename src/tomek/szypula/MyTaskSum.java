package tomek.szypula;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MyTaskSum implements Callable<Double> {
    private int name;
    private int size;
    private List<Double> list;

    public MyTaskSum(int name, int size) {
        this.name = name;
        this.size = size;
        list = Utils.getRandomDoubleList(size,1);
    }

    @Override
    public Double call() {
        double sum = 0;
        System.out.println("Task " + name + " is running.");
        for (Double value :
                list) {
            sum+=Math.log(value);
        }
        return sum;
    }
}
