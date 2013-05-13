package lu.uni.programming2.team8.entity;

import java.util.ArrayList;

import lu.uni.programming2.team8.cubeescape.Item;
import lu.uni.programming2.team8.cubeescape.Riddle;
import lu.uni.programming2.team8.gameEngine.MyCommand;
import lu.uni.programming2.team8.gameEngine.MyParser;
import lu.uni.programming2.team8.room.MyRoom;
import lu.uni.programming2.team8.room.MyRoom.Roomtype;

/**
 * This class is a bot, that can react to riddle and fight
 */
/**
 * 
 * @author FX, Patrick
 *
 */
public class MyBot {
	
	private static MyBot myBot;
	
	//	INSTANCE VARIABLES
	private boolean hasBotBeenActivated = false;
	private boolean isActive = false;
	private Player player;
	private Roomtype neededRoom;

	
	//	CONSTRUCTOR
	/**
	 * We don't want to let the possibility to create several bot open
	 * @param player
	 */
	private MyBot(Player player) {
		hasBotBeenActivated = false;
		isActive = false;
		this.player = player;
	}
	
	public static MyBot getMyBot() {
		if (myBot == null) {
			myBot = new MyBot(new Player("Chuck Norris", new ArrayList<Item>(), Integer.MIN_VALUE));
		}
		return myBot;
	}
	
