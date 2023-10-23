import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class that provides fuzzy search functionality using the Levenshtein
 * Distance algorithm.
 */
public class FuzzySearch {

    /**
     * Returns the minimum of three integers.
     *
     * @param x the first integer
     * @param y the second integer
     * @param z the third integer
     * @return the minimum value among the three integers
     */
    public static int myMin(int x, int y, int z) {

        int min = Math.min(x, y);
        min = Math.min(min, z);

        return min;
    }

    /**
     * Computes the Levenshtein Distance between two strings.
     *
     * @param str1 the first string
     * @param str2 the second string
     * @param x    the current index in the first string
     * @param y    the current index in the second string
     * @return the Levenshtein Distance between the two strings
     */
    public static int LevenshteinDistance(String str1, String str2, int x, int y) {

        if (x == (str1.length()) + 1) {
            return (str2.length()) - y + 1;
        }

        if (y == (str2.length()) + 1) {
            return (str1.length()) - x + 1;
        }

        if (str1.charAt(x - 1) == str2.charAt(y - 1)) {
            return LevenshteinDistance(str1, str2, x + 1, y + 1);
        }

        return 1 + myMin(LevenshteinDistance(str1, str2, x, y + 1), LevenshteinDistance(str1, str2, x + 1, y),
                LevenshteinDistance(str1, str2, x + 1, y + 1));
    }

    /**
     * Constructs a new instance of FuzzySearch.
     */
    public FuzzySearch() {

    }

    /**
     * The main entry point of the FuzzySearch program.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String arr[] = { "reddpie", "bluepie", "pie", "awe", "awaaa", "ew", "2123", "asdjwiuqwyqo", "dooaw" };
        String word = "piesthatareveryredandblue";

        String result[] = Fuzzy(arr, word);

        for (int i = 0; i < Array.getLength(result); i++) {
            System.out.println("result[" + i + "] = " + result[i]);
        }

    }

    /**
     * Performs a fuzzy search on an array of strings using a given word.
     *
     * @param arr  the array of strings to search
     * @param word the word to search for
     * @return an array of strings sorted based on their similarity to the given
     *         word
     */
    public static String[] Fuzzy(String[] arr, String word) {

        Map<String, Integer> map = new HashMap<String, Integer>();

        for (int i = 0; i < Array.getLength(arr); i++) {
            int distance = LevenshteinDistance(word, arr[i], 1, 1);
            map.put(arr[i], distance);
        }

        // Create a list from elements of HashMap
        // https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        LinkedList<String> result = new LinkedList<String>();
        for (int i = 0; i < list.size(); i++) {

            result.add(list.get(i).getKey());
        }

        return result.toArray(new String[result.size()]);
    }

}
