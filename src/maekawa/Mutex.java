package maekawa;

import java.util.ArrayList;

/**
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class Mutex {

    protected static ArrayList<Node> nodeList;
    protected volatile long endTime;

    public static void main(String[] args) {

        nodeList = new ArrayList<>(9);  // Initial capacity of 9 nodes

        if (args.length < 3 || args.length > 4) {
            System.out.println("Usage: java -cp src maekawa.Mutex [cs_int] [next_req] [tot_exec_time] [option]");
            return;
        }

        int csInt = Integer.valueOf(args[0]);
        int timeNextReq = Integer.valueOf(args[1]);
        int totExecTime = Integer.valueOf(args[2]);
        int option = 0;

        if (args.length == 4) {
            if (Integer.valueOf(args[3]) == 1) {
                option = 1;
            } else if (Integer.valueOf(args[3]) != 0)
                throw new IllegalArgumentException("Error: invalid option");
        }

        
    }

    private class Timer extends Thread {
        @Override
        public void run() {

        }
    }

}