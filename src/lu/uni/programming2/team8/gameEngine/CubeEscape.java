package lu.uni.programming2.team8.gameEngine;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import lu.uni.programming2.team8.cubeescape.IO;
import lu.uni.programming2.team8.cubeescape.Item;
import lu.uni.programming2.team8.cubeescape.Profile;
import lu.uni.programming2.team8.cubeescape.Riddle;
import lu.uni.programming2.team8.cubeescape.Score;
import lu.uni.programming2.team8.entity.MyBot;
import lu.uni.programming2.team8.entity.MyCrawler;
import lu.uni.programming2.team8.entity.Player;
import lu.uni.programming2.team8.gameEngine.MapGenerator.Difficulty;
import lu.uni.programming2.team8.room.MyRoom;
import lu.uni.programming2.team8.room.MyRoom.Roomtype;
import lu.uni.programming2.team8.room.SaveRoom;
import lu.uni.programming2.team8.room.TrapRoom.PlayerDeathException;
import framework.Game;

/**
 *
 * @author Patrick, FX, Yan
 */
/**
 * This class is the main class of the game application. This is a very simple,
 * text based adventure game. Users can walk around some scenery. That's all. It
 * should really be extended to make it more interesting!
 *
 * This main class creates and initializes all the others: it creates all rooms,
 * creates the parser to read and "understand" the user's commands and starts
 * the game. It also evaluates the user's commands that the parser returns,
 * until a "quit" command is thrown.
 */
public class CubeEscape extends Game {

    /**
     * This class organize the different menus of the game (including the main
     * menu and the sub menus (new, load, credits, quit) Instead of an own class
     * the methods and variables could just be put in the class CubeEscape. But
     * for a much better overview this inner class is created, which holds all
     * functions of the game menu
     */
	private class GameMenu {

//      /**
//       * String which is the Title of the game
//       */
//      private String title = "******************************\n"
//              + "*      CubeEscape v" + VERSION + "      *"
//              + "\n******************************\n";
      /**
       * String display in the main menu
       */
      private String main_items = "\t\t-----Main Menu-----\n\n\t\t1) New Game\n\t\t2) Load Game\n\t\t3) Tutorial\n\t\t4) Profiles\n\t\t5) HighScores\n\t\t6) Credits\n\t\t7) Quit\n";
      /**
       * List of Strings display in the load menu (order [0], list of save
       * games, [1])
       */
      private String highscore_items = "-----HighScore-----\n\nIf your name is written here, you are a real badass\n0) back\n1) delete highscore\n";
      /**
       * List of Strings display in the new game menu
       */
      private String[] newgame_items = {
          "-----New Game-----\n0) Back to main menu\n", "Please enter your Playername:",
          "Difficulty:\n\t1) Easy\n\t2) Medium\n\t3) Hard\n"};

