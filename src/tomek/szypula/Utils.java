package tomek.szypula;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

    public class Utils {
        private static long startTime;
        private static long endTime;

        public static int getNumberOfProcessors(){
            int numOfProcessors = Runtime.getRuntime().availableProcessors();
            System.out.println("Number of processors : "+numOfProcessors);
            return numOfProcessors;

        }
        public static void measureTimeStart() {
            startTime = System.nanoTime();
        }
        public static long measureTimeStop(){
            endTime = System.nanoTime();
            return endTime-startTime;
        }
        public static List<Double> getRandomDoubleList(int size, double max){
            List<Double> newList = new ArrayList<>(size);
            Random random = new Random();

            for (int i = 0; i < size ; i++) {
                double newElement = random.nextDouble()*max;
                newList.add(newElement);
            }

            return newList;
        }
        public static void writeToFile(String fileName,List<Double> values1, List<Double> values2,String column1,String column2)
                throws IOException {

            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(column1+"\t"+column2+"\n");
            for (int i = 0; i < values1.size() ; i++) {
                printWriter.printf("%f\t%f\n", values1.get(i), values2.get(i));
            }
            printWriter.close();
        }

}
