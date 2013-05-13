package lu.uni.programming2.team8.gameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import lu.uni.programming2.team8.room.MyRoom;
import lu.uni.programming2.team8.room.MyRoom.Roomtype;

/**
 *
 * @author Patrick, FX, Yan
 */
public class MapGenerator {

    /**
     * Constant which stores how a vector has to be modified, if you move in a
     * direction. e.g. our position vector is (0,0,0) and we move north. The
     * constants told us the modification is (-1,0,0) thus our new Vector is
     * (0,0,0) + (-1,0,0) = (-1,0,0)
     */
    private static final HashMap<String, int[]> DIRECTION_MODIFICATION = new HashMap<String, int[]>() {

		/**
		 * This ID is needed for serialization and deserialization
		 */
		private static final long serialVersionUID = 3166032298945941728L;

		{
            int[] north = {-1, 0, 0};
            put("north", north);
            int[] east = {0, 0, -1};
            put("east", east);
            int[] south = {1, 0, 0};
            put("south", south);
            int[] west = {0, 0, 1};
            put("west", west);
            int[] top = {0, 1, 0};
            put("top", top);
            int[] bottom = {0, -1, 0};
            put("bottom", bottom);
        }
    };

    /**
     * This enumeration stores the different difficulties and the parameter
     * connected to the difficulty. It is public because it is a set of
     * constants and it has to be accessible.
     */
    public enum Difficulty {
        /*
         * specifies the parameters of the maze based on difficulty
         */
        TUTORIAL(4, 6, 4, 40, 3, 1, 1, 1, 1, 1, 1, 1, 300000,10000, 10000, 50),	// tutorial difficulty. Easier than EASY to show how the game works
        EASY(6, 3, 21, 10, 10, 20, 10, 10, 10, 10, 2, 35, 300000, 10000, 2500, 50),
        MEDIUM(8, 3, 51, 15, 15, 20, 25, 25, 25, 25, 2, 80, 240000, 8000, 1800, 100),
        HARD(15, 3, 337, 20, 20, 20, 170, 170, 170, 170, 2, 240, 120000, 5000, 1337, 150);
        // number of SAVE, ADDLIFE, and omitted rooms
        public final int csave, caddlife, comit;
        // 0 fire, 1 poison, 2 electricity, 3 laser, 4 razor, 5 empty
        public final HashMap<Roomtype, Integer> trap_possibility;
        // possibility of this room(% = number of a special room/ sum of all special rooms)
        public final int trap_possibility_total_count; // is the sum of all counts
        // The antitrap rooms have no possibility because if a trap is set, then
        // an antitrap is set as well.
        public final int cube_length; // stores the length of the cube
        public final int expected_exit_quantity; // the average number of exits in an room
        public final int timeToSolveRiddle; // time in ms
        public final int timeTillTrapKills; // in ms. Is the interval to deactivate a trap or flee
        public final int creatureSpeed; // time in ms for a creature to type a character
        public final int creatureLifePoints;

        Difficulty(	int cube_length, 
        			int expected_exit_quantity, 
        			int omit,
        			int csave, 
        			int caddlife, 
        			final int pempty, 
        			final int pfire,
        			final int ppoison, 
        			final int pelec, 
        			final int plaser,
        			final int prazor, 
        			final int pentity, 
        			final int timeToSolveRiddle,
                    final int timeTillTrapKills,
        			final int creatureSpeed, 
        			final int creatureLifePoints) {
            this.cube_length = cube_length;
            this.expected_exit_quantity = expected_exit_quantity;
            this.comit = omit;
            this.csave = csave;
            this.caddlife = caddlife;
            trap_possibility = new HashMap<Roomtype, Integer>() {
                private static final long serialVersionUID = 1L;

                {
                    put(Roomtype.EMPTY, pempty);
                    put(Roomtype.FIRE, pfire);
                    put(Roomtype.POISON, ppoison);
                    put(Roomtype.ELECTRICITY, pelec);
                    put(Roomtype.LASER, plaser);
                    put(Roomtype.RAZOR, prazor);
                    put(Roomtype.ENTITY, pentity);
                }
            };

            int tmp = 0;
            for (Roomtype r : trap_possibility.keySet()) {
                tmp += trap_possibility.get(r);
            }
            this.trap_possibility_total_count = tmp;

            this.timeToSolveRiddle = timeToSolveRiddle;
            this.timeTillTrapKills = timeTillTrapKills;
            this.creatureSpeed = creatureSpeed;
            this.creatureLifePoints = creatureLifePoints;
        }
    }

