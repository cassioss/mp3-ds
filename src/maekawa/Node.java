package maekawa;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class Node {

    private int identifier, csInt, timeNextReq, totExecTime, option;
    private ArrayList<Integer> subset;
    private volatile List<Message> messageQueue;
    private State state;
    private Mutex parentMutex;

    public Node(int identifier, int csInt, int timeNextReq, int totExecTime, int option) {
        this.identifier = identifier;
        this.csInt = csInt;
        this.timeNextReq = timeNextReq;
        this.totExecTime = totExecTime;
        this.option = option;
        this.subset = Utils.subset(this.identifier);
        this.state = State.INIT;
        printCurrentState();
        new Listener().start();
        new Sender().start();
    }

    private class Listener extends Thread {
        @Override
        public void run() {
            while (true) {
                if (Mutex.afterInit)
                    changeState(maekawa.State.REQUEST);
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