      /**
       * Opens the main menu. This contains following options: new game, load,
       * credits, quit
       *
       * @return tre = one submenu ended
       */
      public void openMainMenu() {
          boolean closemenu = false;
          boolean inputvalid;
          printTitle();
          while (!closemenu) {

              // display menu
              System.out.println();
              System.out.println();
              System.out.println(main_items);

              // Player input
              do {
                  String input = sc.nextLine();
                  inputvalid = false;
                  System.out.println();

                  if (input.equals("1") || input.equals("1)")
                          || input.equalsIgnoreCase("new game")) {
                      openNewGameMenu();
                      inputvalid = true;

                  } else if (input.equals("2") || input.equals("2)")
                          || input.equalsIgnoreCase("load")) {
                      openLoadGameMenu();
                      inputvalid = true;

                  } else if (input.equals("3") || input.equals("3)")
                          || input.equalsIgnoreCase("tutorial")) {
                      openTutorialMenu();
                      inputvalid = true;
                  } else if (input.equals("4") || input.equals("4)")
                          || input.equalsIgnoreCase("profiles")) {
                      openProfileMenu();
                      inputvalid = true;

                  } else if (input.equals("5") || input.equals("5)")
                          || input.equalsIgnoreCase("highscores")
                          || input.equalsIgnoreCase("highscore")
                          || input.equalsIgnoreCase("scores")) {
                      openHighScoresMenu();
                      inputvalid = true;

                  } else if (input.equals("6") || input.equals("6)")
                          || input.equalsIgnoreCase("credits") || input.equalsIgnoreCase("credit")) {
                      printCredits();
                      inputvalid = true;

                  } else if (input.equals("7") || input.equals("7)")
                          || input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {

                      IO.saveProfile(profiles);
                      IO.saveHighScore(highscore);
                      inputvalid = true;
                      closemenu = true;

                  } else {
                      System.out
                              .println("No valid input. Please insert the number or the name of your option.");
                  }
              } while (!inputvalid);
          }
      }

      private void openTutorialMenu() {
           organiseTutorial();
      }

      /**
       * Creates a new game. First the game will ask for the parameter (name
       * of the player, difficulty). Then a cube will be created and then the
       * game starts.
       */
      private void openNewGameMenu() {
          // title
          System.out.println(newgame_items[0]);

          // playername
          System.out.println(newgame_items[1]);
          String playername = sc.nextLine();
          System.out.println();
          if ((playername.equalsIgnoreCase("back")) || (playername.equalsIgnoreCase("0")) || (playername.equalsIgnoreCase("0)"))) {
              return;
          }

          // difficulty
          Difficulty diff = Difficulty.EASY;
          System.out.println(newgame_items[2]);
          boolean choosen = false;
          do {
              String input = sc.nextLine();
              System.out.println();

              if (input.equals("1") || input.equals("1)")
                      || input.equalsIgnoreCase("easy")) {
                  diff = Difficulty.EASY;
                  choosen = true;
              } else if (input.equals("2") || input.equals("2)")
                      || input.equalsIgnoreCase("medium")) {
                  diff = Difficulty.MEDIUM;
                  choosen = true;
              } else if (input.equals("3") || input.equals("3)")
                      || input.equalsIgnoreCase("hard")) {
                  diff = Difficulty.HARD;
                  choosen = true;
              } else if (input.equalsIgnoreCase("back")) {
                  return;
              } else {
                  System.out
                          .println("No valid input. Please insert the number or the name of your option.");
              }
          } while (!choosen);

          // start game
          organiseStarNewGame(playername, diff);
          playGame();

      }

      /**
       * Display the loadGameMenu = the menu to choose and load a previously
       * saved game
       */
      private void openLoadGameMenu() {

          System.out.println("Load Game Menu");
          System.out.println("Please select the game you want to load:");

          String workingDirectoryPath = System.getProperty("user.dir");

          File[] listOfSavedGame = new File(workingDirectoryPath).listFiles(new FilenameFilter() {
              @Override
              public boolean accept(File dir, String name) {
                  return name.startsWith("savedGame-");
              }
          }); // this close File[] declaration

          // display the saved games
          System.out.println("0) Back");
          for (int i = 0; i < listOfSavedGame.length; i++) {
              System.out.println(i + 1 + ") " + listOfSavedGame[i].getName());
          }

          // Player input
          boolean notchosen = true;
          do {
              String input = sc.nextLine();
              int inputInt = Integer.parseInt(input);
              System.out.println();

              if (input.equals("0") || input.equals("0)")
                      || input.equalsIgnoreCase("back")) {
                  return;
              }
              try {

                  if (inputInt >= 1 && inputInt <= listOfSavedGame.length) {

                      organiseLoadGame(listOfSavedGame[inputInt - 1]
                              .getName());

                  } else {
                      System.out
                              .println("No valid input. Please insert the number or the name of your option.");
                  }

              } catch (NumberFormatException e) {
                  System.out
                          .println("No valid input. Please insert the number or the name of your option.");
              }
          } while (notchosen);

          //System.out.println("<<<<<Under construction.>>>>>\n\n");
      }

      private void openProfileMenu() {
          System.out.println("-----Profile Menu-----\n");
          System.out.println("0) back\n1) switch profile\n");

          //Profile data
          System.out.println("Name:\t\t" + current_profile.getPlayerName());
          Score high = current_profile.getHighscore();
          if (high == null) {
              System.out.println("Highscore:\t --");
          } else {
              System.out.println("Highscore:\t" + high.getScore() + "\t\t" + high.getDifficulty());
          }
          System.out.println("Games played:\t" + current_profile.getNbOfGamesPlayed());
          System.out.println("Games solved:\t" + current_profile.getNbOfGamesSolved());
          System.out.println("Time played:\t" + current_profile.getTimeOfPlay() / 60000);


          // Player input
          boolean notchosen = true;
          do {
              String input = sc.nextLine();
              System.out.println();

              if ((input.equalsIgnoreCase("back")) || (input.equalsIgnoreCase("0")) || (input.equalsIgnoreCase("0)"))) {
                  return;
              } else if ((input.equalsIgnoreCase("switch profile")) || (input.equalsIgnoreCase("1")) || (input.equalsIgnoreCase("1)")) || input.equalsIgnoreCase("switch")) {
                  openSwitchProfileMenu();
                  return;
              }
          } while (notchosen);
      }

      /**
       * Display a menu, in which all exising profiles are displayed, can be
       * choosen, new created and some deleted
       */
      private void openSwitchProfileMenu() {

          System.out.println("-----Edit Profiles-----\n");
          System.out.println("<number> \t-> load corresponding profile\n"
                  + "DELETE <number>\t-> deletes corresponding profile\n"
                  + "CREATE <name>\t-> creates a new profile with name <name>\n");
          System.out.println("0) back");

          //Print Profiles
          for (int i = 0; i < profiles.size(); i++) {
              System.out.println(i + 1 + ") "
                      + profiles.get(i).getPlayerName());
          }
          System.out.println();


          // Player input
          boolean notchosen = true;
          do {
              String input = sc.nextLine();
              String lowercase = input.toLowerCase();


              if ((input.equalsIgnoreCase("back")) || (input.equalsIgnoreCase("0")) || (input.equalsIgnoreCase("0)"))) {
                  return;


              } else if (lowercase.startsWith("delete ")) {
                  String name = input.substring(7);
                  Profile del = findSpecificProfileInProfiles(name);

                  if (del != null) {

                      //security ask
                      System.out.println("\nDo you really want to delete " + del.getPlayerName() + "? (yes/no)");
                      input = sc.nextLine();

                      if ((input.equalsIgnoreCase("yes")) || (input.equalsIgnoreCase("y"))) {
                          profiles.remove(del);
                          System.out.println(del.getPlayerName() + " is now removed");

                          if (del == current_profile) {
                              if (profiles.size() > 0) {
                                  current_profile = profiles.get(0);

                              } else {
                                  current_profile = new Profile("Default");
                              }
                              System.out.println(current_profile.getPlayerName() + " is now the active profile");
                              current_profile.setActive(true);
                          }

                      } else {
                          System.out.println(del.getPlayerName() + " was not removed");
                      }

                      //profile not found
                  } else {
                      System.out.println("Sorry, we does not found a matching profile to " + name);
                  }



              } else if (lowercase.startsWith("create ")) {
                  String name = input.substring(7);

                  //test whether an profile with the same name(not case sensitive) already exists
                  boolean alreadyexist = false;
                  for (Profile p : profiles) {
                      if (name.equalsIgnoreCase(p.getPlayerName())) {
                          alreadyexist = true;
                          break;
                      }
                  }

                  if (alreadyexist) {
                      System.out.println("A profile with this name already exists. Please use another name");
                  } else {
                      current_profile.setActive(false);
                      current_profile = new Profile(name);
                      current_profile.setActive(true);
                      profiles.add(current_profile);
                      notchosen = false;
                  }


              } else {
                  Profile next = findSpecificProfileInProfiles(input);
                  if (next != null) {
                      current_profile.setActive(false);
                      current_profile = next;
                      current_profile.setActive(true);
                      System.out.println(current_profile.getPlayerName() + " is now the active profile.");
                      notchosen = false;

                  } else {
                      System.out.println("There is now profile matching for: " + input);
                  }
              }


          } while (notchosen);
      }

      private Profile findSpecificProfileInProfiles(String identifier) {
          for (int i = 0; i < profiles.size(); i++) {
              if ((identifier.equalsIgnoreCase(String.valueOf(i + 1)))
                      || (identifier.equalsIgnoreCase(String.valueOf(i + 1) + ")"))
                      || (identifier.equalsIgnoreCase(profiles.get(i).getPlayerName()))
                      || (identifier.equalsIgnoreCase(String.valueOf(i + 1) + ") " + profiles.get(i).getPlayerName()))) {

                  return profiles.get(i);
              }
          }
          return null;
      }

      /**
       * Displays the highscore
       */
      private void openHighScoresMenu() {
          System.out.println(highscore_items);

          for (int i = 0; i < highscore.length; i++) {
              if (highscore[i] != null) {
                  System.out.println((i + 1) + ".\t" + highscore[i].getName() + "\t\t" + highscore[i].getScore());
              } else {
                  System.out.println((i + 1) + ".\tempty");
              }
          }
          System.out.println();

          String input;
          do {
              input = sc.nextLine();

              if ((input.equalsIgnoreCase("back")) || (input.equalsIgnoreCase("0")) || (input.equalsIgnoreCase("0)"))) {
                  return;

              } else if ((input.equalsIgnoreCase("delete highscore")) || (input.equalsIgnoreCase("1")) || (input.equalsIgnoreCase("1)")) || input.equalsIgnoreCase("delete")) {

                  System.out.println("Do you really want to delete the highscore? (yes/no)");
                  input = sc.nextLine();
                  if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                      highscore = new Score[10];
                      System.out.println("The highscore has been reseted.");
                      return;
                  } else {
                      System.out.println("The highscore is not reseted.");
                  }


              } else {
                  System.out.println("No valid command here");
              }
          } while (true);

      }
  }

