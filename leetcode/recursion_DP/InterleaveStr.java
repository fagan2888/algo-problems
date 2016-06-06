import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given s1, s2, s3, find whether s3 is formed by the interleaving of s1 and s2.
public class InterleaveStr {
    // Time Limit Exceeded
    public boolean isInterleave(String s1, String s2, String s3) {
        int i1 = 0;
        int i2 = 0;
        int len1 = s1.length();
        int len2 = s2.length();
        int len3 = s3.length();
        for (int i3 = 0; i3 < len3; i3++) {
            char c1 = (i1 < len1) ? s1.charAt(i1) : '\0';
            char c2 = (i2 < len2) ? s2.charAt(i2) : '\0';
            char c3 = s3.charAt(i3);

            if (c3 != c1 && c3 != c2) return false;

            if (c3 == c1 && c3 == c2) {
                return isInterleave(s1.substring(1), s2, s3.substring(1))
                       || isInterleave(s1, s2.substring(1), s3.substring(1));
            }

            if (c3 == c1 && c3 != c2) {
                i1++;
            } else {
                i2++;
            }
        }
        return i1 == len1 && i2 == len2;
    }

    // Time Limit Exceeded
    public boolean isInterleave2(String s1, String s2, String s3) {
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int len1 = s1.length();
        int len2 = s2.length();
        int len3 = s3.length();
        Stack<Integer> marks = new Stack<>();

        for (; ; i3++) {
            if (i3 == len3 && i1 == len1 && i2 == len2) return true;

            if (i3 < len3 && i1 < len1 && i2 < len2) {
                char c1 = s1.charAt(i1);
                char c2 = s2.charAt(i2);
                char c3 = s3.charAt(i3);
                if (i3 < len3 && c3 == c1 && c3 != c2) {
                    if (++i1 < len1) continue;

                    if (s3.substring(i3 + 1).equals(s2.substring(i2))) return true;
                } else if (i3 < len3 && c3 != c1 && c3 == c2) {
                    if (++i2 < len2) continue;

                    if (s3.substring(i3 + 1).equals(s1.substring(i1))) return true;
                } else if (i3 < len3 && c3 == c1 && c3 == c2) {
                    marks.push(i2 + 1);
                    marks.push(i1++);
                    continue;
                }
            }
            if (marks.isEmpty()) break;

            i1 = marks.pop();
            i2 = marks.pop();
            i3 = i1 + i2 - 1;
        }

        if (i1 == len1) {
            return s3.substring(i3).equals(s2.substring(i2));
        }
        if (i2 == len2) {
            return s3.substring(i3).equals(s1.substring(i1));
        }
        return false;
    }

    // beats 33.45%
    public boolean isInterleave3(String s1, String s2, String s3) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 + len2 != s3.length()) return false;

        boolean[][] dp = new boolean[len1 + 1][len2 + 1];
        // could improve by shrinking len1 and len2 by checking heads and tails
        dp[0][0] = true;
        for (int i1 = 0; i1 < len1; i1++) {
            dp[i1 + 1][0] = s3.charAt(i1) == s1.charAt(i1);
            if (!dp[i1 + 1][0]) break; // save time
        }
        for (int i2 = 0; i2 < len2; i2++) {
            dp[0][i2 + 1] = s3.charAt(i2) == s2.charAt(i2);

            if (!dp[0][i2 + 1]) break; // save time
        }
        for (int i1 = 0; i1 < len1; i1++) {
            for (int i2 = 0; i2 < len2; i2++) {
                char c = s3.charAt(i1 + i2 + 1);
                if (c == s1.charAt(i1)) {
                    dp[i1 + 1][i2 + 1] = dp[i1][i2 + 1];
                }
                if (c == s2.charAt(i2)) {
                    dp[i1 + 1][i2 + 1] |= dp[i1 + 1][i2];
                }
            }
        }
        return dp[len1][len2];
    }

    // beats 79.69%
    public boolean isInterleave4(String s1, String s2, String s3) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 + len2 != s3.length()) return false;

        boolean[] dp = new boolean[len2 + 1];
        // first row
        dp[0] = true;
        for (int i2 = 0; i2 < len2; i2++) {
            dp[i2 + 1] = dp[i2] & (s3.charAt(i2) == s2.charAt(i2));
        }
        for (int i1 = 0; i1 < len1; i1++) {
            // first column
            dp[0] &= (s3.charAt(i1) == s1.charAt(i1));
            for (int i2 = 0; i2 < len2; i2++) {
                if (!dp[i2] && !dp[i2 + 1]) continue;

                char c = s3.charAt(i1 + i2 + 1);
                dp[i2 + 1] &= (c == s1.charAt(i1));
                if (dp[i2]) {
                    dp[i2 + 1] |= (c == s2.charAt(i2));
                }
            }
        }
        return dp[len2];
    }

    void test(String s1, String s2, String s3, boolean expected) {
        assertEquals(expected, isInterleave(s1, s2, s3));
        assertEquals(expected, isInterleave2(s1, s2, s3));
        assertEquals(expected, isInterleave3(s1, s2, s3));
        assertEquals(expected, isInterleave4(s1, s2, s3));
    }

    @Test
    public void test1() {
        test("", "", "", true);
        test("", "b", "b", true);
        test("", "b", "a", false);
        test("b", "", "b", true);
        test("a", "b", "a", false);
        test("ab", "bc", "bbac", false);
        test("bc", "bb", "bbcb", true);
        test("bc", "bbca", "bbcbca", true);
        test("ab", "bca", "abbac", false);
        test("ab", "ca", "abac", false);
        test("ab", "cd", "abdc", false);
        test("aa", "aae", "aaeaa", true);
        test("aa", "aae", "aaeaa", true);
        test("aabc", "a", "aabca", true);
        test("aa", "aadaae", "aadaaeaa", true);
        test("aabc", "abad", "aabcabad", true);
        test("bcc", "bbca", "bbcbcac", true);
        test("aabcc", "dbbca", "aadbbcbcac", true);
        test("aabcc", "dbbca", "aadbbbcacc", true);
        test("aabcc", "dbbca", "aadbbbaccc", false);
        test("aacaac", "aacaaeaac", "aacaaeaaeaacaac", false);
        test("aabaac", "aadaaeaaf", "aadaaeaabaafaac", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("InterleaveStr");
    }
}
