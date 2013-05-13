package lu.uni.programming2.team8.gameEngine;

/**
 * This parser reads user's input and tries to interpret it as a command of the
 * game. Every time its method "getCommand()" is called, it reads a line from
 * the terminal and tries to interpret it as a two words command. It returns the
 * command as an object of class Command. (See constraints about the creation of
 * a Command object in the definition of the class.) Remarks: - the second word
 * is not checked at the moment. - if the user entered more than two words, the
 * rest is ignored.
 *
 * The parser has a set of known (valid) command words.
 */
import framework.Parser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import lu.uni.programming2.team8.cubeescape.Item;
import lu.uni.programming2.team8.entity.MyBot;
import lu.uni.programming2.team8.entity.Player;
import lu.uni.programming2.team8.room.MyRoom;
import lu.uni.programming2.team8.room.TrapRoom;
import lu.uni.programming2.team8.room.TrapRoom.LifeLossException;

/**
 *
 * @author Patrick, FX, Yan
 */
public class MyParser extends Parser {

    /**
     * a constant HashMap that holds all valid command prototypes. If the parser
     * identify an command, it clones the needed prototype, fill the parameters
     * and return the clone.
     */
    private static final HashMap<String, MyCommand> commandList = new HashMap<String, MyCommand>() {
        /**
         *
         */
        private static final long serialVersionUID = -1846305625047729841L;

        {
            put("go", new MyCommand("go", 1,
                    "go <direction>\t\tThe player tries to go <direction>") {
                @Override
                public Object execute(Player player, MyRoom currentRoom)
                        throws Exception {

                    String direction = getFirstParameter();
                    MyRoom nextRoom = (MyRoom) currentRoom.nextRoom(direction);

                    // try to leave current room
                    if (nextRoom == null) {
                        System.out.println("There is no door!");
                    } else {

                        try {

                            nextRoom.enter(player);

                        } catch (                        LifeLossException | TrapRoom.SkipException ex) {
                            System.out.println(ex.getMessage());
                            return currentRoom;
                        }
                    }
                    return nextRoom;
                }
            });

            put("quit", new MyCommand("quit", 0, "quit\t\t\tQuit the game") {
                @Override
                public Object execute(Player player, MyRoom currentRoom) {
                    System.out.println("Do you really want to quit the game? (yes/no)");

                    String input;
                    while (true) {

                        input = CubeEscape.sc.nextLine();
                        if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                            return false;
                        } else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
                            return null;
                        } else {
                            System.out.println("No valid input. Please answer: \"yes\" or \"no\"");
                        }
                    }

                }
            });

            put("exit", new MyCommand("exit", 0, "exit\t\t\tSame as quit") {
                @Override
                public Object execute(Player player, MyRoom currentRoom) {
                    try {
                        return commandList.get("quit").execute(player, currentRoom);
                    } catch (Exception ex) {
                        System.out.println("An error occured while reading. The "
                                + "game is not stopped. If you want to leave, retry, please");
                        return null;
                    }

                }
            });

            put("help", new MyCommand("help", 0,
                    "help\t\t\tPrints a list of all commands") {
                @Override
                public Object execute(Player player, MyRoom currentRoom) {
                    System.out.println("Your available command words are:");

                    for (String command : commandList.keySet()) {
                        System.out.println("\t"
                                + commandList.get(command).getDescription());
                    }
                    System.out.println();

                    return null;
                }
            });

            put("inventory", new MyCommand("inventory", 0,
                    "inventory\t\tPrints the content of your inventory") {
                @Override
                public Object execute(Player player, MyRoom currentRoom) {
                    // lists items and count of them
                    HashMap<Item, Integer> count = new HashMap<>();
                    for (Item i : player.getInventory()) {
                        if (count.containsKey(i)) {
                            count.put(i, count.get(i) + 1);
                        } else {
                            count.put(i, 1);
                        }
                    }

                    String output = "Your inventory contains: ";

                    if (count.isEmpty()) {
                        output += "Nothing! \nYour inventory is empty!";
                    } else {
                        for (Item i : count.keySet()) {
                            output += "\n\t" + count.get(i) + " " + i.getName();
                        }
                    }

                    System.out.println(output);
                    System.out.println();

                    return null;
                }
            });

