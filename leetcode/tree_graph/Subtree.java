import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC572: https://leetcode.com/problems/subtree-of-another-tree/
//
// Given two non-empty binary trees s and t, check whether tree t has exactly the
// same structure and node values with a subtree of s. A subtree of s is a tree
// consists of a node in s and all of this node's descendants.
public class Subtree {
    // Recursion
    // time complexity: O(M * N), space complexity: O(N)
    // beats 22.80%(37 ms for 176 tests)
    public boolean isSubtree(TreeNode s, TreeNode t) {
        if (s == null) return t == null;

        return equals(s, t) || equals(s.left, t) || equals(s.right, t) || isSubtree(s.left, t) || isSubtree(s.right, t);
    }

    private boolean equals(TreeNode s, TreeNode t) {
        if (s == null) return t == null;

        return (t != null) && (s.val == t.val) && equals(s.left, t.left) && equals(s.right, t.right);
    }

    // Recursion
    // time complexity: O(M * N), space complexity: O(N)
    // beats 37.89%(32 ms for 176 tests)
    public boolean isSubtree2(TreeNode s, TreeNode t) {
        return (s == null) ? (t == null) : traverse(s, t);
    }

    private boolean traverse(TreeNode s, TreeNode t) {
        return (s != null) && (equals(s, t) || traverse(s.left, t) || traverse(s.right, t));
    }

    // Recursion + Set
    // time complexity: O(M ^ 2 + N ^ 2), space complexity: O(N ^ 2)
    // beats 7.20%(54 ms for 176 tests)
    public boolean isSubtree3(TreeNode s, TreeNode t) {
        String tree = preorder(t, true);
        Set<String> trees = new HashSet<>();
        findAllSubTrees(s, true, trees);
        return trees.contains(tree);
    }

    private String findAllSubTrees(TreeNode s, boolean left, Set<String> trees) {
        if (s == null) return left ? "l" : "r";

        String val = s.val + " " + findAllSubTrees(s.left, true, trees) + " " +
                     findAllSubTrees(s.right, false, trees);
        trees.add(val);
        return val;
    }

    private String preorder(TreeNode t, boolean left) {
        if (t == null) return left ? "l" : "r";

        return t.val + " " + preorder(t.left, true) + " " + preorder(t.right, false);
    }

    // Recursion
    // time complexity: O(M ^ 2 + N ^ 2), space complexity: O(N)
    // beats 49.05%(30 ms for 176 tests)
    public boolean isSubtree4(TreeNode s, TreeNode t) {
        if (s == null) return t == null;

        return traverse(s, true, preorder(t, true)) == null;
    }

    private String traverse(TreeNode s, boolean left, String tree) {
        if (s == null) return left ? "l" : "r";

        String leftStr = traverse(s.left, true, tree);
        if (leftStr == null) return null;

        String rightStr = traverse(s.right, false, tree);
        if (rightStr == null) return null;

        String val = s.val + " " + leftStr + " " + rightStr;
        return val.equals(tree) ? null : val;
    }

    // Time Limit Exceeded
    public boolean isSubtree5(TreeNode s, TreeNode t) {
        if (s == null) return t == null;
        return traverse(s, preorder(t, true));
    }

    private boolean traverse(TreeNode s, String tree) {
        if (s == null) return false;

        return preorder(s, true).equals(tree) || traverse(s.left, tree) || traverse(s.right, tree);
    }

    // Recursion
    // time complexity: O(M ^ 2 + N ^ 2), space complexity: O(max(M, N))
    // beats 14.23%(43 ms for 176 tests)
    public boolean isSubtree6(TreeNode s, TreeNode t) {
        return preorder6(s, true).indexOf(preorder6(t, true)) >= 0;
    }

    private String preorder6(TreeNode t, boolean left) {
        if (t == null) return left ? "l" : "r";
        return "#" + t.val + " " + preorder6(t.left, true) + " " + preorder6(t.right, false);
    }

    // Recursion
    // time complexity: O(M * N), space complexity: O(M + N)
    // beats 37.69%(32 ms for 176 tests)
    public boolean isSubtree7(TreeNode s, TreeNode t) {
        return serialize(s).contains(serialize(t));
    }

    private String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serialize(root, sb);
        return sb.toString();
    }

    private void serialize(TreeNode cur, StringBuilder res) {
        if (cur == null) {
            res.append(",#");
        } else {
            res.append("," + cur.val);
            serialize(cur.left, res);
            serialize(cur.right, res);
        }
    }

    // Stack
    // time complexity: O(M * N), space complexity: O(M + N)
    // beats 30.27%(34 ms for 176 tests)
    public boolean isSubtree8(TreeNode s, TreeNode t) {
        return serialize8(s).contains(serialize8(t));
    }

    private String serialize8(TreeNode s) {
        StringBuilder sb = new StringBuilder();
        Stack<TreeNode> stack = new Stack<>();
        for (stack.push(s); !stack.isEmpty(); ) {
            TreeNode top = stack.pop();
            sb.append(",").append(top.val);
            if (top.right == null) {
                sb.append("#");
            } else {
                stack.push(top.right);
            }
            if (top.left == null) {
                sb.append("#");
            } else {
                stack.push(top.left);
            }
        }
        return sb.toString();
    }

    void test(String s, String t, boolean expected) {
        assertEquals(expected, isSubtree(TreeNode.of(s), TreeNode.of(t)));
        assertEquals(expected, isSubtree2(TreeNode.of(s), TreeNode.of(t)));
        assertEquals(expected, isSubtree3(TreeNode.of(s), TreeNode.of(t)));
        assertEquals(expected, isSubtree4(TreeNode.of(s), TreeNode.of(t)));
        assertEquals(expected, isSubtree5(TreeNode.of(s), TreeNode.of(t)));
        assertEquals(expected, isSubtree6(TreeNode.of(s), TreeNode.of(t)));
        assertEquals(expected, isSubtree7(TreeNode.of(s), TreeNode.of(t)));
        assertEquals(expected, isSubtree8(TreeNode.of(s), TreeNode.of(t)));
    }

    @Test
    public void test() {
        test("1,2,3", "2,3", false);
        test("3,4,5,1,2", "4,1,2", true);
        test("3,4,5,1,2,#,#,0", "4,1,2", false);
        test("29,28,30,27,29,29,31,26,26,28,28,28,28,30,32,25,25,25,25,27,29", "29", true);
        test("-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,2",
             "-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,#,-1111111111,2", true);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}