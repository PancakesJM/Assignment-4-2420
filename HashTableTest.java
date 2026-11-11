package assign09;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Tests the public operations of the Assignment 9 hash table and the
 * three student hash functions.
 *
 * @author Yujia Zhao and Ava Murphy
 * @version November 11, 2026
 */
public class HashTableTest {

	/**
	 * Constructs the test class.
	 */
	public HashTableTest() {
	}

	/**
	 * Represents a test key with a constant hash code to force collisions.
	 */
	private static class CollisionKey {
		private final int id;

		/**
		 * Constructs a collision key with the specified identifier.
		 *
		 * @param id the identifier used when comparing keys
		 */
		CollisionKey(int id) {
			this.id = id;
		}

		/**
		 * Returns the same hash code for every collision key.
		 *
		 * @return the constant hash code seven
		 */
		@Override
		public int hashCode() {
			return 7;
		}

		/**
		 * Determines whether two collision keys have the same identifier.
		 *
		 * @param other the object to compare with this key
		 * @return true if the keys have the same identifier, false otherwise
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof CollisionKey && id == ((CollisionKey) other).id;
		}
	}

	/**
	 * Determines whether an integer is prime.
	 *
	 * @param value the integer to check
	 * @return true if the integer is prime, false otherwise
	 */
	private boolean isPrime(int value) {
		if (value < 2)
			return false;
		for (int divisor = 2; divisor <= value / divisor; divisor++) {
			if (value % divisor == 0)
				return false;
		}
		return true;
	}

	/**
	 * Checks the initial capacity and operations on an empty table.
	 */
	@Test
	public void testDefaultConstructorAndEmptyTable() {
		HashTable<String, Integer> table = new HashTable<>();
		assertEquals(101, table.capacity());
		assertEquals(0, table.size());
		assertTrue(table.isEmpty());
		assertTrue(table.entries().isEmpty());
		assertFalse(table.containsKey("missing"));
		assertFalse(table.containsValue(10));
		assertNull(table.get("missing"));
		assertNull(table.remove("missing"));
	}

	/**
	 * Checks valid load factors and rejects invalid load factors.
	 */
	@Test
	public void testCustomConstructorValidation() {
		assertEquals(101, new HashTable<String, Integer>(0.25).capacity());
		assertEquals(101, new HashTable<String, Integer>(0.75).capacity());

		assertThrows(IllegalArgumentException.class, () -> new HashTable<String, Integer>(0.0));
		assertThrows(IllegalArgumentException.class, () -> new HashTable<String, Integer>(1.0));
		assertThrows(IllegalArgumentException.class, () -> new HashTable<String, Integer>(-0.1));
		assertThrows(IllegalArgumentException.class, () -> new HashTable<String, Integer>(1.1));
		assertThrows(IllegalArgumentException.class, () -> new HashTable<String, Integer>(Double.NaN));
	}

	/**
	 * Checks insertion, lookup, size, and key membership.
	 */
	@Test
	public void testPutGetAndSize() {
		HashTable<String, Integer> table = new HashTable<>();
		assertNull(table.put("Ada", 10));
		assertNull(table.put("Alan", 20));
		assertNull(table.put("Grace", 30));

		assertEquals(3, table.size());
		assertFalse(table.isEmpty());
		assertEquals(Integer.valueOf(10), table.get("Ada"));
		assertEquals(Integer.valueOf(20), table.get("Alan"));
		assertEquals(Integer.valueOf(30), table.get("Grace"));
		assertTrue(table.containsKey("Alan"));
		assertFalse(table.containsKey("Edsger"));
	}

	/**
	 * Checks that putting an existing key updates its value.
	 */
	@Test
	public void testUpdateExistingKey() {
		HashTable<String, Integer> table = new HashTable<>();
		assertNull(table.put("key", 10));
		assertEquals(Integer.valueOf(10), table.put(new String("key"), 25));
		assertEquals(1, table.size());
		assertEquals(Integer.valueOf(25), table.get("key"));
	}

