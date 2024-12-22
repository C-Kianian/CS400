/**
 * This interface defines an ADT for data structures that support storing a 
 * collection of comparable values in their natural ordering.
 */
public interface SortedCollection<T extends Comparable<T>> {

    /**
     * Inserts a new data value into the sorted collection.
     * @param data the new value being insterted
     * @throws NullPointerException if data argument is null, we do not allow
     * null values to be stored within a SortedCollection
     */
    public void insert(T data) throws NullPointerException;

    /**
     * Check whether data is stored in the tree.
     * @param data the value to check for in the collection
     * @return true if the collection contains data one or more times, 
     * and false otherwise
     */
    public boolean contains(Comparable<T> data);

    /**
     * Counts the number of values in the collection, with each duplicate value
     * being counted separately within the value returned.
     * @return the number of values in the collection, including duplicates
     */
    public int size();

    /**
     * Checks if the collection is empty.
     * @return true if the collection contains 0 values, false otherwise
     */
    public boolean isEmpty();

    /**
     * Removes all values and duplicates from the collection.
     */
    public void clear();
    
}
