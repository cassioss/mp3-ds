package maekawa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Cassio dos Santos Sousa <dssntss2@illinois.edu>
 * @version 1.0
 */
public final class Utils {

    /**
     * Generates a list containing the subset of a node given its identifier.
     *
     * @param identifier a number between 1 and 9, corresponding to a given node.
     * @return an array list containing five identifiers for the voting set.
     */
    public static ArrayList<Integer> subset(int identifier) {
        if (identifier < 1 || identifier > 9)
            throw new IllegalArgumentException("You set the wrong identifier");
        HashSet<Integer> subset = new HashSet<>();
        subset.add(identifier);
        switch (identifier % 3) {
            case 1:     // First column
                subset.add(identifier + 1);
                subset.add(identifier + 2);
                if (identifier == 4 || identifier == 7)
                    subset.add(1);
                if (identifier == 1 || identifier == 7)
                    subset.add(4);
                if (identifier == 1 || identifier == 4)
                    subset.add(7);
                break;
            case 2:     // Second column
                subset.add(identifier - 1);
                subset.add(identifier + 1);
                if (identifier == 5 || identifier == 8)
                    subset.add(2);
                if (identifier == 2 || identifier == 8)
                    subset.add(5);
                if (identifier == 2 || identifier == 5)
                    subset.add(8);
                break;
            case 0:     // Third column
                subset.add(identifier - 1);
                subset.add(identifier - 2);
                if (identifier == 6 || identifier == 9)
                    subset.add(3);
                if (identifier == 3 || identifier == 9)
                    subset.add(6);
                if (identifier == 3 || identifier == 6)
                    subset.add(9);
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