            put("life", new MyCommand("life", 0,
                    "life\t\t\tShows your current life") {
                @Override
                public Object execute(Player player, MyRoom currentRoom) {
                    System.out.println("You have " + player.getLife()
                            + " life rest.");
                    return null;

                }
            });

            put("hint", new MyCommand("hint", 0,
                    "hint\t\t\tPrints a hint for you. Can be used every 10 minutes.") {
                private String LASTUSE = "LASTUSE"; //Name for the static parameter in MyCommand
                private long INTERVAL = 300000;       //minimum time to reuse hint
                private HashMap<String, Integer> HINT_POSSIBILITY = new HashMap<String, Integer>() {
                    private static final long serialVersionUID = 7433313457929997181L;

                    {
                        put("baseidentifier", 14);
                        put("showunkownroom", 14);
                        put("namesystem", 2);

                    }
                };

                @Override
                public Object execute(Player player, MyRoom currentRoom) {
                    long currenttime = (new Date()).getTime();
                    long lastuse = 0;
                    if (MyCommand.getStaticParameter(LASTUSE) != null) {
                        lastuse = (long) MyCommand.getStaticParameter(LASTUSE);
                    }

                    long timediffence = currenttime - lastuse;

                    //last use is more than 10 minutes ago
                    if (timediffence > INTERVAL) {                        
                        System.out.println(getHint(currentRoom) + "\n");
                        MyCommand.addStaticParameter(LASTUSE, currenttime);

                    } else {

                        System.out.println("You just used \"hint\". Please wait " + ((INTERVAL - timediffence) / 1000) + " seconds.\n");
                    }
                    return null;
                }

                /**
                 * Choose a hint type (show unkown room, show how rooms a
                 * named,...) and create a string for the hint.
                 */
                private String getHint(MyRoom currentRoom) {
                    //calculte the sum of all summands
                    int ptotal = 0;
                    for (String s : HINT_POSSIBILITY.keySet()) {
                        ptotal += HINT_POSSIBILITY.get(s);
                    }

                    //chose an index between 0(inclusive) and sum(exclusive)
                    int choice = (int) (Math.random() * ptotal);

                    //choice will be decrease until it is smaller than 0, then we
                    //found the hint type
                    for (String s : HINT_POSSIBILITY.keySet()) {
                        choice -= HINT_POSSIBILITY.get(s);
                        if (choice < 0) {

                            switch (s) {
                                case "baseidentifier":
                                    int index = (int) (Math.random() * MyRoom.Roomtype.values().length);
                                    return "Hint: A " + MyRoom.Roomtype.values()[index].getName()
                                            + " has as first number a multiple of "
                                            + MyRoom.getRoomtypeIdentifierBase(MyRoom.Roomtype.values()[index]);

                                case "showunkownroom":
                                    return createUnkownRoomsString(currentRoom);


                                case "namesystem":
                                    return "A roomname consists of three numbers. The first number can somehow"
                                            + " show of which kind the room is.";
                            }
                        }
                    }
                    return "";
                }

                private String createUnkownRoomsString(MyRoom currentRoom) {
                    int choose = 2; //how many rooms will be choosen
                    String hint = "Here are the types of " + choose + " unkown rooms. Be careful. Don't forget them.\n";


                    HashMap<MyRoom, String> possible_rooms = findUnkownRooms(currentRoom, new ArrayList<MyRoom>(), "", 0);

                    if (possible_rooms.isEmpty()) {
                        return "There are no unkown rooms near to you";
                    }
                    if (possible_rooms.size() < choose) {
                        choose = possible_rooms.size();
                    }


                    for (int i = 0; i < choose; i++) {
                        //choose value(path). Sadly keys cannot be choosen randomly   
                        String path = (String) possible_rooms.values().toArray()[(int) (Math.random() * possible_rooms.size())];

                        //find key
                        MyRoom key = null;
                        for (MyRoom m : possible_rooms.keySet()) {
                            if (possible_rooms.get(m).equals(path)) {
                                key = m;
                                break;
                            }
                        }

                        if (key == null) {
                            continue;
                        }
                        hint += "\tHere" + path + " to find a " + key.getRoomType().getName() + "\n";
                        possible_rooms.remove(key);

                    }

                    return hint;

                }

                private HashMap<MyRoom, String> findUnkownRooms(MyRoom currentRoom, ArrayList<MyRoom> visited, String path, int depth) {
                    //only searchs within three room 
                    if ((depth < 3) && (!visited.contains(currentRoom))) {
                        visited.add(currentRoom);
                        HashMap<MyRoom, String> output = new HashMap<>();

                        if (!currentRoom.getVisited()) {
                            output.put(currentRoom, path);
                            return output;

                        } else {
                            for (String direction : MyRoom.DIRECTION) {
                                if (currentRoom.hasExit(direction)) {
                                    String newpath = path + "->" + direction;
                                    output.putAll(findUnkownRooms((MyRoom) currentRoom.nextRoom(direction), visited, newpath, depth + 1));
                                }
                            }

                            return output;
                        }
                    } else {
                        return new HashMap<>();
                    }
                }
            });

