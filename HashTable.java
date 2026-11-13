package assign09;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implements a map using a hash table with quadratic probing and lazy deletion.
 * The table grows when adding an entry would exceed the maximum load factor.
 *
 * @param <K> the type of keys in the map
 * @param <V> the type of values in the map
 * @author Ava Murphy & Yujia Zhao
 * @version November 12, 2026
 */
public class HashTable<K, V> implements Map<K, V> {

	private static final int INITIAL_CAPACITY = 101;
	private static final Object TOMBSTONE = new Object();

	private final double loadFactor;
	private int capacity;
	private Object[] backingArray;
	private int size;
	private long numberOfCollisions;

	/**
	 * Constructs an empty hash table with a maximum load factor of 0.5.
	 */
	public HashTable() {
		this(0.5);
	}

	/**
	 * Constructs an empty hash table with the specified maximum load factor.
	 *
	 * @param loadFactor the maximum allowed load factor
	 * @throws IllegalArgumentException if the load factor is not between 0 and 1
	 */
	public HashTable(double loadFactor) {
		if(!(loadFactor > 0.0 && loadFactor < 1.0))
			throw new IllegalArgumentException("Load factor must be between 0 and 1.0");

		this.loadFactor = loadFactor;
		this.capacity = INITIAL_CAPACITY;
		this.backingArray = new Object[INITIAL_CAPACITY];
		this.size = 0;
		this.numberOfCollisions = 0;
	}

	/**
	 * Removes every mapping and restores the initial table capacity.
	 */
	@Override
	public void clear() {
		backingArray = new Object[INITIAL_CAPACITY];
		capacity = INITIAL_CAPACITY;
		size = 0;
		numberOfCollisions = 0;
	}

	/**
	 * Determines whether the table contains the specified key.
	 *
	 * @param key the key to search for
	 * @return true if the key is present, false otherwise
	 * @throws NullPointerException if the key is null
	 */
	@Override
	public boolean containsKey(K key) {
		return getHashIndex(key, backingArray, false) != -1;
	}

	/**
	 * Determines whether any active entry has the specified value.
	 *
	 * @param value the value to search for, which may be null
	 * @return true if an entry contains the value, false otherwise
	 */
	@Override
	public boolean containsValue(V value) {
		for(int i = 0; i < backingArray.length; i++) {
			if(backingArray[i] != null && backingArray[i] != TOMBSTONE
					&& Objects.equals(cast(i, backingArray).getValue(), value))
				return true;
		}
		return false;
	}

	/**
	 * Returns a list of all active key-value entries in the table.
	 *
	 * @return a list containing the entries in an unspecified order
	 */
	@Override
	public List<MapEntry<K, V>> entries() {
		List<MapEntry<K, V>> list = new ArrayList<>();
		for(int i = 0; i < backingArray.length; i++) {
			if(backingArray[i] != null && backingArray[i] != TOMBSTONE)
				list.add(cast(i, backingArray));
		}
		return list;
	}

	/**
	 * Gets the value associated with the specified key.
	 *
	 * @param key the key to search for
	 * @return the mapped value, or null if the key is absent
	 * @throws NullPointerException if the key is null
	 */
	@Override
	public V get(K key) {
		int index = getHashIndex(key, backingArray, false);
		return index == -1 ? null : cast(index, backingArray).getValue();
	}

	/**
	 * Determines whether the table has no active mappings.
	 *
	 * @return true if the table is empty, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Adds a new mapping or updates the value for an existing key.
	 * Expands the table before inserting if its load factor would be exceeded.
	 *
	 * @param key the key to insert or update
	 * @param value the value associated with the key, which may be null
	 * @return the previous value, or null if the key was not present
	 * @throws NullPointerException if the key is null
	 */
	@Override
	public V put(K key, V value) {
		int index = getHashIndex(key, backingArray, false, true);
		if(index != -1) {
			MapEntry<K, V> entry = cast(index, backingArray);
			V previousValue = entry.getValue();
			entry.setValue(value);
			return previousValue;
		}

		while((size + 1.0) / backingArray.length > loadFactor)
			rehash();

		index = getHashIndex(key, backingArray, true);
		while(index == -1) {
			// Quadratic probing can exhaust its reachable positions even when
			// the load factor is below the user-specified maximum.
			rehash();
			index = getHashIndex(key, backingArray, true);
		}

		backingArray[index] = new MapEntry<>(key, value);
		size++;
		return null;
	}

