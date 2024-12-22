import java.util.NoSuchElementException;

/**
 * This abstract data type represents a collection that maps keys to values,
 * in which duplicate keys are not allowed (each key maps to exactly one value).
 */
public interface MapADT<KeyType, ValueType> {

    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    public void put(KeyType key, ValueType value) throws IllegalArgumentException;

    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    public boolean containsKey(KeyType key);

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    public ValueType get(KeyType key) throws NoSuchElementException;

    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    public ValueType remove(KeyType key) throws NoSuchElementException;

    /**
     * Removes all key,value pairs from this collection.
     */
    public void clear();

    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    public int getSize();

    /**
     * Retrieves this collection's capacity.
     * @return the size of te underlying array for this collection
     */
    public int getCapacity();

}
