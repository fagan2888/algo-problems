// Given a n x n matrix where each of the rows and columns are sorted in

import java.util.*;
import java.util.PriorityQueue;
import java.util.function.Function;
import org.junit.Test;
import static org.junit.Assert.*;

// ascending order, find the kth smallest element in the matrix.
// Note:
// You may assume k is always valid, 1 ≤ k ≤ n ^ 2.
public class KthSmallestInMatrix {
    // time complexity: O(N ^ 2)
    // beats N/A(38 ms)
    public int kthSmallest(int[][] matrix, int k) {
        if (k == 1) return matrix[0][0];

        int n = matrix.length;
        int m = (int)Math.sqrt(k);
        if (m * m < k) {
            m++;
        }
        int pivot = matrix[m - 1][m - 1];
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return b - a;
            }
        });
        for (int i = 0; i < m - 1; i++) { // upper-right corner less than pivot
            for (int j = m; j < n; j++) {
                if (matrix[i][j] < pivot) {
                    pq.offer(matrix[i][j]);
                } else break;
            }
        }
        for (int i = m; i < n; i++) { // lower-left corner less than pivot
            for (int j = 0; j < m - 1; j++) {
                if (matrix[i][j] < pivot) {
                    pq.offer(matrix[i][j]);
                } else break;
            }
        }

        int needRemove = pq.size() + m * m - 1 - k;
        if (needRemove < 0) return matrix[m - 1][m - 1];

        int i = m - 1;
        for (; i > 0; i--) {
           for (int j = i - 1; j >= 0; j--) { // add two sides
               pq.offer(matrix[j][i]);
               pq.offer(matrix[i][j]);
           }

           int corner = matrix[i - 1][i - 1];
           while (needRemove > 0 && !pq.isEmpty() && pq.peek() >= corner) {
               pq.poll();
               needRemove--;
           }
           if (needRemove == 0) break;

           pq.offer(corner);
       }
       while (needRemove-- > 0) {
           pq.poll();
       }
       return i == 0 ? pq.peek() : Math.max(pq.peek(), matrix[i - 1][i - 1]);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[][], Integer, Integer> smallest,
              int [][] matrix, int[] ... expected) {
        for (int[] x : expected) {
            assertEquals(x[1], (int)smallest.apply(matrix, x[0]));
        }
    }

    void test(int[][] matrix, int[] ... expected) {
        KthSmallestInMatrix k = new KthSmallestInMatrix();
        test(k::kthSmallest, matrix, expected);
    }

    @Test
    public void test1() {
        test(new int[][] {{-5}}, new int[] {1, -5});
        test(new int[][] {{1, 2}, {1, 3}}, new int[] {4, 3});
        test(new int[][] {{1, 3, 4}, {1, 8, 8}, {4, 12, 17}}, new int[] {5, 4});
        test(new int[][] {{1,  5,  9, 10}, {10, 11, 13, 14}, {12, 13, 15, 16},
                          {13, 14, 15, 18}},
             new int[] {1, 1}, new int[] {6, 11}, new int[] {7, 12},
             new int[] {10, 13}, new int[] {11, 14});
        test(new int[][] {{1,  5,  9}, {10, 11, 13}, {12, 13, 15}},
             new int[] {8, 13}, new int[] {3, 9}, new int[] {4, 10});
        test(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15},
                          {16, 17, 18, 19, 20}, {21, 22, 23, 24, 25}},
             new int[] {5, 5}, new int[] {6, 6}, new int[] {7, 7},
             new int[] {8, 8}, new int[] {9, 9}, new int[] {10, 10},
             new int[] {11, 11}, new int[] {12, 12}, new int[] {13, 13},
             new int[] {20, 20}, new int[] {1, 1}, new int[] {25, 25});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KthSmallestInMatrix");
    }
}