
/**
 * Some tests for MyList implementation of ListADT.
 */
public class TestMyList {

    /**
     * Runs the following tests, and displays whether they pass or fail.
     */
    public static void main(String[] args) {
	// testSizeOfList()
	if(testSizeOfList())
	    System.out.println("testSizeOfList() passed");
	else
	    System.out.println("testSizeOfList() FALIED -----------");
	
	// testInserting100Numbers()	
	if(testInserting100Numbers())
	    System.out.println("testInserting100Numbers() passed");
	else
	    System.out.println("testInserting100Numbers() FALIED -----------");
	
	// testRemovingFromEnds()
	if(testRemovingFromEnds())
	    System.out.println("testRemovingFromEnds() passed");
	else
	    System.out.println("testRemovingFromEnds() FALIED -----------");
    }

    /** 
     * Checks the size of a new list, and also checks that size after adding,
     * and removing elements, and also after clearing it's contents.
     * @returns true when this test passes, otherwise false
     */
    public static boolean testSizeOfList() {
	MyList<Boolean> list = new MyList<>();
	if(list.size() != 0) {
	    System.out.println("Newly created list is not empty.");
	    return false;
	}
	
	list.add(true);
	list.add(false);
	list.add(true);
	list.add(false);
	if(list.size() != 4) {
	    System.out.println("List size is not 4 after calling add 4 times");
	    return false;
	}

	list.remove(0);
	list.remove(1);
	if(list.size() != 2) {
	    System.out.println("List size is not 2 after removing 2 elements"
			       + " from a list of 4 elements");
	    return false;
	}

	return true;
    }
    
    /**
     * By inserting 100 numbers this test should help ensure that the 
     * capacity of the underlying array is able to grow multiple times.
     * We then check that every pair of sequential elements in this list
     * are still in ascending order.
     * @returns true when this test passes, otherwise false
     */
    public static boolean testInserting100Numbers() {
	MyList<Integer> list = new MyList<>();
	// fill list with ITERATIONS sequentail and increasing elements
	final int ITERATIONS = 100;
	for(int i=0;i<ITERATIONS;i++)
	    list.add(i+1);
	// ensure that elements are still in increasing order
	for(int i=0;i<ITERATIONS-1;i++)
	    if(list.get(i) >= list.get(i+1)) {
		String message = "Found non-ascending elements: "+list.get(i)+
		    ", "+list.get(i+1)+" at indexes "+i+", and "+(i+1);
		System.out.println(message);
		return false;
	    }
	return true;
    }

    /**
     * Removes an element from each end of the array, and then checks what is
     * left in the position of that removed element to test correctnesss.  This
     * method is also relying on and testing size() to compute last index.
     * @returns true when this test passes, otherwise false
     */
    public static boolean testRemovingFromEnds() {
	MyList<String> list = new MyList<>();
	list.add("apple");
	list.add("banana");
	list.add("cherry");
	list.add("durian");

	// test removal from the (highest index) end of the list
	String threeWas = list.remove(list.size()-1);
	String twoIs = list.get(list.size()-1);
	if(!threeWas.equals("durian") || !twoIs.equals("cherry")) {
	    String message = "Problem removing " + threeWas + " from the end" +
		"of the list, so that "+twoIs+" is the new final element.";
	    System.out.println(message);
	    return false;
	}

    	// test removal from the (lowest index) start of the list
	String zeroWas = list.remove(0);
	String zeroIs = list.get(0);
	if(!zeroWas.equals("apple") || !zeroIs.equals("banana")) {
	    String message = "Problem removing " + zeroWas + " from start" +
		"of the list, so that "+zeroIs+" is the new first element.";
	    System.out.println(message);
	    return false;
	}

	return true;
    }   
}