    public static MyRoom returnFixedMap() {
        /*
         * Create all the rooms and link their exits together. Fixed map for
         * testing traps and events.
         */
        MyRoom roomA, roomB, roomC, roomD, roomE, roomF, roomG, roomH, roomI, roomJ, roomK, roomL, roomM, roomN;
        // the five rooms of the sample map create rooms
        roomA = new MyRoom("SPAWN");
        roomA.setRoomType(Roomtype.SPAWN);
        roomB = new MyRoom("FIREPROTECTION");
        roomB.setRoomType(Roomtype.FIREPROTECTION);
        roomC = new MyRoom("FIRE");
        roomC.setRoomType(Roomtype.FIRE);
        roomD = new MyRoom("ANTIDOT");
        roomD.setRoomType(Roomtype.ANTIDOTE);
        roomE = new MyRoom("POISON");
        roomE.setRoomType(Roomtype.POISON);
        roomF = new MyRoom("MIRROR");
        roomF.setRoomType(Roomtype.MIRROR);
        roomG = new MyRoom("LASER");
        roomG.setRoomType(Roomtype.LASER);
        roomH = new MyRoom("RUBBERBOOTS");
        roomH.setRoomType(Roomtype.RUBBERBOOTS);
        roomI = new MyRoom("THUNDER");
        roomI.setRoomType(Roomtype.ELECTRICITY);
        roomJ = new MyRoom("EMPTY");
        roomJ.setRoomType(Roomtype.EMPTY);
        roomK = new MyRoom("RAZOR");
        roomK.setRoomType(Roomtype.RAZOR);
        roomL = new MyRoom("EXIT");
        roomL.setRoomType(Roomtype.EXIT);
        roomM = new MyRoom("SAVE");
        roomM.setRoomType(Roomtype.SAVE);
        roomN = new MyRoom("ADDLIFE");
        roomN.setRoomType(Roomtype.ADDLIFE);

        // initialize room exits: north, east, south, west, top, bottom
        roomA.setExits(roomB, null, null, null, roomM, roomN);
        roomB.setExits(roomC, null, roomA, null, null, null);
        roomC.setExits(roomD, null, roomB, null, null, null);
        roomD.setExits(roomE, null, roomC, null, null, null);
        roomE.setExits(roomF, null, roomD, null, null, null);
        roomF.setExits(roomG, null, roomE, null, null, null);
        roomG.setExits(roomH, null, roomF, null, null, null);
        roomH.setExits(roomI, null, roomG, null, null, null);
        roomI.setExits(roomJ, null, roomH, null, null, null);
        roomJ.setExits(roomK, roomL, roomI, null, null, null);
        roomK.setExits(null, null, roomJ, null, null, null);
        roomL.setExits(null, null, null, roomJ, null, null);
        roomM.setExits(null, null, null, roomJ, null, roomA);
        roomN.setExits(null, null, null, roomJ, roomA, null);

        // start the game in room A
        return roomA;
    }

    /**
     * Generates a random world. Size depending on the difficulty level
     *
     * @param difficulty level of the game
     * @return start room of the world
     */
    public static MyRoom generateRandomMap(Difficulty dif) {

        /*
         * this method is necessary for the method setStartRoomConnections and
         * the following recursive loop (has to be initialized (to be more
         * efficient) in generateRoomArray)
         */
        boolean[][][] connected = new boolean[dif.cube_length][dif.cube_length][dif.cube_length];

        MyRoom[][][] cube = generateRoomArray(dif, connected);
        int[] spawn_coords = setStartAndExitAndSaveAndAddLifeRoom(cube, dif);
        ArrayList<MyRoom> path = setRoomConnections(cube, dif, spawn_coords,
                connected);
        searchForgottenRooms(cube, connected);

        if (path.isEmpty()) {
            return null;
        }

        connected = new boolean[dif.cube_length][dif.cube_length][dif.cube_length];

        setRoomTypesAndNames(cube, dif, spawn_coords, connected, path);

        replaceMyRoomsBySpecificRoomObjects(cube, dif);
        
        return cube[spawn_coords[0]][spawn_coords[1]][spawn_coords[2]];
    }

