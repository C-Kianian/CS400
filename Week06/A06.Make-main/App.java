/**
 * Simple Application that prints out "Hello Venus" when run.
 */
public class App {

    /**
     * Entry point for this application.
     */
    public static void main(String[] args) {
	System.out.println("Hello " + new App().getPlanet());
    }

    /**
     * Returns the name of a planet.
     * returns "Venus"
     */
    public String getPlanet() {
	return "Venus";
    }

}
