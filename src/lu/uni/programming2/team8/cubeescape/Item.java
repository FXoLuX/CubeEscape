package lu.uni.programming2.team8.cubeescape;

import java.io.Serializable;

/**
 * @author Patrick, FX
 */

/** 
 * This class holds information about items that the Player, or Creature can carry in their inventory. 
 *
 */

public class Item implements Serializable {

	/**
	 * This ID is needed for serialization and deserialization
	 */
	private static final long serialVersionUID = -8492210284068854802L;

	private static int idItemCounter;

	// CONSTANTS USED FOR ITEM MANAGEMENT (public because it is a final
	// constant. e.g. Math.PI)
	public static final Item FIREPROTECTION = new Item("fireproof armor");
	public static final Item ANTIDOTE = new Item("antidote");
	public static final Item MIRROR = new Item("mirror");
	public static final Item RUBBERBOOTS = new Item("rubberboots");
	public static final Item PIZZA = new Item("Pizza");
	public static final Item[] ITEMLIST = { FIREPROTECTION, ANTIDOTE, MIRROR,
			RUBBERBOOTS, PIZZA };
	
	// INSTANCE VARIABLES
	private int idItem;
	private String name;

	// CONSTRUCTOR
	public Item(String name) {
		this.name = name;
		this.idItem = idItemCounter++;
	}

	// METHODS
	// Setter/Getter
	public int getIdItem() {
		return idItem;
	}

	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
