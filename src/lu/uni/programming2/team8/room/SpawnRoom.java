package lu.uni.programming2.team8.room;

import lu.uni.programming2.team8.entity.Player;

/**
 *
 * @author Patrick
 */
public class SpawnRoom extends MyRoom {

    /**
	 * This ID is needed for serialization and deserialization
     */
    private static final long serialVersionUID = 1309644555914898677L;

    
    @Override
    public void enter(Player player) throws Exception {
        if (!visited) {
            scriptdescription = getRoomEnterString();
            visited = true;
            done = true;
            System.out.println(scriptdescription);
        } else {
            System.out.println("You woke up here not long ago. Nothing's changed.");
        }
        //System.out.println("You've entered a SpawnRoom"	+ ((done) ? " DONE" : "TO DO"));
    }
}