	/**
	 * Removes the entry for the specified key using a tombstone marker.
	 *
	 * @param key the key whose entry should be removed
	 * @return the previous value, or null if the key was absent
	 * @throws NullPointerException if the key is null
	 */
	@Override
	public V remove(K key) {
		int index = getHashIndex(key, backingArray, false);
		if(index == -1)
			return null;

		V previousValue = cast(index, backingArray).getValue();
		backingArray[index] = TOMBSTONE;
		size--;
		return previousValue;
	}

	/**
	 * Returns the number of active mappings in the table.
	 *
	 * @return the number of mappings
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Returns the current length of the backing array.
	 *
	 * @return the current table capacity
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * Returns the number of collisions encountered during put operations
	 * since the last reset. A collision occurs when probing finds an active
	 * entry with a different key. Searches, rehashing, and repeated internal
	 * probes during the same insertion are not counted.
	 *
	 * @return the number of collisions counted so far
	 */
	public long getNumberOfCollisions() {
		return numberOfCollisions;
	}

	/**
	 * Resets the collision counter without changing the table's entries.
	 */
	public void resetNumberOfCollisions() {
		numberOfCollisions = 0;
	}

	/**
	 * Finds the matching key or an available position using quadratic probing.
	 * A search ignores tombstones; insertion remembers the first tombstone,
	 * while continuing to check for a matching key.
	 *
	 * @param key the key to locate
	 * @param array the backing array to search
	 * @param isInsert true to find an insertion position, false to search for a key
	 * @return the matching or available index, or -1 if none exists
	 */
	private int getHashIndex(K key, Object[] array, boolean isInsert) {
		return getHashIndex(key, array, isInsert, false);
	}

	/**
	 * Probes the array, optionally counting collisions for a put operation.
	 *
	 * @param key the key to locate
	 * @param array the array to search
	 * @param isInsert true for insertion, false for searching
	 * @param countCollisions true to count active entries with other keys
	 * @return the matching or available index, or -1 if none exists
	 */
	private int getHashIndex(K key, Object[] array, boolean isInsert, boolean countCollisions) {
		int initialHash = Math.floorMod(key.hashCode(), array.length);
		int tombstoneIndex = -1;

		// At prime capacities, i^2 visits (capacity + 1) / 2 distinct positions.
		for(int i = 0; i <= array.length / 2; i++) {
			int index = (int) ((initialHash + (long) i * i) % array.length);
			Object slot = array[index];

			if(slot == null) {
				if(isInsert)
					return tombstoneIndex != -1 ? tombstoneIndex : index;
				return -1;
			}

			if(slot == TOMBSTONE) {
				if(tombstoneIndex == -1)
					tombstoneIndex = index;
			}
			else if(cast(index, array).getKey().equals(key))
				return index;
			else if(countCollisions)
				numberOfCollisions++;
		}

		return isInsert ? tombstoneIndex : -1;
	}

	/**
	 * Casts a backing-array element to a map entry.
	 *
	 * @param index the index containing an active map entry
	 * @param array the array containing the entry
	 * @return the entry at the specified index
	 */
	@SuppressWarnings("unchecked")
	private MapEntry<K, V> cast(int index, Object[] array) {
		return (MapEntry<K, V>) array[index];
	}

	/**
	 * Rebuilds the table at the next prime capacity above twice its length.
	 * Only active entries are reinserted, removing all tombstones.
	 */
	private void rehash() {
		int newCapacity = BigInteger.valueOf((long) backingArray.length * 2)
				.nextProbablePrime().intValueExact();
		Object[] newArray = new Object[newCapacity];

		for(int i = 0; i < backingArray.length; i++) {
			if(backingArray[i] != null && backingArray[i] != TOMBSTONE) {
				MapEntry<K, V> entry = cast(i, backingArray);
				int index = getHashIndex(entry.getKey(), newArray, true);
				if(index == -1)
					throw new IllegalStateException("Could not rehash entry");
				newArray[index] = entry;
			}
		}

		backingArray = newArray;
		capacity = newCapacity;
	}
}
