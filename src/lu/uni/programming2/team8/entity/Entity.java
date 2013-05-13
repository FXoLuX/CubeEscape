/**
 * This class is the super class of all living entities of the game. Subclass are
 * Player class and Creature class.
 * An entity has a name, a certain amount of lifepoints and an inventory.
 */
package lu.uni.programming2.team8.entity;

import java.io.Serializable;
import java.util.ArrayList;

import lu.uni.programming2.team8.cubeescape.Item;

/**
 * 
 * @author FX
 */
public class Entity implements Serializable {

	/**
	 * This ID is needed for serialization and deserialization
	 */
	private static final long serialVersionUID = 4578675893990800655L;
	
	private static int idEntityCounter = 0;
	
	// INSTANCE VARIABLES
	protected final int idEntity; // primary key of a creature, no clones in our
									// game
	protected String name;
	protected int lifePoints;
	private ArrayList<Item> inventory;

	// CONSTRUCTOR
	public Entity(String name, int lifePoints, ArrayList<Item> inventory) {
		this.name = name;
		this.lifePoints = lifePoints;
		this.inventory = inventory;
		this.idEntity = idEntityCounter++;
	}

	// METHODS
	// Setter/Getter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIdEntity() {
		return idEntity;
	}

	public int getLifePoints() {
		return lifePoints;
	}

	public ArrayList<Item> getInventory() {
		return inventory;
	}
	
	public void addToInventory(ArrayList<Item> items) {
		this.inventory.addAll(items);
	}

	// Other Methods
	public void decreaseLifePoints(int points) {
		this.lifePoints -= points;
	}

	public void increaseLifePoints(int points) {
		this.lifePoints += points;
	}

}
