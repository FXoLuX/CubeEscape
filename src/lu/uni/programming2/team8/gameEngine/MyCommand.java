package lu.uni.programming2.team8.gameEngine;

import lu.uni.programming2.team8.entity.Player;
import lu.uni.programming2.team8.room.MyRoom;
import framework.Command;
import java.util.HashMap;

/**
 *
 * @author Patrick, FX
 */
/**
 * This class holds information about a command that was issued by the user. A
 * command currently consists of two strings: a command word and a second word
 * (for example, if the command was "take sword", then the two strings obviously
 * are "take" and "sword").
 *
 * Commands must have valid command words when they are created, wrt. the
 * following rules: - if the user entered an invalid command (a first word that
 * is not known in the game commands list) then the command word is "null"; - if
 * the command has only one word, then the second word is "null".
 */
public abstract class MyCommand extends Command implements Cloneable {

    /**
     * Some commands need additional parameter which are used not only for one 
     * instance of a command but for all. They are stored here.
     */
    private static HashMap<String, Object> static_parameter = new HashMap<>();
    private String[] parameter;
    private int parameter_count;
    private String description;


    /*
     * Create a command object. First and second words must be supplied, but
     * either one or both can be null. The command word is null to indicate that
     * this was a command not recognised by the parser.
     */
    public MyCommand(String commandWord, int parameter_count, String description) {
        super(commandWord, null);
        this.parameter_count = parameter_count;
        this.description = description;
    }

    /*
     * Return the second word of this command.
     */
    public String getFirstParameter() {
        if (parameter != null) {
            return parameter[0];
        }

        return null;
    }

    /*
     * Returns all parameters of the command
     * 
     * @return ordered string array of all parameters
     */
    public String[] getParameters() {
        return parameter;
    }

    public int getParameterCount() {
        return this.parameter_count;
    }

    public String getDescription() {
        return description;
    }

    /*
     * Return true if the command has a second word, false otherwise.
     */
    @Override
    public boolean hasSecondWord() {
        return (parameter != null);
    }

    /**
     * Sets the parameter
     *
     * @param parameter
     */
    public void setParameter(String[] parameter) {
        this.parameter = parameter;
    }

    /**
     * Adds the given Object to the static parameter list. If an parameter is
     * already stored under <name> then the old value will be replaced
     * @param name key under which the parameter is stored
     * @param para parameter
     */
    public static void addStaticParameter(String name, Object para){
        static_parameter.put(name, para);
    }
    
    /**
     * Returns the static parameter which is mapped to <name>. If there is no 
     * parameter stored for <name> null will be returned
     * @param name key for the parameter
     * @return parameter
     */
    public static Object getStaticParameter(String name){
        return static_parameter.get(name);
    }
    
    public abstract Object execute(Player player, MyRoom currentRoom)
            throws Exception;

    /**
     * Creates a new Commandobject that is a clone of the original
     */
    @Override
    public MyCommand clone() {
        try {
            return (MyCommand) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }
}
