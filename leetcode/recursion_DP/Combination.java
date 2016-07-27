import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/combinations/
//
// Return all possible combinations of k numbers out of 1 ... n.
public class Combination {
    // beats 90.59%(4 ms)
    public List<List<Integer> > combine(int n, int k) {
        List<List<Integer> > res = new ArrayList<>();
        combine(n, k, res, new ArrayList<>());
        return res;
    }

    private void combine(int n, int k, List<List<Integer> > res,
                         List<Integer> cur) {
        int count = cur.size();
        if (count == k) {
            res.add(new ArrayList<>(cur));
            return;
        }

        int next = (count > 0) ? cur.get(count - 1) + 1 : 1;
        for (int i = next; i <= n; i++) {
            cur.add(i);
            combine(n, k, res, cur);
            cur.remove(count);
        }
    }

    // https://discuss.leetcode.com/topic/12537/a-short-recursive-java-solution-based-on-c-n-k-c-n-1-k-1-c-n-1-k
    // beats 74.37%(14 ms)
    public List<List<Integer> > combine2(int n, int k) {
        if (k == n || k == 0) {
            List<Integer> row = new LinkedList<>();
            for (int i = 1; i <= k; i++) {
                row.add(i);
            }
            return new LinkedList<>(Arrays.asList(row));
        }

        List<List<Integer> > res = combine2(n - 1, k - 1);
        // res.forEach(e -> e.add(n)); // slow(98 ms)
        for (List<Integer> list : res) {
            list.add(n);
        }
        res.addAll(combine2(n - 1, k));
        return res;
    }

    // iterative by bit manipulation
    // https://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation
    // beats 86.24%(6 ms)
    public List<List<Integer> > combine3(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        for (int v = (1 << k) - 1; v < (1 << n); ) {
            res.add(codeToList(v, n));
            int t = v | (v - 1); // set least significant 0 bit to 1
            // set to 1 the most significant bit to change,
            // set to 0 the least significant ones, and add the necessary 1 bits.
            v = (t + 1) | (((~t & -~t) - 1) >> (trailingZero(v) + 1));
        }
        return res;
    }

    private List<Integer> codeToList(int code, int n) {
        List<Integer> res = new ArrayList<>();
        for (int i = 1, mask = 1; i <= n; i++, mask <<= 1) {
            if ((code & mask) != 0) {
                res.add(i);
            }
        }
        return res;
    }

    private int trailingZero(int n) {
        int count = 0;
        for (int x = (n ^ (n - 1)) >> 1; x > 0; count++, x >>= 1) {}
        return count;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, Integer, List<List<Integer>>> combine,
              String name, int n, int k, Integer[][] expected) {
        List<List<Integer> > res = combine.apply(n, k);
        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
        sort(resArray);
        assertArrayEquals(expected, resArray);
    }

    Integer[][] sort(Integer[][] lists) {
        Arrays.sort(lists, (a, b) -> {
            // Arrays.sort(a);
            // Arrays.sort(b);
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++);
            if (i == len) return a.length - b.length;
            return a[i] - b[i];
        });
        return lists;
    }

    void test(int n, int k, Integer[][] expected) {
        Combination c = new Combination();
        test(c::combine, "combine", n, k, expected);
        test(c::combine2, "combine2", n, k, expected);
        test(c::combine3, "combine3", n, k, expected);
    }

    @Test
    public void test1() {
        test(4, 3, new Integer[][] {
            {1, 2, 3}, {1, 2, 4}, {1, 3, 4}, {2, 3, 4}
        });
        test(5, 3, new Integer[][] {
            {1, 2, 3}, {1, 2, 4}, {1, 2, 5}, {1, 3, 4}, {1, 3, 5},
            {1, 4, 5}, {2, 3, 4}, {2, 3, 5}, {2, 4, 5}, {3, 4, 5}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Combination");
    }
}
