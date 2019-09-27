import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.io.*;

public class SimpleSearchPerformance {
    /* define constants */
    static long MAXVALUE =  2000000000;
    static long MINVALUE = -2000000000;
    static int numberOfTrials = 5;
    static int MAXINPUTSIZE  = 100000000;
    static int MININPUTSIZE  =  10000000;
    static int SIZEINCREMENT =  10000000;

    static String ResultFileName = "SimpleSearchResults.txt";

    public static void main(String[] args) {

        FileOutputStream ResultsOutFile = new FileInputStream(ResultFileName);

        System.out.println(" InputSize   AvgTime");
        ResultsOutFile.println("# InputSize    AvgTime"); // use # symbol in gnuplot data files for comments (such as to provide column headers)
        /* for each size of input we want to test... */
        for(int inputSize=MININPUTSIZE;inputSize<=MAXINPUTSIZE; inputSize+=SIZEINCREMENT) {
            /* repeat for desired number of trials (for a specific size of input)... */
            long totalTime = 0;
            for (long trial = 0; trial < numberOfTrials; trial++) {
                /* For one trial: */
                /* generate (random?) input data of desired size (a list of N random numbers) */
                long[] testList = createRandomIntegerList(inputSize);
                long testSearchNumber = (long) (MAXVALUE + Math.random() * (MAXVALUE - MINVALUE));

                /* run the trial */
                long timeStampBefore = getCpuTime(); /* get a "before" time stamp */

                /* apply the test function to the test input */
                long foundIndex = searchNumberList(testSearchNumber, testList);

                /* get "after" timestamp, calculat trial time */
                long timeStampAfter = getCpuTime();
                long trialTime = timeStampAfter - timeStampBefore;

                totalTime = totalTime + trialTime;
            }
            double averageTime = (double) totalTime / (double)numberOfTrials;
            /* print data for this size of input */
            System.out.println(inputSize + "  " + averageTime);
        }
    }

    /* return index of the searched number if found, or -1 if not found */
    public static int searchNumberList(long lookFor, long[] list) {
        for(int i=0; i<list.length; i++ ){
            if (list[i]== lookFor) return i;
        }
        return -1;
    }

    public static long[] createRandomIntegerList(int size){
        long[] newList = new long[size];
        for(int j=0;j<size;j++){
            newList[j] = (long)(MAXVALUE + Math.random() * (MAXVALUE - MINVALUE));
        }
        return newList;
    }

    /** Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadCpuTime( ) : 0L;
    }

}
