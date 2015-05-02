package tests;

import maekawa.Utils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
        Integer[] testSet1 = {1, 2, 3, 4, 7};
        Integer[] testSet2 = {1, 2, 3, 5, 8};
        Integer[] testSet3 = {1, 2, 3, 6, 9};
        Integer[] testSet4 = {1, 4, 5, 6, 7};
        Integer[] testSet5 = {2, 4, 5, 6, 8};
        Integer[] testSet6 = {3, 4, 5, 6, 9};
        Integer[] testSet7 = {1, 4, 7, 8, 9};
        Integer[] testSet8 = {2, 5, 7, 8, 9};
        Integer[] testSet9 = {3, 6, 7, 8, 9};
        assertEquals(Utils.subset(1), new ArrayList<>(Arrays.asList(testSet1)));
        assertEquals(Utils.subset(2), new ArrayList<>(Arrays.asList(testSet2)));
        assertEquals(Utils.subset(3), new ArrayList<>(Arrays.asList(testSet3)));
        assertEquals(Utils.subset(4), new ArrayList<>(Arrays.asList(testSet4)));
        assertEquals(Utils.subset(5), new ArrayList<>(Arrays.asList(testSet5)));
        assertEquals(Utils.subset(6), new ArrayList<>(Arrays.asList(testSet6)));
        assertEquals(Utils.subset(7), new ArrayList<>(Arrays.asList(testSet7)));
        assertEquals(Utils.subset(8), new ArrayList<>(Arrays.asList(testSet8)));
        assertEquals(Utils.subset(9), new ArrayList<>(Arrays.asList(testSet9)));
    }

    /**
     * Tests throwing an exception for an identifier smaller than 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIdentifier0() {
        Utils.subset(0);
    }

    /**
     * Tests throwing an exception for an identifier greater than 9.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIdentifier10() {
        Utils.subset(10);
    }

    @Test
    public void testSquareBracketRemoval() {
        Integer[] testSet = {1, 3, 4, 5, 8};
        assertEquals(Utils.printSubset(new ArrayList<>(Arrays.asList(testSet))), "1 3 4 5 8");
    }
}