	/**
	 * Checks removal of existing and missing keys.
	 */
	@Test
	public void testRemoveExistingAndMissingKeys() {
		HashTable<String, Integer> table = new HashTable<>();
		table.put("one", 1);
		table.put("two", 2);

		assertEquals(Integer.valueOf(1), table.remove("one"));
		assertNull(table.get("one"));
		assertFalse(table.containsKey("one"));
		assertEquals(1, table.size());
		assertNull(table.remove("one"));
		assertNull(table.remove("not present"));
		assertEquals(Integer.valueOf(2), table.remove("two"));
		assertTrue(table.isEmpty());
	}

	/**
	 * Checks null values and searches by value.
	 */
	@Test
	public void testContainsValueAndNullValues() {
		HashTable<String, String> table = new HashTable<>();
		table.put("a", null);
		table.put("b", "hello");
		table.put("c", "hello");

		assertTrue(table.containsKey("a"));
		assertNull(table.get("a"));
		assertTrue(table.containsValue(null));
		assertTrue(table.containsValue(new String("hello")));
		assertFalse(table.containsValue("missing"));

		assertNull(table.put("a", "updated"));
		assertFalse(table.containsValue(null));
		assertEquals(3, table.size());
		assertEquals("updated", table.remove("a"));
	}

	/**
	 * Checks that the entries list contains each active mapping once.
	 */
	@Test
	public void testEntriesContainEveryMapping() {
		HashTable<String, Integer> table = new HashTable<>();
		table.put("a", 1);
		table.put("b", 2);
		table.put("c", 3);
		table.put("b", 20);
		table.remove("c");

		List<MapEntry<String, Integer>> entries = table.entries();
		HashMap<String, Integer> actual = new HashMap<>();
		for (MapEntry<String, Integer> entry : entries) {
			assertFalse(actual.containsKey(entry.getKey()), "Duplicate key in entries()");
			actual.put(entry.getKey(), entry.getValue());
		}

		assertEquals(2, entries.size());
		assertEquals(Integer.valueOf(1), actual.get("a"));
		assertEquals(Integer.valueOf(20), actual.get("b"));
		assertFalse(actual.containsKey("c"));
	}

	/**
	 * Checks retrieval of distinct keys with identical hash codes.
	 */
	@Test
	public void testQuadraticProbingWithCollisions() {
		HashTable<CollisionKey, Integer> table = new HashTable<>();
		for (int i = 0; i < 12; i++)
			assertNull(table.put(new CollisionKey(i), i * 10));

		assertEquals(12, table.size());
		for (int i = 0; i < 12; i++) {
			assertEquals(Integer.valueOf(i * 10), table.get(new CollisionKey(i)));
			assertTrue(table.containsKey(new CollisionKey(i)));
		}
		assertNull(table.get(new CollisionKey(99)));
	}

	/**
	 * Checks that deletion does not break probing for later keys.
	 */
	@Test
	public void testLazyDeletionPreservesProbeChain() {
		HashTable<CollisionKey, Integer> table = new HashTable<>();
		for (int i = 0; i < 8; i++)
			table.put(new CollisionKey(i), i);

		assertEquals(Integer.valueOf(2), table.remove(new CollisionKey(2)));
		assertEquals(Integer.valueOf(4), table.remove(new CollisionKey(4)));
		assertEquals(6, table.size());

		// Deleting an early colliding key must not hide later colliding keys.
		for (int i : new int[] {0, 1, 3, 5, 6, 7})
			assertEquals(Integer.valueOf(i), table.get(new CollisionKey(i)));

		assertNull(table.put(new CollisionKey(100), 100));
		assertEquals(Integer.valueOf(100), table.get(new CollisionKey(100)));
		assertNull(table.put(new CollisionKey(2), 200));
		assertEquals(Integer.valueOf(200), table.get(new CollisionKey(2)));
		assertEquals(8, table.size());
	}