    /**
     * Creates a 3D array of rooms. On the left some rooms are considered out.
     *
     * @param difficulty of the cube
     * @return room array
     */
    private static MyRoom[][][] generateRoomArray(Difficulty dif,
            boolean[][][] connected) {

        MyRoom[][][] cube = new MyRoom[dif.cube_length][dif.cube_length][dif.cube_length];
        int rooms_omit = dif.comit;
        int total_rooms_left = (int) Math.pow(dif.cube_length, 3);

        for (int i = 0; i < dif.cube_length; i++) {
            for (int j = 0; j < dif.cube_length; j++) {
                for (int k = 0; k < dif.cube_length; k++) {

                    // if this calculation is true, the room will be omitted
                    if (Math.random() * total_rooms_left < rooms_omit) {
                        cube[i][j][k] = null;
                        connected[i][j][k] = true;
                        rooms_omit--;
                    } else {
                        cube[i][j][k] = new MyRoom("[" + i + "][" + j + "]["
                                + k + "]");
                        connected[i][j][k] = false;
                    }
                    total_rooms_left--;
                }
            }
        }

        return cube;
    }

    /**
     * Set one room of the cube as a start room and one as an exit
     *
     * @param cube to be modified
     * @return vector - the returned array contains the coordinates of the start
     * room (need less operation to create/pass a vector object than to search
     * for the start room in the cube)
     */
    private static int[] setStartAndExitAndSaveAndAddLifeRoom(
            MyRoom[][][] cube, Difficulty dif) {
        int[] xyz;

        // EXIT-----------------------------------------------------------------
        do {
            xyz = generate3DCoordinate(cube.length);
        } while (cube[xyz[0]][xyz[1]][xyz[2]] == null);

        cube[xyz[0]][xyz[1]][xyz[2]].setRoomType(Roomtype.EXIT);

        // SPAWN------------------------------------------------------------------
        do {
            xyz = generate3DCoordinate(cube.length);
        } while ((cube[xyz[0]][xyz[1]][xyz[2]] == null)
                || (cube[xyz[0]][xyz[1]][xyz[2]].getRoomType() == Roomtype.EXIT));

        cube[xyz[0]][xyz[1]][xyz[2]].setRoomType(Roomtype.SPAWN);
        int[] coords = {xyz[0], xyz[1], xyz[2]};

        // SAVE------------------------------------------------------------------
        for (int i = 0; i < dif.csave; i++) {
            do {
                xyz = generate3DCoordinate(cube.length);
            } while ((cube[xyz[0]][xyz[1]][xyz[2]] == null)
                    || (cube[xyz[0]][xyz[1]][xyz[2]].getRoomType() == Roomtype.EXIT)
                    || (cube[xyz[0]][xyz[1]][xyz[2]].getRoomType() == Roomtype.SPAWN)
                    || (cube[xyz[0]][xyz[1]][xyz[2]].getRoomType() == Roomtype.SAVE));

            cube[xyz[0]][xyz[1]][xyz[2]].setRoomType(Roomtype.SAVE);

        }

        // ADDLIFE---------------------------------------------------------------
        for (int i = 0; i < dif.caddlife; i++) {
            do {
                xyz = generate3DCoordinate(cube.length);
            } while ((cube[xyz[0]][xyz[1]][xyz[2]] == null)
                    || (cube[xyz[0]][xyz[1]][xyz[2]].getRoomType() == Roomtype.EXIT)
                    || (cube[xyz[0]][xyz[1]][xyz[2]].getRoomType() == Roomtype.SPAWN)
                    || (cube[xyz[0]][xyz[1]][xyz[2]].getRoomType() == Roomtype.SAVE)
                    || (cube[xyz[0]][xyz[1]][xyz[2]].getRoomType() == Roomtype.ADDLIFE));

            cube[xyz[0]][xyz[1]][xyz[2]].setRoomType(Roomtype.ADDLIFE);

        }

        return coords;
    }

