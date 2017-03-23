/*
 * Authors: Muriel Brunet, Isaiah Mann 
 * Description: A single thread to work on the blocking queues
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GameOfLifeThread extends Thread {
	// A shared list of all thread ids
	ArrayList<Long> threadIds;
	
	// A shared lookup for each thread's 
	HashMap<Long, BlockingQueue<Long>> lookup;
	
	// This threads blocking queue (which it uses to sync work with other threads)
	BlockingQueue<Long> workerQueue;
	
	// The rows the thread is responsible for calculating
	int firstRow;
	int lastRow;
	
	// How many generations the program should run for (shared between all threads)
	int numGen;
	
	// These pointer are swapped to prevent additional memory overhead of allocating new String[] vars
	String[] newGen;
	String[] currGen;
	
	// Raise this flag to halt the operations within the run loop
	boolean killFlag = false;
	
	public GameOfLifeThread(
			int numThreads, ArrayList<Long> threadIds,
			HashMap<Long, BlockingQueue<Long>> lookup,
			int firstRow, int lastRow, int numGen,
			String[] newGen, String[] currGen) {
		// Tracks its own id in the list
		this.threadIds = threadIds;
		this.threadIds.add(this.getId());
		
		// Tracks its queue in the list associated with its id
		this.workerQueue = new ArrayBlockingQueue<Long>(numThreads);
		this.lookup = lookup;
		this.lookup.put(this.getId(), this.workerQueue);
		
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.numGen = numGen;
		
		this.newGen = newGen;
		this.currGen = currGen;
	}

	@Override
	public void run() {
		for(int i = 0; i < this.numGen; i++) {
			// Check if the manager thread has ended
			if(killFlag) {
				break;
			}
			// Runs the calculations based on the current generation
			calculateNextGeneration();
			// Sync calculating generations
			synchronize();
			switchArrayPointers();
			// Sync swapping references to arrays (updated to new generation)
			synchronize();
		}
		cleanup();
	}

	// Public mutator to set the killFlag, which effectively ends the run() method
	public void kill() {
		this.killFlag = true;
		interrupt();
	}
	
	// A universal indicator of whether this thread is still actively involved in calculations
	void cleanup () {
		this.threadIds.remove(getId());
		this.lookup.remove(getId());
	}
	
	void synchronize () {
		// Need to check for killFlag before each blocking call to avoid deadlock
		if(killFlag) {
			return;
		}
		try {
			// Sends flag to the other thread to indicate it's done w/ generation
			for(Long id : this.threadIds) {
				if(!lookup.get(id).contains(this.getId())) {
					if(killFlag) {
						return;
					}
					// Adds its id to the blocking queue for each thread (including its own)
					lookup.get(id).put(this.getId());
				}
			}
			int flagsReceived = 0;
			// Check threadIds.size() each time in case a thread finishes during this method's execution 
			while(flagsReceived < this.threadIds.size()) {
				if(killFlag) {
					return;
				}
				// Removes the the thread ids until its removed one id for every live thread
				workerQueue.take();
				flagsReceived++;
			}
		} catch(Exception e) {
			cleanup();
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