	/**
	 * Checks retrieval with negative and minimum integer hash codes.
	 */
	@Test
	public void testNegativeHashCodes() {
		HashTable<Integer, String> table = new HashTable<>();
		table.put(-1, "negative one");
		table.put(Integer.MIN_VALUE, "minimum");
		table.put(1, "positive one");

		assertEquals("negative one", table.get(-1));
		assertEquals("minimum", table.get(Integer.MIN_VALUE));
		assertEquals("positive one", table.get(1));
		assertEquals(3, table.size());
	}

	/**
	 * Checks resizing when the default maximum load factor is exceeded.
	 */
	@Test
	public void testResizeAtDefaultLoadFactor() {
		HashTable<Integer, String> table = new HashTable<>();
		int oldCapacity = table.capacity();

		// 50 / 101 is below the default maximum load factor of 0.5.
		for (int i = 0; i < 50; i++)
			table.put(i, "value" + i);
		assertEquals(oldCapacity, table.capacity());

		table.put(50, "value50"); // 51 / 101 exceeds 0.5.
		assertTrue(table.capacity() >= 2 * oldCapacity);
		assertTrue(isPrime(table.capacity()));
		assertEquals(51, table.size());

		for (int i = 0; i <= 50; i++)
			assertEquals("value" + i, table.get(i));
	}

	/**
	 * Checks that updating a key does not trigger a resize.
	 */
	@Test
	public void testUpdatingKeyDoesNotTriggerResize() {
		HashTable<Integer, Integer> table = new HashTable<>();
		for (int i = 0; i < 50; i++)
			table.put(i, i);

		assertEquals(101, table.capacity());
		assertEquals(Integer.valueOf(10), table.put(10, 999));
		assertEquals(50, table.size());
		assertEquals(101, table.capacity());
		assertEquals(Integer.valueOf(999), table.get(10));
	}

	/**
	 * Checks resizing with a user-specified maximum load factor.
	 */
	@Test
	public void testCustomLoadFactorTriggersResize() {
		HashTable<Integer, Integer> table = new HashTable<>(0.1);
		for (int i = 0; i < 10; i++)
			table.put(i, i);
		assertEquals(101, table.capacity());

		table.put(10, 10);
		assertTrue(table.capacity() >= 202);
		assertTrue(isPrime(table.capacity()));
		assertEquals(11, table.size());
		for (int i = 0; i <= 10; i++)
			assertEquals(Integer.valueOf(i), table.get(i));
	}

	/**
	 * Checks clearing after resizing and deleting entries.
	 */
	@Test
	public void testClearAfterResizingAndDeletion() {
		HashTable<Integer, Integer> table = new HashTable<>();
		for (int i = 0; i < 80; i++)
			table.put(i, i + 100);
		table.remove(5);
		table.remove(20);

		table.clear();
		assertTrue(table.isEmpty());
		assertEquals(0, table.size());
		assertTrue(table.entries().isEmpty());
		assertFalse(table.containsValue(101));
		for (int i = 0; i < 80; i++)
			assertNull(table.get(i));

		table.put(42, 900);
		assertEquals(Integer.valueOf(900), table.get(42));
		assertEquals(1, table.size());
	}

	/**
	 * Checks correctness after repeated insertions and removals.
	 */
	@Test
	public void testManyInsertionsAndRemovals() {
		HashTable<Integer, Integer> table = new HashTable<>();
		for (int i = 0; i < 250; i++)
			table.put(i, i * 2);
		assertEquals(250, table.size());

		for (int i = 0; i < 250; i += 2)
			assertEquals(Integer.valueOf(i * 2), table.remove(i));
		assertEquals(125, table.size());

		for (int i = 0; i < 250; i++) {
			if (i % 2 == 0)
				assertFalse(table.containsKey(i));
			else
				assertEquals(Integer.valueOf(i * 2), table.get(i));
		}
	}

