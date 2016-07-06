import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/the-skyline-problem/
//
// A city's skyline is the outer contour of the silhouette formed by all the
// buildings in that city when viewed from a distance. Now suppose you are given
// the locations and height of all the buildings as shown on a cityscape photo,
// write a program to output the skyline formed by these buildings collectively.
// The geometric information of each building is represented by a triplet of
// integers [Li, Ri, Hi], where Li and Ri are the x coordinates of the left and
// right edge of the ith building, respectively, and Hi is its height.
// The output is a list of "key points" (red dots in Figure B) in the format of
// [ [x1,y1], [x2, y2], [x3, y3], ... ] that uniquely defines a skyline. A key
// point is the left endpoint of a horizontal line segment.
//
// Notes:
// The number of buildings in any input list are in the range [0, 10000].
// The input list is already sorted in ascending order by the left x position Li.
// The output list must be sorted by the x position.
public class Skyline {
    // time complexity: O(N * log(N))(?), space complexity: O(N)
    // beats 95.26%(13 ms)
    public List<int[]> getSkyline(int[][] buildings) {
        int n = buildings.length;
        if (n == 0) return Collections.emptyList();

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        Deque<int[]> skylines = new LinkedList<>();
        skylines.offer(buildings[0]);
        for (int i = 1; i < n || !pq.isEmpty(); ) {
            int[] last = skylines.peekLast();
            int[] next;
            int[] head = pq.peek();
            if (head != null && (i >= n || head[0] < buildings[i][0])) {
                next = pq.poll();
            } else {
                next = buildings[i++];
            }

            if (last[1] <= next[0]) { // separate part
                if (last[1] < next[0]) {
                    skylines.offer(new int[] {last[1], next[0], 0});
                    skylines.offer(next);
                } else {
                    if (next[2] == last[2]) {
                        last[1] = Math.max(last[1], next[1]);
                    } else {
                        skylines.offer(next);
                    }
                }
                continue;
            }

            // overlap
            if (next[2] < last[2]) {
                if (next[1] > last[1]) { // not fully covered
                    pq.offer(new int[] {last[1], next[1], next[2]});
                }
            } else if (next[2] == last[2]) {
                if (next[1] > last[1]) { // extend
                    last[1] = next[1];
                }
            } else {
                if (next[1] < last[1]) { // strech
                    pq.offer(new int[] {next[1], last[1], last[2]});
                }
                last[1] = next[0];
                if (last[0] == last[1]) {
                    skylines.pollLast();
                }
                skylines.offer(next);
            }

        }
        List<int[]> res = new ArrayList<>(skylines.size());
        for (int[] skyline : skylines) {
            res.add(new int[] {skyline[0], skyline[2]});
        }
        res.add(new int[] {skylines.peekLast()[1], 0});
        return res;
    }

    // https://discuss.leetcode.com/topic/22482/short-java-solution
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 26.43%(290 ms)
    public List<int[]> getSkyline2(int[][] buildings) {
        List<int[]> heights = new ArrayList<>();
        for(int[] building : buildings) {
            heights.add(new int[] {building[0], -building[2]});
            heights.add(new int[] {building[1], building[2]});
        }
        Collections.sort(heights, (a, b) -> (a[0] != b[0])
                         ? a[0] - b[0] : a[1] - b[1]);
        Queue<Integer> queue = new PriorityQueue<>((a, b) -> b - a);
        queue.offer(0);
        int lastH = 0;
        List<int[]> res = new ArrayList<>();
        for (int[] h : heights) {
            if (h[1] < 0) { // left vertex
                queue.offer(-h[1]);
            } else { // right vertex
                queue.remove(h[1]); // coming here means the building is done
            }
            int curH = queue.peek();
            if (lastH != curH) {
                res.add(new int[] {h[0], curH});
                lastH = curH;
            }
        }
        return res;
    }

