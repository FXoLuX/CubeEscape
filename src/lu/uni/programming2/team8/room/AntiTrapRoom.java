package lu.uni.programming2.team8.room;

import lu.uni.programming2.team8.cubeescape.Riddle;
import lu.uni.programming2.team8.entity.Player;

/**
 *
 * @author Patrick, FX
 */
public class AntiTrapRoom extends MyRoom {

    /**
	 * This ID is needed for serialization and deserialization
     */
    private static final long serialVersionUID = -5233312403193607702L;

  
    @Override
    public void enter(Player player) throws Exception {

        if (!visited) {
            visited = true;
            scriptdescription = super.getRoomEnterString();
        }
        
        System.out
                .println("\n"+scriptdescription + "\n Current state: "
                + ((done) ? " DONE" : "TODO" + "."));
        
        if (!done) {

            // choose riddle
            Riddle r = Riddle.getRandomRiddle();

            try {
                boolean solved = r.execute();
                if (solved) {                    
                    player.getInventory().add(
                            ANTITRAP_REWARD_RELATION.get(rtype));
                    System.out.println("As a reward, you get this item in your inventory: "
                            + ANTITRAP_REWARD_RELATION.get(rtype).getName()
                            + ".");
                }
                done = true;
            } catch (Riddle.RiddleSkipException e) {
            }            
        }
    }
}
