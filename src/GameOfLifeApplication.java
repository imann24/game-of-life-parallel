/*
 * Authors: Muriel Brunet, Isaiah Mann 
 * Description: Runs the game of life in parallel
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameOfLifeApplication {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Please enter the name of the dish file in .txt format: ");
		String dishFileName = keyboard.nextLine();
		File file = new File(dishFileName);
	  	ArrayList<String> mutableDish = new ArrayList<String>();
	    try {
	        Scanner sc = new Scanner(file);
	        while (sc.hasNextLine()) {
	            mutableDish.add(sc.nextLine());
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    int size = mutableDish.size();
		String[] newDish = mutableDish.toArray(new String[size]);
		String[] currDish = mutableDish.toArray(new String[size]);
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
		int numGen = Integer.parseInt(numGenStr);
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
		// Read in number of generations to run program for from keyboard
		int numThreads = Integer.parseInt(numThreadsStr);
		
		ArrayList<Long> threadIds = new ArrayList<Long>(numThreads);
		HashMap<Long, BlockingQueue<Long>> lookup = 
				new HashMap<Long, BlockingQueue<Long>>();
		// Breaking up the dish for threads
		int numRowsInDish = currDish.length;
		int middleRow = numRowsInDish / 2;
		
		GameOfLifeThread[] threads = new GameOfLifeThread[numThreads];
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
		for (int i = 0; i < numThreads; i++) {
			threads[i].start();
		}
		while(anyThreadAlive(threads)) {
			clearScreen();
			print(currDish);
			try {
				// guarantees that one generation is not printed twice
				// gives time for next generation to be computed
				Thread.sleep(GameOfLifeData.CONSOLE_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static boolean anyThreadAlive(GameOfLifeThread[] threads) {
		for(GameOfLifeThread t : threads) {
			if(t.isAlive()) {
				return true;
			}
		}
		return false;
	}
	
	static int getStart(int index, int numThreads, int height) {
		return index / numThreads * height;
	}
	
	static int getEnd(int index, int numThreads, int height) {
		return (index + 1) / numThreads * height - 1;
	}
	
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
	
	static void promptUserForNumber (boolean isRepeat, String key) {
		// Prompt to user
		System.out.println();
		if(isRepeat) {
			System.out.print("Please enter a valid number for number of " + key + ": ");
		} else {
			System.out.print("How many " + key + " should the program run? ");
		}
	}
	
	static boolean isInt(String str)   {  
	  try   {  
	    Integer.parseInt(str);	  
	    return true;  
	  }  
	  catch(NumberFormatException nfe) {  
	    return false;  
	  }    
	}
	
	static void print(String[] dish) {
		for(String s: dish) {
		    System.out.println(s);
	    }
	}

    static void clearScreen () {
    		final String ANSI_CLS = "\u001b[2J";
        final String ANSI_HOME = "\u001b[H";
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.print(ANSI_HOME);
        System.out.flush();
    }

}
