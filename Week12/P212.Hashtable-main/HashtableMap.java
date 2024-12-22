// == CS400 Fall 2024 File Header Information ==
// Name: Cyrus Kianian
// Email: kianian@wisc.edu
// Group: P2.1703
// Lecturer: Gary
// Notes to Grader: :)
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.NoSuchElementException;
import java.util.LinkedList;

/**
 * class that implements a hash table map
 *
 * @param <KeyType>   - type of the keys
 * @param <ValueType> - value type
 */
@SuppressWarnings("unchecked")
public class HashtableMap<KeyType, ValueType> implements MapADT {

  /**
   * class for key value pairs in the map
   */
  protected class Pair {

    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }

  }


  protected LinkedList<Pair>[] table = null;

  /**
   * constructor for a hash table with an initial specified capacity
   *
   * @param capacity - initial capacity
   */
  public HashtableMap(int capacity) {
    //new table
    this.table = (LinkedList<Pair>[]) new LinkedList[capacity];
  }

  /**
   * constructor for a hash table without a specified initial value
   */
  public HashtableMap() {
    // with default capacity = 64
    this.table = (LinkedList<Pair>[]) new LinkedList[64];
  }

  /**
   * Adds a new key,value pair/mapping to this collection.
   *
   * @param key   the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException     if key is null
   */
  @Override
  public void put(Object key, Object value) throws IllegalArgumentException {

    if (key == null) {//ensure key is not null
      throw new NullPointerException("key was null");
    }
    if (this.containsKey(key)) {//ensure key is not already in the map
      throw new IllegalArgumentException("Key is already in map");
    }

    int index = Math.abs(key.hashCode()) % this.getCapacity();//index of new pairing
    Pair pair = new Pair((KeyType) key, (ValueType) value);//make the pair

    if (this.table[index] == null) {//ensure that index is non-null
      this.table[index] = new LinkedList<>();
    }
    this.table[index].add(pair);//add the new key value pair

    this.rehash(); //rehash if necessary
  }

  /**
   * private method that handles rehashing the array
   */
  private void rehash() {
    if ((double) this.getSize() / (double) this.getCapacity() >= 0.8) {
      LinkedList<Pair> pairs = new LinkedList<>();//list to store all the pairs

      for (LinkedList<Pair> lists : this.table) {
        if (lists != null) {
          //add all pairs to a list
          pairs.addAll(lists);
        }
      }

      int cap = this.getCapacity() * 2;
      this.table = (LinkedList<Pair>[]) new LinkedList[cap]; //double the capacity
      this.clear();//clear table

      for (Pair pair : pairs) {//add the original key value pairs to the newly sized table
        this.put(pair.key, pair.value);//add original vals
      }
    }
  }

  /**
   * Checks whether a key maps to a value in this collection.
   *
   * @param key the key to check
   * @return true if the key maps to a value, and false is the key doesn't map to a value
   */
  @Override
  public boolean containsKey(Object key) {
    if (key != null) {
      int index = Math.abs(key.hashCode()) % this.getCapacity();//index of pairing

      if (this.table[index] != null && !this.table[index].isEmpty()) {

        for (Pair pair : this.table[index]) {
          if (pair.key.equals(key)) {//look for exact key match
            return true;
          }
        }
      }

      return false; //doesn't exist
    }

    return false; //doesn't exist
  }

  /**
   * Retrieves the specific value that a key maps to.
   *
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public Object get(Object key) throws NoSuchElementException {
    if (key != null && this.containsKey(key)) {
      int index = Math.abs(key.hashCode()) % this.getCapacity();//index of pairing

      for (Pair pair : this.table[index]) {
        if (pair.key.equals(key)) { //look for key match
          return pair.value; //return pair
        }
      }
    }

    throw new NoSuchElementException("Key is not contained in this map");
  }

  /**
   * Remove the mapping for a key from this collection.
   *
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public Object remove(Object key) throws NoSuchElementException {
    if (key != null && this.containsKey(key)) {
      int index = Math.abs(key.hashCode()) % this.getCapacity();//index of new pairing


      for (Pair pair : this.table[index]) { //look for key match
        if (pair.key.equals(key)) {
          Object val = pair.value;
          this.table[index].remove(pair);
          return val; //return the removed pair
        }
      }

      throw new NoSuchElementException("Key is not contained in this map");
    } else {
      throw new NoSuchElementException("Key is not contained in this map");
    }
  }

  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    for (LinkedList<Pair> lists : this.table) {
      if (lists != null) {
        //remove all pairs
        for (Pair pair : lists) {
          lists.remove(pair);
        }
      }
    }
  }

  /**
   * Retrieves the number of keys stored in this collection.
   *
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    LinkedList<Pair> pairs = new LinkedList<>();//list to store all the pairs

    for (LinkedList<Pair> lists : this.table) {
      if (lists != null) {
        //add all pairs to a list
        pairs.addAll(lists);
      }
    }

    return pairs.size();//return the size of the pairs lists
  }

  /**
   * Retrieves this collection's capacity.
   *
   * @return the size of te underlying array for this collection
   */
  @Override
  public int getCapacity() {
    return this.table.length;//return length of array
  }


  /**
   * Tests the constructors and put method in addition to the rehash method
   */
  @Test
  public void test1() {
    HashtableMap map = new HashtableMap();//create maps with different constructors
    HashtableMap mapInitCap = new HashtableMap(5);

    //ensure constructors worked as expected
    Assertions.assertEquals(map.getCapacity(), 64, "Constructor did not size as expected");
    Assertions.assertEquals(mapInitCap.getCapacity(), 5, "Constructor did not size as expected");

    map.put("a", "1");
    mapInitCap.put("d", "4");

    Assertions.assertTrue(map.containsKey("a"));//ensures values were added to the map
    Assertions.assertTrue(mapInitCap.containsKey("d"));

    try {//should throw an exception if key is already in the map
      map.put("a", 100);
      Assertions.assertEquals(0, 1, "did not throw exception for an already mapped key");
    } catch (IllegalArgumentException e) {
      //works
    }
    try {//should throw an exception if key is null
      map.put(null, 100);
      Assertions.assertEquals(0, 1, "did not throw exception for a null key");
    } catch (NullPointerException e) {
      //works
    }

    //add values to cause rehashing
    mapInitCap.put("a", "1");
    mapInitCap.put("b", "2");
    mapInitCap.put("c", "3");

    //ensure original vals are still in the table
    Assertions.assertEquals(10, mapInitCap.getCapacity(), "did not rehash properly");
    Assertions.assertTrue(mapInitCap.containsKey("a"), "did not contain expected key after rehash");
    Assertions.assertTrue(mapInitCap.containsKey("b"), "did not contain expected key after rehash");
    Assertions.assertTrue(mapInitCap.containsKey("c"), "did not contain expected key after rehash");
    Assertions.assertTrue(mapInitCap.containsKey("d"), "did not contain expected key after rehash");
  }

  /**
   * Tests getCapacity, getSize, and clear methods
   */
  @Test
  public void test2() {
    HashtableMap map = new HashtableMap(100);//make a map

    //ensure that the sizes and capacities are as expected
    Assertions.assertEquals(100, map.getCapacity(), "capacity was not expected");
    Assertions.assertEquals(0, map.getSize(), "Size was not 0");

    //add some values
    map.put("a", "1");
    map.put("b", "2");
    map.put("c", "3");
    map.put("d", "4");

    //check new sizes and same capacities
    Assertions.assertEquals(100, map.getCapacity(), "capacity was not expected");
    Assertions.assertEquals(4, map.getSize(), "Size was not 4");

    //clear the map
    map.clear();

    //ensure sizes are updated to contain nothing
    Assertions.assertEquals(100, map.getCapacity(), "capacity was not expected");
    Assertions.assertEquals(0, map.getSize(), "map was not empty");
  }

  /**
   * Tests containsKey method
   */
  @Test
  public void test3() {
    HashtableMap map = new HashtableMap(100);//make a map

    //add some values
    map.put("a", "1");
    map.put("b", "2");

    //ensure that we can find keys in the map
    Assertions.assertTrue(map.containsKey("a"), "did not contain expected key");
    Assertions.assertTrue(map.containsKey("b"), "did not contain expected key");
    //ensure we don't find non-real keys
    Assertions.assertFalse(map.containsKey("d"), "contained an unexpected key");
  }

  /**
   * Tests get method
   */
  @Test
  public void test4() {
    HashtableMap map = new HashtableMap(100);//make a map

    //add some values
    map.put("a", "1");
    map.put("b", "2");

    //ensure that keys map to their correct value pairs
    Assertions.assertEquals(map.get("a"), "1", "did not have expected pairing");
    Assertions.assertEquals(map.get("b"), "2", "did not have expected pairing");

    try {//ensures that we don't get keys that are non-real
      map.get("d");
      Assertions.assertEquals(0, 1, "Didn't throw exception for non-real key");
    } catch (NoSuchElementException e) {
      //works
    }
  }

  /**
   * Tests remove method
   */
  @Test
  public void test5() {
    HashtableMap map = new HashtableMap(5);//make a map

    //add some values
    map.put("a", "1");
    map.put("b", "2");

    //ensure that keys map to their correct value pairs
    Assertions.assertEquals(map.get("a"), "1", "did not have expected pairing");
    Assertions.assertEquals(map.get("b"), "2", "did not have expected pairing");

    Object rem = map.remove("a");//remove key

    Assertions.assertEquals("1", rem, "did not remove correct value");
    Assertions.assertFalse(map.containsKey("a"), "found a deleted key in map");

    rem = map.remove("b");//remove key

    Assertions.assertEquals("2", rem, "did not remove correct value");
    Assertions.assertFalse(map.containsKey("b"), "found a deleted key in map");

    try {
      map.remove("b");//test non-real key
      Assertions.assertEquals(0, 1, "Didn't throw exception for removing a non-real key");
    } catch (NoSuchElementException e) {
      //works
    }
  }
}
