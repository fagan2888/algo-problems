import org.junit.Test;
import static org.junit.Assert.*;

// LC410: https://leetcode.com/problems/split-array-largest-sum/
//
// Given an array which consists of non-negative integers and an integer m, you
// can split the array into m non-empty continuous subarrays. Write an algorithm
// to minimize the largest sum among these m subarrays.
// Note:
// Given m satisfies the following constraint: 1 ≤ m ≤ length(nums) ≤ 14,000.
public class SplitArray {
    // Binary Search + Delimiter
    // beats N/A(9 ms for 28 tests)
    // time complexity: O(N * log(SUM)), space complexity: O(1)
    public int splitArray(int[] nums, int m) {
        long max = 0;
        long sum = 0;
        for (int num : nums) {
            max = Math.max(max, num);
            sum += num;
        }
        long low = max;
        for (long high = sum; low <= high; ) {
            long mid = (low + high) >>> 1;
            if (check(nums, mid, m)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return (int)low;
    }

    private boolean check(int[] nums, long limit, int m) {
        int sum = 0;
        for (int num : nums) {
            if ((sum += num) > limit) {
                if (--m <= 0) return false;

                sum = num;
            }
        }
        return true;
    }

    // Divide & Conquer
    // Time Limit Exceeded
    public int splitArray2(int[] nums, int m) {
        int n = nums.length;
        long[] sum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        return (int)splitArray2(nums, 0, nums.length, m, sum);
    }

    private long splitArray2(int[] nums, int start, int end, int m, long[] sum) {
        if (start >= end) return 0;

        if (m == 1) return sum[end] - sum[start];

        long min = sum[end];
        for (int i = start + 1; i <= end - m + 1; i++) {
            long first = sum[i] - sum[start];
            if (first >= min) break;

            min = Math.min(min, Math.max(first, splitArray2(nums, i, end, m - 1, sum)));
        }
        return min;
    }

    // Dynamic Programming
    // beats N/A(331 ms for 28 tests)
    // time complexity: O(M * N ^ 2), space complexity: O(N)
    public int splitArray3(int[] nums, int m) {
        int n = nums.length;
        // dp[k, i] is the solution for splitting nums[i..n] into k parts.
        int[] dp = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            dp[i] = dp[i + 1] + nums[i];
        }
        for (int k = 2; k <= m; k++) {
            int maxLeftCut = n + 1 - k;
            for (int i = 0; i < maxLeftCut; i++) {
                dp[i] = Integer.MAX_VALUE;
                int leftSum = 0;
                for (int j = i; j < maxLeftCut; j++) {
                    leftSum += nums[j];
                    if (leftSum > dp[i]) break;

                    dp[i] = Math.min(dp[i], Math.max(leftSum, dp[j + 1]));
                }
                if (k == m) break;
            }
        }
        return dp[0];
    }

    void test(int[] nums, int m, int expected) {
        assertEquals(expected, splitArray(nums, m));
        assertEquals(expected, splitArray2(nums, m));
        assertEquals(expected, splitArray3(nums, m));
    }

    @Test
    public void test1() {
        test(new int[] {1, 2147483647}, 2, 2147483647);
        test(new int[] {1, 2, 3, 4, 5}, 2, 9);
        test(new int[] {7, 2, 5, 10, 8}, 2, 18);
        test(new int[] {7, 2, 5, 10, 8, 9, 2, 7, 31, 2, 9, 8, 10}, 7, 31);
        test(new int[] {7, 3, 2, 5, 9, 10, 8, 6, 9, 2, 7, 31, 2, 5, 10, 21,
                        9, 8, 13, 4, 7, 21, 32, 4, 6, 3, 9, 10}, 10, 32);
        // test(new int[] {5334,6299,4199,9663,8945,3566,9509,3124,6026,6250,7475,5420,9201,9501,38,5897,4411,6638,9845,161,9563,8854,3731,5564,5331,4294,3275,1972,1521,2377,3701,6462,6778,187,9778,758,550,7510,6225,8691,3666,4622,9722,8011,7247,575,5431,4777,4032,8682,5888,8047,3562,9462,6501,7855,505,4675,6973,493,1374,3227,1244,7364,2298,3244,8627,5102,6375,8653,1820,3857,7195,7830,4461,7821,5037,2918,4279,2791,1500,9858,6915,5156,970,1471,5296,1688,578,7266,4182,1430,4985,5730,7941,3880,607,8776,1348,2974,1094,6733,5177,4975,5421,8190,8255,9112,8651,2797,335,8677,3754,893,1818,8479,5875,1695,8295,7993,7037,8546,7906,4102,7279,1407,2462,4425,2148,2925,3903,5447,5893,3534,3663,8307,8679,8474,1202,3474,2961,1149,7451,4279,7875,5692,6186,8109,7763,7798,2250,2969,7974,9781,7741,4914,5446,1861,8914,2544,5683,8952,6745,4870,1848,7887,6448,7873,128,3281,794,1965,7036,8094,1211,9450,6981,4244,2418,8610,8681,2402,2904,7712,3252,5029,3004,5526,6965,8866,2764,600,631,9075,2631,3411,2737,2328,652,494,6556,9391,4517,8934,8892,4561,9331,1386,4636,9627,5435,9272,110,413,9706,5470,5008,1706,7045,9648,7505,6968,7509,3120,7869,6776,6434,7994,5441,288,492,1617,3274,7019,5575,6664,6056,7069,1996,9581,3103,9266,2554,7471,4251,4320,4749,649,2617,3018,4332,415,2243,1924,69,5902,3602,2925,6542,345,4657,9034,8977,6799,8397,1187,3678,4921,6518,851,6941,6920,259,4503,2637,7438,3893,5042,8552,6661,5043,9555,9095,4123,142,1446,8047,6234,1199,8848,5656,1910,3430,2843,8043,9156,7838,2332,9634,2410,2958,3431,4270,1420,4227,7712,6648,1607,1575,3741,1493,7770,3018,5398,6215,8601,6244,7551,2587,2254,3607,1147,5184,9173,8680,8610,1597,1763,7914,3441,7006,1318,7044,7267,8206,9684,4814,9748,4497,2239},
        //  9, 194890);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SplitArray");
    }
}
