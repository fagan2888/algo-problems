import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Implement next permutation, which rearranges numbers into the
// lexicographically next greater permutation of numbers.
public class NextPermutation {
    // beats 69.33% (why sometimes 9.15%?)
    public void nextPermutation(int[] nums) {
        int len = nums.length;
        int i = len - 1;
        for (; i > 0; i--) {
            if (nums[i] > nums[i - 1]) { // swap
                int j = i - 1;
                // for (++i; i < len && nums[j] < nums[i]; i++) {}
                for (++i; i < len; i++) {
                    if (nums[j] >= nums[i]) break;
                }
                swap(nums, --i, j);
                i = j + 1;
                break;
            }
        }
        // reverse from i to the end
        for (int j = len - 1; i < j; i++, j--) {
            swap(nums, i, j);
        }
    }

    // beats 69.33%
    public void nextPermutation2(int[] nums) {
        int len = nums.length;
        int i = len - 1;
        for (; i > 0 && nums[i - 1] >= nums[i]; i--);

        if (i > 0) {
            int j = len - 1;
            for ( ; j >= 0 && nums[j] <= nums[i - 1]; j--) ;
            swap(nums, i - 1, j);
        }
        // reverse from i to the end
        for (int j = len - 1; i < j; i++, j--) {
            swap(nums, i, j);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    void test(int[] nums, int[] expected) {
        int[] input = nums.clone();
        nextPermutation(input);
        assertArrayEquals(expected, input);
        nextPermutation2(nums);
        assertArrayEquals(expected, nums);
        System.out.println(Arrays.toString(nums));
    }

    @Test
    public void test1() {
        test(new int[] {}, new int[] {});
        test(new int[] {1}, new int[] {1});
        test(new int[] {1, 2, 3}, new int[] {1, 3, 2});
        test(new int[] {3, 2, 1}, new int[] {1, 2, 3});
        test(new int[] {1, 1, 5}, new int[] {1, 5, 1});
        test(new int[] {1, 3, 2}, new int[] {2, 1, 3});
        test(new int[] {1, 5, 1}, new int[] {5, 1, 1});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NextPermutation");
    }
}
