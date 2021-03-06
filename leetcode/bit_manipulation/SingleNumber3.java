import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// LC260: https://leetcode.com/problems/single-number-iii/
//
// Given an array of numbers nums, in which exactly two elements appear only
// once and all the other elements appear exactly twice. Find the two elements
// that appear only once.
public class SingleNumber3 {
    // beats 78.41%(1 ms for 30 tests)
    public int[] singleNumber(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        int diffMask = xor & -xor; // or:  xor - (xor & (xor - 1));
        int a = 0;
        int b = 0;
        for (int num : nums) {
            if ((num & diffMask) == 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        return new int[] {a, b};
    }

    // Solution of Choice
    // beats 35.45%(2 ms for 30 tests)
    public int[] singleNumber2(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        xor &= -xor;
        int[] res = new int[2];
        for (int num : nums) {
            res[(num & xor) == 0 ? 0 : 1] ^= num;
        }
        return res;
    }

    void test(Function<int[], int[]> single, int[] expected, int ... nums) {
        int[] res = single.apply(nums);
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(int[] expected, int ... nums) {
        SingleNumber3 s = new SingleNumber3();
        test(s::singleNumber, expected, nums);
        test(s::singleNumber2, expected, nums);
    }

    @Test
    public void test1() {
        test(new int[] {3, 5}, 1, 2, 1, 3, 2, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SingleNumber3");
    }
}
