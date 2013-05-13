package lu.uni.programming2.team8.room;

import lu.uni.programming2.team8.cubeescape.IO;
import lu.uni.programming2.team8.cubeescape.Riddle;
import lu.uni.programming2.team8.entity.Player;
import lu.uni.programming2.team8.gameEngine.CubeEscape;
import lu.uni.programming2.team8.gameEngine.MapGenerator.Difficulty;

/**
 *
 * @author Patrick, FX
 */
public class SaveRoom extends MyRoom {

    /**
	 * This ID is needed for serialization and deserialization
     */
    private static final long serialVersionUID = -9215616782794945922L;
    private boolean available = false; 	// If the riddle is correctly solved
    									// it becomes available for one use
    private Difficulty diff;

    public SaveRoom(Difficulty diff) {
    	super();
        this.diff = diff;
    }

    @Override
    public void enter(Player player) throws Exception {

        if (!visited) {
            scriptdescription = getRoomEnterString();
            visited = true;            
        }
        
        System.out.println( scriptdescription+ "\nCurrent state: "
                    + ((done) ? " DONE" : "TODO" + "."));

        // start riddle
        if (!done) {
            // chose riddle
            Riddle r = Riddle.getRandomRiddle();

            try {
                boolean solved = r.execute();
                if (solved) {
                    available = true;
                }
                done = true;
            } catch (Riddle.RiddleSkipException e) {
            }
        }

        // want to save?
        if (available) {
            System.out
                    .println("You can save your game in this room."
                    + " You can save in this room one time, but you can come "
                    + "back later, if you want.\n Do you want to save now? (y/n)");

            boolean notunderstood = true;
            String input;

            while (notunderstood) {
            	
            	// here the Robot takes the relay if it is currently active
				if (CubeEscape.myRobot != null && CubeEscape.myRobot.isActive()) {
	            	input = CubeEscape.myRobot.reactToSave();
				} else {
					input = CubeEscape.sc.nextLine();
				}
            	
                if (input.equalsIgnoreCase("y")
                        || input.equalsIgnoreCase("yes")) {
                    notunderstood = false;

                    System.out
                            .println("Congratulations! You've won the right to save the current state of the game.");
                    System.out
                            .println("After saving, you can quit the game for a pina-colada and come back later to finish it!");
                    System.out
                            .println("Please enter the name of your save to find it back more easily later");

                    String input2 = CubeEscape.sc.nextLine();

                    IO.saveGame(this, player, diff, input2);
                } else if (input.equalsIgnoreCase("n")
                        || input.equalsIgnoreCase("no")) {

                    notunderstood = false;
                }
            }
        }

    }
}