	/**
	 * Checks that student objects work as keys in the hash table.
	 */
	@Test
	public void testStudentKeys() {
		HashTable<StudentGoodHash, Double> table = new HashTable<>();
		StudentGoodHash student = new StudentGoodHash(1019999, "Alan", "Turing");
		StudentGoodHash sameStudent = new StudentGoodHash(1019999, "Alan", "Turing");

		assertNull(table.put(student, 3.2));
		assertEquals(Double.valueOf(3.2), table.get(sameStudent));
		assertEquals(Double.valueOf(3.2), table.put(sameStudent, 3.8));
		assertEquals(1, table.size());
		assertEquals(Double.valueOf(3.8), table.get(student));

		// Also check the other two student-key types.
		HashTable<StudentBadHash, Integer> badTable = new HashTable<>();
		badTable.put(new StudentBadHash(7, "Ada", "Lovelace"), 1);
		assertEquals(Integer.valueOf(1),
				badTable.get(new StudentBadHash(7, "Ada", "Lovelace")));

		HashTable<StudentMediumHash, Integer> mediumTable = new HashTable<>();
		mediumTable.put(new StudentMediumHash(8, "Grace", "Hopper"), 2);
		assertEquals(Integer.valueOf(2),
				mediumTable.get(new StudentMediumHash(8, "Grace", "Hopper")));
	}

	/**
	 * Checks the equals and hashCode contract for student objects.
	 */
	@Test
	public void testStudentHashCodeMatchesEquals() {
		StudentBadHash bad1 = new StudentBadHash(12, "Ada", "Lovelace");
		StudentBadHash bad2 = new StudentBadHash(12, "Ada", "Lovelace");
		assertEquals(bad1, bad2);
		assertEquals(bad1.hashCode(), bad2.hashCode());

		StudentMediumHash medium1 = new StudentMediumHash(12, "Ada", "Lovelace");
		StudentMediumHash medium2 = new StudentMediumHash(12, "Ada", "Lovelace");
		assertEquals(medium1, medium2);
		assertEquals(medium1.hashCode(), medium2.hashCode());

		StudentGoodHash good1 = new StudentGoodHash(12, "Ada", "Lovelace");
		StudentGoodHash good2 = new StudentGoodHash(12, "Ada", "Lovelace");
		assertEquals(good1, good2);
		assertEquals(good1.hashCode(), good2.hashCode());
	}

	/**
	 * Compares the distribution of the three hash functions on sample data.
	 */
	@Test
	public void testHashFunctionQualityOnSampleStudents() {
		String[] firstNames = {"Ada", "Alan", "Grace", "Edsger", "Linus", "Barbara",
				"Donald", "Margaret", "John", "Katherine", "Dennis", "Frances"};
		Set<Integer> badHashes = new HashSet<>();
		Set<Integer> mediumHashes = new HashSet<>();
		Set<Integer> goodHashes = new HashSet<>();

		for (int i = 0; i < firstNames.length; i++) {
			int uid = 1000000 + i;
			String lastName = "Student" + i;
			badHashes.add(Math.floorMod(new StudentBadHash(uid, firstNames[i], lastName).hashCode(), 101));
			mediumHashes.add(Math.floorMod(new StudentMediumHash(uid, firstNames[i], lastName).hashCode(), 101));
			goodHashes.add(Math.floorMod(new StudentGoodHash(uid, firstNames[i], lastName).hashCode(), 101));
		}

		// This small sample illustrates the intended general distribution quality.
		assertTrue(badHashes.size() < mediumHashes.size());
		assertTrue(goodHashes.size() > badHashes.size());
	}

	/**
	 * Checks that a key beyond a deleted slot is updated rather than duplicated.
	 */
	@Test
	public void testUpdateBehindDeletedSlot() {
		HashTable<CollisionKey, Integer> table = new HashTable<>();
		table.put(new CollisionKey(1), 10);
		table.put(new CollisionKey(2), 20);
		table.put(new CollisionKey(3), 30);
		table.remove(new CollisionKey(1));

		assertEquals(Integer.valueOf(20), table.put(new CollisionKey(2), 200));
		assertEquals(2, table.size());
		assertEquals(Integer.valueOf(200), table.get(new CollisionKey(2)));
		assertEquals(2, table.entries().size());
	}