    /**
     * Three things can cause a game to end. Victory(won), death(loast) or just
     * quiting(stopped). Depending on the kind of end, different things have to
     * be done
     */
    private enum GameEndType {

        WON, LOST, STOPPED;
    }
    /**
     * stores the current version of the game
     */
    public static final double VERSION = 1.0;
    /**
     * menu object. Organizes the game menu. Open "menu.openMainMenu" to start
     * the menu
     */
    private GameMenu menu;
    /**
     * Contains the 10 best scores reached in this game orderd decreasing.
     * (First element has the highest score)
     */
    private Score[] highscore;
    /**
     * Stores the list of all existing profiles.
     */
    private ArrayList<Profile> profiles;
    /**
     * Holds the currently used profile
     */
    private Profile current_profile = null;
    //VARIABLES OF ONE SPECIFIC GAME//
    /**
     * Stores the difficulty of the current game. Will be set in
     * "GameMenu.openNewGame" or "GameMenu.openLoadGameMenu"
     */
    public Difficulty diff;
    /**
     * Stores the player of the current game. Will be set in
     * "GameMenu.openNewGame" or "GameMenu.openLoadGameMenu"
     */
    private Player player;
    /**
     * Stores the time played at this game;
     */
    private long timeplayed;
    
    /**
     * Is used everywhere in the game to get the user input.
     * Is closed when closing the program.
     */
    public static final Scanner sc = new Scanner(System.in);
    
