package maekawa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Cassio
 * @version 1.0
 */
public final class Utils {
    public static ArrayList<Integer> subset(int identifier) {
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
}