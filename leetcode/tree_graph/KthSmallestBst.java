import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/kth-smallest-element-in-a-bst/
//
// Given a binary search tree, write a function kthSmallest to find the kth
// smallest element in it.

// Follow up:
// What if the BST is modified (insert/delete operations) often and you need to
// find the kth smallest frequently?
public class KthSmallestBst {
    // beats 54.10%(1 ms)
    public int kthSmallest(TreeNode root, int k) {
        int count = count(root.left);
        if (count + 1 == k) return root.val;

        if (count + 1 > k) return kthSmallest(root.left, k);

        return kthSmallest(root.right, k - count - 1);
    }

    private int count(TreeNode node) {
        if (node == null) return 0;

        return 1 + count(node.left) + count(node.right);
    }

    // beats 7.61%(6 ms)
    public int kthSmallest2(TreeNode root, int k) {
        return kthSmallest2(root, k, new HashMap<>());
    }

    private int kthSmallest2(TreeNode root, int k, Map<TreeNode, Integer> cache) {
        int count = count(root.left, cache);
        if (count + 1 == k) return root.val;

        if (count + 1 > k) return kthSmallest2(root.left, k, cache);

        return kthSmallest2(root.right, k - count - 1, cache);
    }

    private int count(TreeNode node, Map<TreeNode, Integer> cache) {
        if (node == null) return 0;

        if (cache.containsKey(node)) return cache.get(node);

        int res = 1 + count(node.left, cache) + count(node.right, cache);
        cache.put(node, res);
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<TreeNode, Integer, Integer> kthSmallest, String s,
              int k, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, (int)kthSmallest.apply(root, k));
    }

    void test(String s, int k, int expected) {
        KthSmallestBst smallest = new KthSmallestBst();
        test(smallest::kthSmallest, s, k, expected);
        test(smallest::kthSmallest2, s, k, expected);
    }

    @Test
    public void test1() {
        test("6,4,8,3,5,7,9,2,#,#,#,#,#,#,#,1", 1, 1);
        // test("1", 1, 1);
        // test("1,#,6,5", 2, 5);
        // test("1,#,6,5", 2, 5);
        // test("6,4,8,3,5,7,9,1", 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KthSmallestBst");
    }
}
