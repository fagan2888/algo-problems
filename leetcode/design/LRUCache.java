import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/lru-cache/
//
// Design and implement a data structure for Least Recently Used (LRU) cache. It
// should support the following operations: get and set.
// get(key) - Get the value (will always be positive) of the key if the key
// exists in the cache, otherwise return -1.
// set(key, value) - Set or insert the value if the key is not already present.
// When the cache reached its capacity, it should invalidate the least recently
// used item before inserting a new item.

// Time Limited Exceeded
class LRUCache0 {
    static class TimedVal {
        int serialNum;
        int val;

        TimedVal(int val, int serialNum) {
            this.val = val;
            this.serialNum = serialNum;
        }
    }

    private int capacity;
    private int count;
    private int serialNum;
    Map<Integer, TimedVal> map = new HashMap<>();
    PriorityQueue<Integer> keyQueue = new PriorityQueue<Integer>(
        (a, b) -> map.get(a).serialNum - map.get(b).serialNum);

    public LRUCache0(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();

        this.capacity = capacity;
    }

    // time complexity: O(Log(N))
    public int get(int key) {
        if (!map.containsKey(key)) return -1;

        return adjust(key).val;
    }

    // time complexity: O(Log(N))
    public void set(int key, int value) {
        if (map.containsKey(key)) {
            TimedVal timedVal = adjust(key);
            timedVal.val = value;
            map.put(key, timedVal);
            return;
        }

        if (++count > capacity) {
            count--;
            map.remove(keyQueue.poll());
        }
        map.put(key, new TimedVal(value, ++serialNum));
        keyQueue.add(key);
    }

    private TimedVal adjust(int key) {
        TimedVal timedVal = map.get(key);
        keyQueue.remove(key);
        timedVal.serialNum = ++serialNum;
        keyQueue.add(key);
        return timedVal;
    }
}

// beats 70.33%
public class LRUCache {
    static class Node {
        int key;
        int val;
        Node prev;
        Node next;
        Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    private int capacity;
    Map<Integer, Node> map = new HashMap<>();
    Node head = new Node(-1, 0);
    Node tail = new Node(-1, 0);

    public LRUCache(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();

        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    // time complexity: O(1)
    public int get(int key) {
        if (!map.containsKey(key)) return -1;

        Node node = map.get(key);
        remove(node);
        putFront(node);
        return node.val;
    }

    // time complexity: O(1)
    public void set(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            remove(node);
            putFront(node);
            node.val = value;
            return;
        }

        Node node = new Node(key, value);
        if (map.size() >= capacity) {
            map.remove(tail.prev.key);
            remove(tail.prev);
        }
        putFront(node);
        map.put(key, node);
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void putFront(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    static void test1() {
        LRUCache cache = new LRUCache(3);
        cache.set(1, 1);
        cache.set(2, 4);
        cache.set(3, 9);
        assertEquals(4, cache.get(2));
        cache.set(4, 16);
        assertEquals(16, cache.get(4));
        assertEquals(-1, cache.get(1));
    }

    static void test2() {
        LRUCache cache = new LRUCache(2);
        cache.set(2, 1);
        // System.out.println(cache.map);
        cache.set(2, 2);
        // System.out.println(cache.map);
        assertEquals(2, cache.get(2));
        cache.set(1, 1);
        cache.set(4, 1);
        assertEquals(-1, cache.get(2));
    }

    public static void main(String[] args) {
        // org.junit.runner.JUnitCore.main("LRUCache");
        test1();
        test2();
    }
}