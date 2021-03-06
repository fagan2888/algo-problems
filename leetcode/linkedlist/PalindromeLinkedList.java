import java.util.*;

import common.ListNode;

import org.junit.Test;
import static org.junit.Assert.*;

// LC234: https://leetcode.com/problems/palindrome-linked-list/
//
// Given a singly linked list, determine if it is a palindrome.
// Could you do it in O(n) time and O(1) space?
public class PalindromeLinkedList {
    // time complexity: O(N), space complexity: O(N)
    // beats 7.96%(7 ms)
    public boolean isPalindrome(ListNode head) {
        List<Integer> list = new ArrayList<>();
        for (ListNode n = head; n != null; n = n.next) {
            list.add(n.val);
        }
        for (int i = 0, j = list.size() - 1; i < j; i++, j--) {
            if (!list.get(i).equals(list.get(j))) return false;
        }
        return true;
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // beats 23.11%(3 ms)
    public boolean isPalindrome2(ListNode head) {
        if (head == null || head.next == null) return true;

        ListNode slow = head;
        for (ListNode fast = head.next; fast != null && fast.next != null;
             slow = slow.next, fast = fast.next.next) {}
        ListNode right = slow.next;
        right = reverse(right);

        boolean isPalindrome = true;
        for (ListNode n = head, m = right; m != null; n = n.next, m = m.next) {
            if (n.val != m.val) {
                isPalindrome = false;
                break;
            }
        }
        // recover(if omitted, beat rate is 34.27%, time is 2 ms)
        reverse(right);
        return isPalindrome;
    }

    private ListNode reverse(ListNode head) {
        ListNode prev = null;
        for (ListNode cur = head, next; cur != null; cur = next) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
        }
        return prev;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 3.42%(9 ms)
    public boolean isPalindrome3(ListNode head) {
        if (head == null) return true;

        ListNode slow = head;
        for (ListNode fast = head.next; fast != null && fast.next != null;
             slow = slow.next, fast = fast.next.next) {}
        Stack<Integer> stack = new Stack<>();
        for (ListNode n = slow.next; n != null; n = n.next) {
            stack.push(n.val);
        }
        for (ListNode n = head; !stack.isEmpty(); n = n.next) {
            if (!stack.pop().equals(n.val)) return false;
        }
        return true;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 29.22%(3 ms for 26 tests)
    public boolean isPalindrome3_2(ListNode head) {
        ListNode mid = head;
        Stack<Integer> stack = new Stack<>();
        for (ListNode p = head; p != null; p = p.next) {
            stack.push(mid.val);
            p = p.next;
            mid = mid.next;
            if (p == null) {
                stack.pop();
                break;
            }
        }
        for (ListNode p = mid; p != null; p = p.next) {
            if (stack.pop() != p.val) return false;
        }
        return true;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // 12.51%(6 ms)
    public boolean isPalindrome4(ListNode head) {
        return isPalindrome(new ListNode[] {head}, head);
    }

    private boolean isPalindrome(ListNode[] first, ListNode last) {
        if (last == null) return true;

        if (!isPalindrome(first, last.next)) return false;

        boolean res = (first[0].val == last.val);
        first[0] = first[0].next;
        return res;
    }

    void test(int[] nums, boolean expected) {
        ListNode list = ListNode.of(nums);
        assertEquals(expected, isPalindrome(list));
        assertEquals(expected, isPalindrome2(list));
        assertEquals(expected, isPalindrome3(list));
        assertEquals(expected, isPalindrome3_2(list));
        assertEquals(expected, isPalindrome4(list));
    }

    @Test
    public void test1() {
        test(new int[] {}, true);
        test(new int[] {1}, true);
        test(new int[] {1, 1}, true);
        test(new int[] {1, 2, 1}, true);
        test(new int[] {1, 2, 2, 1}, true);
        test(new int[] {1, 2, 3}, false);
        test(new int[] {1, 2, 3, 4}, false);
        test(new int[] {1, 2, 3, 4, 1}, false);
        test(new int[] {1, 2, 3, 2, 1}, true);
        test(new int[] {1, 2, 3, 3, 2, 1}, true);
        test(new int[] {-16557, -8725, -29125, 28873, -21702, 15483, -28441, -17845, -4317, -10914,
                        -10914, -4317, -17845, -28441, 15483, -21702, 28873, -29125, -8725, -16557},
             true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
