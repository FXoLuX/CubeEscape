package lu.uni.programming2.team8.cubeescape;

import java.io.Serializable;

/**
 * 
 * @author FX
 */
/**
 * This class compiles all data about the player: its name, number of games played and won, its highscore and total playing time.
 * It is Serializable to let us save it, then the Player can later retrieve his/her own profile, and keep
 */
public class Profile implements Serializable {

	/**
	 * This ID is needed for serialization and deserialization
	 */
	private static final long serialVersionUID = -5562370282688014519L;
	
	// INSTANCE VARIABLES
	private String playerName;
	private int nbOfGamesPlayed = 0;
	private int nbOfGamesSolved = 0;
	private Score highscore;
	private long timeOfPlay;
    private boolean active = false;

    
	// CONSTRUCTOR
	public Profile(String name) {
		this.playerName = name;
	}
	
		
	// METHODS
	// Setter/Getter
	public String getPlayerName() {
		return playerName;
	}

	public int getNbOfGamesPlayed() {
		return nbOfGamesPlayed;
	}

	public int getNbOfGamesSolved() {
		return nbOfGamesSolved;
	}

	public Score getHighscore() {
		return highscore;
	}

	public long getTimeOfPlay() {
		return timeOfPlay;
	}

    public boolean getActive(){
        return this.active;
    }
    
	public void setTimeOfPlay(long time) {
		timeOfPlay = time;
	}
	
	public void setHighscore(Score newScore) {
		if (this.highscore.getScore() < newScore.getScore()) {
			this.highscore = newScore;
		}
	}
        
        /**
         * Sets the parameter active (true if this profile object is in use)
         * on the given value
         * @param active true = in use; false = not in use
         */
        public void setActive(boolean active){
            this.active = active;
        }
	
	// Other Methods
	public void addTime(long time) {
		this.timeOfPlay += time;
	}

	public void increaseNbOfGamesPlayed() {
		this.nbOfGamesPlayed++;
	}

	public void increaseNbOfGamesSolved() {
		this.nbOfGamesSolved++;
	}
	
	public void displayProfile() {
		System.out.println("Profile information.");
		System.out.println();
		System.out.println("Player name: " + this.getPlayerName());
		System.out.println("Number of games played: " + this.getNbOfGamesPlayed());
		System.out.println("Number of games won: " + this.getNbOfGamesSolved());
		System.out.println("Total playing time: " + this.getTimeOfPlay());
		System.out.println("Highscore: " + this.getHighscore().getScore());
	}

}