	/**
	 * Checks that deleted entries do not reappear after a rehash.
	 */
	@Test
	public void testRehashExcludesDeletedEntries() {
		HashTable<Integer, Integer> table = new HashTable<>();
		for (int i = 0; i < 40; i++)
			table.put(i, i);
		for (int i = 0; i < 10; i++)
			table.remove(i);
		for (int i = 40; i < 61; i++)
			table.put(i, i);

		assertEquals(51, table.size());
		assertTrue(isPrime(table.capacity()));
		for (int i = 0; i < 10; i++)
			assertFalse(table.containsKey(i));
		for (int i = 10; i < 61; i++)
			assertEquals(Integer.valueOf(i), table.get(i));
	}

	/**
	 * Checks that inserting a null key raises a NullPointerException.
	 */
	@Test
	public void testNullKey() {
		HashTable<String, Integer> table = new HashTable<>();
		assertThrows(NullPointerException.class, () -> table.put(null, 1));
	}

	/**
	 * Checks that students differing in any identity field are not equal.
	 */
	@Test
	public void testStudentsWithDifferentFieldsAreNotEqual() {
		StudentGoodHash student = new StudentGoodHash(7, "Ada", "Lovelace");
		assertNotEquals(student, new StudentGoodHash(8, "Ada", "Lovelace"));
		assertNotEquals(student, new StudentGoodHash(7, "Alan", "Lovelace"));
		assertNotEquals(student, new StudentGoodHash(7, "Ada", "Hopper"));
		assertNotEquals(student, new StudentMediumHash(7, "Ada", "Lovelace"));
	}

	/**
	 * Checks that infinite values are rejected as maximum load factors.
	 */
	@Test
	public void testInfiniteLoadFactorsAreRejected() {
		assertThrows(IllegalArgumentException.class, () -> new HashTable<String, Integer>(Double.POSITIVE_INFINITY));
		assertThrows(IllegalArgumentException.class, () -> new HashTable<String, Integer>(Double.NEGATIVE_INFINITY));
	}

	/**
	 * Checks that a larger custom load factor does not cause an early resize.
	 */
	@Test
	public void testHighCustomLoadFactor() {
		HashTable<Integer, Integer> table = new HashTable<>(0.9);
		for(int i = 0; i < 80; i++)
			table.put(i, i * 3);

		assertEquals(101, table.capacity());
		assertEquals(80, table.size());
		for(int i = 0; i < 80; i++)
			assertEquals(Integer.valueOf(i * 3), table.get(i));
	}

	/**
	 * Checks null keys in the other operations that take a key.
	 */
	@Test
	public void testNullKeyInAllKeyOperations() {
		HashTable<String, Integer> table = new HashTable<>();
		table.put("valid", 1);

		assertThrows(NullPointerException.class, () -> table.containsKey(null));
		assertThrows(NullPointerException.class, () -> table.get(null));
		assertThrows(NullPointerException.class, () -> table.remove(null));
	}

	/**
	 * Checks that a mapping with a null value can be removed correctly.
	 */
	@Test
	public void testRemoveNullValuedMapping() {
		HashTable<String, String> table = new HashTable<>();
		table.put("null-key-value", null);
		assertTrue(table.containsKey("null-key-value"));
		assertEquals(1, table.size());

		assertNull(table.remove("null-key-value"));
		assertFalse(table.containsKey("null-key-value"));
		assertFalse(table.containsValue(null));
		assertTrue(table.entries().isEmpty());
		assertEquals(0, table.size());
	}

