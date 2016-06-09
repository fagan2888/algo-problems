import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given preorder and inorder traversal of a tree, construct the binary tree.
// You may assume that duplicates do not exist in the tree.
public class TreeFromPreorderInorder {
    // beats 72.17%
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        int len = preorder.length;
        if (len == 0) return null;

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            map.put(preorder[i], i);
        }

        int rootVal = preorder[0];
        int rootIndex = len - 1;
        for (; rootIndex >= 0; rootIndex--) {
            if (inorder[rootIndex] == rootVal) break;
        }

        TreeNode root = new TreeNode(rootVal);
        root.left = buildTree(map, inorder, 0, rootIndex);
        root.right = buildTree(map, inorder, rootIndex + 1, len);
        return root;
    }

    private TreeNode buildTree(Map<Integer, Integer> map, int[] nums,
                               int start, int end) {
        if (start >= end) return null;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(new TreeNode(nums[start]));
        for (int i = start + 1; i < end; i++) {
            TreeNode cur = stack.peek();
            TreeNode node = new TreeNode(nums[i]);
            if (after(nums[i], cur, map)) {
                cur.right = node;
            } else {
                while (!stack.isEmpty()) {
                    cur = stack.pop();
                    if (after(nums[i], cur, map)) {
                        stack.push(cur);
                        TreeNode tmp = cur.right;
                        cur.right = node;
                        cur = tmp;
                        break;
                    }
                }
                node.left = cur;
            }
            stack.push(node);
        }
        return stack.elementAt(0);
    }

    private boolean after(int n, TreeNode node, Map<Integer, Integer> map) {
        return map.get(n) > map.get(node.val);
    }

    // beats 73.81%
    public TreeNode buildTree2(int[] preorder, int[] inorder) {
        int len = preorder.length;
        if (len == 0) return null;

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            map.put(inorder[i], i);
        }

        Stack<TreeNode> stack = new Stack<>();
        TreeNode root = new TreeNode(preorder[0]);
        stack.push(root);
        for (int i = 1; i < len; i++) {
            TreeNode cur = stack.peek();
            int val = preorder[i];
            TreeNode node = new TreeNode(val);
            if (after(val, cur, map)) {
                while (!stack.isEmpty()) {
                    cur = stack.pop();
                    if (!stack.isEmpty() && !after(val, stack.peek(), map)) {
                        break;
                    }
                }
                cur.right = node;
            } else {
                cur.left = node;
            }
            stack.push(node);
        }
        return root;
    }

    // beats 25.32%
    public TreeNode buildTree3(int[] preorder, int[] inorder) {
        return buildTree3(preorder, new int[1], inorder, 0, inorder.length);
    }

    private TreeNode buildTree3(int[] preorder, int[] from,
                                int[] inorder, int start, int end) {
        if (start >= end) return null;


        TreeNode root = new TreeNode(preorder[from[0]++]);
        int rootIndex = start;
        for (; rootIndex < end; rootIndex++) {
            if (inorder[rootIndex] == root.val) break;
        }

        root.left = buildTree3(preorder, from, inorder, start, rootIndex);
        root.right = buildTree3(preorder, from, inorder, rootIndex + 1, end);
        return root;
    }

    // beats 74.94%
    public TreeNode buildTree4(int[] preorder, int[] inorder) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return buildTree4(map, preorder, new int[1], inorder, 0, inorder.length);
    }

    private TreeNode buildTree4(Map<Integer, Integer> map, int[] preorder,
                                int[] from, int[] inorder, int start, int end) {
        if (start >= end) return null;

        TreeNode root = new TreeNode(preorder[from[0]++]);
        int rootIndex = map.get(root.val);
        root.left = buildTree4(map, preorder, from, inorder, start, rootIndex);
        root.right = buildTree4(map, preorder, from, inorder, rootIndex + 1, end);
        return root;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], int[], TreeNode> build, int[] preorder,
              int[] inorder, String expected) {
        assertEquals(expected, build.apply(preorder, inorder).toString());
    }

    void test(int[] preorder, int[] inorder, String expected) {
        TreeFromPreorderInorder t = new TreeFromPreorderInorder();
        test(t::buildTree, preorder, inorder, expected);
        test(t::buildTree2, preorder, inorder, expected);
        test(t::buildTree3, preorder, inorder, expected);
        test(t::buildTree4, preorder, inorder, expected);
    }

    @Test
    public void test1() {
        test(new int[] {4, 2, 1, 3}, new int[] {1, 2, 3, 4}, "{4,2,#,1,3}");
        test(new int[] {4, 1, 2, 3}, new int[] {1, 2, 3, 4}, "{4,1,#,#,2,#,3}");
        test(new int[] {3, 1, 2, 4}, new int[] {1, 2, 3, 4}, "{3,1,4,#,2}");
        test(new int[] {1, 2, 4, 3}, new int[] {1, 2, 3, 4}, "{1,#,2,#,4,3}");
        test(new int[] {1, 2, 3}, new int[] {1, 3, 2}, "{1,#,2,3}");
        test(new int[] {3, 9, 20, 15, 7}, new int[] {9, 3, 15, 20, 7},
             "{3,9,20,#,#,15,7}");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreeFromPreorderInorder");
    }
}