            put("north", new MyCommand("north", 0,
                    "north\t\t\tThe player tries to go north") {
                @Override
                public Object execute(Player player, MyRoom currentRoom)
                        throws Exception {
                    MyRoom nextRoom = (MyRoom) currentRoom.nextRoom("north");

                    // try to leave current room
                    if (nextRoom == null) {
                        System.out.println("There is no door!");
                    } else {

                        try {

                            nextRoom.enter(player);

                        } catch (                        LifeLossException | TrapRoom.SkipException ex) {
                            System.out.println(ex.getMessage());
                            return currentRoom;
                        }
                    }
                    return nextRoom;

                }
            });

            put("east", new MyCommand("east", 0,
                    "east\t\t\tThe player tries to go east") {
                @Override
                public Object execute(Player player, MyRoom currentRoom)
                        throws Exception {
                    MyRoom nextRoom = (MyRoom) currentRoom.nextRoom("east");

                    // try to leave current room
                    if (nextRoom == null) {
                        System.out.println("There is no door!");
                    } else {

                        try {

                            nextRoom.enter(player);

                        } catch (                        LifeLossException | TrapRoom.SkipException ex) {
                            System.out.println(ex.getMessage());
                            return currentRoom;
                        }
                    }
                    return nextRoom;

                }
            });

            put("south", new MyCommand("south", 0,
                    "south\t\t\tThe player tries to go south") {
                @Override
                public Object execute(Player player, MyRoom currentRoom)
                        throws Exception {
                    MyRoom nextRoom = (MyRoom) currentRoom.nextRoom("south");

                    // try to leave current room
                    if (nextRoom == null) {
                        System.out.println("There is no door!");
                    } else {

                        try {

                            nextRoom.enter(player);

                        } catch (                        LifeLossException | TrapRoom.SkipException ex) {
                            System.out.println(ex.getMessage());
                            return currentRoom;
                        }
                    }
                    return nextRoom;

                }
            });

            put("west", new MyCommand("west", 0,
                    "west\t\t\tThe player tries to go west") {
                @Override
                public Object execute(Player player, MyRoom currentRoom)
                        throws Exception {
                    MyRoom nextRoom = (MyRoom) currentRoom.nextRoom("west");

                    // try to leave current room
                    if (nextRoom == null) {
                        System.out.println("There is no door!");
                    } else {

                        try {

                            nextRoom.enter(player);

                        } catch (                        LifeLossException | TrapRoom.SkipException ex) {
                            System.out.println(ex.getMessage());
                            return currentRoom;
                        }
                    }
                    return nextRoom;

                }
            });

            put("top", new MyCommand("top", 0,
                    "top\t\t\tThe player tries to go top") {
                @Override
                public Object execute(Player player, MyRoom currentRoom)
                        throws Exception {
                    MyRoom nextRoom = (MyRoom) currentRoom.nextRoom("top");

                    // try to leave current room
                    if (nextRoom == null) {
                        System.out.println("There is no door!");
                    } else {

                        try {

                            nextRoom.enter(player);

                        } catch (                        LifeLossException | TrapRoom.SkipException ex) {
                            System.out.println(ex.getMessage());
                            return currentRoom;
                        }
                    }
                    return nextRoom;

                }
            });

            put("bottom", new MyCommand("bottom", 0,
                    "bottom\t\t\tThe player tries to go bottom") {
                @Override
                public Object execute(Player player, MyRoom currentRoom)
                        throws Exception {
                    MyRoom nextRoom = (MyRoom) currentRoom.nextRoom("bottom");

                    // try to leave current room
                    if (nextRoom == null) {
                        System.out.println("There is no door!");
                    } else {

                        try {

                            nextRoom.enter(player);

                        } catch (LifeLossException | TrapRoom.SkipException ex) {
                            System.out.println(ex.getMessage());
                            return currentRoom;
                        }
                    }
                    return nextRoom;

                }
            });
            
