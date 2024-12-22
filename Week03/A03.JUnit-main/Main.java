/**
 * This is a simple application that demonstrates the use of MyList.
 */
public class Main {

    public static void main(String[] args) {
	// populate zoo with ant, bear, cat, dog, and elephant
	ListADT<String> miniZoo = new MyList<>();
	miniZoo.add("ant");
	miniZoo.add("bear");
	miniZoo.add("cat");
	miniZoo.add("dog");
	miniZoo.add("eagle");
	System.out.println("Miniature Zoo started with: "+miniZoo);

	// TODO: add code here to trade a bear for a fox
	// 1) remove the bear element miniZoo
	// 2) add a new element with the value "fox" to miniZoo
	miniZoo.remove(1);
	miniZoo.add("fox");


	System.out.println("Miniature Zoo ended with: "+miniZoo);
    }

}