    /**
     * 
     */
    public static MyBot myRobot;

    /**
     * CONSTRUCTOR Creates the main object of the game, which controls
     * everything. (Does not create one special cube with player). The creation
     * of the object does not start the game. To start CubeEscape the method
     * CubeEscape.openMainMenu has to be called.
     */
    public CubeEscape() {
    	
        MyRoom.setRoomEnterStrings(IO.loadScripts());
        Riddle.loadRiddlesFromFile();
        highscore = IO.loadScore();
        loadProfiles();
        menu = new GameMenu();
        this.setParser(new MyParser());
        this.setFinished(false);
    }

    /**
     * Open the main menu of CubeEscape From the main menu every thing else will
     * be started (new game, load game, credits)
     */
    public void openMainMenu() {

        menu.openMainMenu();

        printGoodbye();

    }

    /**
     * Initialized a new cube for the game.
     */
    public void initializeNewCube() {
        /*
         * It may occur that a cube is not solvable, then null is returned.
         */
        do {
            this.setCurrentRoom(MapGenerator.generateRandomMap(this.diff));

        } while ((getCurrentRoom() == null));
        ArrayList<MyRoom> path = MyCrawler.findShortestPath((MyRoom) this.getCurrentRoom(), Roomtype.EXIT);
        for(MyRoom m: path){
        	System.out.println(m.shortDescription() + "\t" + m.getRoomType());
        }

    }

    /**
     * Loads the existing, stored profiles and sets the current profile. If no
     * profiles are found, a default profile will be created
     */
    private void loadProfiles() {
        profiles = IO.loadProfiles();

        if (profiles.size() == 0) {
            current_profile = new Profile("Default");
            profiles.add(current_profile);
            return;
        }
        for (Profile p : profiles) {
            if (p.getActive()) {
                current_profile = p;
                return;
            }
        }

        current_profile = profiles.get(0);
    }

    /**
     * Initialized a new cube for the game from a saved game.
     */
    public void initializeCubeFromSavedGame(String savedGameFileName) {
        setCurrentRoom((SaveRoom) IO.loadGame(savedGameFileName)[0]);
    }

