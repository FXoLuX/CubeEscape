package lu.uni.programming2.team8.room;

import java.util.Date;
import lu.uni.programming2.team8.cubeescape.Item;
import lu.uni.programming2.team8.entity.Player;
import lu.uni.programming2.team8.gameEngine.CubeEscape;
import lu.uni.programming2.team8.gameEngine.MapGenerator;
import lu.uni.programming2.team8.gameEngine.MapGenerator.Difficulty;

/**
 *
 * @author Patrick
 */
public class TrapRoom extends MyRoom {

    /**
     * This ID is needed for serialization and deserialization
     */
    private static final long serialVersionUID = 551804268647137251L;

    public static class LifeLossException extends Exception {

        private static final long serialVersionUID = 1L;

        public LifeLossException(String msg) {
            super(msg);
        }
    }

    public static class PlayerDeathException extends Exception {

        private static final long serialVersionUID = 1L;

        public PlayerDeathException(String msg) {
            super(msg);
        }
    }

    public static class SkipException extends Exception {

        private static final long serialVersionUID = 1L;

        public SkipException(String msg) {
            super(msg);
        }
    }
    
    private Difficulty diff = MapGenerator.Difficulty.EASY;

    public TrapRoom(Difficulty diff) {
        super();
        this.diff = diff;
    }

    @Override
    public void enter(Player player) throws LifeLossException,
            PlayerDeathException, SkipException {

        if (!visited) {
            scriptdescription = getRoomEnterString();
            visited = true;
        }

        System.out.println("You've entered a(n) "
                + TRAP_OUTPUTNAME_RELATION.get(rtype) + ".\n " + scriptdescription + ". Current state: "
                + ((done) ? "disarmed.\n" : "active.\n"));


        if (!done) {
            System.out.println("What do you want to do? \"flee\" or \"use <itemname>\"");            

            long starttime = (new Date()).getTime();
            boolean stilltime = true;
            String input;

            while (stilltime && (!this.done)) {
            	
            	// here the Robot takes the relay if it is currently active
				if (CubeEscape.myRobot != null && CubeEscape.myRobot.isActive()) {
	            	input = CubeEscape.myRobot.deactivateTrap(this);
				} else {
					input = CubeEscape.sc.nextLine();
				}
            	
                input = input.toLowerCase();
                
                //too slow
                if ((new Date()).getTime() - starttime > diff.timeTillTrapKills) {
                    System.out.println("\nYou were to slowly, the trap got you.");
                    stilltime = false;
                    player.decreaseLife();
                    if (player.getLife() <= 0) {
                        throw new PlayerDeathException("It kills you.");
                    } else {
                        throw new LifeLossException("");
                    }
                }

                //process input
                if (input.equals("flee") || input.equals("f")) {                    
                    throw new SkipException("\nYou fled. You coward!");
                    
                } else if (input.startsWith("use ")) {                    
                    input = input.replaceFirst("use ", "");
                    disableTrap(player, input);
                } else if (input.startsWith("u ")) {                    
                    input = input.replaceFirst("u ", "");
                    disableTrap(player, input);
                    
                } else {
                    System.out.println("No valid input. Please use: \"flee\" or \"use <itemname>\"");
                }
                
                
            }


        }
    }

    /**
     * Tries to disable the trap. If not possible It prints what the problem is
     * @param player playing plaer
     * @param input parameter of the players use command     
     */
    private void disableTrap(Player player, String input) {
        Item needed = ANTITRAP_REWARD_RELATION.get(TRAP_ANTITRAP_RELATION.get(rtype));
        Item choosen = null;
        
        //find item, the player wants to use
        for(Item i : Item.ITEMLIST){
            if(i.getName().equalsIgnoreCase(input)){
                choosen = i;
                break;
            }
        }
        
        if (choosen == null){
            System.out.println("There is no such item called "+ input);
            return;
        }
        
        if (player.getInventory().contains(choosen)){
            if (needed == choosen){
                
            player.getInventory().remove(choosen);
            done = true;
            System.out.println("You've used " + choosen.getName()
                    + " for the trap. It's removed from your inventory and "
                    + "the trap is disarmed.");
            
            } else {
                System.out.println("It seems that using "+ choosen.getName()+" has no effect on this trap.");
            }
            
            //not in inventory
        } else {
            System.out.println("You have no "+ choosen.getName() + " in you inventory.");
        }   
    }
}
