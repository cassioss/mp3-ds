package tests;

import maekawa.Content;
import maekawa.Message;
import maekawa.Utils;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class UtilsTest {

    /**
     * Tests the correct output for the subset() method.
     */
    @Test
    public void testSubset() {
        Integer[] testSet0 = {0, 1, 2, 3, 6};
        Integer[] testSet1 = {0, 1, 2, 4, 7};
        Integer[] testSet2 = {0, 1, 2, 5, 8};
        Integer[] testSet3 = {0, 3, 4, 5, 6};
        Integer[] testSet4 = {1, 3, 4, 5, 7};
        Integer[] testSet5 = {2, 3, 4, 5, 8};
        Integer[] testSet6 = {0, 3, 6, 7, 8};
        Integer[] testSet7 = {1, 4, 6, 7, 8};
        Integer[] testSet8 = {2, 5, 6, 7, 8};
        assertEquals(Utils.subset(0), new ArrayList<>(Arrays.asList(testSet0)));
        assertEquals(Utils.subset(1), new ArrayList<>(Arrays.asList(testSet1)));
        assertEquals(Utils.subset(2), new ArrayList<>(Arrays.asList(testSet2)));
        assertEquals(Utils.subset(3), new ArrayList<>(Arrays.asList(testSet3)));
        assertEquals(Utils.subset(4), new ArrayList<>(Arrays.asList(testSet4)));
        assertEquals(Utils.subset(5), new ArrayList<>(Arrays.asList(testSet5)));
        assertEquals(Utils.subset(6), new ArrayList<>(Arrays.asList(testSet6)));
        assertEquals(Utils.subset(7), new ArrayList<>(Arrays.asList(testSet7)));
        assertEquals(Utils.subset(8), new ArrayList<>(Arrays.asList(testSet8)));
    }

    /**
     * Tests throwing an exception for an identifier smaller than 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIdentifierNegative() {
        Utils.subset(-1);
    }

    /**
     * Tests throwing an exception for an identifier greater than 8.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIdentifier9() {
        Utils.subset(9);
    }

    /**
     * Tests the removal of square brackets and commas in an array list.
     */
    @Test
    public void testSquareBracketAndCommaRemoval() {
        Integer[] testSet = {1, 3, 4, 5, 8};
        assertEquals(Utils.printSubset(new ArrayList<>(Arrays.asList(testSet))), "1 3 4 5 8");
    }

    @Test
    public void testOrderingMessages() {
        Message m1 = new Message(1, 3, Content.RELEASE);    // This message comes before
        Message m2 = new Message(2, 3, Content.RELEASE);    // this one
        List<Message> messageList = new LinkedList<>();
        messageList.add(m2);
        messageList.add(m1);
        assertEquals(messageList.get(0), m2);               // Checks that, initially,
        assertEquals(messageList.get(1), m1);               // the nodes are unordered
        Utils.sortMessageList(messageList);
        assertEquals(messageList.get(0), m1);               // Checks now that
        assertEquals(messageList.get(1), m2);               // The nodes are ordered
    }
}