import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple junit tests for the Make activity's App program.
 */
public class TestApp {

    /**
     * Ensures that a planet's name is not Pluto.
     */
    @Test
    public void appPlanetNotPluto() {
	String planet = new App().getPlanet();
	assertTrue(!planet.equals("Pluto"), "Found a planet named Pluto.");
    }

    /**
     * Ensures that at least one out of 1000 planets is named Venus.
     */
    @Test
    public void appPlanetIsVenus() {
	boolean foundVenus = false;
	for(int i=0;i<1000;i++)
	    foundVenus |=  new App().getPlanet().equals("Venus");
	assertTrue(foundVenus, "Out of 1000 planets, none named Venus");
    }

}
