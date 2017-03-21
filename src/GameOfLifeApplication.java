/*
 * Authors: Muriel Brunet, Isaiah Mann 
 * Description: Runs the game of life in parallel
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.ArrayList;
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
		promptUserForNumber(false);
		String numGenStr = "";
		while(keyboard.hasNextLine()) {
			numGenStr = keyboard.nextLine();
			// Error checking in case user has not entered an int
			if(isInt(numGenStr)) {
				break;
			} else {
				// Prompt to user to enter a valid number
				promptUserForNumber(true);
			}
		}
		// Read in number of generations to run program for from keyboard
		int numGen = Integer.parseInt(numGenStr);
		
		BlockingQueue<Integer> firstThreadQueue = new ArrayBlockingQueue<Integer>(1);
		BlockingQueue<Integer> secondThreadQueue = new ArrayBlockingQueue<Integer>(1);
		
		// Breaking up the dish for threads
		int numRowsInDish = currDish.length;
		int middleRow = numRowsInDish / 2;
		
		GameOfLifeThread firstThread = new GameOfLifeThread(
				secondThreadQueue, 
				firstThreadQueue,
				0,
				middleRow - 1,
				numGen,
				newDish,
				currDish);
		
		GameOfLifeThread secondThread = new GameOfLifeThread(
				firstThreadQueue, 
				secondThreadQueue,
				middleRow,
				numRowsInDish - 1,
				numGen,
				newDish,
				currDish);
		
		// Use "start" instead of "run" because "run" blocks the main thread
		firstThread.start();
		secondThread.start();
		while(firstThread.isAlive() || secondThread.isAlive()) {
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
	
	static void promptUserForNumber (boolean isRepeat) {
		// Prompt to user
		System.out.println();
		if(isRepeat) {
			System.out.print("Please enter a valid number for number of generations  ");
		} else {
			System.out.print("How many generations should the program run?  ");
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
