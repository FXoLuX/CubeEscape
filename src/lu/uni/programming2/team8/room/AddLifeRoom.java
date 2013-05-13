package lu.uni.programming2.team8.room;

import lu.uni.programming2.team8.cubeescape.Riddle;
import lu.uni.programming2.team8.entity.Player;

/**
 *
 * @author Patrick, FX
 */
public class AddLifeRoom extends MyRoom {

    /**
	 * This ID is needed for serialization and deserialization
     */
    private static final long serialVersionUID = -3083081445700654498L;


    @Override
    public void enter(Player player) throws Exception {
        if (!visited) {
            scriptdescription = getRoomEnterString();
            visited = true;
        }
        
        System.out.println(scriptdescription + "\nCurrent State: "
                + ((done) ? " DONE" : "TODO" + "."));
        
        if (!done) {

            // chose riddle
            Riddle r = Riddle.getRandomRiddle();

            try {
                boolean solved = r.execute();
                if (solved) {
                    player.increaseLife();
                }
                done = true;
            } catch (Riddle.RiddleSkipException e) {
            }            
        }
    }
}
