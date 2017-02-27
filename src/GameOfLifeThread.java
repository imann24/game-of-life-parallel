/*
 * Authors: Muriel Brunet, Isaiah Mann 
 * Description: A single thread to work on the blocking queues
 */

import java.util.concurrent.BlockingQueue;

public class GameOfLifeThread extends Thread {
	BlockingQueue<Integer> sendQueue;
	BlockingQueue<Integer> receiveQueue;

	int firstRow;
	int lastRow;

	String[] dish;
	String[] previousDish;

	public GameOfLifeThread(BlockingQueue<Integer> send,
			BlockingQueue<Integer> receive, int firstRow, int lastRow) {

		this.sendQueue = send;
		this.receiveQueue = receive;

		this.firstRow = firstRow;
		this.lastRow = lastRow;

		this.dish = GameOfLifeData.currentDish;
		this.previousDish = GameOfLifeData.previousDish;
	}

	@Override
	public void run() {
		System.out.println("Hello World (thread)");
		System.out.println("Starting from row " + firstRow);
		System.out.println("Ending at row " + lastRow);
	}

	void calculateNextGeneration() {
		for (int row = this.firstRow; row <= this.lastRow; row++) {// each row
			dish[row] = "";
			for (int i = 0; i < dish[row].length(); i++) {// each char in the
															// row
				int neighbors = 0;
				char current = dish[row].charAt(i);

				// loop in a block that is 3x3 around the current cell
				// and count the number of '#' cells.
				for (int r = row - 1; r <= row + 1; r++) {
					// make sure we wrap around from bottom to top
					int realr = r;
					if (r == -1)
						realr = dish.length - 1;
					if (r == previousDish.length)
						realr = 0;

					for (int j = i - 1; j <= i + 1; j++) {

						// make sure we wrap around from left to right
						int realj = j;
						if (j == -1)
							realj = previousDish[row].length() - 1;
						if (j == previousDish[row].length())
							realj = 0;

						if (r == row && j == i)
							continue; // current cell is not its
										// neighbor
						if (previousDish[realr].charAt(realj) == '#')
							neighbors++;
					}
				}
				if (current == '#')
					if (neighbors < 2 || neighbors > 3)
						dish[row] += " ";
					else
						dish[row] += "#";
				if (current == ' ')
					if (neighbors == 3)
						dish[row] += "#";
					else
						dish[row] += " ";

			}
		}
	}

}
