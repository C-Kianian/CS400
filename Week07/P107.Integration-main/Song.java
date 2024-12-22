import java.util.Comparator;

/**
 * This class represents the data contained within a single song.
 * A comparator can be provided to each Song as the basis for it's compareTo,
 * otherwise songs will be ordered by their title.
 */
public class Song implements Comparable<Song> {

    // fields
    
    private String title;
    private String artist;
    private String genres;
    private int year;
    private int bpm;
    private int energy;
    private int danceability;
    private int loudness;
    private int liveness;
    private Comparator<Song> comparator;
    
    // constructors
    
    public Song(String title,
		String artist,
		String genre,
		int year,
		int bpm,
		int energy,
		int danceability,
		int loudness,
		int liveness,
		Comparator<Song> comparator) {
	this.title = title;
	this.artist = artist;
	this.genres = genres;
	this.year = year;
	this.bpm = bpm;
	this.energy = energy;
	this.danceability = danceability;
	this.loudness = loudness;
	this.liveness = liveness;
	this.comparator = comparator;
    }
    
    public Song(String title,
		String artist,
		String genre,
		int year,
		int bpm,
		int energy,
		int danceability,
		int loudness,
		int liveness) { // no comparator, defaults to null
	this(title,
	     artist,
	     genre,
	     year,
	     bpm,
	     energy,
	     danceability,
	     loudness,
	     liveness,
	     null);
    }

    // accessors
    
    public String getTitle() { return title; } 
    public String getArtist() { return artist; } 
    public String getGenres() { return genres; } 
    public int getYear() { return year; } 
    public int getBPM() { return bpm; } 
    public int getEnergy() { return energy; } 
    public int getDanceability() { return danceability; } 
    public int getLoudness() { return loudness; } 
    public int getLiveness() { return liveness; }

    // comparisons
    
    public int compareTo(Song other) {
	if(this.comparator != null)
	    return this.comparator.compare(this,other);
	else
	    return this.title.compareTo(other.title);
    }
}
