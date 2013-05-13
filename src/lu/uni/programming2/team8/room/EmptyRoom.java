package lu.uni.programming2.team8.room;

import lu.uni.programming2.team8.entity.Player;

/**
 *
 * @author Patrick, FX
 */
//this is an empty room, as you can see the class is empty
public class EmptyRoom extends MyRoom {

    /**
	  * This ID is needed for serialization and deserialization
     */
    private static final long serialVersionUID = 846596552215625326L;

  
    @Override
    public void enter(Player player) throws Exception {
        if (!visited) {
            scriptdescription = super.getRoomEnterString();            
            visited = true;
            done = true;
            
        }

        System.out.println(scriptdescription);
    }
}
