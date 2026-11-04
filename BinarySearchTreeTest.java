package assign08;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

/**
 * Tests the BinarySearchTree implementation using JUnit 5.
 *
 * @author Yujia Zhao
 * @version November 4, 2026
 */
public class BinarySearchTreeTest {

    /** Checks the behavior of an empty set. */
    @Test
    public void emptySet() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertFalse(tree.contains(1));
        assertFalse(tree.remove(1));
        assertFalse(tree.addAll(Collections.<Integer>emptyList()));
        assertTrue(tree.containsAll(Collections.<Integer>emptyList()));
        assertFalse(tree.removeAll(Collections.<Integer>emptyList()));
        assertEquals(Collections.emptyList(), tree.toArrayList());
    }

    /** Checks that minimum on an empty set throws the expected exception. */
    @Test
    public void emptyMinThrows() {
        assertThrows(NoSuchElementException.class, () -> new BinarySearchTree<Integer>().min());
    }

    /** Checks that maximum on an empty set throws the expected exception. */
    @Test
    public void emptyMaxThrows() {
        assertThrows(NoSuchElementException.class, () -> new BinarySearchTree<Integer>().max());
    }

    /** Checks insertion, duplicates, ordering, and lookup. */
    @Test
    public void insertAndLookup() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        for (int value : new int[] {8, 3, 10, 1, 6, 14, 4, 7, 13}) {
            assertTrue(tree.add(value));
        }
        assertFalse(tree.add(8));
        assertEquals(9, tree.size());
        assertEquals(Integer.valueOf(1), tree.min());
        assertEquals(Integer.valueOf(14), tree.max());
        assertTrue(tree.contains(6));
        assertFalse(tree.contains(5));
        assertEquals(Arrays.asList(1, 3, 4, 6, 7, 8, 10, 13, 14), tree.toArrayList());
    }

    /** Checks all deletion cases, including deletion of the root. */
    @Test
    public void deleteLeafOneChildAndTwoChildren() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.addAll(Arrays.asList(8, 3, 10, 1, 6, 14, 4, 7, 13));
        assertTrue(tree.remove(1)); // leaf
        assertTrue(tree.remove(14)); // one child
        assertTrue(tree.remove(3)); // two children
        assertTrue(tree.remove(8)); // root with two children
        assertFalse(tree.remove(999));
        assertEquals(Arrays.asList(4, 6, 7, 10, 13), tree.toArrayList());
        assertEquals(5, tree.size());
    }

    /** Checks batch operations, clear, and set reuse. */
    @Test
    public void batchesAndClear() {
        BinarySearchTree<String> tree = new BinarySearchTree<>();
        assertTrue(tree.addAll(Arrays.asList("c", "a", "b", "a")));
        assertFalse(tree.addAll(Arrays.asList("a", "b")));
        assertTrue(tree.containsAll(Arrays.asList("a", "c")));
        assertFalse(tree.containsAll(Arrays.asList("a", "z")));
        assertTrue(tree.removeAll(Arrays.asList("a", "z", "b")));
        assertFalse(tree.removeAll(Arrays.asList("a", "z")));
        assertEquals(Arrays.asList("c"), tree.toArrayList());
        tree.clear();
        assertTrue(tree.isEmpty());
        assertTrue(tree.add("new"));
        assertEquals(Arrays.asList("new"), tree.toArrayList());
    }

    /** Checks deletion of a single root and of the root with one child. */
    @Test
    public void rootReplacement() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(2);
        assertTrue(tree.remove(2));
        assertTrue(tree.isEmpty());
        tree.add(2);
        tree.add(1);
        assertTrue(tree.remove(2));
        assertEquals(Arrays.asList(1), tree.toArrayList());
        tree.clear();
        tree.add(2);
        tree.add(3);
        assertTrue(tree.remove(2));
        assertEquals(Arrays.asList(3), tree.toArrayList());
    }

    /** Compares randomized operations with Java's reference TreeSet. */
    @Test
    public void randomizedComparison() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        TreeSet<Integer> reference = new TreeSet<>();
        Random random = new Random(2420);
        for (int i = 0; i < 3000; i++) {
            int value = random.nextInt(200) - 100;
            if (random.nextBoolean()) {
                assertEquals(reference.add(value), tree.add(value));
            } else {
                assertEquals(reference.remove(value), tree.remove(value));
            }
            assertEquals(reference.size(), tree.size());
            assertEquals(new ArrayList<>(reference), tree.toArrayList());
            assertEquals(reference.contains(value), tree.contains(value));
            if (!reference.isEmpty()) {
                assertEquals(reference.first(), tree.min());
                assertEquals(reference.last(), tree.max());
            }
        }
    }
}