	// 	METHODS
	//	Setter/Getter
	public boolean hasBotBeenActivated() {
		return hasBotBeenActivated;
	}
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
		if (isActive) {
			hasBotBeenActivated = true;
		}
	}

	//	Other Methods
	
	/**
	 * This method initialize
	 * @param player
	 */
	public static void bootBot(Player player) {
		if (myBot == null) {
			myBot = new MyBot(player);
		}
	}
	
	
	/**
	 * This method allows our bot to type a String in the console, just lika a normal player
	 * @param a String to input
	 */
	public void writeInConsole(String a) {
		System.out.println(a);
	}
	
	/**
	 * Our bot has no fear, it never runs away from a Riddle!
	 * This method is called only when the bot is prompt for trying a riddle.
	 * The bot can never avoid a riddle, then the method always returns "solve".
	 * 
	 * @return solve
	 */
	public String startRiddle() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("solve");
		System.out.println();
		return "solve";
	}
	
	/**
	 * Our bot knows the answer of the Riddle!
	 * This method is called only when the bot is prompt for answering a riddle
	 * 
	 * @return a string with the right answer or "I don't know" ater 5 tries
	 */
	public String reactToRiddle(Riddle riddle) {
		for (int i = 0; i < 6; i++) {
			if ((int) (Math.random()*10) > 6) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println();
				System.out.println(riddle.getAnswer());
				System.out.println();
				return riddle.getAnswer();
			} 
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("I don't know, that riddle's too hard for me. Maybe you should try yourself.");
		System.out.println();
		return "I don't know, that riddle's too hard for me. Maybe you should try yourself.";
	}


	/**
	 * Our bot has no fear, it never runs away from a fight!
	 * 
	 * This method is called only when the bot is prompt for starting a fight or flee.
	 * The bot always send yes and can not avoid the fight.
	 * 
	 * @return yes
	 */
	public String startFight() {	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("yes");
		System.out.println();
		return "yes";
	}
	
	
	/**
	 * Our bot knows which char is expected, but has only 70% of chance to
	 * answer correctly.
	 * 
	 * @param expectedChar
	 * @return
	 */
	public String reactToFight(char expectedChar) {
		if ((int) (Math.random()*10) > 2) {
			System.out.println(expectedChar);
			return expectedChar + " ";
		}
		System.out.println("?");
		return "?";
	}

	
	/**
	 * Our bot knows which object to use to deactivate any trap!
	 * This method find the right object needed to deactivate the trap, and if it is present
	 * in the inventory deactivates it. Otherwise the bot flee (but this should never happen, 
	 * as the bot use the crawler to find all the needed object before entering a TrapRoom.
	 * 
	 * @return the command to use the item needed to deactivate the trap
	 */
	public String deactivateTrap(MyRoom room){
        Item neededItem = MyRoom.getAntitrapRewardRelation(MyRoom.getTrapAntitrapRelation(room.getRoomType()));
        		
		if (player.getInventory().contains(neededItem)) {
			System.out.println("use " + neededItem.getName());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println();
			return "use neededItem.getName()";
		} else {
			System.out.println();
			System.out.println("flee");
			System.out.println();
			return "flee";
		}
	}
	
	/**
	 * This method send no when the bot is prompt to save the game.
	 * Saving the game is a human player privilege!
	 * 
	 * @return "no"
	 */
	public String reactToSave() {
		System.out.println();
		return "no";
	}
	
	/**
	 * This method find the right direction from the current to the next with, if needed, a recursive call of the crawler.
	 * @return the command = the direction the bot must take
	 */	
	public MyCommand getMovingCommand(MyRoom currentRoom){

		ArrayList<MyRoom> pathToExit = MyCrawler.findShortestPath(currentRoom, Roomtype.EXIT);
		ArrayList<MyRoom> pathToAntitrapRoom = null;
		
		if (currentRoom.getRoomType() == neededRoom) {
			neededRoom = null;
		}
		
		// if the neededRoom is empty, it means we are not looking for a specific object, 
		// so we can look for the shortest path to the exit
		if (neededRoom == null) {
			if (MyRoom.getTrapAntitrapRelation(pathToExit.get(1).getRoomType()) != null) {
				Roomtype antitrap = MyRoom.getTrapAntitrapRelation(pathToExit.get(1).getRoomType());
				pathToAntitrapRoom = MyCrawler.findShortestPath(currentRoom, antitrap);
				
				antitrap = MyRoom.getTrapAntitrapRelation(pathToAntitrapRoom.get(1).getRoomType());
				neededRoom = MyRoom.getTrapAntitrapRelation(pathToAntitrapRoom.get(1).getRoomType());
				
				while (MyRoom.getTrapAntitrapRelation(pathToAntitrapRoom.get(1).getRoomType()) != null) {
					pathToAntitrapRoom = MyCrawler.findShortestPath(currentRoom, antitrap);
				}		
				return (MyParser.getCommandList().get(MyRoom.getDirection(currentRoom, pathToAntitrapRoom.get(1))));
			}
			return (MyParser.getCommandList().get(MyRoom.getDirection(currentRoom, pathToExit.get(1))));
			
		} else {
			pathToAntitrapRoom = MyCrawler.findShortestPath(currentRoom, neededRoom);			
			return (MyParser.getCommandList().get(MyRoom.getDirection(currentRoom, pathToAntitrapRoom.get(1))));
		}
		
		
		
	}
	
	
	/**
	 * This methods will make our bot act strangely, like talking to the player, telling jokes
	 * or even commit a suicide!
	 * 
	 * Why this method would you ask? because it's fun of course!
	 */
	public void randomBehavior() {
		int randomNumber = (int) (Math.random() * 1000);
		
		
		if (randomNumber > 995) {
			System.out.println("I'm so bored of living. I commit a suicide!");
			isActive = true;
		}
		
		if (randomNumber < 50) {
			System.out.println("I'm really wondering how long should I play instead of that lazy player...??");
		}
		
		if (randomNumber < 200 || randomNumber > 150) {
			System.out.println("- Knock! Knock! ...... Is anybody out there?");
			System.out.println("- Who's there?");
			System.out.println("...");
			System.out.println("(very long pause)");
			System.out.println("...");
			System.out.println("- Java!");
		}
		
		if (randomNumber < 300 || randomNumber > 250) {
			System.out.println("- Do you know how to obtain a really random String?");
			System.out.println("- Put a freshman computer science student in front of a vim terminal and tell him to save and quit!");
		}
		
		if (randomNumber < 400 || randomNumber > 350) {
			System.out.println("I know a good joke about the GNOME text editor, but it's ok, you might not gedit.");
		}
		
		if (randomNumber < 450 || randomNumber > 400) {
			System.out.println("When Chuck Norris throws an exception, it's across the room.");
		}
	}
}
