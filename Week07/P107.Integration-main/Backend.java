///////////////////////////////////////////////////////////////////////////////
//
// Title: iSongly Back-end
// Course: CS 400 Fall 2024
//
// Author: Limo Kemei
// Email: kkemei@wisc.edu
// Lecturer: Gary Dahl
//
///////////////////////////////////////////////////////////////////////////////
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Back-end class simply implements BackendInterface.
 * @author Limo Kemei
 */
public class Backend implements BackendInterface {

    private IterableSortedCollection<Song> tree;
    private Integer lowEnergy;
    private Integer highEnergy;
    private Integer danceabilityThreshold;
    private List<Song> songHistory;

    /**
     * Constructs a new Backend instance with a given tree
     * 
     * @param tree the collection to store songs
     */
    public Backend(IterableSortedCollection<Song> tree) {
        this.tree = tree;
        this.lowEnergy = null;
        this.highEnergy = null;
        this.danceabilityThreshold = null;
        this.songHistory = new ArrayList<>();
    }

    /**
     * Loads song data from a CSV file and inserts songs into the tree.
     * We expect the file to have specific headers but the order may vary.
     * This method parses each line, creates Song objects, and adds them to the tree and song history.
     *
     * @param filename the name of the CSV file to read data from
     * @throws IOException if an I/O error occurs during file reading
     */
    @Override
    public void readData(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine(); // Read the header line
        if (line == null) {
            reader.close();
            return;
        }

        // use my private CSV reader to read the header
        String[] headers = parseCSVLine(line);

        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headerMap.put(headers[i].trim().toLowerCase(), i);
        }

        // use map to map the required column names to indices
        Map<String, Integer> columnIndices = new HashMap<>();

        // expected columns/header names
        String[] expectedColumns = {"title", "artist", "top genre", "year", "bpm", "nrgy", "dnce", "db", "live"};

        for (String colName : expectedColumns) {
            for (String header : headerMap.keySet()) {
                if (header.equalsIgnoreCase(colName)) {
                    columnIndices.put(colName, headerMap.get(header));
                    break;
                }
            }
        }

        // check if columns are matching
        for (String colName : expectedColumns) {
            if (!columnIndices.containsKey(colName)) {
                reader.close();
                throw new IOException("could not find column '" + colName + "' in file.");
            }
        }

