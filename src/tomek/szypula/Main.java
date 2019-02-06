package tomek.szypula;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        int numberOfTasks = 20;
        MyExecutorTester tester = new MyExecutorTester();
        long myTime = tester.testMyExecutor(numberOfTasks);
        System.out.println("MyExec done");
        long hisTime = tester.testCompletionService(numberOfTasks);
        System.out.println("My Time : "+myTime+" His Time : "+hisTime);
    }
}
