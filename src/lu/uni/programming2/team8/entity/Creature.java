package lu.uni.programming2.team8.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import lu.uni.programming2.team8.cubeescape.Item;
import lu.uni.programming2.team8.gameEngine.CubeEscape;
import lu.uni.programming2.team8.gameEngine.Timer;
import lu.uni.programming2.team8.gameEngine.MapGenerator.Difficulty;
import lu.uni.programming2.team8.room.TrapRoom;

/**
 *
 * @author FX
 */

/**
 * This class holds information about Creature that the player might see in the cube.
 * A creature has only 1 life, its number of lifepoints is related to the difficulty level of the game.
 * It also has a speed, that determine its velocity during fights.
 *
 */
public class Creature extends Entity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5868233054706589095L;
	
	
	// INSTANCE VARIABLES
    int speed;
    Random random;
    Difficulty diff;

    // CONSTRUCTOR
    public Creature(String name, int lifePoints, ArrayList<Item> inventory,
            Difficulty diff) {
        super(name, diff.creatureLifePoints, inventory);                     
        this.diff = diff;
        this.speed = diff.creatureSpeed;
        random = new Random();
        
        addToInventory(generateRandomInventory(diff));
    }
    

    // METHODS
    // Setter/Getter
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    
    /**
     * If the player kills a Creature, he will get some item in reward (the number of items is related to the difficulty level of the game).
     * @param diff
     * @return
     */
    private ArrayList<Item> generateRandomInventory(Difficulty diff) {
        HashMap<Difficulty, Integer> relation_difficulty_amount = new HashMap<Difficulty, Integer>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1863883940380073727L;

            {
                put(Difficulty.EASY, 4);
                put(Difficulty.MEDIUM, 3);
                put(Difficulty.HARD, 2);
            }
        };
        ArrayList<Item> reward = new ArrayList<Item>();
        int amount_of_reward = (int) (Math.random() * relation_difficulty_amount
                .get(diff));

        for (int i = 0; i < amount_of_reward; i++) {
            reward.add(Item.ITEMLIST[(int) (Math.random() * Item.ITEMLIST.length)]);
        }

        return reward;

    }

    // Other Methods
    
    /**
     * 
     * This method is the fight engine.
     * We display a randomly generated array of char, one by one, and the first to press that letter, the Creature or the Player, hits the other.
     * We use here the speed of the Creature as a parameter for the Timer.
     * 
     * @param player
     * @return
     * @throws TrapRoom.LifeLossException
     * @throws TrapRoom.PlayerDeathException
     */
    public ArrayList<Item> attack(Player player) throws TrapRoom.LifeLossException,
            TrapRoom.PlayerDeathException {

        char[] fightChars = generateFightChars();
        int i = 0;
        int startlife = player.getLife();       
      
        do {
			System.out.println();
            System.out.println(fightChars[i]);
            Timer fightTimer = new Timer(diff.creatureSpeed);
            try {
            	String playerInput;
            	
            	// here the Robot takes the relay if it is currently active
				if (CubeEscape.myRobot != null && CubeEscape.myRobot.isActive()) {
					playerInput = CubeEscape.myRobot.reactToFight(fightChars[i]);
				} else {
					playerInput = CubeEscape.sc.nextLine();
				}      	
            	
				fightTimer.setStopTimeCounter(true);
				
				if (playerInput.isEmpty()) {
					System.out.println();
					System.out.println("Are you trying to avoid the fight?");
					System.out.println("You shouldn't!");
					player.decreaseLifePoints(10);
					System.out.println("Your lifepoints: " + player.getLifePoints() + "\t\t Creature lifepoints: " + this.getLifePoints());
					continue;
				}

				char playerChar = playerInput.charAt(0);
				if (fightTimer.getTimerEnded()) {
					System.out.println();
					System.out.println("Too slow!");
					player.decreaseLifePoints(10);
					System.out.println("Your lifepoints: " + player.getLifePoints() + "\t\t Creature lifepoints: " + this.getLifePoints());
				} else {
					if (playerChar == fightChars[i]) {
						System.out.println();
						System.out
								.println("You hit first, the creature loose 10 hitpoints!");
						this.decreaseLifePoints(10);
						System.out.println("Your lifepoints: " + player.getLifePoints() + "\t\t Creature lifepoints: " + this.getLifePoints());

						Thread.sleep(500);
					} else if (playerInput == "") {
						System.out.println();
						System.out.println("Are you trying to avoid the fight?");
					} else {
						System.out.println();
						System.out.println("Wrong!");
						player.decreaseLifePoints(10);
						System.out.println("Your lifepoints: " + player.getLifePoints() + "\t\t Creature lifepoints: " + this.getLifePoints());
						Thread.sleep(500);
					}
				}

			} catch (InterruptedException e) {
                System.out
                        .println("Something mysterious happened. Interruped Exception!"
                        + e.getMessage());
                fightTimer.setStopTimeCounter(true);
            }                       
            i++;
        } while ((startlife == player.getLife()) && (this.getLifePoints() > 0));
        
        
        //normally you close a stream after using. But somehow, if we close the
        //stream System.in for the here created BufferReader object, the stream
        //for the BufferReader in MyParser will also be closed. No idee why.
        //if you know an explanation please tell us.
        
        if (player.getLife() != startlife) {
            if (player.getLife() == 0) {                
                throw new TrapRoom.PlayerDeathException("The creature killed you. You are dead!");
                
            } else {                
                throw new TrapRoom.LifeLossException("The creature killed you. Be faster next time! You just lost one life.");
            }
        } else {
        	System.out.println();
            System.out.println("Congratulations, you're a real warrior... or savage!");
            System.out.println("You ransack the creature's deadbody for some useful items... Look to your inventory to see what you got.");            
            return super.getInventory();
        }

    }

    /**
     * This method generate randomly an array of characters, with length equals to the number of life points of the player and the Creature, divided by ten.
     * We don't need more characters.
     *
     * @return
     */
    public char[] generateFightChars() {
        char[] fightChars = new char[10 + (super.lifePoints / 10)];

        for (int i = 0; i < fightChars.length; i++) {
            fightChars[i] = (char) (random.nextInt(26) + 97);
        }

        return fightChars;
    }
}