    /**
     * Main game routine. Loops until end of play.
     */
    @Override
    public void playGame() {
        long starttime = System.currentTimeMillis();
        MyCommand command; // the last command entered by the user

        // Game Introduction
        System.out.println();
        printWelcome();
        try {
            ((MyRoom) getCurrentRoom()).enter(player);
        } catch (Exception e) {
        }


        // repeatedly read commands and execute them until the game is over
        while (!isFinished()) {
            // System.out.println();
            System.out.println(getCurrentRoom().longDescription());
            System.out.println();

            // Parse Command and execute
           
            // here the Robot takes the relay if it is currently active
			if (CubeEscape.myRobot != null && CubeEscape.myRobot.isActive()) {
				command = CubeEscape.myRobot.getMovingCommand((MyRoom) getCurrentRoom());
			} else {
	            command = ((MyParser) getParser()).getCommand();
			}   
            
            if (command != null) {
                Object result;

                try {
                    result = command.execute(player, (MyRoom) getCurrentRoom());

                    // depending on return type, some actions will be performed
                    if (result instanceof MyRoom) {
                        setCurrentRoom((MyRoom) result);

                        if (((MyRoom) getCurrentRoom()).getRoomType() == Roomtype.EXIT) {
                            organiseGameEnd(starttime, GameEndType.WON);
                        }
                    }

                    if (result instanceof Boolean) {
                        if (!(boolean) result) {
                            organiseGameEnd(starttime, GameEndType.STOPPED);
                        }
                    }

                } catch (PlayerDeathException ex) {
                    System.out.println(ex.getMessage() + "\n\n");
                    organiseGameEnd(starttime, GameEndType.LOST);
                } catch (Exception ex) {
                }

            }

        }
        // some bye bye words...
        //printGoodbye();
    }

    /**
     * Organise the start of a new game. This includes: create a player, set
     * finished false, set timeplayed to 0, set difficulty, adjust riddles to
     * difficulty and initialize a cube.
     *
     * @param playername Name of the player
     * @param diff Difficulty for this game
     */
    private void organiseStarNewGame(String playername, Difficulty diff) {
        this.diff = diff;
        this.player = new Player(playername, new ArrayList<Item>(), 10);
        this.timeplayed = 0;
        this.setFinished(false);
        Riddle.adjustRiddlesSolveTimeToDifficulty(diff);

        // initialize new map
        initializeNewCube();
        current_profile.increaseNbOfGamesPlayed();
        IO.saveProfile(profiles);
    }
    
    /** Organise the start of game as a tutorial, meaning, with a bot playing it instead of the Player.
     * Then the player can see all the different challenges in the cube.     * 
     */
    private void organiseTutorial(){
        this.diff = Difficulty.TUTORIAL;
        this.player = new Player("Tutorial", new ArrayList<Item>(), 10);
        this.timeplayed = 0;
        this.setFinished(false);
        Riddle.adjustRiddlesSolveTimeToDifficulty(diff);

        // initialize new map
        setCurrentRoom(MapGenerator.returnFixedMap());
        //bootBot(player);
        //MyBot.getMyBot().setActive(true);
        playGame();
    }

    /**
     * Load the saved elements player and difficult from a previously saved game.
     */
    private void organiseLoadGame(String gamename) {
        Object[] read = IO.loadGame(gamename);
        // Load of Player, difficulty
        player = (Player) read[1];
        this.diff = (Difficulty) read[2];
        Riddle.adjustRiddlesSolveTimeToDifficulty(diff);

        // initialize new map
        initializeCubeFromSavedGame(gamename);

        // start game
        setFinished(false);
        playGame();
    }

    /**
     * Organised what has to happend if the game ends. Reached exit room, or
     * died trying.
     *
     * @param won if the game ends by reaching exit = true, if ends by dead =
     * false
     */
    private void organiseGameEnd(long sessiontime, GameEndType get) {
        setFinished(true);

        if (get == GameEndType.LOST) {
            createScore(sessiontime, false, myRobot.hasBotBeenActivated());
            printGameOverText();
            printGameOverImg();
        } else if (get == GameEndType.WON) {
            createScore(sessiontime, true, myRobot.hasBotBeenActivated());
            current_profile.increaseNbOfGamesSolved();
            printExitReached();
        }
        current_profile.addTime((new Date()).getTime() - sessiontime);
        
        MyBot.getMyBot().setActive(false);

        IO.saveProfile(profiles);
        IO.saveHighScore(highscore);
    }

