package lu.uni.programming2.team8.room;

import java.util.ArrayList;

import lu.uni.programming2.team8.cubeescape.Item;
import lu.uni.programming2.team8.entity.Creature;
import lu.uni.programming2.team8.entity.Player;
import lu.uni.programming2.team8.gameEngine.CubeEscape;
import lu.uni.programming2.team8.gameEngine.MapGenerator.Difficulty;

/**
 *
 * @author FX
 */
public class CreatureRoom extends MyRoom {

	 /**
	  * This ID is needed for serialization and deserialization
	  */
    private static final long serialVersionUID = 6969353993808130960L;
   
    //	INSTANCE VARIABLES
    protected Creature creature;

    //	CONSTRUCTOR
    public CreatureRoom(Difficulty diff) {
        super();
        this.creature = new Creature("Evil Dog", diff.creatureLifePoints,
                new ArrayList<Item>(), diff);
    }


    //	Other Methods
    @Override
    public void enter(Player player) throws TrapRoom.LifeLossException,
            TrapRoom.PlayerDeathException {
        if (!visited) {
            scriptdescription = super.getRoomEnterString();
            visited = true;
        }

        System.out
                .println(scriptdescription + "\n Current state: "
                + ((done) ? " DONE" : "TODO" + "."));
        if (!done) {

            System.out.println();
            System.out.println();
            System.out.println("A \"creature\" appears.");
            System.out
                    .println("It looks like a human being ... like someone trapped in this cube for a long, long time.");
            System.out
                    .println("The creature is attacking you, you don't know why, but that's you or it...");

            System.out.println("Fight mechanism is quite simple:");
            System.out
                    .println("A char will appear on screen, and you have to type it faster than the creature facing you.");
            System.out.println("(Type the char then enter)");
            System.out.println("Be quick... or die!");
                        
            boolean understood = false;
            String userInput = null;
            do {
				System.out.println("Are you ready? (yes/no)");
				
				// here the Robot takes the relay if it is currently active
				if (CubeEscape.myRobot != null && CubeEscape.myRobot.isActive()) {
	            	userInput = CubeEscape.myRobot.startFight();
				} else {
					userInput = CubeEscape.sc.nextLine();
				}
				
				if (userInput.equalsIgnoreCase("yes") || userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("ready")) {
					understood = true;
				} else if (userInput.equalsIgnoreCase("no") || userInput.equalsIgnoreCase("n")) {
					System.out.println();
					System.out.println("Ok, take your time and stretch your fingers");
				} else {
					System.out.println();
					System.out.println("Answer not understood. Type yes when your ready.");
				}
			} while (!understood);
            
            player.addToInventory(creature.attack(player));

            if (this.creature.getLifePoints() <= 0) {
                done = true;
            }
        } else {
            System.out
                    .println("You think you can smell death here.");
        }


    }
}
