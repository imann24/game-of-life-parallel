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
		System.out.print("How many generations should the program run?  ");
		// Read in number of generations to run program for from keyboard
		int numGen = keyboard.nextInt();
		
		BlockingQueue<Integer> firstThreadQueue = new ArrayBlockingQueue<Integer>(1);
		BlockingQueue<Integer> secondThreadQueue = new ArrayBlockingQueue<Integer>(1);
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
		for(int i = 0; i < numGen; i++) {
			try {
				firstThreadQueue.take();
				secondThreadQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			print(GameOfLifeData.newDish);
		}
		print(GameOfLifeData.newDish);
//		try {
//			firstThread.join();
//			secondThread.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	public static void print(String[] dish) {
		for ( String s: dish ) {
		    System.out.println(s);
	    }
	}

}