    /**
     * Generates a valid vector pointing at a coordinate in a cube of the given
     * length.
     *
     * @param cube length
     * @return vector to one coordinate in the cube
     */
    private static int[] generate3DCoordinate(int length) {
        int x, y, z;

        x = (int) (Math.random() * length);
        y = (int) (Math.random() * length);
        z = (int) (Math.random() * length);

        int[] res = {x, y, z};
        return res;
    }

    /**
     * Randomly connects the rooms in the cube. This is the start method
     * starting with the spawn room. Calls for the recursive loop
     * setRoomConnection_recursive
     *
     * @param cube - 3D Array of rooms, which will be connected
     * @param dif - difficulty of the cube
     * @param coords - coordinates of the room of this recursion step
     * @param connected - 3D Array of booleans; stores which rooms are already
     * connected
     * @return first found path from exit to spawn room
     */
    private static ArrayList<MyRoom> setRoomConnections(MyRoom[][][] cube,
            Difficulty dif, int[] coords, boolean[][][] connected) {
        MyRoom room = cube[coords[0]][coords[1]][coords[2]];
        connected[coords[0]][coords[1]][coords[2]] = true;
        HashMap<MyRoom, int[]> neighbours = new HashMap<>();

        String[] exits_direction = {"north", "east", "south", "west"};

        // set the exits
        MyRoom neighbour;
        for (int i = 0; i < exits_direction.length; i++) {

            int[] neighbour_coords = getCoordinates(coords,
                    DIRECTION_MODIFICATION.get(exits_direction[i]),
                    dif.cube_length);

            // coordinates not out of cube bounds
            if (neighbour_coords != null) {

                // can only create exits to rooms which are not processed
                // already
                if (!connected[neighbour_coords[0]][neighbour_coords[1]][neighbour_coords[2]]) {

                    neighbour = cube[neighbour_coords[0]][neighbour_coords[1]][neighbour_coords[2]];
                    neighbour.addExit(getOppositeDirection(exits_direction[i]),
                            room);
                    room.addExit(exits_direction[i], neighbour);

                    neighbours.put(neighbour, neighbour_coords);
                }
            }

        }

        // If this is the Exit, the path variable will be filled
        ArrayList<MyRoom> path = new ArrayList<>();
        if (room.getRoomType() == MyRoom.Roomtype.EXIT) {
            path.add(room);
        }
        // recursive call
        for (MyRoom r : neighbours.keySet()) {
            ArrayList<MyRoom> res = setRoomConnections_rekursiv(cube, dif,
                    neighbours.get(r), connected);

            // If recursive call was in a room which is part of the
            // Exit-Spawn-Path
            if (res.size() > 0) {
                path = res;
                path.add(room);
            }
        }

        return path;

    }

