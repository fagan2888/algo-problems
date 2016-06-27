import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/largest-number/
//
// Given a list of non negative integers, arrange them such that they form the
// largest number.
public class LargestNumber {
    // beats 8.51%(152 ms)
    public String largestNumber(int[] nums) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(nums).boxed().sorted(new NumComparator())
                    .forEach(num -> sb.append(num));
        while (sb.length() > 1 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    class NumComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return compare(String.valueOf(a), String.valueOf(b));
        }

        private int compare(String s1, String s2) {
            int len1 = s1.length();
            int len2 = s2.length();
            int len = Math.min(len1, len2);

            for (int i = 0; i < len; i++) {
                int diff = s1.charAt(i) - s2.charAt(i);
                if (diff > 0) return -1;
                if (diff < 0) return 1;
            }

            if (s1.equals(s2)) return 0;

            if (len1 > len2)
                return compare(s1.substring(len2) + s1.substring(0, len2), s1);

            return compare(s2, s2.substring(len1) + s2.substring(0, len1));
        }
    }

    // beats 10.68%(148 ms)
    public String largestNumber2(int[] nums) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(nums).boxed().sorted(new NumComparator2())
                    .forEach(num -> sb.append(num));
        while (sb.length() > 1 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    class NumComparator2 implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            String s1 = String.valueOf(a);
            String s2 = String.valueOf(b);
            return (s2 + s1).compareTo(s1 + s2);
        }
    }

    void test(String expected, int ... nums) {
        assertEquals(expected, largestNumber(nums));
        assertEquals(expected, largestNumber2(nums));
    }

    @Test
    public void test1() {
        test("0", 0, 0);
        test("0", 0, 0, 0);
        test("34330", 3, 30, 34);
        test("9534330", 3, 30, 34, 5, 9);
        test("12812", 128, 12);
        test("12121", 121, 12);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LargestNumber");
    }
}
