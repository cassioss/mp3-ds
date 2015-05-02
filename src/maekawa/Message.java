package maekawa;

/**
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class Message {
    int identifier;
    Content content;

    public Message(int identifier, Content content) {
        this.identifier = identifier;
        this.content = content;
    }
}