    /**
     * Randomly connects rooms in the cube. This is the recursive call. To start
     * the recursion call setRoomConnections()
     *
     * @param cube - 3D Array of rooms, which shall be connected
     * @param dif - difficulty of the cube
     * @param coords - coordinates of the room of this recursion step
     * @param connected - 3D Array of booleans, stores which rooms are already
     * connected
     * @return first found path from exit to epawn room
     */
    private static ArrayList<MyRoom> setRoomConnections_rekursiv(
            MyRoom[][][] cube, Difficulty dif, int[] coords,
            boolean[][][] connected) {
        MyRoom room = cube[coords[0]][coords[1]][coords[2]];
        connected[coords[0]][coords[1]][coords[2]] = true;
        HashMap<MyRoom, int[]> neighbours = new HashMap<>();

        /*
         * calculates the number of exits to create. first part calculates the
         * number of exits wanted second subtracts the number of exits already
         * existed
         */

        int count_wanted_exits = (int) (Math.random() * 6) + 1
                - room.getNumberOfExits();
        String[] exits_direction = getExitPriorityList(); // shuffled list of
        // directions

        MyRoom neighbour;

        // for each entry in exits_direction (if enough exits are set, the loop
        // breaks
        for (int i = 0; i < exits_direction.length; i++) {

            if (count_wanted_exits <= 0) {
                break;
            }

            if (room.hasExit(exits_direction[i])) {
                continue;
            }

            int[] neighbour_coords = getCoordinates(coords,
                    DIRECTION_MODIFICATION.get(exits_direction[i]),
                    dif.cube_length);

            // equal to null, if coordinates would be out of the cube
            if (neighbour_coords != null) {
                // Neighbor is not processed already
                if (!connected[neighbour_coords[0]][neighbour_coords[1]][neighbour_coords[2]]) {

                    neighbour = cube[neighbour_coords[0]][neighbour_coords[1]][neighbour_coords[2]];
                    neighbour.addExit(getOppositeDirection(exits_direction[i]),
                            room);
                    room.addExit(exits_direction[i], neighbour);

                    neighbours.put(neighbour, neighbour_coords);
                    count_wanted_exits--;
                }
            }

        }

        // if this is the Exit, the path variable will be filled
        ArrayList<MyRoom> path = new ArrayList<>();
        if (room.getRoomType() == MyRoom.Roomtype.EXIT) {
            path.add(room);
        }

        // recursive call
        for (MyRoom r : neighbours.keySet()) {
            ArrayList<MyRoom> res = setRoomConnections_rekursiv(cube, dif,
                    neighbours.get(r), connected);
            if (res.size() > 0) {
                path = res;
                path.add(room);
            }
        }

        return path;
    }

    /**
     * Returns an array with shuffled directions.
     *
     * @return shuffled array
     */
    private static String[] getExitPriorityList() {
        String[] result = new String[MyRoom.DIRECTION.length];

        ArrayList<String> tmp = new ArrayList<String>();
        tmp.addAll(Arrays.asList(MyRoom.DIRECTION));

        int k;

        while (tmp.size() > 0) {
            k = (int) (Math.random() * tmp.size());
            result[tmp.size() - 1] = tmp.get(k);
            tmp.remove(k);
        }

        return result;
    }

    /**
     * Return the coordinates of coords + mod. If they are out of the range of
     * the cube, null will be returned
     *
     * @param coords - room coordinates
     * @param mod - modifier for direction
     * @param length - cube length
     * @return new coordinates or null
     */
    private static int[] getCoordinates(int[] coords, int[] mod, int length) {
        int[] res = new int[coords.length];

        for (int i = 0; i < coords.length; i++) {
            res[i] = coords[i] + mod[i];
            if ((res[i] < 0) || (res[i] >= length)) {
                return null;
            }
        }
        return res;
    }

