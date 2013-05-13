/**
 * This class represents one location (room) in the scenery (map) of the game.
 * It is connected to at most 6 other rooms via exits. The exits are labeled
 * north, east, south, west,top,bottom. For each direction, the room stores a
 * reference to the neighboring room, or "null" if there is no exit in that
 * direction.
 */
package lu.uni.programming2.team8.room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import lu.uni.programming2.team8.cubeescape.Item;
import lu.uni.programming2.team8.entity.Player;
import lu.uni.programming2.team8.gameEngine.MapGenerator;
import lu.uni.programming2.team8.gameEngine.MapGenerator.Difficulty;
import framework.Room;

/**
 *
 * @author Patrick, FX
 */
public class MyRoom extends Room implements Serializable {

    /**
     * This enumeration divides all possible roomtypes. SPAWN will be the
     * starting room, EXIT - the end room, SAVE - save game room, ADDLIFE a room
     * to gain additional life points, and the rest trap or antitrap rooms.
     */
    public static enum Roomtype {

        SPAWN("Spawn"), EXIT("Exit"), SAVE("Saveroom"), EMPTY("Emptyroom"), ADDLIFE("Addliferoom"),
        FIRE("Firetrap"), FIREPROTECTION("Fireprotectionroom"), POISON("Poisontrap"),
        ANTIDOTE("Antidotroom"), ELECTRICITY("Electricitytrap"), RUBBERBOOTS("Rubberbootsroom"),
        RAZOR("Razortrap"), LASER("Lasertrap"), MIRROR("Mirrorroom"), ENTITY("Entityroom");
        private String name;

