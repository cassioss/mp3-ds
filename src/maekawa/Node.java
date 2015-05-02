package maekawa;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Simulates a node in a distributed system that implements the Maekawa's algorithm.
 *
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class Node {

    private volatile int identifier, csInt, timeNextReq, subsetSize;
    private volatile ArrayList<Integer> subset;
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
        this.subset = Utils.subset(this.identifier);
        this.subsetSize = this.subset.size();
        this.state = State.INIT;
        this.voted = false;
        this.sentMessages = false;
        messageQueue = Collections.synchronizedList(new LinkedList<>());
        responseQueue = Collections.synchronizedList(new LinkedList<>());
        repliesList = Collections.synchronizedList(new LinkedList<>());
        printCurrentState();
        new Listener().start();
        new StateMachine().start();
    }

    private class Listener extends Thread {
        @Override
        public void run() {
            while (!Mutex.timeout) {
                if (messageQueue.size() > 0) {
                    //Utils.sortMessageList(messageQueue);
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

        private void processRequest(Message message) {
            if (state == maekawa.State.HELD || voted) {
                responseQueue.add(message);
                System.out.println("Node " + identifier + " archived REQUEST from node " + message.getSenderID());
            } else {
                sendMessageTo(Content.REPLY, message.getSenderID());
                voted = true;
                //System.out.println("Node " + identifier + " voted for node " + message.getSenderID() + ", sending REPLY");
            }
        }

        private void processReply(Message message) {
            if (repliesList.size() < subsetSize) {
                if (!Utils.hasMessageSentBy(repliesList, message.getSenderID()) && subset.contains(message.getSenderID())) {
                    repliesList.add(message);
                    System.out.println("Node " + identifier + " received REPLY from node " + message.getSenderID());
                }
            }
        }

        private void processRelease() {
            if (responseQueue.size() > 0) {
                //Utils.sortMessageList(responseQueue);
                Message earlyMessage = responseQueue.remove(0);
                sendMessageTo(Content.REPLY, earlyMessage.getSenderID());
                System.out.println("Node " + identifier + " received RELEASE from node " + earlyMessage.getSenderID());
                voted = true;
            } else voted = false;
        }

    }

    private void printMessageLog(Message message) {
        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime + " " + message.getReceiverID() + " " + message.getSenderID() + " " + message.getContent() + " " + message.getSentTime());
    }

    private class StateMachine extends Thread {
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
            if (!sentMessages) {
                sentMessages = true;
                repliesList.clear();
                multicast(Content.REQUEST);
            }
            if (repliesList.size() == subsetSize)
                changeState(maekawa.State.HELD);
        }

        private void processHeld() {
            try {
                sleep(csInt);
                changeState(maekawa.State.RELEASE);
                multicast(Content.RELEASE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void processRelease() {
            try {
                sleep(timeNextReq);
                changeState(maekawa.State.REQUEST);
                sentMessages = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void multicast(Content content) {
        for (Integer id : subset)
            sendMessageTo(content, id);
    }

    private void sendMessageTo(Content content, Integer receiverID) {
        Mutex.sendMessage(new Message(identifier, receiverID, content));
        //System.out.println("Node " + identifier + " sent " + content + " to node " + receiverID);
    }

    private void printCriticalSectionLog() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss.SSS");
        Date now = new Date();
        String currentTime = sdfDate.format(now);
        System.out.println(currentTime + " " + identifier + " " + Utils.printSubset(subset));
    }

    private void printCurrentState() {
        System.out.println("Node " + identifier + " " + state);
    }

    private void changeState(State newState) {
        if (state != newState) {
            state = newState;
            printCurrentState();
            if (newState == State.HELD)
                printCriticalSectionLog();
        }
    }

}
