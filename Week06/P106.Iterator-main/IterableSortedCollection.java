/**
 * This interface defines an ADT for data structures that support storing a 
 * collection of comparable values in their natural ordering (similar to the
 * SortedCollection interface), and that are also iterable.
 */
public interface IterableSortedCollection<T extends Comparable<T>>
                                    extends SortedCollection<T>, Iterable<T> {

    public void setIteratorMin(Comparable<T> min); // null to clear min
    public void setIteratorMax(Comparable<T> max); // null to clear max
    
    
}
