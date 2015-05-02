package maekawa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    protected static volatile boolean timeout = false;

    /**
     * Main method that creates all nodes and simulates the Maekawa mutex system.
     *
     * @param args all arguments required to run the program properly.
     */
    public static void main(String[] args) {

        nodeList = new ArrayList<>(9);          // Initial capacity of 9 nodes

        // Checks if the user set enough values to start running the algorithm
        if (args.length < 3 || args.length > 4) {
            System.out.println("Usage: java -cp src maekawa.Mutex [cs_int] [next_req] [tot_exec_time] [option]");
            return;
        }

        int csInt = Integer.valueOf(args[0]);
        int timeNextReq = Integer.valueOf(args[1]);
        int totExecTime = Integer.valueOf(args[2]) * 1000; // In order to be counted in seconds

        boolean option = false;

        // Checks if the option being received is either 0, 1 or nothing
        if (args.length == 4) {
            if (Integer.valueOf(args[3]) == 1) {
                option = true;
            } else if (Integer.valueOf(args[3]) != 0)
                throw new IllegalArgumentException("Error: invalid option");
        }

        // Creates each one of the 9 nodes
        for (int identifier = 0; identifier < 9; identifier++) {
            nodeList.add(new Node(identifier, csInt, timeNextReq, option));
        }

        // Executes timer
        endTime = totExecTime + System.currentTimeMillis();
        new Timer().start();

        //Releases nodes from INIT
        afterInit = true;
    }

    /**
     * Gets the reicever of a Message and sends it to him.
     *
     * @param message a Message object sent by a node.
     */
    protected static void sendMessage(Message message) {
        nodeList.get(message.getReceiverID()).messageQueue.add(message);
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
                    timeout = true;
                    System.out.println("TIMEOUT");
                    System.exit(0);
                }
            }
        }
    }

}