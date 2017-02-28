/*
 * Authors: Muriel Brunet, Isaiah Mann 
 * Description: Runs the game of life in parallel
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Scanner;

public class GameOfLifeApplication {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		// Prompt to user
		System.out.print("How many generations should the program run?  ");
		// Read in number of generations to run program for from keyboard
		int numGen = keyboard.nextInt();
		
		BlockingQueue<Integer> firstThreadQueue = new ArrayBlockingQueue<Integer>(1);
		BlockingQueue<Integer> secondThreadQueue = new ArrayBlockingQueue<Integer>(1);
		
		// Breaking up the dish for threads
		int numRowsInDish = GameOfLifeData.newDish.length;
		int middleRow = numRowsInDish / 2;
		
		GameOfLifeThread firstThread = new GameOfLifeThread(
				secondThreadQueue, 
				firstThreadQueue,
				0,
				middleRow - 1,
				numGen);
		
		GameOfLifeThread secondThread = new GameOfLifeThread(
				firstThreadQueue, 
				secondThreadQueue,
				middleRow,
				numRowsInDish - 1,
				numGen);
		
		// Use "start" instead of "run" because "run" blocks the main thread
		firstThread.start();
		secondThread.start();
		while(firstThread.isAlive() || secondThread.isAlive()) {
			clearScreen();
			print(GameOfLifeData.currDish);
			// Wait a millisecond before trying to print again
			try {
				Thread.sleep(GameOfLifeData.CONSOLE_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
