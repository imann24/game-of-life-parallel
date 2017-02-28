/*
 * Authors: Muriel Brunet, Isaiah Mann 
 * Description: A single thread to work on the blocking queues
 */

import java.util.concurrent.BlockingQueue;

public class GameOfLifeThread extends Thread {
	
	BlockingQueue<Integer> sendQueue;
	BlockingQueue<Integer> receiveQueue;

	// used for printing out every generation
	BlockingQueue<Integer> managerQueue;
	
	int firstRow;
	int lastRow;
	int numGen;
	
	// used by threads to communicate when they are done with each other
	final int doneFlag = GameOfLifeData.DONE_FLAG; 
	
	String[] newGen;
	String[] currGen;

	boolean useManagerQueue;
	
	public GameOfLifeThread(
			BlockingQueue<Integer> send,
			BlockingQueue<Integer> receive,
			BlockingQueue<Integer> manager,
			int firstRow, int lastRow, int numGen,
			boolean useManager) {

		this.sendQueue = send;
		this.receiveQueue = receive;

		this.managerQueue = manager;
		
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.numGen = numGen;
		
		this.newGen = GameOfLifeData.newDish;
		this.currGen = GameOfLifeData.currDish;
		
		// only used when printing out every generation
		this.useManagerQueue = useManager;
	}

	@Override
	public void run() {
		for(int i = 0; i < this.numGen; i++) {
			calculateNextGeneration();
			try {
				// Sends flag to the other thread to indicate it's done w/ generation
				this.sendQueue.put(this.doneFlag);
				if(this.useManagerQueue) { // used when printing out every generation
					this.managerQueue.put(this.doneFlag);
				}
				this.receiveQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switchArrayPointers();
		}
	}

	/**
	 * Updates dish with new generation
	 * */
	void switchArrayPointers () {
		String[] temp = this.newGen;
		this.newGen = this.currGen;
		this.currGen = temp;
	}
	
	/**
	 * Adapted from Dominique Thiebaut's serial Java code
	 */
	void calculateNextGeneration() {
		for (int row = this.firstRow; row <= this.lastRow; row++) {// each row
			newGen[row] = "";
			for (int i = 0; i < currGen[row].length(); i++) {// each char in the row
															
				int neighbors = 0;
				char current = currGen[row].charAt(i);

				// loop in a block that is 3x3 around the current cell
				// and count the number of '#' cells.
				for (int r = row - 1; r <= row + 1; r++) {
					// make sure we wrap around from bottom to top
					int realr = r;
					if (r == -1)
						realr = currGen.length - 1;
					if (r == currGen.length)
						realr = 0;

					for (int j = i - 1; j <= i + 1; j++) {

						// make sure we wrap around from left to right
						int realj = j;
						if (j == -1)
							realj = currGen[row].length() - 1;
						if (j == currGen[row].length())
							realj = 0;

						if (r == row && j == i)
							continue; // current cell is not its neighbor
						if (currGen[realr].charAt(realj) == '#')
							neighbors++;
					}
				}
				if (current == '#')
					if (neighbors < 2 || neighbors > 3)
						newGen[row] += " ";
					else
						newGen[row] += "#";
				if (current == ' ')
					if (neighbors == 3)
						newGen[row] += "#";
					else
						newGen[row] += " ";

			}
		}	
	}
}
