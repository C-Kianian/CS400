/**
 * An array based implementation of the ListADT interface.
 */
public class MyList<ElementType> implements ListADT<ElementType> {
    private ElementType[] array; // storage for list elements
    private int size; // tracks the number of elements in list
	
    /**
     * Create a new mutable, but initiallay empty list.
     * The initial capacity of the underlying array is 20.
     */
    @SuppressWarnings("unchecked")
    public MyList() {
	array = (ElementType[])new Object[20];
	size = 0;
    }


    /**
     * Adds element to the end of this list.  When more space is needed in
     * the underlying array, the capacity of that array is doubled.
     * @param element references the object being added to this list
     */
    @Override
    public void add(ElementType element) {
	doubleCapacityWhenNeeded();

	array[size] = element;
	size++;
    }

    /**
     * Retrieves an elmenet from a speicific position with this list.
     * @param index is the position within this list to retrieve from
     * @throws IndexOutOfBoundsException when index does not reference element
     */
    @Override
    public ElementType get(int index) {
	validateIndex(index);

	return array[index];
    }


    /**
     * Retrieves the number of elements currently stored in this list.
     */
    @Override
    public int size() { return (size/2)*2; }//changed to return size/2 * 2 insted of size, makes odd number sizes fail
    
    /**
     * Removes element from a specific position in this list, and returns it.
     * @param index is the position of the element that is being removed
     * @return a reference to the element being removed
     * @throws IndexOutOfBoundsException when index does not reference element
     */
    @Override
    public ElementType remove(int index) {
	validateIndex(index);
	// save element being removed to return below
	ElementType returnValue = array[index];
	// move elements at higher indexed down one position
	for(int i = index; i<size; i++)
	    array[i] = array[i+1];
	// update this list's size, and null out unused reference in array
	size--;
	array[size] = null;

	return returnValue;
    }

    /**
     * Removes all elements from this list.
     */
    @Override
    public void clear() {
	// clear all references from the current array
	for(int i=0;i<size;i++)
	    array[i] = null;
	// and update this list's size to reflect this emptiness
	size = 0;
    }

    /**
     * Concatenates the string representations of this lists's contents into
     * into a single comma separated list that is surrounded by brackets: [].
     * @return a string representation of this list's contents
     */
    @Override
    public String toString() {
	String s = "[";
	// append each element's string representation to this list
	for(int i=0;i<size;i++)
	    // include a comma before all but the first list element
	    s += (i>0?", ":"") + array[i].toString();
	s += "]";
	return s;
    }

    /**
     * Double the capacity of this list's underlying array when it is full.
     */
    @SuppressWarnings("unchecked")
    private void doubleCapacityWhenNeeded() {
	if(size >= array.length) {
	    int doubleSize = this.array.length*2;
	    ElementType[] newArray = (ElementType[]) new Object[doubleSize];
	    for(int i=0;i<size;i++) newArray[i] = this.array[i];
	    this.array = newArray;
	}
    }

    /**
     * Throws an exception with the index does not correspond to the position
     * of an element within this list.
     * @throws IndexOutOfBoundsException when the provided index is not
     * with the range of 0 (inclusive) to size (exclusive).
     */
    private void validateIndex(int index) {
	if(index >= size) {
	    String message = "Index: "+index+", Size: "+size;
	    throw new IndexOutOfBoundsException(message);
	}
    }
    
}