            put("bottom", new MyCommand("bottom", 0,
                    "bottom\t\t\tThe player tries to go bottom") {
                @Override
                public Object execute(Player player, MyRoom currentRoom)
                        throws Exception {
                    MyRoom nextRoom = (MyRoom) currentRoom.nextRoom("bottom");

                    // try to leave current room
                    if (nextRoom == null) {
                        System.out.println("There is no door!");
                    } else {

                        try {

                            nextRoom.enter(player);

                        } catch (LifeLossException | TrapRoom.SkipException ex) {
                            System.out.println(ex.getMessage());
                            return currentRoom;
                        }
                    }
                    return nextRoom;

                }
            });

            put("bot", new MyCommand("bot", 1,
                    "bot (de)activate\tActivates/Deactivates the bot") {
                @Override
                public Object execute(Player player, MyRoom currentRoom)
                        throws Exception {

                    if (getFirstParameter().equalsIgnoreCase("activate") || getFirstParameter().equalsIgnoreCase("a")) {
                    	CubeEscape.myRobot = MyBot.getMyBot();
                        MyBot.getMyBot().setActive(true);
                        System.out.println("The bot has been activated");
                    } else if (getFirstParameter().equalsIgnoreCase("deactivate") || getFirstParameter().equalsIgnoreCase("d")) {
                        MyBot.getMyBot().setActive(false);
                        System.out.println("The bot has been deactivated");
                    } else {
                        System.out.println("No valid parameter. Please use as parameter \"activate\" or \"deactivate\"");
                    }
                    return null;

                }
            });

