import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1627: https://leetcode.com/problems/graph-connectivity-with-threshold/
//
// We have n cities labeled from 1 to n. Two different cities with labels x and y are directly
// connected by a bidirectional road if and only if x and y share a common divisor strictly greater
// than some threshold. Given the two integers, n and threshold, and an array of queries, you must
// determine for each queries[i] = [ai, bi] if cities ai and bi are connected.
// Return an array answer, where answer.length == queries.length and answer[i] is true if for the
// ith query, there is a path between ai and bi, or answer[i] is false if there is no path.
// Constraints:
// 2 <= n <= 10^4
// 0 <= threshold <= n
// 1 <= queries.length <= 10^5
// queries[i].length == 2
// 1 <= ai, bi <= cities
// ai != bi
public class ConnectivityWithThreshold {
    // Union Find
    // time complexity: O(N*log(N) + Q), space complexity: O(N)
    // 6 ms(96.14%), 72.7 MB(6.12%) for 66 tests
    public List<Boolean> areConnected(int n, int threshold, int[][] queries) {
        int[] id = new int[n + 1];
        Arrays.fill(id, -1);
        for (int i = threshold + 1; i <= n; i++) {
            for (int j = 2 * i; j <= n; j += i) {
                union(id, i, j);
            }
        }
        List<Boolean> res = new ArrayList<>();
        for (int[] query : queries) {
            res.add(root(id, query[0]) == root(id, query[1]));
        }
        return res;
    }

    private boolean union(int[] id, int x, int y) {
        int px = root(id, x);
        int py = root(id, y);
        if (px == py) { return false; }

        if (id[py] < id[px]) {
            int tmp = px;
            px = py;
            py = tmp;
        }
        id[px] += id[py];
        id[py] = px;
        return true;
    }

    private int root(int[] id, int x) {
        for (; id[x] >= 0; x = id[x]) {}
        return x;
    }

    public List<Boolean> areConnected2(int n, int threshold, int[][] queries) {
        int[] id = new int[n + 1];
        Arrays.fill(id, -1);
        for (int i = threshold + 1; i <= n; i++) {
            for (int k = 2; ; k++) {
                int j = k * i;
                if (j > n) { break; }

                union(id, i, j);
            }
        }
        List<Boolean> res = new ArrayList<>();
        for (int[] query : queries) {
            res.add(root(id, query[0]) == root(id, query[1]));
        }
        return res;
    }

