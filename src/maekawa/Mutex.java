package maekawa;

import java.util.ArrayList;
import java.util.Collections;
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
    protected static volatile boolean multicast = false;
    protected static volatile List<Integer> sendersToRequest;

    /**
     * Main method that creates all nodes and simulates the Maekawa mutex system.
     *
     * @param args all arguments required to run the program properly.
     */
    public static void main(String[] args) {

        nodeList = new ArrayList<>(9);          // Initial capacity of 9 nodes
        sendersToRequest = Collections.synchronizedList(new LinkedList<>());

        // Checks if the user set enough values to start running the algorithm
        if (args.length < 3 || args.length > 4) {
            System.out.println("Usage: java -cp src maekawa.Mutex [cs_int] [next_req] [tot_exec_time] [option]");
            return;
        }

        // Parses the information into processing times
        int csInt = Integer.valueOf(args[0]);
        int timeNextReq = Integer.valueOf(args[1]);
        int totExecTime = Integer.valueOf(args[2]) * 1000; // In order to be counted in seconds

        // Checks if the option being received is either 0, 1 or nothing
        boolean option = false;
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
     * Gets the receiver of a Message and sends it to him.
     *
     * @param message a Message object sent by a node.
     */
    protected static void sendMessage(Message message) {
        nodeList.get(message.getReceiverID()).messageQueue.add(message);
    }

    /**
     * Multicasts a message to all the nodes in a sender's subset.
     *
     * @param senderID the sender identifier.
     * @param content  the intended content.
     */
    protected static void sendMessageToSubsetOf(int senderID, Content content) {
        if (content == Content.REQUEST)     // This is a REQUEST multicast, deadlocks might happen
            semaphore(senderID);
        else                                // This is a RELEASE multicast, no deadlocks will happen
            resendMessage(senderID, content);
    }

    /**
     * Processes a sender's REQUEST multicast in a semaphore.
     *
     * @param senderID the sender identifier.
     */
    protected static void semaphore(int senderID) {
        sendersToRequest.add(senderID); // Adds every sender ID to a queue

        // Basic structure of a semaphore, in order to coordinate every REQUEST multicast
        // This semaphore process every multicast in the order they were received in the queue
        if (!multicast) {
            multicast = true;
            int senderToRequest = sendersToRequest.remove(0);
            resendMessage(senderToRequest, Content.REQUEST);
            multicast = false;
        }
    }

    /**
     * Resends a content in a message to all the nodes that are in a sender's subset.
     *
     * @param senderID the sender identifier.
     * @param content  the intended content.
     */
    protected static void resendMessage(int senderID, Content content) {
        List<Integer> senderSubset = nodeList.get(senderID).subset;
        for (Integer identifier : senderSubset)
            sendMessage(new Message(senderID, identifier, content));
    }

    /**
     * Simulates a timer that counts the total execution time.
     */
    private static class Timer extends Thread {

        /**
         * Runs the Timer thread, which basically coordinates all thread's timeout.
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