/**
 * Represents a simple ordered collection.
 */
public interface ListADT<ElementType> {
    public void add(ElementType element); // adds to end of list
    public ElementType get(int index); // retrieve by position in list
    public int size(); // returns the number of elements stored in list
    public ElementType remove(int index); // remove element, and for all
    // elements at higher indexes, decrements their indexes by one
    public void clear(); // removes all elements from list    
}
