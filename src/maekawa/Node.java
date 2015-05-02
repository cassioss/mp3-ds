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

    private volatile int identifier, csInt, timeNextReq;
    private volatile ArrayList<Integer> subset;
    private volatile List<Message> messageQueue;
    private volatile State state;
    private volatile boolean option, voted;

    /**
     * Creates a node based on its identifier, the time it stays in the critical section, the time until the next
     * request, and a simple flag for log printing.
     *
     * @param identifier  the node identifier.
     * @param csInt       the time (in milliseconds) the node stays in the critical section.
     * @param timeNextReq the time (in milliseconds) the node waits after leaving the critical section.
     * @param option      a boolean that checks whether the node should show the messages it received or not.
     */
    public Node(int identifier, int csInt, int timeNextReq, boolean option) {
        this.identifier = identifier;
        this.csInt = csInt;
        this.timeNextReq = timeNextReq;
        this.option = option;
        this.subset = Utils.subset(this.identifier);
        this.state = State.INIT;
        this.voted = false;
        messageQueue = new LinkedList<>();
        printCurrentState();
        new Listener().start();
        new Sender().start();
    }

    private class Listener extends Thread {
        @Override
        public void run() {
            while (!Mutex.timeout) {
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

        private void processRequest() {
            changeState(maekawa.State.HELD);
        }

        /**
         * Process the HELD state, when the node enters the critical section.
         */
        private void processHeld() {
            printCriticalSectionLog();
            changeState(maekawa.State.RELEASE);
        }

        private void processRelease() {
            changeState(maekawa.State.REQUEST);
        }
    }

    private class Sender extends Thread {
        @Override
        public void run() {
            while (!Mutex.timeout) {
                if (state == maekawa.State.REQUEST)
                    processRequest();
                else if (state == maekawa.State.HELD)
                    processHeld();
                else if (state == maekawa.State.RELEASE)
                    processRelease();
            }
        }

        private void processRequest() {

        }

        private void processHeld() {

        }

        private void processRelease() {

        }

        public Message requestMessageTo(int receiverID) {
            return new Message(identifier, receiverID, Content.REQUEST);
        }

        public Message replyMessageTo(int receiverID) {
            return new Message(identifier, receiverID, Content.REPLY);
        }

        public Message releaseMessageTo(int receiverID) {
            return new Message(identifier, receiverID, Content.RELEASE);
        }

    }

    private void printCriticalSectionLog() {
        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime + " " + identifier + " " + Utils.printSubset(subset));
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
