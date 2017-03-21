/*
 * Authors: Muriel Brunet, Isaiah Mann 
 * Description: Runs the game of life in parallel
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameOfLifeApplication {
	static final int REQ_NUM_ARGS = 3;
	
	// If passing args via command line, should be in the order: {dishFileName, numThreads, numGen, shouldPrint}
	public static void main(String[] args) {
		String dishFileName;
		int numThreads;
		int numGen;
		boolean shouldPrint = true;
		if(args.length >= REQ_NUM_ARGS) {
			dishFileName = args[0];
			numThreads = Integer.parseInt(args[1]);
			numGen = Integer.parseInt(args[2]);
			if(args.length > REQ_NUM_ARGS) {
				shouldPrint = Boolean.parseBoolean(args[3]);
			}
		} else {
			// Prompts the user for the text file name
			System.out.print("Please enter the name of the dish file in .txt format: ");
			
			// Read in the text file name
			// Keep the keyboard scanner open to use for later number input
			Scanner keyboard = new Scanner(System.in);
			dishFileName = keyboard.nextLine();

			// Prompt to user to enter a number of generations
			promptUserForNumber(false, "generations");
			String numGenStr = "";
			while(keyboard.hasNextLine()) {
				numGenStr = keyboard.nextLine();
				// Error checking in case user has not entered an int
				if(isInt(numGenStr)) {
					break;
				} else {
					// Prompt to user to enter a valid number
					promptUserForNumber(true, "generations");
				}
			}
			// Read in number of generations to run program for from keyboard
			numGen = Integer.parseInt(numGenStr);
			
			// Read in number of threads
			promptUserForNumber(false, "threads");
			String numThreadsStr = "";
			while(keyboard.hasNextLine()) {
				numThreadsStr = keyboard.nextLine();
				// Error checking in case user has not entered an int
				if(isInt(numThreadsStr)) {
					break;
				} else {
					// Prompt to user to enter a valid number
					promptUserForNumber(true, "threads");
				}
			}
			// Read in number of threads to run program with from keyboard
			numThreads = Integer.parseInt(numThreadsStr);

			// Garbage Collection on the keyboard scanner
			keyboard.close();
			
		}
		File file = new File(dishFileName);
		
	  	// Use an ArrayList because the file line count is not known
		ArrayList<String> mutableDish = new ArrayList<String>();
	  	
		// Parse the file line by line
	  	try {
	        Scanner sc = new Scanner(file);
	        while (sc.hasNextLine()) {
	            mutableDish.add(sc.nextLine());
	        }
	        sc.close();
	    } catch(FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    
	    // Memoize this value to prevent recalling the method twice
	    int size = mutableDish.size();
		
	    // Creates two arrays (cloned from the text file)
	    String[] newDish = mutableDish.toArray(new String[size]);
		String[] currDish = mutableDish.toArray(new String[size]);
		// Collections used to synchronize threads 
		ArrayList<Long> threadIds = new ArrayList<Long>(numThreads);
		HashMap<Long, BlockingQueue<Long>> lookup = 
				new HashMap<Long, BlockingQueue<Long>>();
		
		GameOfLifeThread[] threads = new GameOfLifeThread[numThreads];
		
		// Creates an array of threads
		for(int i = 0; i < numThreads; i++) {
			threads[i] = createThread(
				numThreads,
				threadIds,
				lookup,
				getStart(i, numThreads, currDish.length),
				getEnd(i, numThreads, currDish.length),
				numGen,
				newDish,
				currDish
			);
		}
		
		// Starts all the threads 
		for (int i = 0; i < numThreads; i++) {
			threads[i].start();
		}
		
		// Runs the print loop while all threads are still alive
		while(allThreadAlive(threadIds, numThreads)) {
			if(shouldPrint) {
				clearScreen();
				print(currDish);
			}
			try {
				// guarantees that one generation is not printed twice
				// gives time for next generation to be computed
				Thread.sleep(GameOfLifeData.CONSOLE_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Raises a kill flag inside each thread once one has terminated (to avoid deadlock)
		killAllThreads(threads);
	}

	static void killAllThreads(GameOfLifeThread[] threads) {
		for(GameOfLifeThread t : threads) {
			// Raises a boolean flag
			t.kill();
		}
	}
	
	// threadIds only stores a list of alive thread ids
	static boolean allThreadAlive(ArrayList<Long> threadIds, int totalThreads) {
		return threadIds.size() == totalThreads;
	}
	
	// Gets the start index for a thread (within the rows of the dish)
	static int getStart(int index, int numThreads, int height) {
		return index / numThreads * height;
	}

	// Gets the end index for a thread (within the rows of the dish)
	static int getEnd(int index, int numThreads, int height) {
		return (index + 1) / numThreads * height - 1;
	}
	
	// Wrapper function for the thread constructor
	static GameOfLifeThread createThread (
		int numThreads, ArrayList<Long> threadIds,
		HashMap<Long, BlockingQueue<Long>> lookup,
		int firstRow, int lastRow, int numGen,
		String[] newDish, String[] currDish) {
		return new GameOfLifeThread(
				numThreads,
				threadIds,
				lookup,
				firstRow,
				lastRow,
				numGen,
				newDish,
				currDish);
	}
	
	// Prints out a prompt to get number input from the command line 
	static void promptUserForNumber (boolean isRepeat, String key) {
		// Prompt to user
		System.out.println();
		if(isRepeat) {
			System.out.print("Please enter a valid number for number of " + key + ": ");
		} else {
			System.out.print("How many " + key + " should the program run? ");
		}
	}
	
	//Checks whether a String can be correctly parsed into an int
	static boolean isInt(String str)   {  
	  try   {  
	    Integer.parseInt(str);	  
	    return true;  
	  }  
	  catch(NumberFormatException nfe) {  
	    return false;  
	  }    
	}
	
	// Prints out the whole dish
	static void print(String[] dish) {
		for(String s: dish) {
		    System.out.println(s);
	    }
	}

	// NOTE: Only works properly in command line. Clears the console
    static void clearScreen () {
    		final String ANSI_CLS = "\u001b[2J";
        final String ANSI_HOME = "\u001b[H";
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.print(ANSI_HOME);
        System.out.flush();
    }

}