    /**
     * Some rooms may not be connected because of the RNG used to connect them.
     * Therefore these rooms will now be found and connected to one neighbour
     * room.
     *
     * @param cube - stores the rooms
     * @param connected - forgotten rooms are the last false entries
     */
    private static void searchForgottenRooms(MyRoom[][][] cube,
            boolean[][][] connected) {
        for (int i = 0; i < connected.length; i++) {
            for (int j = 0; j < connected.length; j++) {
                for (int k = 0; k < connected.length; k++) {

                    // found unconnected room
                    if (!connected[i][j][k]) {

                        int[] coords = {i, j, k};
                        String[] exit = getExitPriorityList();

                        // search for possible neighbors
                        boolean solved = false;
                        for (String s : exit) {
                            int[] neighbour = getCoordinates(coords,
                                    DIRECTION_MODIFICATION.get(s), cube.length);

                            if ((neighbour != null)
                                    && (cube[neighbour[0]][neighbour[1]][neighbour[2]] != null)) {

                                cube[i][j][k]
                                        .addExit(
                                        s,
                                        cube[neighbour[0]][neighbour[1]][neighbour[2]]);
                                cube[neighbour[0]][neighbour[1]][neighbour[2]]
                                        .addExit(getOppositeDirection(s),
                                        cube[i][j][k]);
                                solved = true;
                                break;
                            }

                        }

                        // problem could not be solved => room will be omitted
                        if (!solved) {
                            cube[i][j][k] = null;
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets recursively the roomtype for all rooms without Roomtype and for all
     * rooms without a name. for setting roomtype the method chooseRoomTypes()
     * will be called for setting name setRoomName() will be called
     *
     * @param cube - 3D Array of rooms
     * @param dif - Difficulty of the cube
     * @param coords - Vector pointing to the current room
     * @param processed - 3D boolean array storing which room was already
     * visited
     * @param path - a known path from Spawn to Exit (random path, not
     * shortest). Used in a sub-method
     */
    private static void setRoomTypesAndNames(MyRoom[][][] cube, Difficulty dif,
            int[] coords, boolean[][][] processed, ArrayList<MyRoom> path) {
        // Unvisited room until now
        if (!processed[coords[0]][coords[1]][coords[2]]) {
            processed[coords[0]][coords[1]][coords[2]] = true;

            MyRoom room = cube[coords[0]][coords[1]][coords[2]];

            // roomtype not set till now
            if (room.getRoomType() == null) {
                chooseRoomTypes(room, cube, dif, coords, path);
            }

            room.generateRoomName();            

            // recursive call for each exit
            for (String s : MyRoom.DIRECTION) {
                if (room.hasExit(s)) {

                    // new coordinates; should never be null.
                    int[] next = getCoordinates(coords,
                            DIRECTION_MODIFICATION.get(s), dif.cube_length);
                    if (next != null) {
                        setRoomTypesAndNames(cube, dif, next, processed, path);
                    }
                }
            }
        }
    }

    /**
     * chooses randomly a roomtype(just traps). Antitrap room won't be chosen
     * because if you choose a trap, the antitrap will be placed at the current
     * position and the trap in a range of 3 rooms. Rooms without traps (SAVE,
     * ADDLIFE) are already set(because they have no possibility to be set they
     * have a count which WILL be set).
     *
     * @param room - room for which the type will be chosen
     * @param cube - 3D array of rooms
     * @param dif - current difficulty
     * @param coords - coords of room
     * @param path - path Exit-Spawn used in a sub method
     */
    private static void chooseRoomTypes(MyRoom room, MyRoom[][][] cube,
            Difficulty dif, int[] coords, ArrayList<MyRoom> path) {
        // creates 0 <= rnd < total_count
        int rnd = (int) (Math.random() * dif.trap_possibility_total_count);

        // decrease rnd as long as rnd > 0
        for (Roomtype trap : dif.trap_possibility.keySet()) {

            if (rnd - dif.trap_possibility.get(trap) >= 0) {
                rnd -= dif.trap_possibility.get(trap);
                continue;
            }

            // ROOMTYPE CHOOSEN
            Roomtype counterroom = MyRoom.getTrapAntitrapRelation(trap);

            // no counter-room necessary
            if (counterroom == null) {

                // Razors will not be set on the path
                // Razor equals auto-death
                if ((path.contains(room)) && (trap == Roomtype.RAZOR)) {
                    room.setRoomType(Roomtype.EMPTY);
                } else {
                    room.setRoomType(trap);
                }

            } else {
                /*
                 * In this case the method sets in the current room the antitrap
                 * and places a trap next to it
                 */

                ArrayList<MyRoom> possible = findCounterRoom(cube, dif, coords,
                        0);
                if (possible.isEmpty()) {
                    room.setRoomType(Roomtype.EMPTY);
                } else {
                    int i = (int) (Math.random() * possible.size());
                    room.setRoomType(counterroom);
                    possible.get(i).setRoomType(trap);

                }

            }
            return;

        }

    }

    /**
     * Search for rooms where the trap room could be placed. Returns an Array of
     * possible rooms.
     *
     * @param cube - 3d array of rooms
     * @param dif - difficulty of the cube
     * @param coords - coordinates of the current room
     * @param distance - distance from the trap room
     * @return list of possible places for the trap room
     */
    private static ArrayList<MyRoom> findCounterRoom(MyRoom[][][] cube,
            Difficulty dif, int[] coords, int distance) {
        ArrayList<MyRoom> res = new ArrayList<MyRoom>();
        if (distance > 3) {
            return res;
        }

        MyRoom room = cube[coords[0]][coords[1]][coords[2]];

        if (room.getRoomType() == null) {
            res.add(room);
        }

        // recursive call for each exit
        ArrayList<MyRoom> recursiv_found = new ArrayList<MyRoom>();
        for (String s : MyRoom.DIRECTION) {
            if (room.hasExit(s)) {

                // new coordinates, should never be null.
                int[] next = getCoordinates(coords,
                        DIRECTION_MODIFICATION.get(s), dif.cube_length);
                if (next != null) {

                    recursiv_found = findCounterRoom(cube, dif, next,
                            (distance + 1));

                    for (MyRoom r : recursiv_found) {
                        if (!res.contains(r)) {
                            res.add(r);
                        }
                    }

                }
            }
        }

        return res;
    }

    /**
     * Replace all MyRoom objects in the cube with the correct objects for their
     * roomtype (e.g. Firetrap roomtype => Object TrapRoom).
     *
     * I know it's not the best way to create a cube filled with Rooms of
     * different classes, but this major design change was done after the
     * generateRandomMap, was already done. This is the easiest, a good and a
     * straight forward way, to solve the problem.
     *
     * @param cube 3D cube with MyRoom objects
     */
    private static void replaceMyRoomsBySpecificRoomObjects(MyRoom[][][] cube,
            Difficulty diff) {
        for (int i = 0; i < cube.length; i++) {
            for (int j = 0; j < cube[i].length; j++) {
                for (int k = 0; k < cube[i][j].length; k++) {
                    MyRoom m = cube[i][j][k];
                    if (m != null) {

                        cube[i][j][k] = m.expandToRoomTypeObject(diff);
                    }
                }
            }
        }
    }

    
    /**
     * Make some changes in the world, like switching the place of two rooms
     *
     * @param currentroom - current room of the player
     * @return current room of the player
     */
    public static MyRoom generateRandomMapChanges(MyRoom currentroom) {
        return currentroom;
    }

    // map visualizer, dev function
    private static void printCube(MyRoom[][][] cube) {
        String first = "";
        String second = "";
        String third = "";

        for (int j = 0; j < cube.length; j++) {
            for (int i = 0; i < cube.length; i++) {
                for (int k = cube.length - 1; k >= 0; k--) {

                    if (cube[i][j][k] == null) {
                        first += "\t\t\t\t\t\t\t";
                        second += "\t\t\tomit\t\t\t";
                        third += "\t\t\t\t\t\t\t";

                    } else {

                        first += (cube[i][j][k].hasExit("north")) ? ("\t\t\t|\t\t\t")
                                : ("\t\t\t\t\t\t\t");
                        second += (cube[i][j][k].hasExit("west")) ? ("\t<-\t")
                                : ("\t\t");

                        if (cube[i][j][k].hasExit("top")) {
                            if (cube[i][j][k].hasExit("bottom")) {
                                second += "^ " + cube[i][j][k].getRoomType()
                                        + "-"
                                        + cube[i][j][k].shortDescription()
                                        + " v";
                            } else {
                                second += "^ " + cube[i][j][k].getRoomType()
                                        + "-"
                                        + cube[i][j][k].shortDescription();
                            }
                        } else {
                            if (cube[i][j][k].hasExit("bottom")) {
                                second += cube[i][j][k].getRoomType() + "-"
                                        + cube[i][j][k].shortDescription()
                                        + " v";
                            } else {
                                second += cube[i][j][k].getRoomType() + "-"
                                        + cube[i][j][k].shortDescription();
                            }
                        }

                        second += (cube[i][j][k].hasExit("east")) ? ("\t->\t")
                                : ("\t\t");
                        third += (cube[i][j][k].hasExit("south")) ? ("\t\t\t|\t\t\t")
                                : ("\t\t\t\t\t\t\t");

                    }
                }
                System.out.println(first);
                System.out.println(second);
                System.out.println(third);
                first = "";
                second = "";
                third = "";
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
    }

    /**
     * Returns for the given direction the opposite direction
     *
     * @param s - input direction
     * @return opposite direction
     */
    public static String getOppositeDirection(String s) {
        switch (s) {
            case "north":
                return "south";
            case "east":
                return "west";
            case "south":
                return "north";
            case "west":
                return "east";
            case "top":
                return "bottom";
            case "bottom":
                return "top";
            default:
                return "north";
        }
    }
}