    private void test(int n, int threshold, int[][] queries, Boolean[] expected) {
        List<Boolean> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, areConnected(n, threshold, queries));
        assertEquals(expectedList, areConnected2(n, threshold, queries));
    }

    @Test public void test() {
        test(6, 2, new int[][] {{1, 4}, {2, 5}, {3, 6}}, new Boolean[] {false, false, true});
        test(6, 0, new int[][] {{4, 5}, {3, 4}, {3, 2}, {2, 6}, {1, 3}},
             new Boolean[] {true, true, true, true, true});
        test(5, 1, new int[][] {{4, 5}, {4, 5}, {3, 2}, {2, 3}, {3, 4}},
             new Boolean[] {false, false, false, false, false});
        test(26, 3, new int[][] {{12, 6}, {17, 3}}, new Boolean[] {true, false});
        test(26, 3,
             new int[][] {{8, 3}, {14, 9}, {22, 23}, {22, 25}, {12, 6}, {17, 3}, {25, 17}, {26, 14},
                          {4, 12}, {16, 12}, {16, 9}, {26, 3}, {20, 22}, {17, 18}, {3, 16},
                          {20, 17}, {24, 9}, {12, 26}, {1, 4}, {23, 24}, {12, 8}, {6, 22}, {18, 20},
                          {22, 13}, {11, 3}, {8, 18}, {18, 15}, {16, 11}, {4, 15}, {25, 3}, {1, 12},
                          {2, 15}, {2, 1}, {5, 2}, {15, 25}, {18, 10}, {16, 1}, {16, 6}, {7, 22},
                          {26, 13}, {24, 6}, {23, 4}, {9, 25}, {24, 26}, {7, 17}, {6, 25}, {2, 9},
                          {13, 9}, {1, 20}, {5, 26}, {15, 14}, {5, 16}, {7, 9}, {15, 19}, {16, 2},
                          {1, 13}, {3, 7}, {9, 26}, {3, 13}, {8, 23}, {7, 1}, {10, 19}, {23, 9},
                          {16, 13}, {18, 25}, {14, 2}, {16, 17}, {21, 7}, {19, 4}, {6, 9}, {25, 12},
                          {9, 5}, {1, 18}, {26, 7}, {12, 21}, {12, 20}, {24, 3}, {24, 19}, {5, 14},
                          {12, 3}, {20, 2}, {7, 23}, {24, 21}, {4, 5}, {8, 20}, {19, 22}, {21, 5},
                          {26, 6}, {16, 19}, {23, 25}, {18, 26}, {10, 25}, {3, 5}, {14, 19},
                          {21, 22}, {22, 1}, {4, 25}, {15, 11}, {15, 24}, {10, 15}, {23, 17},
                          {25, 11}, {23, 21}, {8, 2}, {5, 22}, {19, 23}, {3, 20}, {21, 2}, {5, 25},
                          {4, 7}, {8, 21}, {1, 6}, {18, 6}, {17, 13}, {26, 8}, {23, 6}, {6, 11},
                          {6, 10}, {7, 8}, {18, 5}, {20, 15}, {11, 14}, {24, 1}, {16, 24}, {2, 22},
                          {3, 22}, {25, 21}, {7, 2}, {1, 11}, {19, 12}, {19, 1}, {22, 8}, {10, 3},
                          {16, 10}, {6, 15}, {26, 4}, {9, 8}, {5, 20}, {4, 13}, {23, 3}, {18, 21},
                          {10, 23}, {14, 16}, {24, 25}, {13, 11}, {4, 2}, {26, 2}, {3, 1}, {18, 14},
                          {14, 6}, {6, 2}, {14, 4}, {24, 11}, {1, 5}, {4, 21}, {22, 17}, {17, 14},
                          {10, 11}, {11, 26}, {6, 4}, {19, 26}, {16, 20}, {18, 4}, {8, 6}, {26, 25},
                          {10, 17}, {20, 13}, {9, 20}, {16, 18}, {8, 5}, {5, 6}, {21, 26}, {17, 21},
                          {26, 16}, {5, 7}, {14, 7}, {3, 9}, {23, 18}, {9, 10}, {17, 26}, {15, 16},
                          {13, 21}, {1, 21}, {17, 8}, {21, 9}, {10, 8}, {24, 4}, {19, 20}, {20, 7},
                          {1, 8}, {1, 9}, {24, 20}, {18, 7}, {4, 9}, {2, 17}, {22, 18}, {25, 19},
                          {7, 6}, {23, 5}, {14, 1}, {13, 24}, {5, 13}, {14, 21}, {23, 26}, {6, 13},
                          {10, 5}, {17, 1}, {24, 22}, {3, 15}, {11, 5}, {22, 14}, {17, 24}, {7, 25},
                          {24, 7}, {18, 11}, {26, 1}, {11, 7}, {25, 20}, {24, 8}, {19, 17},
                          {12, 23}, {17, 12}, {12, 13}, {23, 13}, {14, 24}, {16, 22}, {25, 13},
                          {18, 24}, {20, 23}, {25, 16}, {24, 5}, {10, 14}, {12, 14}, {3, 14},
                          {11, 9}, {8, 13}, {21, 16}, {12, 9}, {26, 15}, {4, 11}, {5, 17}, {23, 14},
                          {20, 10}, {11, 23}, {2, 11}, {10, 7}, {4, 8}, {24, 10}, {25, 8}, {8, 16},
                          {7, 13}, {3, 18}, {2, 23}, {10, 26}, {19, 11}, {12, 24}, {2, 3}, {15, 9},
                          {11, 17}, {11, 8}, {21, 19}, {14, 8}, {11, 20}, {21, 10}, {12, 7},
                          {12, 5}, {21, 15}, {11, 12}, {2, 10}, {3, 6}, {17, 15}, {2, 24}, {16, 23},
                          {13, 15}, {19, 18}, {13, 10}, {4, 3}, {21, 20}, {22, 9}, {10, 12},
                          {18, 2}, {2, 12}, {6, 21}, {13, 19}, {15, 1}, {15, 5}, {3, 19}, {22, 10},
                          {17, 4}, {1, 10}, {7, 15}, {6, 20}, {14, 20}, {11, 21}, {18, 13}, {19, 5},
                          {20, 26}},
             new Boolean[] {false, false, false, false, true, false, false, false, true, true, true,
                            false, false, false, false, false, true, false, false, false, true,
                            false, true, false, false, true, true, false, true, false, false, false,
                            false, false, true, true, false, true, false, true, true, false, true,
                            false, false, true, false, false, false, false, false, true, false,
                            false, false, false, false, false, false, false, false, false, false,
                            false, true, false, false, true, false, true, true, true, false, false,
                            false, true, false, false, false, false, false, false, false, true,
                            true, false, false, false, false, false, false, true, false, false,
                            false, false, true, false, true, true, false, false, false, false,
                            false, false, false, false, true, false, false, false, true, false,
                            false, false, false, true, false, true, true, false, false, true, false,
                            false, false, false, false, false, false, false, false, true, true,
                            false, true, true, false, false, false, false, false, true, false,
                            false, false, false, false, false, false, false, false, false, false,
                            false, false, false, false, true, false, true, true, true, false, false,
                            false, true, true, true, true, false, false, false, false, true, false,
                            false, true, false, true, false, false, false, false, true, true, false,
                            false, false, false, true, false, true, false, false, false, false,
                            false, false, false, false, true, false, false, true, false, false,
                            false, false, false, false, false, false, false, false, false, true,
                            true, false, false, false, false, false, false, false, false, true,
                            false, true, true, false, false, false, false, false, false, true,
                            false, false, false, false, true, false, false, false, true, true, true,
                            true, false, false, false, false, false, true, false, true, false,
                            false, false, false, false, false, false, true, false, false, false,
                            false, false, false, false, false, false, false, false, false, false,
                            true, false, false, false, false, false, true, false, false, false,
                            false, false, true, false, false, false, false, false});

    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
