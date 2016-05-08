import org.junit.Test;
import static org.junit.Assert.*;

// Find the longest common prefix string amongst an array of strings.
public class LongestCommonPrefix {
    // beats 40.93%
    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";

        String first = strs[0];
        for (int i = 0; i < first.length(); i++) {
            char c = first.charAt(i);
            int j = strs.length - 1;
            for (; j > 0; j--) {
                String s = strs[j];
                if (s.length() <= i || s.charAt(i) != c) {
                    return first.substring(0, i);
                }
            }
        }
        return first;
    }

    // from the solution
    // beats 80.10%
    public String longestCommonPrefix2(String[] strs) {
        if (strs.length == 0) return "";

        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        }
        return prefix;
    }

    // from the solution
    public String longestCommonPrefix3(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        return longestCommonPrefix3(strs, 0, strs.length - 1);
    }

    private String longestCommonPrefix3(String[] strs, int l, int r) {
        if (l == r) return strs[l];

        int mid = (l + r)/2;
        String lcpLeft = longestCommonPrefix3(strs, l, mid);
        String lcpRight = longestCommonPrefix3(strs, mid + 1, r);
        return commonPrefix(lcpLeft, lcpRight);
    }

    private String commonPrefix(String left, String right) {
        int min = Math.min(left.length(), right.length());
        for (int i = 0; i < min; i++) {
            if (left.charAt(i) != right.charAt(i))
                return left.substring(0, i);
        }
        return left.substring(0, min);
    }

    // from the solution
    public String longestCommonPrefix4(String[] strs) {
        if (strs == null || strs.length == 0) return "";

        int minLen = Integer.MAX_VALUE;
        for (String str : strs) {
            minLen = Math.min(minLen, str.length());
        }
        int low = 0;
        int high = minLen;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (isCommonPrefix(strs, middle)) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        return strs[0].substring(0, (low + high) / 2);
    }

    private boolean isCommonPrefix(String[] strs, int len){
        String str1 = strs[0].substring(0,len);
        for (int i = 1; i < strs.length; i++) {
            if (!strs[i].startsWith(str1)) return false;
        }
        return true;
    }

    void test(String[] strs, String expected) {
        assertEquals(expected, longestCommonPrefix(strs));
        assertEquals(expected, longestCommonPrefix2(strs));
        assertEquals(expected, longestCommonPrefix3(strs));
        assertEquals(expected, longestCommonPrefix4(strs));
    }

    @Test
    public void test1() {
        test(new String[] {"abc", "abef", "abk", "aba"}, "ab");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestCommonPrefix");
    }
}
