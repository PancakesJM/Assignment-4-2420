package assign08;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

/**
 * Tests the ArraySortedSet implementation against the SortedSet contract.
 *
 * @author Yujia Zhao
 * @version November 4, 2026
 */
public class ArraySortedSetTest {

    /** Tests an empty set, including its collection and search methods. */
    @Test
    public void emptySet() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.contains(5));
        assertTrue(set.containsAll(Collections.<Integer>emptyList()));
        assertEquals(Collections.emptyList(), set.toArrayList());
    }

    /** Tests the first insertion into an initially empty array. */
    @Test
    public void firstInsertion() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        assertTrue(set.add(10));
        assertFalse(set.isEmpty());
        assertEquals(1, set.size());
        assertTrue(set.contains(10));
        assertEquals(Arrays.asList(10), set.toArrayList());
    }

    /** Tests ordering, insertion positions, and rejection of duplicates. */
    @Test
    public void orderingAndDuplicates() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        for (int value : new int[] {8, 3, 12, 1, 6, 10}) {
            assertTrue(set.add(value));
        }
        assertFalse(set.add(8));
        assertFalse(set.add(1));
        assertEquals(6, set.size());
        assertEquals(Arrays.asList(1, 3, 6, 8, 10, 12), set.toArrayList());
        assertEquals(Integer.valueOf(1), set.min());
        assertEquals(Integer.valueOf(12), set.max());
    }

    /** Tests successful and unsuccessful binary searches at boundaries. */
    @Test
    public void containsPresentAndAbsent() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        set.addAll(Arrays.asList(10, 20, 30));
        assertTrue(set.contains(10));
        assertTrue(set.contains(20));
        assertTrue(set.contains(30));
        assertFalse(set.contains(5));
        assertFalse(set.contains(15));
        assertFalse(set.contains(35));
    }

    /** Tests whether addAll reports a change according to the interface. */
    @Test
    public void addAllReturnValue() {
        ArraySortedSet<String> set = new ArraySortedSet<>();
        assertFalse(set.addAll(Collections.<String>emptyList()));
        assertTrue(set.addAll(Arrays.asList("c", "a", "b", "a")));
        assertFalse(set.addAll(Arrays.asList("a", "b")));
        assertTrue(set.addAll(Arrays.asList("b", "d")));
        assertEquals(Arrays.asList("a", "b", "c", "d"), set.toArrayList());
    }

    /** Tests containsAll with empty, matching, and nonmatching collections. */
    @Test
    public void containsAllCases() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        set.addAll(Arrays.asList(2, 4, 6));
        assertTrue(set.containsAll(Collections.<Integer>emptyList()));
        assertTrue(set.containsAll(Arrays.asList(2, 6, 2)));
        assertFalse(set.containsAll(Arrays.asList(2, 5)));
        assertFalse(set.containsAll(Arrays.asList(2, 9)));
    }

    /** Tests both exceptions required for minimum and maximum on an empty set. */
    @Test
    public void emptyMinAndMaxThrow() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        assertThrows(NoSuchElementException.class, () -> set.min());
        assertThrows(NoSuchElementException.class, () -> set.max());
    }

    /** Tests array expansion past initial capacity and sorted output. */
    @Test
    public void expansionAndSortedOutput() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        for (int value = 100; value >= 0; value--) {
            assertTrue(set.add(value));
        }
        assertEquals(101, set.size());
        for (int value = 0; value <= 100; value++) {
            assertTrue(set.contains(value));
        }
        ArrayList<Integer> expected = new ArrayList<>();
        for (int value = 0; value <= 100; value++) {
            expected.add(value);
        }
        assertEquals(expected, set.toArrayList());
    }

    /** Tests clearing a populated set and inserting again afterwards. */
    @Test
    public void clearAndReuse() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        set.addAll(Arrays.asList(1, 2, 3));
        set.clear();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.contains(1));
        assertEquals(Collections.emptyList(), set.toArrayList());
        assertTrue(set.add(7));
        assertEquals(Arrays.asList(7), set.toArrayList());
    }

    /** Tests natural ordering of strings, not only integer ordering. */
    @Test
    public void stringOrdering() {
        ArraySortedSet<String> set = new ArraySortedSet<>();
        set.addAll(Arrays.asList("pear", "apple", "banana"));
        assertEquals(Arrays.asList("apple", "banana", "pear"), set.toArrayList());
        assertEquals("apple", set.min());
        assertEquals("pear", set.max());
    }

    /** Compares deterministic random insertions with Java's TreeSet. */
    @Test
    public void randomizedComparison() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        TreeSet<Integer> expected = new TreeSet<>();
        Random random = new Random(2420);
        for (int i = 0; i < 500; i++) {
            int value = random.nextInt(300) - 150;
            assertEquals(expected.add(value), set.add(value),
                    "Incorrect add result for value " + value + " at step " + i);
            assertEquals(expected.size(), set.size());
            assertEquals(new ArrayList<>(expected), set.toArrayList());
        }
    }

    /** Tests the optional removal operations' default unsupported behavior. */
    @Test
    public void optionalRemovalIsUnsupported() {
        ArraySortedSet<Integer> set = new ArraySortedSet<>();
        assertThrows(UnsupportedOperationException.class, () -> set.remove(1));
        assertThrows(UnsupportedOperationException.class,
                () -> set.removeAll(Arrays.asList(1, 2)));
    }
}
