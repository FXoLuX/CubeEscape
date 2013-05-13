package lu.uni.programming2.team8.room;

import lu.uni.programming2.team8.entity.Player;

/**
 *
 * @author Patrick, FX
 */
public class ExitRoom extends MyRoom {

    /**
	 * This ID is needed for serialization and deserialization
     */
    private static final long serialVersionUID = -3394178187958904061L;


    @Override
    public void enter(Player player) throws Exception {
        if (!visited) {
            scriptdescription = super.getRoomEnterString();
            visited = true;
        }
        System.out.println(scriptdescription + "Current state: "
                + ((done) ? " DONE" : "TODO" + "."));
    }
}
