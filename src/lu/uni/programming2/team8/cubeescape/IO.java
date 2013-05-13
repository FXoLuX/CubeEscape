package lu.uni.programming2.team8.cubeescape;

/**
 * This class handles Input and Output.
 * It contains static methods that let us load all the riddles and the highscores when starting the game.
 * There's also methods to save/load player profile and the game itself to be able to continue playing after a break.
 * 
 * All methods are static, then easily accessible during the game.
 * 
 * Several saving/loading methods have been used:
 * 	- aggregating all information of a class in a single String, then recreating the object by cutting the String
 * 	- Serialization and desirialization of the objects (via Serializable interface).
 */


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;


import lu.uni.programming2.team8.entity.Player;
import lu.uni.programming2.team8.gameEngine.MapGenerator.Difficulty;
import lu.uni.programming2.team8.room.MyRoom;
import lu.uni.programming2.team8.room.MyRoom.Roomtype;
import lu.uni.programming2.team8.room.SaveRoom;

/**
 *
 * @author FX, Patrick
 */
public class IO {

    private static final String SPLITSIGN = "@";

    /**
     * Stores the path of the different files to load data
     */
    private enum Path {

        RIDDLEPATH("riddles.klkwl"), SCRIPTPATH("scripts.klkwl"), HIGHSCOREPATH("highscore.klkwl"), PROFILEPATH("playerProfiles.klkwl");
        private final String path;

        Path(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    private enum IdentifierScore {

        TIME_PLAYED("TIMEPLAYED="), VISITED_ROOM_NUMBER("VISITEDROOM="),
        DIFFICULTY("DIFF="), NAME("NAME="), DATE("DATE="), WON("WON=");
        private final String identifier;

        IdentifierScore(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }
    }

  
    /**
     * Will write the profile of the player to the external file, i.e. general
     * statistics: number of games played, won, lost, highscore.
     * 
     * We first load the profiles that are already saved, then we add the current profile
     * to the ArrayList of Profile, and then finally, we save, via ObjectOutputStream this ArrayList.
     *
     * @param currentprofile
     */
    public static void saveProfile(ArrayList<Profile> allProfiles){

        

        ObjectOutputStream oos = null;
        try {

            oos = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream(new File(Path.PROFILEPATH.getPath()))));

            oos.writeObject(allProfiles);

        } catch (FileNotFoundException e) {
            System.out.println("Error during backup Player Profile.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error during backup Player Profile.");
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        }  finally {
            try {
            	if (oos != null) {
                    oos.close();
				}
            } catch (IOException ex) {
            	System.out.println("Error during closing object output stream.");
                System.err.println("IOException: " + ex.getMessage()); 
            }
        }

        System.out.println();
        System.out.println("Player Profile sucessfully saved");
        System.out.println();

    }

