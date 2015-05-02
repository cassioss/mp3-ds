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
    private long sentTime;

    public Message(int senderID, int receiverID, Content content) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.sentTime = System.nanoTime();
    }

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public Content getContent() {
        return content;
    }

    public long getSentTime() {
        return sentTime;
    }
}
