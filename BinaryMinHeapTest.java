package assign10;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

/**
 * Tests the constructors and public operations of BinaryMinHeap.
 *
 * @author Ava Murphy and Yujia Zhao
 * @version November 18, 2026
 */
public class BinaryMinHeapTest {

	/** Tests the default constructor and the state of an empty heap. */
	@Test
	public void testDefaultConstructor() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>();
		assertTrue(heap.isEmpty());
		assertEquals(0, heap.size());
		assertArrayEquals(new Object[0], heap.toArray());
	}

	/** Tests the constructor that accepts a comparator. */
	@Test
	public void testComparatorConstructor() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>(Comparator.reverseOrder());
		assertTrue(heap.isEmpty());
		heap.add(2);
		heap.add(5);
		heap.add(1);
		assertEquals(Integer.valueOf(5), heap.peek());
		assertEquals(Integer.valueOf(5), heap.extract());
		assertEquals(Integer.valueOf(2), heap.extract());
		assertEquals(Integer.valueOf(1), heap.extract());
		assertTrue(heap.isEmpty());
	}

	/** Tests building a heap from an unordered list using natural ordering. */
	@Test
	public void testListConstructor() {
		List<Integer> values = List.of(36, 17, 3, 100, 19, 2, 70);
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>(values);
		assertEquals(values.size(), heap.size());
		assertHeapOrder(heap.toArray(), Comparator.<Integer>naturalOrder());
		assertExtractionOrder(heap, List.of(2, 3, 17, 19, 36, 70, 100));
	}

	/** Tests building a heap from a list with a comparator. */
	@Test
	public void testListAndComparatorConstructor() {
		Comparator<Integer> comparator = Comparator.reverseOrder();
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>(List.of(4, -1, 3, 4, 8), comparator);
		assertEquals(5, heap.size());
		assertHeapOrder(heap.toArray(), comparator);
		assertExtractionOrder(heap, List.of(8, 4, 4, 3, -1));
	}

	/** Tests building heaps from empty lists. */
	@Test
	public void testEmptyListConstructors() {
		BinaryMinHeap<Integer> natural = new BinaryMinHeap<Integer>(List.of());
		BinaryMinHeap<Integer> custom = new BinaryMinHeap<Integer>(List.of(), Comparator.reverseOrder());
		assertTrue(natural.isEmpty());
		assertTrue(custom.isEmpty());
		natural.add(1);
		custom.add(2);
		assertEquals(Integer.valueOf(1), natural.extract());
		assertEquals(Integer.valueOf(2), custom.extract());
	}

	/** Tests a heap containing one element and adding again after extraction. */
	@Test
	public void testSingleElement() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>(List.of(9));
		assertEquals(1, heap.size());
		assertEquals(Integer.valueOf(9), heap.peek());
		assertEquals(Integer.valueOf(9), heap.extract());
		assertTrue(heap.isEmpty());
		heap.add(7);
		assertEquals(Integer.valueOf(7), heap.extract());
		assertTrue(heap.isEmpty());
	}

	/** Tests that repeated calls to peek do not remove an element. */
	@Test
	public void testPeek() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>(List.of(3, 1, 2));
		for(int i = 0; i < 4; i++) {
			assertEquals(Integer.valueOf(1), heap.peek());
			assertEquals(3, heap.size());
		}
	}

	/** Tests that peek and extract throw the required exception on an empty heap. */
	@Test
	public void testEmptyExceptions() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>();
		assertThrows(NoSuchElementException.class, () -> heap.peek());
		assertThrows(NoSuchElementException.class, () -> heap.extract());
		heap.add(1);
		heap.extract();
		assertThrows(NoSuchElementException.class, () -> heap.peek());
		assertThrows(NoSuchElementException.class, () -> heap.extract());
	}

	/** Tests insertion in descending order, which requires percolating up. */
	@Test
	public void testDescendingInsertion() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>();
		for(int i = 100; i >= 1; i--) {
			heap.add(i);
			assertEquals(Integer.valueOf(i), heap.peek());
			assertEquals(101 - i, heap.size());
			assertHeapOrder(heap.toArray(), Comparator.<Integer>naturalOrder());
		}
		for(int i = 1; i <= 100; i++) {
			assertEquals(Integer.valueOf(i), heap.extract());
			assertHeapOrder(heap.toArray(), Comparator.<Integer>naturalOrder());
		}
		assertTrue(heap.isEmpty());
	}

	/** Tests insertion in ascending order followed by extraction. */
	@Test
	public void testAscendingInsertion() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>();
		for(int i = 0; i < 50; i++)
			heap.add(i);
		for(int i = 0; i < 50; i++)
			assertEquals(Integer.valueOf(i), heap.extract());
		assertTrue(heap.isEmpty());
	}

	/** Tests duplicate values, zero and negative values. */
	@Test
	public void testDuplicates() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>();
		for(int value : new int[] {5, -2, 0, 5, -2, 5, 1})
			heap.add(value);
		assertExtractionOrder(heap, List.of(-2, -2, 0, 1, 5, 5, 5));
	}

	/** Tests interleaved insertion and extraction. */
	@Test
	public void testMixedOperations() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>();
		heap.add(8);
		heap.add(4);
		heap.add(6);
		assertEquals(Integer.valueOf(4), heap.extract());
		heap.add(1);
		heap.add(9);
		assertEquals(4, heap.size());
		assertEquals(Integer.valueOf(1), heap.extract());
		heap.add(2);
		assertExtractionOrder(heap, List.of(2, 6, 8, 9));
	}

	/** Tests clear on an empty and nonempty heap, and reuse after clearing. */
	@Test
	public void testClear() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>(Comparator.reverseOrder());
		heap.clear();
		for(int i = 0; i < 50; i++)
			heap.add(i);
		heap.clear();
		assertEquals(0, heap.size());
		assertTrue(heap.isEmpty());
		assertArrayEquals(new Object[0], heap.toArray());
		assertThrows(NoSuchElementException.class, () -> heap.peek());
		assertThrows(NoSuchElementException.class, () -> heap.extract());
		heap.add(2);
		heap.add(5);
		assertExtractionOrder(heap, List.of(5, 2));
	}

	/** Tests the order, length and contents of the array returned by toArray. */
	@Test
	public void testToArray() {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>();
		for(int value : new int[] {36, 17, 3, 100, 19, 2, 70})
			heap.add(value);
		Object[] original = heap.toArray();
		assertEquals(7, original.length);
		assertEquals(heap.peek(), original[0]);
		assertHeapOrder(original, Comparator.<Integer>naturalOrder());
		Object[] sorted = original.clone();
		Arrays.sort(sorted);
		assertArrayEquals(new Object[] {2, 3, 17, 19, 36, 70, 100}, sorted);
		assertEquals(7, heap.size());
		assertEquals(Integer.valueOf(2), heap.extract());
		Object[] values = heap.toArray();
		assertEquals(6, values.length);
		assertEquals(heap.peek(), values[0]);
		assertHeapOrder(values, Comparator.<Integer>naturalOrder());
	}

	/** Tests natural ordering with strings. */
	@Test
	public void testStrings() {
		BinaryMinHeap<String> heap = new BinaryMinHeap<String>(List.of("pear", "apple", "banana", "apple"));
		assertExtractionOrder(heap, List.of("apple", "apple", "banana", "pear"));
	}

	/** Tests subtype lists and a comparator defined for a supertype. */
	@Test
	public void testGenericComparator() {
		List<Integer> values = List.of(3, 1, 8, 2);
		Comparator<Number> comparator = (a, b) -> Double.compare(a.doubleValue(), b.doubleValue());
		BinaryMinHeap<Number> heap = new BinaryMinHeap<Number>(values, comparator);
		heap.add(1.5);
		assertExtractionOrder(heap, List.<Number>of(1, 1.5, 2, 3, 8));
	}

	/** Tests distinct objects that have equal priority under a comparator. */
	@Test
	public void testEqualPriorities() {
		Comparator<String> comparator = Comparator.comparingInt(String::length);
		BinaryMinHeap<String> heap = new BinaryMinHeap<String>(List.of("aaa", "b", "c", "dd"), comparator);
		List<String> extracted = new ArrayList<String>();
		while(!heap.isEmpty())
			extracted.add(heap.extract());
		assertEquals(1, extracted.get(0).length());
		assertEquals(1, extracted.get(1).length());
		assertEquals("dd", extracted.get(2));
		assertEquals("aaa", extracted.get(3));
		Collections.sort(extracted);
		assertEquals(List.of("aaa", "b", "c", "dd"), extracted);
	}

	/** Tests that list construction uses a linear number of comparisons on descending input. */
	@Test
	public void testBuildHeapComparisons() {
		int count = 4096;
		List<Integer> values = new ArrayList<Integer>();
		for(int i = count; i > 0; i--)
			values.add(i);
		AtomicInteger comparisons = new AtomicInteger();
		Comparator<Integer> comparator = (a, b) -> {
			comparisons.incrementAndGet();
			return Integer.compare(a, b);
		};
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>(values, comparator);
		assertTrue(comparisons.get() <= 4 * count);
		assertEquals(count, heap.size());
		assertEquals(Integer.valueOf(1), heap.peek());
		assertHeapOrder(heap.toArray(), Comparator.<Integer>naturalOrder());
	}

	/** Tests resizing and extraction order on a larger reproducible data set. */
	@Test
	public void testLargeHeap() {
		Random random = new Random(2420);
		List<Integer> values = new ArrayList<Integer>();
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<Integer>();
		for(int i = 0; i < 3000; i++) {
			int value = random.nextInt(5000) - 2500;
			values.add(value);
			heap.add(value);
		}
		BinaryMinHeap<Integer> built = new BinaryMinHeap<Integer>(values);
		Collections.sort(values);
		assertHeapOrder(heap.toArray(), Comparator.<Integer>naturalOrder());
		assertHeapOrder(built.toArray(), Comparator.<Integer>naturalOrder());
		assertExtractionOrder(heap, values);
		assertExtractionOrder(built, values);
	}

	/**
	 * Checks that the root is at index zero and every parent has priority over its children.
	 *
	 * @param <E> the type of heap elements
	 * @param values - heap contents in array order
	 * @param comparator - ordering used by the heap
	 */
	@SuppressWarnings("unchecked")
	private <E> void assertHeapOrder(Object[] values, Comparator<E> comparator) {
		for(int i = 1; i < values.length; i++) {
			int parent = (i - 1) / 2;
			assertTrue(comparator.compare((E) values[parent], (E) values[i]) <= 0);
		}
	}

	/**
	 * Checks the extraction order and the size after each removal.
	 *
	 * @param <E> the type of heap elements
	 * @param heap - heap to be emptied
	 * @param expected - expected extraction order
	 */
	private <E> void assertExtractionOrder(BinaryMinHeap<E> heap, List<E> expected) {
		assertEquals(expected.size(), heap.size());
		for(int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i), heap.extract());
			assertEquals(expected.size() - i - 1, heap.size());
		}
		assertTrue(heap.isEmpty());
		assertEquals(0, heap.toArray().length);
	}
}