	/**
	 * Checks repeated insertion and deletion when every key collides.
	 */
	@Test
	public void testRepeatedCollisionsAndTombstones() {
		HashTable<CollisionKey, Integer> table = new HashTable<>();

		for(int i = 0; i < 30; i++)
			table.put(new CollisionKey(i), i);
		for(int i = 0; i < 30; i += 2)
			assertEquals(Integer.valueOf(i), table.remove(new CollisionKey(i)));

		for(int i = 30; i < 45; i++)
			table.put(new CollisionKey(i), i);
		assertEquals(30, table.size());

		for(int i = 0; i < 45; i++) {
			CollisionKey key = new CollisionKey(i);
			if(i < 30 && i % 2 == 0) {
				assertFalse(table.containsKey(key));
				assertNull(table.get(key));
			}
			else {
				assertTrue(table.containsKey(key));
				assertEquals(Integer.valueOf(i), table.get(key));
			}
		}
		assertNull(table.get(new CollisionKey(1000)));
		assertNull(table.remove(new CollisionKey(1000)));
	}

	/**
	 * Checks that every mapping survives multiple capacity increases.
	 */
	@Test
	public void testMultipleResizesKeepAllMappings() {
		HashTable<Integer, String> table = new HashTable<>();
		for(int i = 0; i < 600; i++) {
			assertNull(table.put(i, "item" + i));
			assertTrue((double) table.size() / table.capacity() <= 0.5,
					"The load factor exceeded 0.5 after insertion " + i);
		}

		assertTrue(table.capacity() >= 1200);
		assertTrue(isPrime(table.capacity()));
		assertEquals(600, table.size());
		for(int i = 0; i < 600; i++)
			assertEquals("item" + i, table.get(i));
		assertEquals(600, table.entries().size());
	}

	/**
	 * Checks repeated updates to one key without changing the table size.
	 */
	@Test
	public void testRepeatedUpdatesDoNotAddMappings() {
		HashTable<String, Integer> table = new HashTable<>();
		assertNull(table.put("same", 0));
		for(int i = 1; i <= 100; i++) {
			assertEquals(Integer.valueOf(i - 1), table.put(new String("same"), i));
			assertEquals(1, table.size());
		}

		assertEquals(101, table.capacity());
		assertEquals(Integer.valueOf(100), table.get("same"));
	}

	/**
	 * Compares many deterministic map operations against Java's HashMap.
	 * Collision keys are used to exercise repeated probing and deletion.
	 */
	@Test
	public void testRandomizedOperationsAgainstHashMap() {
		HashTable<CollisionKey, Integer> table = new HashTable<>();
		HashMap<CollisionKey, Integer> expected = new HashMap<>();
		Random random = new Random(2420);

		for(int step = 0; step < 2000; step++) {
			CollisionKey key = new CollisionKey(random.nextInt(80));
			int operation = random.nextInt(4);
			if(operation <= 1) {
				Integer value = random.nextInt(5) == 0 ? null : random.nextInt(1000);
				assertEquals(expected.put(key, value), table.put(key, value));
			}
			else if(operation == 2)
				assertEquals(expected.remove(key), table.remove(key));
			else
				assertEquals(expected.get(key), table.get(key));

			assertEquals(expected.size(), table.size(), "Incorrect size at operation " + step);
			assertEquals(expected.isEmpty(), table.isEmpty());

			if(step % 25 == 0) {
				for(int i = 0; i < 80; i++) {
					CollisionKey testKey = new CollisionKey(i);
					assertEquals(expected.containsKey(testKey), table.containsKey(testKey));
					assertEquals(expected.get(testKey), table.get(testKey));
				}

				HashMap<CollisionKey, Integer> entries = new HashMap<>();
				for(MapEntry<CollisionKey, Integer> entry : table.entries()) {
					assertFalse(entries.containsKey(entry.getKey()), "Duplicate key in entries()");
					entries.put(entry.getKey(), entry.getValue());
				}
				assertEquals(expected, entries);
			}
		}
	}
}
