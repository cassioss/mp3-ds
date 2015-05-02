package maekawa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Simple static methods for the remaining classes. Most of them are (or can be) tested.
 *
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public final class Utils {

    /**
     * Generates a list containing the subset of a node given its identifier.
     *
     * @param identifier a number between 0 and 8, corresponding to a given node.
     * @return an array list containing five identifiers for the voting set.
     */
    public static ArrayList<Integer> subset(int identifier) {
        if (identifier < 0 || identifier > 8)
            throw new IllegalArgumentException("You set the wrong identifier");
        HashSet<Integer> subset = new HashSet<>();
        subset.add(identifier);
        switch (identifier % 3) {
            case 0:     // First column
                subset.add(identifier + 1);
                subset.add(identifier + 2);
                if (identifier == 3 || identifier == 6)
                    subset.add(0);
                if (identifier == 0 || identifier == 6)
                    subset.add(3);
                if (identifier == 0 || identifier == 3)
                    subset.add(6);
                break;
            case 1:     // Second column
                subset.add(identifier - 1);
                subset.add(identifier + 1);
                if (identifier == 4 || identifier == 7)
                    subset.add(1);
                if (identifier == 1 || identifier == 7)
                    subset.add(4);
                if (identifier == 1 || identifier == 4)
                    subset.add(7);
                break;
            case 2:     // Third column
                subset.add(identifier - 1);
                subset.add(identifier - 2);
                if (identifier == 5 || identifier == 8)
                    subset.add(2);
                if (identifier == 2 || identifier == 8)
                    subset.add(5);
                if (identifier == 2 || identifier == 5)
                    subset.add(8);
                break;
        }
        ArrayList<Integer> subsetList = new ArrayList<>(subset);
        Collections.sort(subsetList);
        return subsetList;
    }

    public static String printSubset(ArrayList<Integer> subset) {
        return subset.toString().replaceAll("[,\\[\\]]", "");
    }
}