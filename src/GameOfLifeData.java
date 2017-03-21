/*
 * Authors: Muriel Brunet, Isaiah Mann 
 * Description: Stores data for game of life
 */

public class GameOfLifeData {

	// Constant to use in the blocking queues
	public static final int DONE_FLAG = 1;
	public static final long CONSOLE_DELAY = 10;
	
	public static String[] newDish = {
        "                                                                                  ",
        "   #                                                                              ",
        " # #                                            ###                               ",
        "  ##                                                                              ",
        "                                                                                  ",
        "                                                      #                           ",
        "                                                    # #                           ",
        "                                                     ##                           ",
        "                                                                                  ",
        "                                                                                  "
    };
	
	public static String[] currDish = {
        "                                                                                  ",
        "   #                                                                              ",
        " # #                                            ###                               ",
        "  ##                                                                              ",
        "                                                                                  ",
        "                                                      #                           ",
        "                                                    # #                           ",
        "                                                     ##                           ",
        "                                                                                  ",
        "                                                                                  "
    };
	
}
