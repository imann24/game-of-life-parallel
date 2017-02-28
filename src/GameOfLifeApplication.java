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
		
		// Determines whether program prints every generation
		boolean printEveryGen = false;
		String response = keyboard.nextLine();
		while(!(response.equals("y") || response.equals("n"))) {
			System.out.print("Print every generation (y/n)?  ");
			response = keyboard.nextLine();
		}
		if(response.equals("y")) {
			printEveryGen = true;
		}		
		
		BlockingQueue<Integer> firstThreadQueue = new ArrayBlockingQueue<Integer>(1);
		BlockingQueue<Integer> secondThreadQueue = new ArrayBlockingQueue<Integer>(1);
		
		// Controls when the manager prints (if printing every generation)
		BlockingQueue<Integer> firstThreadManagerQueue = new ArrayBlockingQueue<Integer>(1);
		BlockingQueue<Integer> secondThreadManagerQueue = new ArrayBlockingQueue<Integer>(1);
		
		// Breaking up the dish for threads
		int numRowsInDish = GameOfLifeData.newDish.length;
		int middleRow = numRowsInDish / 2;
		
		GameOfLifeThread firstThread = new GameOfLifeThread(
				secondThreadQueue, 
				firstThreadQueue,
				firstThreadManagerQueue,
				0,
				middleRow - 1,
				numGen,
				printEveryGen);
		
		GameOfLifeThread secondThread = new GameOfLifeThread(
				firstThreadQueue, 
				secondThreadQueue,
				secondThreadManagerQueue,
				middleRow,
				numRowsInDish - 1,
				numGen,
				printEveryGen);
		
		// Use "start" instead of "run" because "run" blocks the main thread
		firstThread.start();
		secondThread.start();
		if(printEveryGen) {  // CASE A: Synchronized printing
			for(int i = 0; i < numGen; i++) {
				try {
					firstThreadManagerQueue.take();
					secondThreadManagerQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				print(GameOfLifeData.newDish);
			}
		} else { // CASE B: Printing last generation
			try {
				firstThread.join();
				secondThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			print(GameOfLifeData.newDish);
		}
	}
	
	public static void print(String[] dish) {
		for(String s: dish) {
		    System.out.println(s);
	    }
	}

}
