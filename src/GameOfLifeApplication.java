/*
 * Authors: Muriel Brunet, Isaiah Mann 
 * Description: Runs the game of life in parallel
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GameOfLifeApplication {

	public static void main(String[] args) {
		BlockingQueue<Integer> firstThreadQueue = new ArrayBlockingQueue<Integer>(1);
		BlockingQueue<Integer> secondThreadQueue = new ArrayBlockingQueue<Integer>(1);
		int numRowsInDish = GameOfLifeData.currentDish.length;
		int middleRow = numRowsInDish / 2;
		
		GameOfLifeThread firstThread = new GameOfLifeThread(
				secondThreadQueue, 
				firstThreadQueue,
				0,
				middleRow - 1);
		
		GameOfLifeThread secondThread = new GameOfLifeThread(
				firstThreadQueue, 
				secondThreadQueue,
				middleRow,
				numRowsInDish - 1);
		
		firstThread.run();
		secondThread.run();
	}
}