        while ((line = reader.readLine()) != null) {
            // use private CSV line reader
            String[] values = parseCSVLine(line);

            if (values.length < headers.length) {
                continue; // skip useless lines
            }

            try {
                String title = values[columnIndices.get("title")].trim().replaceAll("^\"|\"$", "");
                String artist = values[columnIndices.get("artist")].trim().replaceAll("^\"|\"$", "");
                String genres = values[columnIndices.get("top genre")].trim().replaceAll("^\"|\"$", "");

                // handling numeric fields while also handling decimal values
                int year = (int) Math.round(Double.parseDouble(values[columnIndices.get("year")].trim()));
                int bpm = (int) Math.round(Double.parseDouble(values[columnIndices.get("bpm")].trim()));
                int energy = (int) Math.round(Double.parseDouble(values[columnIndices.get("nrgy")].trim()));
                int danceability = (int) Math.round(Double.parseDouble(values[columnIndices.get("dnce")].trim()));
                int loudness = (int) Math.round(Double.parseDouble(values[columnIndices.get("db")].trim()));
                int liveness = (int) Math.round(Double.parseDouble(values[columnIndices.get("live")].trim()));

                Song song = new Song(title, artist, genres, year, bpm, energy, danceability, loudness, liveness);

                tree.insert(song);
                songHistory.add(song);
            } catch (NumberFormatException e) {
                // Throw an error message
                System.out.println("Expected a number but found a non-number value");

            } catch (IndexOutOfBoundsException e) {
                // We ran out of bounds, something must be wrong
                System.out.println("CSV Reader went out of bounds, recheck the file.");
            }       
        }
        reader.close();
    }

    /**
     * Private method to read a CSV line, handling quoted fields that contain commas.
     *
     * @param line the CSV line to parse
     * @return an array of strings representing fields in the CSV line
     */
    private String[] parseCSVLine(String line) {
        // Initialize Arraylist to keep track of 
        // the the current line 
        List<String> tokens = new ArrayList<>();
        // Initialize the StringBuilder
        StringBuilder sb = new StringBuilder();
        boolean insideQuote = false;
        
        // Iterate through each character in the input string 'line'
        for (int i = 0; i < line.length(); i++) {
          // Get the current character at index 'i' in the string
          char c = line.charAt(i);

          // Check if we're starting or ending a quoted string
          if (c == '"' && (i == 0 || line.charAt(i - 1) != '\\')) {
            // Toggle the 'insideQuote' boolean  when encountering a quote that's not escaped
            insideQuote = !insideQuote;
           }

           // Check if we've encountered a comma outside of quotes
           else if (c == ',' && !insideQuote) {
              // Add the current substring to the token list and reset the StringBuilder
              tokens.add(sb.toString());
              sb.setLength(0);
          }

            // For any other character, append it to the StringBuilder
           else {
            sb.append(c);
           }
    }
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }
    
    /**
     * Gets a list of song titles from the tree that are within the energy range.
     * If a danceability filter has been set, only songs passing that filter are shown
     *
     * @param low  the minimum energy of songs to include in da list
     * @param high the maximum energy of songs to include
     * @return a list of song titles within the specified energy range and filters
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {
        this.lowEnergy = low;
        this.highEnergy = high;
        List<Song> songList = new ArrayList<>();

        for (Song song : tree) {
            // Declare energy variable
            int energy = song.getEnergy();
            // Declare boolean values for comparison
            boolean withinLow = (low == null) || (energy >= low);
            boolean withinHigh = (high == null) || (energy <= high);
            // Compare boolean values, if a match is found
            // add the song to the songList
            if (withinLow && withinHigh) {
                songList.add(song);
            }
        }

        // Apply Danceability filter if it has been set
        if (danceabilityThreshold != null) {
            Iterator<Song> iterator = songList.iterator();
            while (iterator.hasNext()) {
                Song song = iterator.next();
                if (song.getDanceability() <= danceabilityThreshold) {
                    iterator.remove();
                }
            }
        }

        // sort songs by Energy
        songList.sort(Comparator.comparingInt(Song::getEnergy));

        // get titles
        List<String> titles = new ArrayList<>();
        for (Song song : songList) {
            titles.add(song.getTitle());
        }
        return titles;
    }

    /**
     * Sets a danceability threshold filter and retrieves song titles that match the filter.
     *
     * @param threshold the danceability threshold 
     * @return a list of song titles that pass the danceability filter and/or energy range
     */
    @Override
    public List<String> setFilter(Integer threshold) {
        this.danceabilityThreshold = threshold;
        List<Song> songList = new ArrayList<>();

        for (Song song : tree) {
            int danceability = song.getDanceability();
            boolean passesThreshold = (threshold == null) || (danceability > threshold);

            // Also check Energy range
            int energy = song.getEnergy();
            boolean withinLow = (lowEnergy == null) || (energy >= lowEnergy);
            boolean withinHigh = (highEnergy == null) || (energy <= highEnergy);

            if (passesThreshold && withinLow && withinHigh) {
                songList.add(song);
            }
        }

        // Sort songs by Energy
        songList.sort(Comparator.comparingInt(Song::getEnergy));

        // Collect titles
        List<String> titles = new ArrayList<>();
        for (Song song : songList) {
            titles.add(song.getTitle());
        }
        return titles;
    }

    /**
     * Returns a list of up to five most recent song titles that meet the current energy range and danceability filter.
     * The songs are retrieved from the song history, most recent first.
     *
     * @return a list of five most recent song titles that meet the filter
     */
    @Override
    public List<String> fiveMost() {
        List<String> titles = new ArrayList<>();
        int count = 0;
        // Start loop that counts only to size < 5
        for (int i = songHistory.size() - 1; i >= 0 && count < 5; i--) {
            // Get song data and check if it makes the filter criteria
            Song song = songHistory.get(i);
            int energy = song.getEnergy();
            boolean withinLow = (lowEnergy == null) || (energy >= lowEnergy);
            boolean withinHigh = (highEnergy == null) || (energy <= highEnergy);

            int danceability = song.getDanceability();
            boolean passesThreshold = (danceabilityThreshold == null) || (danceability > danceabilityThreshold);
            
            // If all criterias are met, append it into the list
            if (withinLow && withinHigh && passesThreshold) {
                titles.add(song.getTitle());
                count++;
            }
        }
        return titles;
    }
}