    /**
     * Creates a score for the currently finished game and put it in the
     * highscore, if it is good enough
     *
     * @param sessiontime time the games was played
     * @param won whether the player won or lost
     * @param bot whether the bot has been used or not
     */
    private void createScore(long sessiontime, boolean won, boolean bot) {
        timeplayed += System.currentTimeMillis() - sessiontime;
        int time = (int) timeplayed;


        Score s = new Score(player.getName(), time, countRoomVisited((MyRoom) getCurrentRoom(), new ArrayList<MyRoom>()), diff, won, bot);

        int i = 0;
        while ((highscore[i] != null) && (highscore[i].getScore() > s.getScore())) {
            i++;
        }
        for (int k = highscore.length - 1; k > i; k--) {
            highscore[k] = highscore[k - 1];
        }
        highscore[i] = s;
    }

    /**
     * counts recursivly the number of rooms visited Starts with one room, calls
     * iteself for every not visited neighbour.
     *
     * @param r room of the current recursiv step
     * @param checked store which rooms are already testet (for the first call
     * use an empty ArrayList)
     * @return number of visited rooms
     */
    private int countRoomVisited(MyRoom r, ArrayList<MyRoom> checked) {
        if ((!checked.contains(r)) && r.getVisited()) {

            int visited = 1;
            checked.add(r);
            for (String s : MyRoom.DIRECTION) {
                if (r.hasExit(s)) {
                    visited += countRoomVisited((MyRoom) r.nextRoom(s), checked);
                }
            }
            return visited;

        } else {
            return 0;
        }
    }

    /**
     * Print out the opening message for the player.
     */
    protected void printWelcome() {
        System.out.println("Helle, my dear " + player.getName() + ".");
    	System.out.println("Welcome in our Cube. It's not a coincidence if you're here.");
    	System.out.println("As you may already have heard, the cube is filled with many different rooms.");
        System.out.println("Some are traps, most of them actually, and you're likely to die ... or worst!"
                + "\nSome rooms help you by providing"
                + " items to disable traps. Every room has its own code.\nIf you"
                + " understand the code, you know which room you will enter.\n"
                + "Explore the Cube, try to survive, and if you're smart, fast and lucky, you might find an exit.\n"
                + "\nDon't worry if you lose some lifes, there is only one important"
                + " life ... the last one.\n");
        System.out.println("\nType 'help' to see available commands.");
        System.out.println("\n***\t***\t***\t***\n");
    }

    /**
     * Print out the good bye message for the player.
     */
    protected void printGoodbye() {
        System.out.println();
        System.out.println("Thank you for playing...");
    }

    /**
     * Will be called, when the player reaches the exit
     */
    private void printExitReached() {
        System.out.println("The Cube has been conquered.\n YOU DID IT!\n\n"
                + "`8.`8888.      ,8'  ,o888888o.     8 8888      88           8 888888888o.       8 8888 8 888888888o.                 8 8888 8888888 8888888888 \n"
                + " `8.`8888.    ,8'. 8888     `88.   8 8888      88           8 8888    `^888.    8 8888 8 8888    `^888.              8 8888       8 8888       \n"
                + "  `8.`8888.  ,8',8 8888       `8b  8 8888      88           8 8888        `88.  8 8888 8 8888        `88.            8 8888       8 8888       \n"
                + "   `8.`8888.,8' 88 8888        `8b 8 8888      88           8 8888         `88  8 8888 8 8888         `88            8 8888       8 8888       \n"
                + "    `8.`88888'  88 8888         88 8 8888      88           8 8888          88  8 8888 8 8888          88            8 8888       8 8888       \n"
                + "     `8. 8888   88 8888         88 8 8888      88           8 8888          88  8 8888 8 8888          88            8 8888       8 8888       \n"
                + "      `8 8888   88 8888        ,8P 8 8888      88           8 8888         ,88  8 8888 8 8888         ,88            8 8888       8 8888       \n"
                + "       8 8888   `8 8888       ,8P  ` 8888     ,8P           8 8888        ,88'  8 8888 8 8888        ,88'            8 8888       8 8888       \n"
                + "       8 8888    ` 8888     ,88'     8888   ,d8P            8 8888    ,o88P'    8 8888 8 8888    ,o88P'              8 8888       8 8888       \n"
                + "       8 8888       `8888888P'        `Y88888P'             8 888888888P'       8 8888 8 888888888P'                 8 8888       8 8888       \n"
                + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nYou are ready for the new tests.\n"
                + "Now, take your pills.");
    }

    /**
     * Prints the credits of the game.
     */
    private void printCredits() {
        System.out
                .println("CubeEscape\nBINFO Project \"Summer\" 2013\n\n"
                + "written by:\n\tFERBER Patrick\n\tFLOTTERER FX\n\tMEDERNACH Yan\n\n");
    }

