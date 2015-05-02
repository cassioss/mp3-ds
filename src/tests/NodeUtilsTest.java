package tests;

import maekawa.NodeUtils;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests most of the methods inside the NodeUtils class.
 *
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public class NodeUtilsTest {

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
        assertEquals(NodeUtils.subset(0), new ArrayList<>(Arrays.asList(testSet0)));
        assertEquals(NodeUtils.subset(1), new ArrayList<>(Arrays.asList(testSet1)));
        assertEquals(NodeUtils.subset(2), new ArrayList<>(Arrays.asList(testSet2)));
        assertEquals(NodeUtils.subset(3), new ArrayList<>(Arrays.asList(testSet3)));
        assertEquals(NodeUtils.subset(4), new ArrayList<>(Arrays.asList(testSet4)));
        assertEquals(NodeUtils.subset(5), new ArrayList<>(Arrays.asList(testSet5)));
        assertEquals(NodeUtils.subset(6), new ArrayList<>(Arrays.asList(testSet6)));
        assertEquals(NodeUtils.subset(7), new ArrayList<>(Arrays.asList(testSet7)));
        assertEquals(NodeUtils.subset(8), new ArrayList<>(Arrays.asList(testSet8)));
    }

    /**
     * Tests throwing an exception for an identifier smaller than 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIdentifierNegative() {
        NodeUtils.subset(-1);
    }

    /**
     * Tests throwing an exception for an identifier greater than 8.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIdentifier9() {
        NodeUtils.subset(9);
    }

    /**
     * Tests the removal of square brackets and commas in an array list.
     */
    @Test
    public void testSquareBracketAndCommaRemoval() {
        Integer[] testSet = {1, 3, 4, 5, 8};
        assertEquals(NodeUtils.printSubset(new ArrayList<>(Arrays.asList(testSet))), "1 3 4 5 8");
    }
}