            put("map", new MyCommand("map", 0,
                    "map\t\t\tPrints a map of your environment") {
                @Override
                public Object execute(Player player, MyRoom currentRoom) {
                    System.out.println();
                    System.out.println("You try to recall your surrounding :");

                    if (currentRoom.hasExit("north")) {
                        printRoomLine((MyRoom) currentRoom.nextRoom("north"));
                    }
                    printRoomLine(currentRoom);
                    if (currentRoom.hasExit("south")) {
                        printRoomLine((MyRoom) currentRoom.nextRoom("south"));
                    }
                    System.out
                            .println("^ - door to the top, v - door to the bottom");
                    System.out.println();
                    System.out.println();

                    return null;
                }

                /**
                 * Concatenates 1 room to the String Array.
                 *
                 * @param layout - array of string to which we add room
                 * @param room - room which we add
                 * @return
                 */
                private String[] printRoom(String[] layout, MyRoom room) {
                    String[] blanks = {"###############", "#             #",
                        "#             #", "#             #", "#             #",
                        "#             #", "#             #", "#             #",
                        "###############"};

                    // test room layout, for placing messages
                    // layout[0] += "####### #######";
                    // layout[1] += "#      #      #";
                    // layout[2] += "###    #    ###";
                    // layout[3] += "#      #      #";
                    // layout[4] += "#     ###     #";
                    // layout[5] += "#      #      #";
                    // layout[6] += "###    #    ###";
                    // layout[7] += "#      #      #";
                    // layout[8] += "###############";
                    if (room == null) {
                        // layout = blocks;
                        for (int i = 0; i < layout.length; i++) {
                            layout[i] += "###############";
                        }

                    } else {
                        if (room.getVisited()) {
                            layout[0] += (room.hasExit("north")) ? ("###### ^ ######")
                                    : ("###############");
                            layout[8] += (room.hasExit("south")) ? ("###### v ######")
                                    : ("###############");
                            layout[4] += (room.hasExit("west")) ? ("<") : ("#");
                            layout[3] += (room.hasExit("west")) ? (" ") : ("#");
                            layout[5] += (room.hasExit("west")) ? (" ") : ("#");

                            layout[2] += "# " + room.shortDescription() + " #";

                            if (room.getDone()) {
                                layout[6] += "#   D O N E   #";
                            } else {
                                layout[6] += "#   T O D O   #";
                            }

                            layout[1] += "#             #";
                            layout[3] += "             ";
                            layout[5] += "             ";
                            layout[7] += "#             #";

                            if (room.hasExit("top")) {
                                if (room.hasExit("bottom")) {
                                    layout[4] += "     ^ v     ";

                                } else {
                                    layout[4] += "     ^       ";
                                }
                            } else {
                                if (room.hasExit("bottom")) {
                                    layout[4] += "       v     ";
                                } else {
                                    layout[4] += "             ";
                                }
                            }
                            layout[4] += (room.hasExit("east")) ? (">") : ("#");
                            layout[3] += (room.hasExit("east")) ? (" ") : ("#");
                            layout[5] += (room.hasExit("east")) ? (" ") : ("#");
                        } else {
                            for (int i = 0; i < layout.length; i++) {
                                if ((i == 2) && room.getVisited()) {
                                    layout[i] += "# " + room.shortDescription() + " #";
                                    continue;
                                }
                                layout[i] += blanks[i];
                            }
                        }
                    }
                    return layout;
                }

                /**
                 * Take one room, and prints on the screen: western neighbor
                 * room, current room, southern neighbor room
                 *
                 * @param r - room in the middle
                 */
                private void printRoomLine(MyRoom r) {
                    String[] temp = {"", "", "", "", "", "", "", "", ""};

                    if (r.hasExit("west")) {
                        printRoom(temp, (MyRoom) r.nextRoom("west"));
                    } else {
                        for (int i = 0; i < temp.length; i++) {
                            temp[i] += "###############";
                        }
                    }
                    printRoom(temp, r);
                    if (r.hasExit("west")) {
                        printRoom(temp, (MyRoom) r.nextRoom("east"));
                    } else {
                        for (int i = 0; i < temp.length; i++) {
                            temp[i] += "###############";
                        }
                    }

                    System.out.println(temp[0] + "\n" + temp[1] + "\n" + temp[2] + "\n"
                            + temp[3] + "\n" + temp[4] + "\n" + temp[5] + "\n" + temp[6]
                            + "\n" + temp[7] + "\n" + temp[8]);
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] += "";
                    }
                }
            });

        }
    };
    
    public static HashMap<String, MyCommand>getCommandList(){
    	return commandList;
    }

    public MyParser() {
        super();
    }

    /**
     * Reads an input from the commandline and parse it in an MyCommand object.
     *
     * @return MyCommand object of the input command string.
     */
    @Override
    public MyCommand getCommand() {
        String inputLine = ""; // holds the full input line
        String command = null; // holds the first word of the input line, if any
        String[] parameter = null; // holds the second word of the input line,
        // if any

        // print out the prompt
        System.out.print("> ");
        try {
            // read the input line            
            inputLine = super.getReader().readLine();     
            
        } catch (java.io.IOException e) {
            System.out.println("MyParser: There was an error during reading: "
                    + e.getMessage());
        }


        // extract words from the input line
        StringTokenizer tokenizer = new StringTokenizer(inputLine);

        // get first word
        if (tokenizer.hasMoreTokens()) {
            command = tokenizer.nextToken();
        }

        // get all other words
        String[] tmp;
        while (tokenizer.hasMoreTokens()) {
            // start condition
            if (parameter == null) {
                parameter = new String[1];
                parameter[0] = tokenizer.nextToken();

                // every turn after the first
            } else {
                tmp = parameter;
                parameter = new String[tmp.length + 1];
                System.arraycopy(tmp, 0, parameter, 0, tmp.length);
                parameter[tmp.length] = tokenizer.nextToken();

            }
        }

        /*
         * Checks whether the first word is known as a valid command if so,
         * create a command with it if not, create a "null" command (for unknown
         * command)
         */
        if (isValidCommand(command, parameter)) {
            MyCommand cmd = commandList.get(command).clone();
            cmd.setParameter(parameter);
            return cmd;
        } else {
            return null;
        }
    }

    /**
     * Checks whether the given command (command identified by String command)
     * and it's parameter are valid
     *
     * @param command given command
     * @param parameter given parameter for the (valid?) command
     * @return
     */
    protected boolean isValidCommand(String command, String[] parameter) {

        if (commandList.containsKey(command)) {
            int parameter_count = (parameter == null) ? 0 : parameter.length;

            if (commandList.get(command).getParameterCount() == parameter_count) {
                return true;
            }
        }
        // if we get here, the string was not found in the commands
        return false;
    }
}
