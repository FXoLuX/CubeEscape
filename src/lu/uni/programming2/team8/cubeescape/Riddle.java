/**
 * Riddles class execute riddle, while displaying its text, calling a Timer and
 * waiting for the right answer.
 */
package lu.uni.programming2.team8.cubeescape;

import java.util.ArrayList;
import lu.uni.programming2.team8.entity.Player;
import lu.uni.programming2.team8.gameEngine.CubeEscape;
import lu.uni.programming2.team8.gameEngine.MapGenerator.Difficulty;
import lu.uni.programming2.team8.gameEngine.Timer;

/**
 *
 * @author FX, Patrick
 */
public class Riddle {

    private static ArrayList<Riddle> riddleList = new ArrayList<>();

    /**
     * If the player skips the execution of a riddle this Exception will be
     * thrown
     */
    public class RiddleSkipException extends Exception {

        private static final long serialVersionUID = 1L;

        public RiddleSkipException(String msg) {
            super(msg);
        }
    }
    /**
     * Standard riddle, for test propose, therefore public
     */
    public static final Riddle DEFAULT = new Riddle(
            "Default riddle. Answer \"true\"", "true", "blubb", Difficulty.TUTORIAL,
            Item.PIZZA);
    private static int idRiddleCounter = 0;
    // INSTANCE VARIABLES
    private String riddleText;
    private String answer;
    private String explanation;
    private int timeToSolveRiddle;
    private Item reward;
    Player player;
    private int idRiddle;

    // CONSTRUCTOR
    public Riddle() {
        this.idRiddle = idRiddleCounter++;
    }

    public Riddle(String riddleText, String answer, String explanation,
            Difficulty diff, Item reward) {
        this.idRiddle = idRiddleCounter++;
        this.riddleText = riddleText;
        this.answer = answer;
        this.explanation = explanation;
        this.timeToSolveRiddle = diff.timeToSolveRiddle / 1000;
        this.reward = reward;
    }

    // METHODS
    // Setter/Getter
    public int getIdRiddle() {
        return idRiddle;
    }

    public String getRiddleText() {
        return riddleText;
    }

    public void setRiddleText(String riddleText) {
        this.riddleText = riddleText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        if (explanation != null) {
            return explanation;
        } else {
            return "";
        }
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setTimeToFindAnswer(int timeToSolveRiddle) {
        this.timeToSolveRiddle = timeToSolveRiddle;
    }

    /**
     * Organised the execution of the riddle. Let the player choose whether to
     * start the riddle or skip it
     *
     * @return true if solved (in time) or false if not solved in time
     * @throws cubeescape.Riddle.RiddleSkipException if the player skips the
     * riddle
     */
    public boolean execute() throws RiddleSkipException {

        System.out.println("You face up a new riddle...\nYou can try now to solve it or skip it for the moment. (solve/skip)");
        String input;

        while (true) {
        	
        	// here the Robot takes the relay if it is currently active
			if (CubeEscape.myRobot != null && CubeEscape.myRobot.isActive()) {
            	input = CubeEscape.myRobot.startRiddle();
			} else {
				input = CubeEscape.sc.nextLine();
			}

            //process input
            if (input.equalsIgnoreCase("solve")) {
                return solveRiddle();

            } else if (input.equalsIgnoreCase("skip")) {
                throw new RiddleSkipException("You skipped the riddle. Reenter the room to face the riddle again.");

            } else {
                System.out.println("No valid input. Please insert \"solve\" or \"skip\"");
            }
        }
    }

    /**
     * Organise the solving of a riddle. Displays the riddle and process the
     * answers
     *
     * @return true = solved in time; false = not solved in time
     */
    public boolean solveRiddle() {
        System.out.println("\nYou have " + timeToSolveRiddle / 1000
                + " seconds to solve it.");
        System.out.println(this.riddleText);
        Timer riddleTimer = new Timer(timeToSolveRiddle);

		String playerAnswer;

		while (!riddleTimer.getTimerEnded()) {

			// here the Robot takes the relay if it is currently active
			if (CubeEscape.myRobot != null && CubeEscape.myRobot.isActive()) {
				playerAnswer = CubeEscape.myRobot.reactToRiddle(this);
			} else {
				playerAnswer = CubeEscape.sc.nextLine();
			}

			if (isRightAnswer(playerAnswer)) {
				if (!riddleTimer.getTimerEnded()) {
					System.out.println("Good answer!");
					riddleTimer.setStopTimeCounter(true);
					return true;
				} else {
					System.out.println("Your answer was correct, but to slow.");
					return false;
				}
			} else {
				System.out
                        .println("Wrong answer, try again... Your time is running out!");
            }
        }
        System.out.println("Time is over. You were to slow.");
        return false;
    }

    /**
     * Test whether the answer of the player is correct. The test is not case
     * sensitive,all special characters are removed and the order of the words
     * doesn't matter (because sometimes it's necessary to answer with an
     * unordered list)
     *
     * @param playerAnswer Answer of the player
     * @return true = correct answer; false = wrong answer
     */
    private boolean isRightAnswer(String playerAnswer) {
        String[] panswer = processString(playerAnswer);
        String[] ranswer = processString(this.answer);

        return areProcessedStringsEquale(ranswer, panswer);
    }

    /**
     * edit the input string so that it can be easily compared to other
     * processed Strings
     *
     * @param input
     * @return
     */
    private String[] processString(String input) {
        input = input.toLowerCase();
        String[] words = input.split(" ");
        String[] ret = new String[words.length];
        
        //only numbers a letters
        for (int i = 0; i < words.length; i++) {
            for (char c : words[i].toCharArray()) {
                if((c >47)&&( c <58) || (c >64)&&(c<91) || (c>96)&&(c<123)){
                    ret[i] += c;
                }
            }
        }
        
        return ret;
    }
    
    /**
     * Test whether they contain a similar cleaned up string. Similar means, it 
     * is ok, if b contains more information than a. This is so that if the answer
     * is "monday, tuesday" the player input "monday and tuesday" is still valid.
     * @param a smaller/equal array (minimum words)
     * @param b equal/greater array
     * @return true if similar, otherwise false
     */
    private boolean areProcessedStringsEquale(String[] a, String[] b){
        boolean match;  //stores whether a counterpart is found in the other array
        
        for(int i = 0; i < a.length; i++){
            //skip empty strings
            if(a[i].equals("")){
                continue;
            }
            match = false;
            
            for(int j = 0; j<b.length; j++){
                if(a[i].equalsIgnoreCase(b[j])){
                    b[j] = "";                    
                    match = true;
                    break;
                }
            }
            
            if(match == false){
                return false;
            }
        }
        
        return true;
        
        
    }

    public void rewardPlayer() {
        player.getInventory().add(reward);
    }

    /**
     * Delegate the loading of the riddles
     */
    public static void loadRiddlesFromFile() {
        riddleList = IO.loadRiddle();
    }

    /**
     * Sets for every riddle the timeToSolve variable depending on the current
     * difficulty
     *
     * @param diff
     */
    public static void adjustRiddlesSolveTimeToDifficulty(Difficulty diff) {
        for (Riddle r : riddleList) {
            r.setTimeToFindAnswer(diff.timeToSolveRiddle);
        }
    }

    /**
     * Randomly returns one Riddle from all possible riddles
     *
     * @return
     */
    public static Riddle getRandomRiddle() {
        return riddleList.get((int) (Math.random() * riddleList.size()));
    }
}
