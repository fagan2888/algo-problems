import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/2984486/dashboard#s=p2
// Round 1A 2014: Problem C - Proper Shuffle
//
// Sometimes we just want to pick one permutation at random uniformly. Here's
// the pseudocode for one of the possible algorithms(good algorithm):
// for k in 0 .. N-1:
//   a[k] = k
// for k in 0 .. N-1:
//   p = randint(k .. N-1)
//   swap(a[k], a[p])
// Here's the pseudocode for a bad algorithm:
// for k in 0 .. N-1:
//   a[k] = k
// for k in 0 .. N-1:
//   p = randint(0 .. N-1)
//   swap(a[k], a[p])
// In each test case, you will be given a permutation that was generated in the
// following way: first, we choose either the good or the bad algorithm
// described above, each with probability 50%. Then, we generate a permutation
// using the chosen algorithm. Can you guess which algorithm was chosen just by
// looking at the permutation?(Your solution will be considered correct if your
// answers for at least G = 109 cases are correct)
// Input
// The first line of the input gives the number of test cases, T (which will
// always be 120). Each test case contains two lines: the first line contains
// the single integer N (which will always be 1000), and the next line contains
// N space-separated integers - the permutation that was generated using one of
// the two algorithms.
// Output
// For each test case, output one line containing "Case #x: y", where x is the
// test case number (starting from 1) and y is either "GOOD" or "BAD". You
// should output "GOOD" if you guess that the permutation was generated by the
// first algorithm described in the problem statement, and "BAD" if you guess
// that the permutation was generated by the second algorithm.
// Limits
// T = 120
// G = 109
// N = 1000
// Each number in the permutation will be between 0 and N-1 (inclusive), and
// each number from 0 to N-1 will appear exactly once in the permutation.
public class ProperShuffle {
    private static Map<Integer, Integer> thresholdMap = new HashMap<>();

    public static boolean isGood(int[] a) {
        int n = a.length;
        int threshold = thresholdMap.getOrDefault(n, 0);
        if (threshold == 0) {
            threshold = getThreshold(n);
            thresholdMap.put(n, threshold);
        }
        return score(a) >= threshold;
    }

    private static int getThreshold(int n) {
        int[] sample = new int[n];
        int goodScores = 0;
        final int test = 10000;
        for (int i = 0; i < test; i++) {
            goodShuffle(sample);
            goodScores += score(sample);
        }

        int badScores = 0;
        for (int i = 0; i < test; i++) {
            badShuffle(sample);
            badScores += score(sample);
        }
        return (goodScores + badScores) / test / 2;
    }

    private static int score(int[] a) {
        int res = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] <= i) {
                res++;
            }
        }
        return res;
    }

    private static void goodShuffle(int[] a) {
        int n = a.length;
        for (int k = 0; k < n; k++) {
            a[k] = k;
        }
        for (int k = 0; k < n; k++) {
            int p = ThreadLocalRandom.current().nextInt(k, n);
            swap(a, k, p);
        }
    }

    private static void badShuffle(int[] a) {
        int n = a.length;
        for (int k = 0; k < n; k++) {
            a[k] = k;
        }
        for (int k = 0; k < n; k++) {
            int p = ThreadLocalRandom.current().nextInt(0, n);
            swap(a, k, p);
        }
    }

    private static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    // TODO: Bayes Classifier as in
    // https://code.google.com/codejam/contest/2984486/dashboard#s=a&a=2

    // TODO: entropy method

    void test(int[] permutation, boolean expected) {
        assertEquals(expected, isGood(permutation));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3, 4, 0}, false);
        test(new int[] {2, 3, 1, 0, 4}, true);
        test(new int[] {4, 2, 3, 0, 1}, true);
        test(new int[] {4, 1, 3, 2, 0}, true);
        // test(new int[] {0, 1, 2}, false);
        test(new int[] {2, 0, 1}, true);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz =
                new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n", clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }
        try {
            in = new Scanner(new File(args[0]));
            if (args.length > 1) {
                out = new PrintStream(args[1]);
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int N = in.nextInt();
        int[] permutation = new int[N];
        for (int i = 0; i < N; i++) {
            permutation[i] = in.nextInt();
        }
        boolean res = isGood(permutation);
        out.println(res ? "GOOD" : "BAD");
    }
}
