import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a set of candidate numbers and a target T, find all unique combinations
// where the candidate numbers sums to T.
// The same repeated number may be chosen unlimited number of times.
// All numbers (including target) will be positive integers.
public class CombinationSum {
    private int[] preprocess(int[] candidates) {
        if (candidates == null || candidates.length == 0) return null;

        SortedSet<Integer> candidateSet = new TreeSet<>();
        for (int i : candidates) {
            candidateSet.add(i);
        }
        candidates = candidateSet.stream().mapToInt(i->i).toArray();
        if (candidates[0] <= 0) return null;
        return candidates;
    }

    // beats 0.68%
    public List<List<Integer> > combinationSum(int[] candidates, int target) {
        candidates = preprocess(candidates);
        if (candidates == null) return null;

        List<List<Integer> > res = new ArrayList<>();
        combinationSum(candidates, target, 0, Collections.emptyList(), res);
        return res;
    }

    private void combinationSum(int[] candidates, int target,
                                int index, List<Integer> preselected,
                                List<List<Integer> > res) {
        if (target == 0) {
            res.add(preselected);
            return;
        }

        if (index >= candidates.length) return;

        int first = candidates[index];
        for (int times = target / first; times >= 0; times--) {
            List<Integer> combination = new ArrayList<>(preselected);
            for (int i = times; i > 0; i--) {
                combination.add(first);
            }
            combinationSum(candidates, target - times * first, index + 1,
                           combination, res);
        }
    }

    // beats 0.97%
    public List<List<Integer> > combinationSum2(int[] candidates, int target) {
        candidates = preprocess(candidates);
        if (candidates == null) return null;

        List<List<Integer> > res = new ArrayList<>();
        combinationSum2(candidates, target, 0, Collections.emptyList(), res);
        return res;
    }

    private void combinationSum2(int[] candidates, int target,
                                int index, List<Integer> preselected,
                                List<List<Integer> > res) {
        int first = candidates[index];
        for (int times = target / first; times >= 0; times--) {
            List<Integer> combination = new ArrayList<>(preselected);
            for (int i = times; i > 0; i--) {
                combination.add(first);
            }
            int newTarget = target - times * first;
            if (newTarget == 0) {
                res.add(combination);
            } else if (newTarget >= first && index + 1 < candidates.length) {
                combinationSum2(candidates, newTarget, index + 1,
                                combination, res);
            }
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], Integer, List<List<Integer>>> sum,
              int[][] expected, int target, int ... candidates) {
        List<List<Integer> > res = sum.apply(candidates, target);
        // System.out.println(res);
        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
        assertArrayEquals(expected, resArray);
    }

    void test(int[][] expected, int target, int ... candidates) {
        CombinationSum sum = new CombinationSum();
        test(sum::combinationSum, expected, target, candidates);
        test(sum::combinationSum2, expected, target, candidates);
    }

    @Test
    public void test1() {
        test(new int[][] { {2, 2, 3}, {7} }, 7, 7, 2, 2, 6, 3);
        test(new int[][] { {2, 2, 2, 2, 2}, {2, 2, 3, 3}, {2, 2, 6}, {3, 7} },
             10, 7, 2, 2, 6, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CombinationSum");
    }
}
