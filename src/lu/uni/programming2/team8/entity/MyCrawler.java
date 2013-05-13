/**
 * This class is a crawler that will run through the cube and find a path from
 * the current room, to the exit and also looks for the items locations needed
 * to deactivate the trap on this path.
 *
 *
 * There is no reason for this class to exist, as it is basically just a static
 * method that could have been let in MapGenerator class (as there already another crawling method,
 * slightely different though), but we really love these little crawlers. They are so cute, lovely, and
 * most of the time misunderstood little creature!
 *
 */
package lu.uni.programming2.team8.entity;

import java.util.ArrayList;

/**
 * @author Patrick, FX
 */
import lu.uni.programming2.team8.cubeescape.Item;
import lu.uni.programming2.team8.room.MyRoom;
import lu.uni.programming2.team8.room.MyRoom.Roomtype;

public class MyCrawler extends Entity {

    /**
     *
     */
    private static final long serialVersionUID = 103817754141452154L;
    private static boolean hasCrawlerBeenActivated = false;

    /**
     * Constructor is private, as we don't need to create an object Crawler,
     * we'll just need to call the method crawlTheCube which is static. But we
     * want to yell: "It lives!"
     */
    private MyCrawler(String name, int lifePoints, ArrayList<Item> inventory) {
        super("My Little Crawler", 1, null);
    }

    public static boolean hasCrawlerBeenActivated() {
        return hasCrawlerBeenActivated;
    }

    public static void setHasCrawlerBeenActivated(
            boolean hasCrawlerBeenActivated) {
        MyCrawler.hasCrawlerBeenActivated = hasCrawlerBeenActivated;
    }

    /**
     * This method is actually the crwaler. It recursively run the cube nutil it
     * finds the exit and also store the location of rooms containing antitrap
     * items needed.
     *
     *
     */
    public static ArrayList<MyRoom> crawlTheCube() {
        ArrayList<MyRoom> pathToExit = new ArrayList<>();
        setHasCrawlerBeenActivated(true);


        return pathToExit;

    }

    /**
     * This method searchs the cube for the shortest path from the current room to
     * a room of specific roomtype. Then it will return the path.
     * 
     * WHY NOT A RECURSIVE ALGORITHM:
     * You said you would prefere a recursive solution. The problem is that recursion
     * has no simple approach for breath search. Folowing one path until it is in an dead
     * and then continue from the next farest end. It is easy to implement an
     * algorithm which find a way, but for a shortest path it is useless. We would need
     * to calculate every path (maybe less with some tricks) but it would be near
     * to brute force.
     * For our problem an iterative approach is much more efficient. I think if you
     * look at the code you will agree that this implementation is very efficient.
     * @param room Start room
     * @param find roomtype you want to find
     * @return path from the current room to a room of the type <find>
     */
    public static ArrayList<MyRoom> findShortestPath(MyRoom room, Roomtype find) {

        //initialization
        Node root = new Node(room);
        ArrayList<Node> leaves = new ArrayList<>(); //stores unprocessed leaves
        leaves.add(root);
        ArrayList<MyRoom> visited = new ArrayList<>(); //stores all visited rooms
        visited.add(room);

        
        while (leaves.size() > 0) {
            Node current = leaves.get(0);

            //for every neighbour room
            for (String direction : MyRoom.DIRECTION) {
                if (current.getCurrent().hasExit(direction)) {

                    //is end of chain?
                    MyRoom possible = (MyRoom) current.getCurrent().nextRoom(direction);
                    if (possible.getRoomType() == Roomtype.RAZOR) {
                        continue;
                    } else if (visited.contains(possible)) {
                        continue;
                    }

                    Node next = new Node(possible, current);

                    //found?
                    if ((possible.getRoomType() == find) && (!possible.getDone())) {
                        return convertNodeToArrayList(next);
                        
                    //needs further process
                    } else {
                        visited.add(possible);
                        leaves.add(next);
                    }
                }
            }
            //processed
            leaves.remove(current);
        }

        return null;
    }

    /**
     * Converts the path contained in a node into a arraylist
     * @param n Node that contains the path
     * @return ArrayList of MyRoom. First Element is the startroom, last element the room of the input node
     */
    private static ArrayList<MyRoom> convertNodeToArrayList(Node n){
        //Lists all visited room, starting by the exit(=inverted)
        ArrayList<MyRoom> path = new ArrayList<>();
        while(n.getParent() != null){
            path.add(n.getCurrent());
            n = n.getParent();
        }
        path.add(n.getCurrent());
        
        //inverts the inverted list => correct order
        MyRoom tmp;
        for(int i = 0; i < (path.size()/2); i++){
            tmp = path.get(i);
            path.set(i, path.get(path.size()-i-1));
            path.set(path.size()-i-1, tmp);
        }
        
        return path;
    }

    
    /**
     * This class is needed in the search function to store the path.
     * Every node stores his parent. Thus if we found the exit, we only need
     * the final node to get the whole path.
     * It is much faster than working with ArrayList<ArrayList<MyRoom>>. They
     * would cause a lot of redundance and the creation of this redundance needs
     * a hugh amount of time (We used this in the first try. It worked but had such
     * a complexity that in 50% of the hard cube you could make a coffe or even
     * bake a pizza).
     */
    private static class Node {

        private Node parent;       //parent of this node
        private MyRoom current;  //value of this node      

        public Node(MyRoom current) {
            this.current = current;
        }

        public Node(MyRoom current, Node parent) {
            this.current = current;
            this.parent = parent;
        }

        public Node getParent(){
            return parent;
        }
        public MyRoom getCurrent() {
            return current;
        }

    }
}
