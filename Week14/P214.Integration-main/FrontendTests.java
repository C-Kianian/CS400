import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.List;
import java.util.ArrayList;

/**
 * This class contains JUnit test methods for the Frontend class
 */
public class FrontendTests {

    BackendInterface backend = new Backend_Placeholder(new Graph_Placeholder()); //Creating Instance

    /**
     * Test method for generateShortestPathPromptHTML.
     */
    @Test
    public void roleTest1() {
        Frontend frontend = new Frontend(backend);
        
        // Calling the method
        String htmlOutput = frontend.generateShortestPathPromptHTML();
        
        // Checking input fields and button
        Assertions.assertTrue(htmlOutput.contains("id='start'"), "HTML should contain input field for start location.");
        Assertions.assertTrue(htmlOutput.contains("id='end'"), "HTML should contain input field for end location.");
        Assertions.assertTrue(htmlOutput.contains("Find Shortest Path"), "HTML should contain a button to find shortest path.");
    }

    /**
     * Test method for generateShortestPathResponseHTML.
     */
    @Test
    public void roleTest2() {
        Frontend frontend = new Frontend(backend);
        
        // Start and end locations
        String htmlResponse = frontend.generateShortestPathResponseHTML("Computer Sciences and Statistics", "Union South");
        
        // Check for correct output structure
        Assertions.assertTrue(htmlResponse.contains("Shortest path from"), 
                   "HTML not displaying correct output");
    }

    /**
     * Test method for generateTenClosestDestinationsPromptHTML and generateTenClosestDestinationsResponseHTML.
     */
    @Test
    public void roleTest3() {
        Frontend frontend = new Frontend(backend);
        
        // Testing generateTenClosestDestinationsPromptHTML
        String promptHTML = frontend.generateTenClosestDestinationsPromptHTML();

        // Check for correct output structure
        Assertions.assertTrue(promptHTML.contains("id='from'"), "HTML should contain input field for starting location.");
        Assertions.assertTrue(promptHTML.contains("Ten Closest Destinations"), "HTML should contain a button.");
        
        // Testing generateTenClosestDestinationsResponseHTML 
        String closestResponseHTML = frontend.generateTenClosestDestinationsResponseHTML("Mosse Humanities Building");

        // Check for correct output structure
        Assertions.assertTrue(closestResponseHTML.contains("Closest destinations from Mosse Humanities Building:"), 
                   "HTML should display start location.");
        Assertions.assertTrue(closestResponseHTML.contains("<li>"), "HTML should include list of destinations.");
    }
}