    // beats 69.085(119 ms)
    // TreeMap remove opeartion is more efficient than PriorityQueue
    public List<int[]> getSkyline3(int[][] buildings) {
        List<int[]> heights = new ArrayList<>();
        for(int[] building : buildings) {
            heights.add(new int[] {building[0], -building[2]});
            heights.add(new int[] {building[1], building[2]});
        }
        Collections.sort(heights, (a, b) -> (a[0] != b[0])
                         ? a[0] - b[0] : a[1] - b[1]);
        SortedMap<Integer, Integer> map = new TreeMap<>(Collections.reverseOrder());
        map.put(0, 1);
        int lastH = 0;
        List<int[]> res = new ArrayList<>();
        for (int[] h : heights) {
            if (h[1] < 0) { // left vertex
                Integer cnt = map.get(-h[1]);
                map.put(-h[1], (cnt == null) ? 1 : cnt + 1);
            } else { // right vertex
                Integer cnt = map.get(h[1]);
                if (cnt == 1) {
                    map.remove(h[1]);
                } else {
                    map.put(h[1], cnt - 1);
                }
            }
            int curH = map.firstKey();
            if (lastH != curH) {
                res.add(new int[] {h[0], curH});
                lastH = curH;
            }
        }
        return res;
    }

    // divide and conquer
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 97.13%(10 ms)
    public List<int[]> getSkyline4(int[][] buildings) {
        if (buildings.length == 0) return Collections.emptyList();

        return getSkyline(0, buildings.length - 1, buildings);
    }

    private List<int[]> getSkyline(int start, int end, int[][] buildings) {
        if (start == end) {
            return Arrays.asList(
                new int[] {buildings[start][0], buildings[start][2]},
                new int[] {buildings[start][1], 0});
        }

        int mid = start + (end - start) / 2;
        return merge(getSkyline(start, mid, buildings),
                     getSkyline(mid + 1, end, buildings));
    }

    private List<int[]> merge(List<int[]> left, List<int[]> right) {
        List<int[]> res = new ArrayList<>();
        int i = 0;
        int j = 0;
        int leftSize = left.size();
        int rightSize = right.size();
        for (int leftH = 0, rightH = 0, maxH = 0; i < leftSize && j < rightSize; ) {
            int[] leftPt = left.get(i);
            int[] rightPt = right.get(j);
            if (leftPt[0] < rightPt[0]) {
                leftH = leftPt[1];
                i++;
            } else if (leftPt[0] > rightPt[0]) {
                rightH = rightPt[1];
                j++;
            } else {
                leftH = leftPt[1];
                rightH = rightPt[1];
                i++;
                j++;
            }
            if (maxH != Math.max(leftH, rightH)) {
                maxH = Math.max(leftH, rightH);
                res.add(new int[] {Math.min(leftPt[0], rightPt[0]), maxH});
            }
        }
        while (i < leftSize) {
            res.add(new int[] {left.get(i)[0], left.get(i)[1]});
            i++;
        }
        while (j < rightSize) {
            res.add(new int[] {right.get(j)[0], right.get(j)[1]});
            j++;
        }
        return res;
    }

    // TODO: https://briangordon.github.io/2014/08/the-skyline-problem.html

    void test(Function<int[][], List<int[]> >getSkyline,
              int[][] buildings, int[][] expected) {
        int[][] res = getSkyline.apply(buildings).toArray(new int[0][0]);
        assertArrayEquals(expected, res);
    }

    void test(int[][] buildings, int[][] expected) {
        Skyline s = new Skyline();
        test(s::getSkyline, buildings, expected);
        test(s::getSkyline2, buildings, expected);
        test(s::getSkyline3, buildings, expected);
        test(s::getSkyline4, buildings, expected);
    }

    @Test
    public void test1() {
        test(new int[][] {{0, 2, 3}}, new int[][] {{0, 3}, {2, 0}});
        test(new int[][] {{3, 10, 20}, {3, 9, 19}, {3, 8, 18}, {3, 7, 17},
                          {3, 6, 16}, {3, 5, 15}, {3, 4, 14}},
             new int[][] {{3, 20}, {10, 0}});
        test(new int[][] {{0, 2, 3}, {2, 5, 3}},
             new int[][] {{0, 3}, {5, 0}});
        test(new int[][] {{2, 9, 10}, {3, 7, 15}, {5, 12, 12},
                          {15, 20, 10}, {19, 24, 8}},
             new int[][] {{2, 10}, {3, 15}, {7, 12}, {12, 0},
                          {15, 10}, {20, 8}, {24, 0}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Skyline");
    }
}
