package maekawa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Simulates a thread and implements the Maekawa's algorithm.
 *
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class Node {

    private int identifier, csInt, timeNextReq, option;
    private ArrayList<Integer> subset;
    private volatile List<Message> messageQueue;
    private State state;

    /**
     * Creates a node based on its identifier, the time it stays in the critical section, the time until the next
     * request, and a simple flag for log printing.
     *
     * @param identifier
     * @param csInt
     * @param timeNextReq
     * @param option
     */
    public Node(int identifier, int csInt, int timeNextReq, int option) {
        this.identifier = identifier;
        this.csInt = csInt;
        this.timeNextReq = timeNextReq;
        this.option = option;
        this.subset = Utils.subset(this.identifier);
        this.state = State.INIT;
        messageQueue = new LinkedList<>();
        printCurrentState();
        new Listener().start();
        new Sender().start();
    }

    private class Listener extends Thread {
        @Override
        public void run() {
            while (true) {
                if (Mutex.afterInit && state == maekawa.State.INIT)
                    changeState(maekawa.State.REQUEST);
                if (state == maekawa.State.REQUEST)
                    processRequest();
                else if (state == maekawa.State.HELD)
                    processHeld();
                else if (state == maekawa.State.RELEASE)
                    processRelease();
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

    private String criticalSectionLog(int identifier, ArrayList<Integer> subset) {
        long currentTime = System.currentTimeMillis();
        return currentTime + " " + identifier + " " + Utils.printSubset(subset);
    }

    private void printCurrentState() {
        System.out.println("Node " + identifier + " " + state);
    }

    private void changeState(State newState) {
        if (state != newState) {
            state = newState;
            printCurrentState();
        }
    }

}
