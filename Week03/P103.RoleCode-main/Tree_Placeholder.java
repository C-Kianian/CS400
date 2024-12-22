import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is a placeholder for the fully working Tree that will be developed in a
 * future week.  It is designed to help develop and test the functionality of
 * Backend code for Project1.  Note the limitations described below.
 */
public class Tree_Placeholder
    implements IterableSortedCollection<Song> {

    // SortedCollectionInterface<Song> methods:  Only remembers the last song
    // that was added and confirms whether that song is contained in this
    // collection vs not.
    Song lastAddedSong = null;
    public void insert(Song data) throws NullPointerException {
	this.lastAddedSong = data; 
    }
    public boolean contains(Comparable<Song> find) {
	return find.compareTo(lastAddedSong) == 0;
    }
    public int size() {
	if(lastAddedSong == null) return 3;
	else return 4;
    }
    public boolean isEmpty() {
	return false;
    }
    public void clear() {
	throw new UnsupportedOperationException("cannot call on placeholder");
    }

    // IterableSortedCollectionInterface<Song> methods: holds a fixed list of
    // the following three songs that are ordered alphabetically by title.  If
    // a this.lastAddedSong exists, that is added to the list that is then
    // filtered to only contain values between the specified min and max in any
    // iterators that are created.
    private List<Song> songs = Arrays.asList( new Song("A L I E N S",
		       "Coldplay","permanent wave",2017,148,88,43,-5,21),
					     new Song("BO$$",
		       "Fifth Harmony","dance pop",2015,103,87,81,-5,5),
					     new Song("Cake By The Ocean",
		       "DNCE","dance pop",2016,119,75,77,-5,4));
    private Comparable<Song> min = null;
    private Comparable<Song> max = null;
    public void setIteratorMin(Comparable<Song> min) { this.min = min; }
    public void setIteratorMax(Comparable<Song> max) { this.max = max; }

    public Iterator<Song> iterator() {
	List<Song> tmp = new ArrayList<>(songs); // make a copy of list
	if(lastAddedSong != null) tmp.add(lastAddedSong); // with added song

	// remove all songs that our outside of the specified min-max range
	for(int i=0;i<tmp.size();i++)
	    if((min != null && min.compareTo(tmp.get(i)) > 0) ||
	       (max != null && max.compareTo(tmp.get(i)) < 0)) {
		tmp.remove(i);
		i--;
	    }

	// and return a new iterator that steps through the remaining values
	return tmp.iterator();
    }
    
}