        Roomtype(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    };
    /**
     * Assigns for each roomtype a list of possible descriptions messages of the
     * room. This descriptions are for a better visualization/imagination of the
     * room.
     */
    private static HashMap<Roomtype, ArrayList<String>> ROOM_ENTER_MESSAGES;
    /**
     *
     * <Trap, Antitrap> assigns an Item Room to a Trap Room e.g. room with an
     * antidote is assigned to poison trap room.
     */
    protected static final HashMap<Roomtype, Roomtype> TRAP_ANTITRAP_RELATION = new HashMap<Roomtype, Roomtype>() {
        private static final long serialVersionUID = 1L;

        {
            put(Roomtype.FIRE, Roomtype.FIREPROTECTION);
            put(Roomtype.POISON, Roomtype.ANTIDOTE);
            put(Roomtype.ELECTRICITY, Roomtype.RUBBERBOOTS);
            put(Roomtype.LASER, Roomtype.MIRROR);
            put(Roomtype.RAZOR, null);
            put(Roomtype.EMPTY, null);
        }
    };
    /**
     * <Antitrap, Item> assigns an antitrap item (which is a reward) to the Item
     * roomtype
     */
    protected static final HashMap<Roomtype, Item> ANTITRAP_REWARD_RELATION = new HashMap<Roomtype, Item>() {
        private static final long serialVersionUID = 1L;

        {
            put(Roomtype.FIREPROTECTION, Item.FIREPROTECTION);
            put(Roomtype.ANTIDOTE, Item.ANTIDOTE);
            put(Roomtype.RUBBERBOOTS, Item.RUBBERBOOTS);
            put(Roomtype.MIRROR, Item.MIRROR);

        }
    };
    /**
     * A part of the roomname depends on the roomtype of the room. This part
     * will be created by multiplying the base (assigned prime number in this
     * HashMap) with prime numbers (which are not contained as values in this
     * HashMap)
     */
    protected static final HashMap<Roomtype, Integer> ROOMTYPE_IDENTIFIER_BASE = new HashMap<Roomtype, Integer>() {
        private static final long serialVersionUID = 1L;

        {
            put(Roomtype.SPAWN, 0);
            put(Roomtype.EXIT, 0);
            put(Roomtype.SAVE, 17);
            put(Roomtype.EMPTY, 19);
            put(Roomtype.ADDLIFE, 23);
            put(Roomtype.FIRE, 29);
            put(Roomtype.FIREPROTECTION, 31);
            put(Roomtype.POISON, 37);
            put(Roomtype.ANTIDOTE, 41);
            put(Roomtype.ELECTRICITY, 43);
            put(Roomtype.RUBBERBOOTS, 47);
            put(Roomtype.LASER, 53);
            put(Roomtype.MIRROR, 59);
            put(Roomtype.RAZOR, 61);
            put(Roomtype.ENTITY, 67);
        }
    };
    protected static final HashMap<Roomtype, String> TRAP_OUTPUTNAME_RELATION = new HashMap<Roomtype, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(Roomtype.FIRE, "fire trap");
            put(Roomtype.POISON, "poison trap");
            put(Roomtype.ELECTRICITY, "electric trap");
            put(Roomtype.LASER, "laser trap");
            put(Roomtype.RAZOR, "razor trap");
        }
    };
    
    /**
   	 * This ID is needed for serialization and deserialization
     */
    private static final long serialVersionUID = 2529498810016818863L;
    private static int idRoomCounter = 0;
    /**
     * Constant stores the String for the directions. It's public because it's a
     * final constant. Cannot be changed. Tested with Math.PI.
     */
    public static final String[] DIRECTION = {"north", "east", "south",
        "west", "top", "bottom"};
    protected int idRoom;
    protected Roomtype rtype = null; // Roomtype of this room
    protected Item requirement;
    protected String name;
    /**
     * On the first entering of a room, the rooms gets a description assigned.
     * This string is stored in scriptdescription.
     */
    protected String scriptdescription;
    /*
     * stores whether the events of this room are done. this means. If it is a
     * trap, whether the player have already got the damage; if a antitrap, he
     * has already got the reward; Save room, he's already saved;... or not
     */
    protected boolean done = false;
    protected boolean visited = false; // used for printing the map

    /*
     * Constructor without a name, because it can't be created before we know
     * which Roomtype the room has.
     * 
     */
    public MyRoom() {
        super("");
        setExits(null, null, null, null, null, null);
    }


    /*
     * Create a room described as "name". Initially, it has no exits.
     */
    public MyRoom(String name) {
        super(name);
        this.name = name;
        setExits(null, null, null, null, null, null);
        this.idRoom = idRoomCounter++;
    }

    /**
     * Define the exits of the current room. Every direction either leads to
     * another room or is "null" (no exit there).
     *
     * @param north room - north to the current room
     * @param east room - east to the current room
     * @param south room - south to the current room
     * @param west room - west to the current room
     * @param top room - above of the current room
     * @param bottom room - under of the current room
     */
    public void setExits(MyRoom north, MyRoom east, MyRoom south, MyRoom west,
            MyRoom top, MyRoom bottom) {
        HashMap<String, Room> exits;
        exits = super.getExits();
        if (north != null) {
            exits.put(DIRECTION[0], north);
        }
        if (east != null) {
            exits.put(DIRECTION[1], east);
        }
        if (south != null) {
            exits.put(DIRECTION[2], south);
        }
        if (west != null) {
            exits.put(DIRECTION[3], west);
        }
        if (top != null) {
            exits.put(DIRECTION[4], top);
        }
        if (bottom != null) {
            exits.put(DIRECTION[5], bottom);
        }
    }

    /**
     * Sets the roomtype of this room to the value of rtype.
     * 
     * @param rtype new roomtype
     */
    public void setRoomType(Roomtype rtype) {
        this.rtype = rtype;
        setRequirement();
    }

    /**
     * Sets the required item depending on the roomtype
     */
    protected void setRequirement() {
        if (TRAP_ANTITRAP_RELATION.containsKey(rtype)) {

            if (ANTITRAP_REWARD_RELATION
                    .containsKey(getTrapAntitrapRelation(rtype))) {
                this.requirement = ANTITRAP_REWARD_RELATION
                        .get(TRAP_ANTITRAP_RELATION.get(rtype));

                // Easter Egg
            } else if (rtype == Roomtype.RAZOR) {
                this.requirement = Item.PIZZA;
            }

        } else {
            requirement = null;
        }
    }

    public Roomtype getRoomType() {
        return this.rtype;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addExit(String direction, Room room) {
        super.getExits().put(direction, room);
    }

    public boolean hasExit(String direction) {
        if (super.getExits().get(direction) != null) {
            return true;
        } else {
            return false;
        }
    }

    public int getNumberOfExits() {
        int count = 0;
        for (Object s : super.getExits().keySet()) {
            if (super.getExits().get(s) != null) {
                count++;
            }
        }
        return count;
    }

    public Item getRequirement() {
        return requirement;
    }

    public void setRequirement(Item requirement) {
        this.requirement = requirement;
    }

    public int getID() {
        return this.idRoom;
    }

    /**
     * Return a string describing the possible exits of this room.
     */
    protected String exitsString() {
        String returnString = "";//Exit(s):

        if (this.getNumberOfExits() == 1) {
            returnString += "Exit: \n";
        } else {
            returnString += "Exits: \n";
        }


        for (Object r : super.getExits().keySet()) {
            MyRoom m = (MyRoom) super.getExits().get(r);

            returnString += "\t" + r.toString() + "\t-  "
                    + m.shortDescription() + "\t- ";

            if (!m.getVisited()) {
                returnString += "Unvisited";
            }else{
                returnString += m.getRoomType().getName() + ((m.getDone())?"(disabled)":"(active)");
            }
            
            returnString += "\n";
        }
        return returnString;
    }

    /**
     * Will be called if the player enters this room.
     *
     * @param player Playerobject
     */
    public void enter(Player player) throws Exception {
        if (!visited) {
            scriptdescription = getRoomEnterString();
            visited = true;
        }

        System.out.println(scriptdescription + "\n");

    }

    /**
     * Returns to a given trap roomtype an antitrap roomtype (the room, where
     * the player can gain the protection item). If there is now Antitrap
     * related to the given trap, null will be returned.
     *
     * @param trap roomtype of the trap
     * @return roomtype of the antitrap
     */
    public static Roomtype getTrapAntitrapRelation(Roomtype trap) {
        if (TRAP_ANTITRAP_RELATION.containsKey(trap)) {
            return TRAP_ANTITRAP_RELATION.get(trap);
        } else {
            return null;
        }
    }

    /**
     * Returns to a given antitrap roomtype the item, which the player will gain
     * in this room(e.g. Fireprotection room -> Item: fireprotection) If the
     * relation is not found, null will be returned.
     *
     * @param antitrap antitrap roomtype the item is looked for
     * @return item correspond to the antitrap.
     */
    public static Item getAntitrapRewardRelation(Roomtype antitrap) {
        if (ANTITRAP_REWARD_RELATION.containsKey(antitrap)) {
            return ANTITRAP_REWARD_RELATION.get(antitrap);
        } else {
            return null;
        }
    }
    /**
     * Returns to a given item the antitrap which give the player this item.
     *
     * @param reward reward for which the antitrap will be returned
     * @return antitrap
     */
    public static Roomtype getAntitrapRewardRelation(Item reward) {
        if (ANTITRAP_REWARD_RELATION.containsValue(reward)) {
            for(Roomtype r: ANTITRAP_REWARD_RELATION.keySet()){
                if(ANTITRAP_REWARD_RELATION.get(r) == reward){
                    return r;
                }
            }   
            return null;
        } else {
            return null;
        }
    }

    /**
     * A part of the roomnumbers will be created depending on the roomtype. This
     * method will returns the numeric prime base for the given roomtype. If
     * there is no entry for the roomtype, 0 will be returned.
     *
     * @param rtype roomtype for which the prime base is searched
     * @return prime base
     */
    public static int getRoomtypeIdentifierBase(Roomtype rtype) {
        if (ROOMTYPE_IDENTIFIER_BASE.containsKey(rtype)) {
            return ROOMTYPE_IDENTIFIER_BASE.get(rtype);
        } else {
            return 0;
        }
    }

    /**
     * Returns to a given antitrap roomtype the item, which the player will gain
     * in this room(e.g. Fireprotection room -> Item: fireprotection) If the
     * relation is not found, null will be returned.
     *
     * @param antitrap antitrap roomtype the item is looked for
     * @return item correspond to the antitrap.
     */
    public static String getTrapOutputnameRelation(Roomtype rtype) {
        if (TRAP_OUTPUTNAME_RELATION.containsKey(rtype)) {
            return TRAP_OUTPUTNAME_RELATION.get(rtype);
        } else {
            return null;
        }
    }

    public MyRoom expandToRoomTypeObject(Difficulty diff) {
        MyRoom out = new MyRoom();
        // Is trap
        if (getTrapOutputnameRelation(getRoomType()) != null) {
            out = new TrapRoom(diff);
            // AntiTrap
        } else if (getAntitrapRewardRelation(getRoomType()) != null) {
            out = new AntiTrapRoom();
        } else if (rtype == Roomtype.SAVE) {
            out = new SaveRoom(diff);
        } else if (rtype == Roomtype.SPAWN) {
            out = new SpawnRoom();
        } else if (rtype == Roomtype.ADDLIFE) {
            out = new AddLifeRoom();
        } else if (rtype == Roomtype.EXIT) {
            out = new ExitRoom();
        } else if (rtype == Roomtype.EMPTY) {
            out = new EmptyRoom();
        } else if(rtype == Roomtype.ENTITY){
            out = new CreatureRoom(diff);
        }

        out.setRequirement(this.getRequirement());
        out.setName(this.shortDescription());
        out.setRoomType(this.getRoomType());

        for (String direction : MyRoom.DIRECTION) {
            if (this.hasExit(direction)) {
                out.addExit(direction, this.nextRoom(direction));
                ((MyRoom) this.nextRoom(direction)).addExit(
                        MapGenerator.getOppositeDirection(direction), out);
            }
        }

        return out;
    }

    public boolean getDone() {
        // is called from the MyParse
        return done;
    }

    public boolean getVisited() {
        // is called from the MyParse
        return visited;
    }

    @Override
    public String longDescription() {
        String temp = "";
        temp += "You are in the room with number " + name + ".\n";
        if (this.getNumberOfExits() == 1) {
            temp += "You see " + this.getNumberOfExits() + " door.\n";
        } else {
            temp += "You see " + this.getNumberOfExits() + " doors.\n";
        }
        temp += exitsString();
        return temp;
    }

    @Override
    public String shortDescription() {
        return name;
    }

    /**
     * Sets the HashMap which relates strings for entering a room with the room
     * types in which they should be printed
     *
     * @param res HashMap which relates entering messages to roomtypes
     */
    public static void setRoomEnterStrings(HashMap<Roomtype, ArrayList<String>> res) {
        ROOM_ENTER_MESSAGES = res;
    }

    /**
     * Randomly choose and return a string which can be printed when you enter
     * this room
     *
     * @return enter message. If no string was found, "You entered an unkown
     * room. Misterious" will be printed
     */
    public String getRoomEnterString() {

        if (ROOM_ENTER_MESSAGES.containsKey(this.rtype)) {
            ArrayList<String> msgs = ROOM_ENTER_MESSAGES.get(this.rtype);

            if (!msgs.isEmpty()) {
                int index = (int) (Math.random() * msgs.size());
                return msgs.get(index);
            }
        }
        return "You entered an unkown room. Mysterious";
    }
    
    /**
     * Calculates and sets for the given name the roomname. Style
     * Roomtype.Path(Y/N).Random
     *
     * @param r - room which gets the name
     * @param path - path from spawn to exit
     */
    public void generateRoomName(){
        String name = ""; // X.Y.Z, each 3 digits. X calculated depending on
        // roomtype
        int[] multi = {2, 3, 5, 7, 11, 13};
        int last, number;

        // Calculating first roomnumber
        number = MyRoom.getRoomtypeIdentifierBase(rtype);
        for (int i = 0; i < 4; i++) {
            last = number;
            number *= multi[(int) (Math.random() * multi.length)];

            if (number >= 1000) {
                number = last;
                break;
            }
        }

        name = String.valueOf(number);
        for (int i = name.length(); i < 3; i++) {
            name = "0" + name;
        }

        name += ".";
        name += String.valueOf((int) (Math.random() * 900 + 100));
        name += "." + String.valueOf((int) (Math.random() * 900 + 100));

        this.setName(name);

    }
    
    /**
     * Determine the direction from the current room to aim.
     * 
     * @param current
     * @param aim
     * @return the direction from current to aim
     */
	public static String getDirection(MyRoom current, MyRoom aim) {
		for(String direction: MyRoom.DIRECTION){
			if(current.hasExit(direction)){
				if(((MyRoom)current.nextRoom(direction)) == aim){
					return direction;
				}
			}
		}
		return null;
	}
	
}
