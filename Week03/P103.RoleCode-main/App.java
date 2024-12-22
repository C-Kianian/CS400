import java.util.Scanner;

/**
 * Main entry point for running the CS400 Project 1: iSongly app.
 */
public class App {
    public static void main(String[] args) {
	IterableSortedCollection<Song> tree = new Tree_Placeholder(); 
	BackendInterface backend = new Backend_Placeholder(tree);
	Scanner in = new Scanner(System.in);
	FrontendInterface frontend = new Frontend(in,backend);

	System.out.println("Welcome to iSongly");
	System.out.println("==================");
	
	frontend.runCommandLoop();

	System.out.println();
	System.out.println("====================");
	System.out.println("Thanks, and Goodbye.");
    }
}
