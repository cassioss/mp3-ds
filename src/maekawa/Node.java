package maekawa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class Node {

    private int identifier, csInt, timeNextReq, totExecTime, option;
    ArrayList<Integer> subset;
    volatile LinkedList<String> messageQueue;

    public Node(int identifier, int csInt, int timeNextReq, int totExecTime, int option) {
        this.identifier = identifier;
        this.csInt = csInt;
        this.timeNextReq = timeNextReq;
        this.totExecTime = totExecTime;
        this.option = option;
        this.subset = Utils.subset(this.identifier);
    }

    private class Listener extends Thread {
        @Override
        public void run() {
            while (true) {

            }
        }
    }

    private class Sender extends Thread {
        @Override
        public void run() {
            while (true) {

            }
        }
    }

}
