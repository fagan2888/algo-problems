import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/minimum-size-subarray-sum/
//
// Given an array of positive integers and a positive integer s, find the
// minimal length of a subarray of which the sum >= s. If there isn't one,
// return 0 instead.
public class MinSubarrayLen {
    static class PartialSum {
        int len;
        int sum;
        PartialSum(int len, int sum) {
            this.len = len;
            this.sum = sum;
        }
    }

    // DP
    // time complexity: O(N), space complexity: O(N)
    // beats 4.33%(3 ms)
    public int minSubArrayLen(int s, int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (s <= 0) return 1;

        PartialSum[] sums = new PartialSum[n];
        sums[0] = new PartialSum(1, nums[0]);
        for (int i = 1; i < n; i++) {
            PartialSum last = sums[i - 1];
            int len = last.len + 1;
            int sum = last.sum + nums[i];
            while (sum - nums[i - len + 1] >= s) {
                sum -= nums[i - len + 1];
                len--;
            }
            sums[i] = new PartialSum(len, sum);
        }

        if (sums[n - 1].sum < s) return 0;

        int minLen = n;
        for (PartialSum sum : sums) {
            if (sum.sum >= s && sum.len < minLen) {
                minLen = sum.len;
            }
        }
        return minLen;
    }

    // Two pointers
    // time complexity: O(N), space complexity: O(1)
    // beats 16.03%(1 ms)
    public int minSubArrayLen2(int s, int[] nums) {
        if (s <= 0) return 1;

        int left = 0;
        int sum = 0;
        int n = nums.length;
        int minLen = n;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            if (sum < s) continue;

            while (sum - nums[left] >= s) {
                sum -= nums[left];
                left++;
            }
            minLen = Math.min(minLen, i - left + 1);
        }
        return sum < s ? 0 : minLen;
    }

    // binary search
    // time complexity: O(N * log(N)), space complexity: O(1)
    // beats 4.33%(3 ms)
    public int minSubArrayLen3(int s, int[] nums) {
        int n = nums.length;
        int lowLen = 1;
        int highLen = n;
        while (lowLen <= highLen) {
            int mid = lowLen + (highLen - lowLen) / 2;
            if (isEnough(nums, s, mid)) {
                highLen = mid - 1;
            } else {
                lowLen = mid + 1;
            }
        }
        return highLen >= n ? 0 : lowLen;
    }

    private boolean isEnough(int[] nums, int s, int len) {
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (i >= len) {
                sum -= nums[i - len];
            }
            if (sum >= s) return true;
        }
        return false;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, int[], Integer> minLen, String name,
              int s, int[] nums, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)minLen.apply(s, nums));
        if (nums.length > 50) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
    }
    void test(int s, int[] nums, int expected) {
        MinSubarrayLen m = new MinSubarrayLen();
        test(m::minSubArrayLen, "minSubArrayLen", s, nums, expected);
        test(m::minSubArrayLen2, "minSubArrayLen2", s, nums, expected);
        test(m::minSubArrayLen3, "minSubArrayLen3", s, nums, expected);
    }

    @Test
    public void test1() {
        test(1, new int[] {}, 0);
        test(0, new int[] {2, 3}, 1);
        test(6, new int[] {2, 3}, 0);
        test(6, new int[] {2, 3, 1}, 3);
        test(7, new int[] {2, 3, 1, 2, 4, 3}, 2);
        test(697439, new int[] {5334,6299,4199,9663,8945,3566,9509,3124,6026,
            6250,7475,5420,9201,9501,38,5897,4411,6638,9845,161,9563,8854,3731,
            5564,5331,4294,3275,1972,1521,2377,3701,6462,6778,187,9778,758,550,
            7510,6225,8691,3666,4622,9722,8011,7247,575,5431,4777,4032,8682,5888,
            8047,3562,9462,6501,7855,505,4675,6973,493,1374,3227,1244,7364,
            2298,3244,8627,5102,6375,8653,1820,3857,7195,7830,4461,7821,
            5037,2918,4279,2791,1500,9858,6915,5156,970,1471,5296,1688,578,
            7266,4182,1430,4985,5730,7941,3880,607,8776,1348,2974,1094,6733,
            5177,4975,5421,8190,8255,9112,8651,2797,335,8677,3754,893,1818,
            8479,5875,1695,8295,7993,7037,8546,7906,4102,7279,1407,2462,4425,
            2148,2925,3903,5447,5893,3534,3663,8307,8679,8474,1202,3474,2961,
            1149,7451,4279,7875,5692,6186,8109,7763,7798,2250,2969,7974,9781,
            7741,4914,5446,1861,8914,2544,5683,8952,6745,4870,1848,7887,6448,
            7873,128,3281,794,1965,7036,8094,1211,9450,6981,4244,2418,8610,8681,
            2402,2904,7712,3252,5029,3004,5526,6965,8866,2764,600,631,9075,2631,
            3411,2737,2328,652,494,6556,9391,4517,8934,8892,4561,9331,1386,4636,
            9627,5435,9272,110,413,9706,5470,5008,1706,7045,9648,7505,6968,7509,
            3120,7869,6776,6434,7994,5441,288,492,1617,3274,7019,5575,6664,6056,
            7069,1996,9581,3103,9266,2554,7471,4251,4320,4749,649,2617,3018,
            4332, 415,2243,1924,69,5902,3602,2925,6542,345,4657,9034,8977,6799,
            8397, 1187, 3678,4921,6518,851,6941,6920,259,4503,2637,7438,3893,
            5042, 8552,6661, 5043,9555,9095,4123,142,1446,8047,6234,1199,8848,
            5656, 1910,3430,2843, 8043,9156,7838,2332,9634,2410,2958,3431,4270,
            1420, 4227,7712,6648,1607, 1575,3741,1493,7770,3018,5398,6215,8601,
            6244,7551,2587,2254,3607,1147, 5184,9173,8680,8610,1597,1763,7914,
            3441,7006,1318,7044,7267,8206, 9684,4814,9748,4497,2239}, 132);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinSubarrayLen");
    }
}
