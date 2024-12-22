///////////////////////////////////////////////////////////////////////////////
//
// Title: iSongly Back-end Tester
// Course: CS 400 Fall 2024
//
// Author: Limo Kemei
// Email: kkemei@wisc.edu
// Lecturer: Gary Dahl
//
///////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException; // this is supposed to be used based on backend interface description but
                            // due to placeholder restrictions it is not being used in the tests

/**
 * These tests aim to test whether Backend's implementation is
 * correct in line with the specification and the methods I
 * have used to implement it.
 * 
 * @author Limo Kemei
 */
public class BackendTests {

    /**
     * Test method for Backend's readData and getRange methods.
     * This test checks if the getRange method returns the right list of song titles within an energy range,
     * and the songs in the Tree_Placeholder
     */
    @Test
    public void roleTest1() {
        // Create tree place holder
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        // Create new instance of Backend
        Backend backend = new Backend(tree);
        try {
            // random songs CSV for our "testing" purposes
            String csvTest = "title,artist,top genre,year,bpm,nrgy,dnce,dB,live\n"
                              + "\"Hey, Soul Sister\",Train,neo mellow,2010,97,89,67,-4,8\n"
                              + "\"Viva la Vida\",Coldplay,alternative rock,2008,138,80,60,-5,12\n"
                              + "\"Bohemian Rhapsody\",Queen,classic rock,1975,72,40,48,-6,15\n";
            // Write this content to a temporary file
            String filename = "test.csv";
            FileWriter fw = new FileWriter(filename);
            fw.write(csvTest);
            fw.close();
            // Read data from da csv file
            backend.readData(filename);

            // getRange to include all energies
            List<String> songs = backend.getRange(0, 100);

            // since we expect Tree_Placeholder to only remember the last inserted song and hardcoded songs,
            // we expect the last song from the CSV ("Bohemian Rhapsody") and the hardcoded songs.

            // check that the number of songs is 4
            assertEquals(4, songs.size(), "there should be 4 songs");

            // check that bohemian rhapsody in the songs
            assertTrue(songs.contains("Bohemian Rhapsody"), "Expected 'Bohemian Rhapsody' in da songs list");

            // just to confirm, check whether hardcoded songs are present
            assertTrue(songs.contains("A L I E N S"), "Expected 'A L I E N S' in da list");
            assertTrue(songs.contains("BO$$"), "Expected 'BO$$' in the list");
            assertTrue(songs.contains("Cake By The Ocean"), "Expected 'Cake By The Ocean' in da list");

        } catch (Exception e) {
            fail("Exception found!?: " + e.getMessage());
        } finally {
            // Delete file
            File file = new File("test_songs.csv");
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * Test method for Backend's setFilter and getRange methods.
     * This test checks if the setFilter method sets the danceability value and if
     * getRange method returns songs that meet the energy range and danceability filter
     */
    @Test
    public void roleTest2() {
        // Create a placeholder for the tree
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        // Create an instance of Backend
        Backend backend = new Backend(tree);
        try {
            // Create a sample CSV file content with quoted fields
            String csvContent = "title,artist,top genre,year,bpm,nrgy,dnce,dB,live\n"
                              + "\"Hey, Soul Sister\",Train,neo mellow,2010,97,89,67,-4,8\n"
                              + "\"Viva la Vida\",Coldplay,alternative rock,2008,138,80,60,-5,12\n"
                              + "\"Bohemian Rhapsody\",Queen,classic rock,1975,72,40,48,-6,15\n";
            // Write this content to a temporary file
            String filename = "test_songs.csv";
            FileWriter fw = new FileWriter(filename);
            fw.write(csvContent);
            fw.close();
            // Read data from the sample CSV file
            backend.readData(filename);
            // Set a danceability filter
            backend.setFilter(65); // Should only include songs with danceability > 65
            // Get songs within an energy range
            List<String> songs = backend.getRange(50, 90);

            // Expected songs are hardcoded songs with danceability > 65 and energy between 50 and 90
            // so BO$$ with Danceability 81 n Energy 87
            // and Cake By The Ocean with Danceability 77 and Energy 75
            assertEquals(2, songs.size());
            assertTrue(songs.contains("BO$$"));
            assertTrue(songs.contains("Cake By The Ocean"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Bad news, exception was found: " + e.getMessage());
        } finally {
            // Delete the temporary file
            File file = new File("test_songs.csv");
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * Test method for Backend's fiveMost method.
     * This test checks if the fiveMost method returns the five most recent songs
     * that meet the current energy range and danceability filter.
     */
    @Test
    public void roleTest3() {
        // Create tree placeholder 
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        // Create back-end instance
        Backend backend = new Backend(tree);
        try {
            // CSV file with hardcoded songs from Tree_Placeholder java file
            String csvHardcoded = "title,artist,top genre,year,bpm,nrgy,dnce,dB,live\n"
                              + "\"A L I E N S\",Coldplay,permanent wave,2017,148,88,43,-5,21\n"
                              + "\"BO$$\",Fifth Harmony,dance pop,2015,103,87,81,-5,5\n"
                              + "\"Cake By The Ocean\",DNCE,dance pop,2016,119,75,77,-5,4\n";
            // Write to temp file
            String filename = "test_songs.csv";
            FileWriter fw = new FileWriter(filename);
            fw.write(csvHardcoded);
            fw.close();
            // Read data from 
            backend.readData(filename);

            // setting energy range and danceability filter so
            // we can filter out the other song
            backend.getRange(80, 90);
            backend.setFilter(70);

            // Get the five most recent songs
            List<String> songs = backend.fiveMost();

            // the expected song is titled "BO$$"
            assertEquals(1, songs.size(), "Expected 1 song in the result");
            assertTrue(songs.contains("BO$$"), "Expected 'BO$$' to be fileted in the result");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception should not be thrown: " + e.getMessage());
        } finally {
            // Delete the temporary file
            File file = new File("test_songs.csv");
            if (file.exists()) {
                file.delete();
            }
        }
    }
}