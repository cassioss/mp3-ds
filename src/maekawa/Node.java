package maekawa;

import java.util.*;

/**
 * Simulates a node in a distributed system that implements the Maekawa's algorithm.
 *
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class Node {

    private volatile int identifier, csInt, timeNextReq, subsetSize;
    protected volatile List<Integer> subset;
    protected volatile List<Message> messageQueue, responseQueue, repliesList;
    private volatile State state;
    private volatile boolean shouldPrintLogs, voted, sentMessages;

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
        this.shouldPrintLogs = option;
        this.subset = Collections.synchronizedList(Utils.subset(this.identifier));
        this.subsetSize = this.subset.size();
        this.state = State.INIT;
        this.voted = false;
        this.sentMessages = false;
        messageQueue = Collections.synchronizedList(new LinkedList<>());
        responseQueue = Collections.synchronizedList(new LinkedList<>());
        repliesList = Collections.synchronizedList(new LinkedList<>());
        new Listener().start();
        new StateMachine().start();
    }

    /**
     * Runs a thread that listens to the message queue and processes every message being received.
     *
     * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
     * @version 1.0
     */
    private class Listener extends Thread {

        /**
         * Runs the Listener thread, keeping track of every message being sent to this node.
         */
        @Override
        public void run() {
            while (!Mutex.timeout) {
                if (messageQueue.size() > 0) {
                    Message firstMessage = messageQueue.remove(0);
                    if (shouldPrintLogs)
                        printMessageLog(firstMessage);
                    if (firstMessage.getContent() == Content.REQUEST)
                        processRequest(firstMessage);
                    else if (firstMessage.getContent() == Content.REPLY)
                        processReply(firstMessage);
                    else
                        processRelease();
                }
            }
        }

        /**
         * Processes a REQUEST message from another node inside its subset.
         *
         * @param message a Message object with a REQUEST content.
         */
        private void processRequest(Message message) {
            if (state == maekawa.State.HELD || voted) {
                responseQueue.add(message);
            } else {
                sendMessageTo(Content.REPLY, message.getSenderID());
                voted = true;
            }
        }

        /**
         * Processes a REPLY message from another node inside its subset.
         *
         * @param message a Message object with a REPLY content.
         */
        private void processReply(Message message) {
            if (repliesList.size() < subsetSize) {
                if (!Utils.hasMessageSentBy(repliesList, message.getSenderID()) && subset.contains(message.getSenderID()))
                    repliesList.add(message);
            }
        }

        /**
         * Processes a RELEASE message from another node inside its subset.
         */
        private void processRelease() {
            if (responseQueue.size() > 0) {
                Message earlyMessage = responseQueue.remove(0);
                sendMessageTo(Content.REPLY, earlyMessage.getSenderID());
                voted = true;
            } else voted = false;
        }

    } // End of Listener thread


    /**
     * Runs a thread that keeps track of every  of this node.
     *
     * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
     * @version 1.0
     */
    private class StateMachine extends Thread {

        /**
         * Runs the StateMachine thread. INIT is not processed continuously, since the node only stays in it once, and at the beginning.
         */
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

        /**
         * Processes the REQUEST state.
         */
        private void processRequest() {
            if (!sentMessages) {
                sentMessages = true;
                repliesList.clear();
                multicast(Content.REQUEST);
            }
            if (repliesList.size() == subsetSize)
                changeState(maekawa.State.HELD);
        }

        /**
         * Processes the HELD state.
         */
        private void processHeld() {
            try {
                sleep(csInt);
                changeState(maekawa.State.RELEASE);
                multicast(Content.RELEASE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Processes the RELEASE state.
         */
        private void processRelease() {
            try {
                sleep(timeNextReq);
                changeState(maekawa.State.REQUEST);
                sentMessages = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    } // End of StateMachine thread

    // Message-sending methods

    /**
     * Sends a Message object with a specific content to a specific receiver.
     *
     * @param content    the message content (REQUEST, REPLY or RELEASE).
     * @param receiverID the receiver identifier.
     */
    private void sendMessageTo(Content content, Integer receiverID) {
        Mutex.sendMessage(new Message(identifier, receiverID, content));
        //System.out.println("Node " + identifier + " sent " + content + " to node " + receiverID);
    }

    /**
     * Multicasts the same content as a Message object to all nodes inside this node's subset. They are sent to the nodes at practically the same time.
     *
     * @param content a content being multicast.
     */
    private void multicast(Content content) {
        Mutex.sendMessageToSubsetOf(identifier, content);
    }

    // State-related methods

    /**
     * Changes a node's state. If the node goes to the HELD state, it prints a message log.
     *
     * @param newState the node's new state.
     */
    private void changeState(State newState) {
        if (state != newState) {
            state = newState;
            if (newState == State.HELD)
                printCriticalSectionLog();
        }
    }

    // Log printing methods

    /**
     * Prints a message log whenever the node enters the critical section.
     */
    private void printCriticalSectionLog() {
        System.out.println(Utils.getCurrentTime() + " " + identifier + " " + Utils.printSubset(subset));
    }

    /**
     * Prints a message log whenever a node receives it. Only works if option was set to 1 before running Mutex.
     *
     * @param message a Message object being received.
     */
    private void printMessageLog(Message message) {
        System.out.println(Utils.getCurrentTime() + " " + message.getReceiverID() + " " + message.getSenderID() + " " + message.getContent());
    }
}