    private void printTitle() {
        try {
            System.out.println("*****************************************************************");
            Thread.sleep(100);
            System.out.println("*          _____     _       _____                              *");
            Thread.sleep(100);
            System.out.println("*         |     |_ _| |_ ___|   __|___ ___ ___ ___ ___          *");
            Thread.sleep(100);
            System.out.println("*         |   --| | | . | -_|   __|_ -|  _| .'| . | -_|         *");
            Thread.sleep(100);
            System.out.println("*         |_____|___|___|___|_____|___|___|__,|  _|___| v" + VERSION + "    *");
            Thread.sleep(100);
            System.out.println("*                                             |_|               *");
            Thread.sleep(100);
            System.out.println("*\t\t\t\t\t\t\t\t*");
            Thread.sleep(100);
            System.out.println("*****************************************************************");
            Thread.sleep(100);
            System.out.println("*\t\t         _________________________\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t        / _____________________  /|\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t       / / ___________________/ / |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t      / / /| |               / /  |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t     / / / | |              / / . |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t    / / /| | |             / / /| |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t   / / / | | |            / / / | |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t  / / /  | | |           / / /| | |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t / /_/___| | |__________/ / / | | |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t/________| | |___________/ /  | | |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| _______| | |__________ | |  | | |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| | |    | | |_________| | |__| | |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| | |    | |___________| | |____| |\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| | |   / / ___________| | |_  / /\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| | |  / / /           | | |/ / /\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| | | / / /            | | | / /\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| | |/ / /             | | |/ /\t\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| | | / /              | | ' /\t\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| | |/_/_______________| |  /\t\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t| |____________________| | /\t\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t|________________________|/\t\t\t*");
            Thread.sleep(100);
            System.out.println("*\t\t\t\t\t\t\t\t*");
            Thread.sleep(100);
            System.out.println("*****************************************************************");
            
            System.out.println();
            System.out.println();
            System.out.println();

            System.out.println("In a not so distant future, World's governments crumble under the pressure of prevalent numbers of ultra-nationalists and fundamentalists.");
            System.out.println("The extremists seized the power and started to cull all the elements deemed undesirable for society.");
            System.out.println("The method used are unorthodox: let the unwanted fight for their lives for the amusement of general public in special constructions laid out with traps, called \"Cube\".");
            System.out.println("You are one of these undesirables...");
            System.out.println();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printGameOverText() {
        int x = (int) (2.0 * Math.random()) + 1;
        String[] texts = {
            "\t	 ####    ##   #    # ###### \n"
            + "\t	#    #  #  #  ##  ## #      \n"
            + "\t	#      #    # # ## # #####  \n"
            + "\t	#  ### ###### #    # #      \n"
            + "\t	#    # #    # #    # #      \n"
            + "\t	 ####  #    # #    # ###### \n\n"
            + "\t	 ####  #    # ###### #####  \n"
            + "\t	#    # #    # #      #    # \n"
            + "\t	#    # #    # #####  #    # \n"
            + "\t	#    # #    # #      #####  \n"
            + "\t	#    #  #  #  #      #   #  \n"
            + "\t	 ####    ##   ###### #    # \n",
            "	 ######      ###    ##     ## ########     #######  ##     ## ######## ########  \n"
            + "	##    ##    ## ##   ###   ### ##          ##     ## ##     ## ##       ##     ## \n"
            + "	##         ##   ##  #### #### ##          ##     ## ##     ## ##       ##     ## \n"
            + "	##   #### ##     ## ## ### ## ######      ##     ## ##     ## ######   ########  \n"
            + "	##    ##  ######### ##     ## ##          ##     ##  ##   ##  ##       ##   ##   \n"
            + "	##    ##  ##     ## ##     ## ##          ##     ##   ## ##   ##       ##    ##  \n"
            + "	 ######   ##     ## ##     ## ########     #######     ###    ######## ##     ## \n",
            "\t	 ######      ###    ##     ## ######## \n"
            + "\t	##    ##    ## ##   ###   ### ##       \n"
            + "\t	##         ##   ##  #### #### ##       \n"
            + "\t	##   #### ##     ## ## ### ## ######   \n"
            + "\t	##    ##  ######### ##     ## ##       \n"
            + "\t	##    ##  ##     ## ##     ## ##       \n"
            + "\t	 ######   ##     ## ##     ## ######## \n\n"
            + "\t	 #######  ##     ## ######## ########  \n"
            + "\t	##     ## ##     ## ##       ##     ## \n"
            + "\t	##     ## ##     ## ##       ##     ## \n"
            + "\t	##     ## ##     ## ######   ########  \n"
            + "\t	##     ##  ##   ##  ##       ##   ##   \n"
            + "\t	##     ##   ## ##   ##       ##    ##  \n"
            + "\t	 #######     ###    ######## ##     ## \n"};
        System.out.println();
        if (x == 1) {
            System.out.println(texts[1]);
        } else if (x == 2) {
            System.out.println(texts[2]);
        } else {
            System.out.println(texts[0]);
        }
    }

    public void printGameOverImg() {
        int x = (int) (2.0 * Math.random()) + 1;
        String[] arts = {
            "\t       _____	 \n" + "\t\t     //  +  \\	 \n"
            + "\t    ||  RIP  |	 \n" + "\t\t    ||       |	 \n"
            + "\t    ||       |	 \n" + "\t\t   \\||/\\/\\//\\|/ \n",
            "\t	              ...								\n"
            + "\t	             ;::::;								\n"
            + "\t	           ;::::; :;							\n"
            + "\t	         ;:::::'   :;							\n"
            + "\t	        ;:::::;     ;.							\n"
            + "\t	       ,:::::'       ;           OOO\\			\n"
            + "\t	       ::::::;       ;          OOOOO\\			\n"
            + "\t	       ;:::::;       ;         OOOOOOOO			\n"
            + "\t	      ,;::::::;     ;'         / OOOOOOO		\n"
            + "\t	    ;:::::::::`. ,,,;.        /  / DOOOOOO		\n"
            + "\t	  .';:::::::::::::::::;,     /  /     DOOOO		\n"
            + "\t	 ,::::::;::::::;;;;::::;,   /  /        DOOO	\n"
            + "\t	;`::::::`'::::::;;;::::: ,#/  /          DOOO	\n"
            + "\t	:`:::::::`;::::::;;::: ;::#  /            DOOO	\n"
            + "\t	::`:::::::`;:::::::: ;::::# /              DOO	\n"
            + "\t	`:`:::::::`;:::::: ;::::::#/               DOO	\n"
            + "\t	 :::`:::::::`;; ;:::::::::##                OO	\n"
            + "\t	 ::::`:::::::`;::::::::;:::#                OO	\n"
            + "\t	 `:::::`::::::::::::;'`:;::#                O	\n"
            + "\t	  `:::::`::::::::;' /  / `:#					\n"
            + "\t	   ::::::`:::::;'  /  /   `#					\n",
            "\t                            ,--.	\n"
            + "\t                           {    }	\n"
            + "\t                           K,   }	\n"
            + "\t                          /  ~Y`	\n"
            + "\t                     ,   /   /		\n"
            + "\t                    {_'-K.__/		\n"
            + "\t                      `/-.__L._	\n"
            + "\t                      /  ' /`\\_}	\n"
            + "\t                     /  ' /		\n"
            + "\t             ____   /  ' /		\n"
            + "\t      ,-'~~~~    ~~/  ' /_		\n"
            + "\t    ,'             ``~~~  ',		\n"
            + "\t   (                        Y		\n"
            + "\t  {                         I		\n"
            + "\t {      -                    `,	\n"
            + "\t |       ',                   )	\n"
            + "\t |        |   ,..__      __. Y		\n"
            + "\t |    .,_./  Y ' / ^Y   J   )|		\n"
            + "\t \\           |' /   |   |   ||		\n"
            + "\t  \\          L_/    . _ (_,.'(		\n"
            + "\t   \\,   ,      ^^\"\"' / |      )	\n"
            + "\t     \\_  \\          /,L]     /		\n"
            + "\t       '-_~-,       ` `   ./`		\n"
            + "\t          `'{_            )		\n"
            + "\t              ^^\\..___,.--`		\n"};
        System.out.println();
        if (x == 1) {
            System.out.println(arts[1]);
        } else if (x == 2) {
            System.out.println(arts[2]);
        } else {
            System.out.println(arts[0]);
        }
    }

    public void tst() {
        this.openMainMenu();
        player = new Player("asdf", new ArrayList<Item>(), 6);
        diff = Difficulty.TUTORIAL;
        initializeNewCube();
        playGame();

        sc.close();
    }

    public static void main(String[] args) {
        CubeEscape ce = new CubeEscape();        
        ce.openMainMenu();
//        ce.organiseStarNewGame("asdf", Difficulty.HARD);
//        ce.playGame();
        
        sc.close();
    }
}
