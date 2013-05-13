package lu.uni.programming2.team8.entity;

/**
 * This class holds all information about the human player.
 * In details, we store here player's name, lives, lifepoints and inventory.
 */

import java.util.ArrayList;

import lu.uni.programming2.team8.cubeescape.Item;

/**
 * 
 * @author FX
 */

public class Player extends Entity {
	/**
	 * This ID is needed for serialization and deserialization
	 */
	private static final long serialVersionUID = -1652073202544481636L;
	
	// INSTANCE VARIABLES
	protected int life;

	// CONSTRUCTOR
	public Player(String name, ArrayList<Item> inventory, int life) {
		super(name, 100, inventory); // a player has always 100 hitpoints
		this.life = life;
	}
	
	
	// METHODS
	// Setter/Getter
	public int getLife() {
		return life;
	}

	// Other Methods
	// self-explanatory methods, will be used in interactions with creatures and traps
	// (fights)
	@Override
	public void decreaseLifePoints(int points) {
		this.lifePoints -= points;
		int x = (int) (3.0 * Math.random()) + 1;
		if (x==1) System.out.println("You were bitten on the thumb for 10 hitpoints.\n It's nothing but a scratch. -10 hitpoints");
		else if (x==2) System.out.println("You were hit to the groin for 10 hitpoints.\n The pain! It's excruciating!. -10 hitpoints");
		else if (x==3)System.out.println("You lost your balance and fell on the floor.\n No dice! -10 hitpoints for you.");
		else System.out.println("You've just lost 10 hitpoints.\n It's nothing but a scratch.");
		if (this.lifePoints <= 0) {
			decreaseLife();
		}
	}

	public void decreaseLife() {
		this.life--;
		this.lifePoints = 100;
		int x = (int) (3.0 * Math.random()) + 1;
		if (x==1) System.out.println("For a glimpse of the moment your whole life passed in front of your eyes.");
		else if (x==2) System.out.println("You've just realised that you are not a cat.");
		else if (x==3) System.out.println("That's gonna leave a scar for a very long time.");
		else System.out.print("It hurts. ");
		System.out.print("You've lost 1 life.\n");
		if (this.getLife() > 1) {
			System.out.println("You still have: " + this.getLife() + " lives.");
		} else {
			System.out.println("You still have: " + this.getLife() + " life.");

		}
	}

	public void increaseLife() {
		this.life++;
	}

}
