package maekawa;

/**
 * Creates a message that contains the sender node's identifier and a simple content (either REQUEST, REPLY or RELEASE).
 *
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class Message {
    private int senderID, receiverID;
    private Content content;
    private String sentTime;
    private long sentNanoTime;

    /**
     * Creates a message based on its sender, its receiver and its content. Its timestamp is the current system time.
     *
     * @param senderID   the sender identifier.
     * @param receiverID the receiver identifier.
     * @param content    the content of the message (REQUEST, REPLY or RELEASE).
     */
    public Message(int senderID, int receiverID, Content content) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        setSentTime();
    }

    /**
     * Sets the current time in a reasonable format for the message.
     */
    private void setSentTime() {
        sentNanoTime = System.nanoTime();
        sentTime = Utils.getCurrentTime();
    }

    // Simple getters for each information

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public Content getContent() {
        return content;
    }

    public String getSentTime() {
        return sentTime;
    }


    public long getNanoTime() {
        return sentNanoTime;
    }
}
