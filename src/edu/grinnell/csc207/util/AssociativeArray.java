package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

import java.util.Iterator;

/**
 * A basic implementation of Associative Arrays with keys of type K and values of type V.
 * Associative Arrays store key/value pairs and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author Grant Sackmann
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> implements Iterable<KVPair<K, V>> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /** The default capacity of the initial array. */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** The size of the associative array (the number of key/value pairs). */
  int size;

  int curCapacity;

  /** The array of key/value pairs. */
  KVPair<K, V>[] pairs;


  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /** Create a new, empty associative array. */
  @SuppressWarnings({"unchecked"})
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITA.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(), DEFAULT_CAPACITY);
    this.size = 0;
    curCapacity = DEFAULT_CAPACITY;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> clone = new AssociativeArray<K, V>();

    for (KVPair<K, V> pair : this.pairs) {
      try {
        if (pair != null) {
          clone.set(pair.key, pair.val);
        } // if
      } catch (Exception e) {
        // Do nothing.
      } // try-catch
    } // for
    return clone;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    if (this.size >= 1) {
      String rString = "";
      rString += "{";
      for (int i = 0; i < this.size - 1; i++) {
        rString += this.pairs[i].key + ":" + this.pairs[i].val + ", ";
      } // for
      rString += this.pairs[this.size - 1].key + ":" + this.pairs[this.size - 1].val + "}";
      return rString;
    } else {
      return "{}";
    } // if
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to get(key) will return value.
   *
   * @param key The key whose value we are seeting.
   * @param value The value of that key.
   * @throws NullKeyException If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException("Null key Exception.");
    } else if (hasKey(key)) {
      try {
        this.pairs[find(key)].val = value;
      } catch (KeyNotFoundException e) {
        // Do nothing.
      } // try-catch
    } else {
      // check for expanding the aa
      if (this.size == pairs.length) {
        this.expand();
      } // if
      this.pairs[this.size] = new KVPair<K, V>(key, value);
      this.size += 1;
    } // if
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key A key
   * @return The corresponding value
   * @throws KeyNotFoundException when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    try {
      return this.pairs[find(key)].val;
    } catch (KeyNotFoundException e) {
      throw new KeyNotFoundException();
    } // try-catch
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should return false for the null key, since
   * it cannot appear.
  =
   * @param key The key we're looking for.
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    if (key == null) {
      return false;
    } // if

    for (KVPair<K, V> pair : this.pairs) {
      if (pair == null) {
        break;
      } else if (pair.key.equals(key)) {
        return true;
      } // if
    } // for
    return false;
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls to get(key) will throw an
   * exception. If the key does not appear in the associative array, does nothing.
   *
   * @param key The key to remove.
   */
  public void remove(K key) {
    if (hasKey(key)) {
      try {
        int overWrittenIndex = find(key);
        int swappingIndex = this.size - 1;
        if (swappingIndex >= 0) {
          this.pairs[overWrittenIndex] = this.pairs[swappingIndex];
        } // if
        pairs[swappingIndex] = null;
        this.size -= 1;
      } catch (KeyNotFoundException e) {
        // Does nothing.
      } // try-catch
    } // if
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /** Expand the underlying array. */
  void expand() {
    int newCapacity = this.size * 2;
    this.pairs = java.util.Arrays.copyOf(this.pairs, newCapacity);
    curCapacity = newCapacity;
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key. If no such entry is found,
   * throws an exception.
   *
   * @param key The key of the entry.
   * @return The index of the key, if found.
   * @throws KeyNotFoundException If the key does not appear in the associative array.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if (pairs[i].key.equals(key)) {
        return i;
      } // if
    } // for
    throw new KeyNotFoundException();
  } // find(K)


/**
 * Iterator() implementation for Associative array.
 * @return Iterator<KVPair<K, V>>
 * */
@Override
public Iterator<KVPair<K, V>> iterator() {

  /**
   * Iterator object with anonymous class declaration.
   */
  return new Iterator<KVPair<K, V>>() {
    int curIndex = 0;

    /**
     * Returns boolean indicating if there is another KVPair<K,V>
     * to iterate over.
     * @return boolean
     * */
    @Override
    public boolean hasNext() {
      return size<curCapacity;
    } // hasNext()

    /**
     * Returns next pair in associative array.
     * @return KVPair<K,V>
     * */
    @Override
    public KVPair<K, V> next() {
      return pairs[curIndex++];
    } // next()

    /**
     * Unsupported method form Iterator class.
     * */
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    } // remove()
  };
}
} // class AssociativeArray
