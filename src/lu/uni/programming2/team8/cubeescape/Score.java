package lu.uni.programming2.team8.cubeescape;

/**
 * This class holds the information about highscore. It calculates the highscore
 * with several parameters, such that the difficulty, the time of play, the
 * number of room visited, etc...
 *
 * It is serializable, to allow us to save Highscore.
 *
 */
import java.io.Serializable;
import java.util.Date;

import lu.uni.programming2.team8.gameEngine.MapGenerator.Difficulty;

/**
 *
 * @author FX
 */
public class Score implements Serializable {

    /**
     * serialVersionUID is used for serialization when saving game
     */
    private static final long serialVersionUID = 6419548098157663887L;
    // INSTANCE VARIABLES
    private long timeOfPlay;
    private int nbOfRoomVisited;
    private Difficulty diff;
    private double score;
    private Date date;
    private boolean won;
    private boolean bot;
    private String name;

    // CONSTRUCTOR
    public Score(String name, long timeOfPlay, int nbOfRoomVisited,
            Difficulty diff, boolean won, boolean bot) {
        this.setName(name);
        this.timeOfPlay = timeOfPlay;
        this.nbOfRoomVisited = nbOfRoomVisited;
        this.diff = diff;
        this.won = won;
        this.bot = bot;
        setDate(new Date());
        calculateScore();

    }

    public Score(String name, long timeOfPlay, int nbOfRoomVisited,
            Difficulty diff, boolean won, Date date) {
        this.setName(name);
        this.timeOfPlay = timeOfPlay;
        this.nbOfRoomVisited = nbOfRoomVisited;
        this.diff = diff;
        setDate(date);
        this.won = won;
        calculateScore();

    }

    // METHODS
    // Setter/Getter
    /**
     * Returns the difficulty level with which this score was created
     *
     * @return difficulty level(easy, medium, hard)
     */
    public Difficulty getDifficulty() {
        return this.diff;
    }

    /**
     * Return the calculated score
     *
     * @return scorevalue
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Return the name of the player who played this score
     *
     * @return playername
     */
    public String getName() {
        return name;
    }

    /**
     * Return the time needed to end the game
     *
     * @return
     */
    public long getTimeOfPlay() {
        return timeOfPlay;
    }

    /**
     * Returns the number of visited rooms during the game
     *
     * @return
     */
    public int getNumberOfVisitedRooms() {
        return nbOfRoomVisited;
    }

    /**
     * Returns the date on which this score was done
     *
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns whether the player found the exit(true) or died in the
     * cube(false;)
     *
     * @return
     */
    public boolean getWon() {
        return won;
    }

    /**
     * Sets the date, on which this score was done
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the name of the player who did the score
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public boolean getBotActivated() {
        return bot;
    }

    // Other Methods
    /**
     * Calculates the score
     */
    private void calculateScore() {
        double gameend = (won) ? 2 : 0.5;
        double botused = (bot) ? 0.5 : 1;

        this.score = gameend * botused * 100000 * diff.cube_length * diff.cube_length
                * diff.cube_length * nbOfRoomVisited / timeOfPlay;

    }

    public void displayScore() {
        System.out.println("Here is your score for this game: " + this.score);
    }
}
