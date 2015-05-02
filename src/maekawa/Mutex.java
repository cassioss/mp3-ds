package maekawa;

import java.util.ArrayList;

/**
 * Main class that coordinates the Maekawa mutex system.
 *
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class Mutex {

    protected static ArrayList<Node> nodeList;
    protected static volatile long endTime;
    protected static volatile boolean afterInit = false;

    /**
     * Main method that creates all nodes and simulates the Maekawa mutex system.
     *
     * @param args all arguments required to run the program properly.
     */
    public static void main(String[] args) {

        nodeList = new ArrayList<>(9);  // Initial capacity of 9 nodes

        if (args.length < 3 || args.length > 4) {
            System.out.println("Usage: java -cp src maekawa.Mutex [cs_int] [next_req] [tot_exec_time] [option]");
            return;
        }

        int csInt = Integer.valueOf(args[0]) * 1000;        // To be counted in seconds
        int timeNextReq = Integer.valueOf(args[1]) * 1000;  // To be counted in seconds
        int totExecTime = Integer.valueOf(args[2]) * 1000;  // To be counted in seconds
        int option = 0;

        if (args.length == 4) {
            if (Integer.valueOf(args[3]) == 1) {
                option = 1;
            } else if (Integer.valueOf(args[3]) != 0)
                throw new IllegalArgumentException("Error: invalid option");
        }

        endTime = totExecTime + System.currentTimeMillis();

        for (int identifier = 0; identifier < 9; identifier++) {
            nodeList.add(new Node(identifier, csInt, timeNextReq, option));
        }

        new Timer().start();
        afterInit = true;
    }

    /**
     * Simulates a timer that counts the total execution time.
     */
    private static class Timer extends Thread {

        /**
         * Runs the Timer thread.
         */
        @Override
        public void run() {
            while (true) {
                if (endTime <= System.currentTimeMillis()) {
                    System.out.println("TIMEOUT");
                    System.exit(0);
                }
            }
        }
    }

}