    /**
     * Will write the current room, the player and the difficulty, directly as objects via ObjectOutputStream
     * using the interface Serializable.
     * 
     * @param currentRoom
     * @param player
     * @param diff
     * @param filename
     */
    public static void saveGame(SaveRoom currentRoom, Player player, Difficulty diff,
            String filename) {

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream(new File("savedGame-" + filename
                    + ".klkwl"))));

            oos.writeObject(currentRoom);
            oos.writeObject(player);
            oos.writeObject(diff);
            Date date = new Date();
            oos.writeObject(date);

        } catch (FileNotFoundException e) {
            System.out.println("Error during backup Game.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error during backup Game.");
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
            	if (oos != null) {
                    oos.close();
				}
            } catch (IOException ex) {
            	System.out.println("Error during closing object output stream.");
                System.err.println("IOException: " + ex.getMessage()); 
            }
        }

        System.out.println();
        System.out.println("Game sucessfully saved");
        System.out.println();

    }

    /**
     * This method load player profiles via ObjectInputStream (DeSerialization)
     * @return ArrayList<Profile> containing all previously saved Profiles
     */
    public static ArrayList<Profile> loadProfiles() {
        ObjectInputStream ois = null;

        ArrayList<Profile> arrayListOfPlayersProfiles = new ArrayList<>();

        
        try {
            ois = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream(new File(Path.PROFILEPATH.getPath()))));
          
            // we have here a warning concerning an unchecked cast, but we know that
            // we read and ArrayList<Profile>.
            // Novertheless, we catch the ClassCastException in a catch block
            arrayListOfPlayersProfiles = (ArrayList<Profile>) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("Error during Loading Players.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error during Loading Players.");
            System.err.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error during Loading Players.");
            System.err.println("ClassNotFoundException: " + e.getMessage());
        } catch (ClassCastException e) {									// I have an unchecked cast when I put the stream in arrayListOfProfile, so I catch here ClassCastException in case the stream wasn't what it was supposed to be
			System.out.println("Error during Loading Players.");
            System.err.println("ClassCastException: " + e.getMessage());
        } finally {
            try {
            	if (ois != null) {
                    ois.close();
				}
            } catch (IOException ex) {
            	System.out.println("Error during closing object output stream.");
                System.err.println("IOException: " + ex.getMessage()); 
            }
        }
   
        return arrayListOfPlayersProfiles;
    }

    /**
     * This method will load a previously saved game from a file, 
     * given as a parameter (via its name as a String).
     * DeSerialization.
     * 
     * @param filename
     * @return an array of Object, conataining the current room, the player,
     * the difficulty and the date.
     * 
     */
    public static Object[] loadGame(String filename) {
        ObjectInputStream ois = null;

        Object[] savedGameInfo = new Object[4];

        try {
            ois = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream(
                    new File(filename))));

            for (int i = 0; i < savedGameInfo.length; i++) {
                savedGameInfo[i] = ois.readObject();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error during loading game.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error during loading game.");
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error during loading game.");
            System.err.println("ClassNotFoundException: " + e.getMessage());
        } finally {
            try {
            	if (ois != null) {
                    ois.close();
				}
            } catch (IOException ex) {
            	System.out.println("Error during closing object output stream.");
                System.err.println("IOException: " + ex.getMessage()); 
            }
        }

        return savedGameInfo;
    }
    
    
    /**
     * Loads the riddle from external file
     * @return ArrayList<Riddle> containing all the riddles and their solution
     */

    public static ArrayList<Riddle> loadRiddle() {
        ArrayList<Riddle> riddles = new ArrayList<Riddle>();
        Scanner sc;
        try {
            //opens file
            sc = new Scanner(new FileInputStream(Path.RIDDLEPATH.getPath()));
                        
            String line;
            String[] parts;

            //reads every line in the file
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line.equals("")) {
                    continue;
                }

                parts = line.split("@");

                //creates riddle
                Riddle r = new Riddle();
                r.setRiddleText(parts[0]);      //Question, mandatory
                r.setAnswer(parts[1]);          //Answer, mandatory
                if (parts.length == 3) {
                    r.setExplanation(parts[2]); //Explanation, optional
                }

                riddles.add(r);
            }
            sc.close();
            return riddles;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

   
    /**
     * The returned HashMap connects to a roomtype a list of possible
     * description texts, which can be printed while entering a room.
     *
     * @return <specific roomtype: list of desciptions textes for this roomtype>
     */
    public static HashMap<Roomtype, ArrayList<String>> loadScripts() {
        HashMap<Roomtype, ArrayList<String>> output = new HashMap<Roomtype, ArrayList<String>>();
        Scanner sc;
        try {

            sc = new Scanner(new File(Path.SCRIPTPATH.getPath())); //opens file which contains scipt text
            String input;
            String[] parts;

            while (sc.hasNextLine()) {
                input = sc.nextLine();

                //tests for each roomtype, whether the inputline starts with the name of the roomtype
                for (Roomtype rt : MyRoom.Roomtype.values()) {
                    if (input.startsWith(rt.getName())) {

                        //found fitting roomtype
                        parts = input.split(SPLITSIGN);
                        if (parts.length >= 2) {

                            //if there is no script for this room till now, then there
                            //is no HashMap entry. Then this clause create it.
                            if (!output.containsKey(rt)) {
                                output.put(rt, new ArrayList<String>());
                            }

                            output.get(rt).add(parts[1]);
                        }
                        break;
                    }
                }
            }
            
            sc.close();
        } catch (IOException ex) {
        }
        return output;
    }

    /**
     * Stores the highscore (which is represented as an array with increasing
     * score) in a file.
     *
     * @param hs Array which contains the highscore list
     */
    public static void saveHighScore(Score[] hs) {
        try {
            FileWriter fw = new FileWriter(new File(Path.HIGHSCOREPATH.getPath()));
            String msg = "";

            //create string to store the object
            for (Score s : hs) {
                if (s != null) {
                    msg += saveHighScore_convertScoreToString(s) + "\n";
                }
            }

            //store string and close file stream
            fw.write(msg);
            fw.close();


        } catch (IOException ex) {
            System.out.println();
            System.out.println("-------------");
            System.out.println("Error while storing the highscore.");
            System.out.println("-------------");
        }
    }

    /**
     * Creates for one Score item the string needed to store it
     *
     * @param s Score item which should be stored
     * @return String to store this item.
     */
    private static String saveHighScore_convertScoreToString(Score s) {
        String output = "";
        output += IdentifierScore.NAME.getIdentifier() + s.getName() + SPLITSIGN;
        output += IdentifierScore.DIFFICULTY.getIdentifier() + s.getDifficulty().toString() + SPLITSIGN;        
        output += IdentifierScore.TIME_PLAYED.getIdentifier() + String.valueOf(s.getTimeOfPlay()) + SPLITSIGN;
        output += IdentifierScore.VISITED_ROOM_NUMBER.getIdentifier() + String.valueOf(s.getNumberOfVisitedRooms()) + SPLITSIGN;
        output += IdentifierScore.WON.getIdentifier() + String.valueOf(s.getWon()) + SPLITSIGN;
        output += IdentifierScore.DATE.getIdentifier() + String.valueOf(s.getDate().getTime());

        return output;
    }

    
    /**
     * Loads the highscore from the file.
     * @return decreasing ordered score object list of length 10
     */
    public static Score[] loadScore() {
        ArrayList<Score> scorelist = new ArrayList<Score>();
        Scanner sc;
        try {

            sc = new Scanner(new File(Path.HIGHSCOREPATH.getPath()));
            Score score;

            while (sc.hasNextLine()) {
                score = loadScore_convertStringToScore(sc.nextLine());
                if (score != null) {
                    scorelist.add(score);
                }

            }

            Score[] highscore = new Score[10];
            int index;
            for(int i = 0; i < 10 ; i++){
                if(scorelist.isEmpty()){
                    break;
                }
                
                index = loadScore_findHighestScore(scorelist);
                highscore[i] = scorelist.get(index);
                scorelist.remove(index);
                
            }
            sc.close();
            return highscore;

        } catch (FileNotFoundException ex) {
            System.out.println("Unable to load the highscore.");
            return new Score[10];
        }
    }

    
    /**
     * Creates from an input string if possible an score object. If not possible 
     * null will be returned
     * @param input 
     * @return score object or null(if not possible to convert)
     */
    private static Score loadScore_convertStringToScore(String input) {
        String[] parts = input.split(SPLITSIGN);
        
        //Variables which define a score
        /*
         * Variables must be initialized otherwise it could happens that they
         * are not initialized on the score object creation. This would cause 
         * the program to shut down
         */
        String name = "";
        long timeplayed = -1;
        int visited_rooms = -1;        
        int won = -1;
        Difficulty diff = Difficulty.TUTORIAL;        
        Date date = new Date(0);
        
        
        //reading each part and organise the information        
        for(String s: parts){
            try{
            //READS DATE
            if(s.startsWith(IdentifierScore.DATE.getIdentifier())){
                s = s.replace(IdentifierScore.DATE.getIdentifier(), "");
                date = new Date(Long.valueOf(s));
            
                //READS DIFFICULTY
            }else if(s.startsWith(IdentifierScore.DIFFICULTY.getIdentifier())){
                s = s.replace(IdentifierScore.DIFFICULTY.getIdentifier(), "");
                for(Difficulty d: Difficulty.values()){
                    if(d.toString().equals(s)){
                        diff = d;
                        break;
                    }
                }
                
                //READS NAME
            } else if(s.startsWith(IdentifierScore.NAME.getIdentifier())){
                s = s.replace(IdentifierScore.NAME.getIdentifier(), "");
                name = s;

                //READS TIME PLAYED
            } else if(s.startsWith(IdentifierScore.TIME_PLAYED.getIdentifier())){
                s = s.replace(IdentifierScore.TIME_PLAYED.getIdentifier(), "");
                timeplayed = Long.valueOf(s);
                
                //READS VISTED ROOM NUMBER
            } else if(s.startsWith(IdentifierScore.VISITED_ROOM_NUMBER.getIdentifier())){
                s = s.replace(IdentifierScore.VISITED_ROOM_NUMBER.getIdentifier(), "");
                visited_rooms = Integer.valueOf(s);
                
                //READS WHETHER GAME WAS ONE
            } else if(s.startsWith(IdentifierScore.WON.getIdentifier())){
                s = s.replace(IdentifierScore.WON.getIdentifier(), "");
                boolean tmp = Boolean.valueOf(s);
                won = (tmp)?1:0;
            }
            } catch(NumberFormatException e){
                System.out.println("Could not load one score");
                break;
            }
        }
        
        //if every information was found, create object
        if ((!name.equals("")) && (timeplayed != -1) && (visited_rooms != -1) 
                && (diff != Difficulty.TUTORIAL) && (won != -1) && (date.getTime() != 0)){
            boolean tmp = (won == 0)?false:true;
            return new Score(name, timeplayed, visited_rooms, diff, tmp, date);
            
        } else {
            return null;
        }
        
    }

    /**
     * Find the highest score.
     * 
     * @param s = an ArrayList of Score
     * @return the index of the highest score
     */
    private static int loadScore_findHighestScore(ArrayList<Score> s) {
        if (s.size() > 0) {
            int index = 0;

            for (int i = 1; i < s.size(); i++) {

                //found a better score
                if (s.get(i).getScore() > s.get(index).getScore()) {
                    index = i;

                    //scores are equal, therefore the older score shall be better.
                } else if (s.get(i).getScore() == s.get(index).getScore()) {
                    if (s.get(i).getDate().getTime() < s.get(index).getDate().getTime()) {
                        index = i;
                    }
                }
            }

            return index;

        } else {
            return -1;
        }
    }
}
