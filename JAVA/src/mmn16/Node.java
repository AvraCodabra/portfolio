package mmn16;

/**
 * this is the Node class of the Wired Binary Search Tree
 * @author Tal Ben Avraham
 * @version 2018b
 */
public class Node{
    String name;
    int id;
    protected Node left;
    protected Node right; 
    Node parent;

    public  Node( int id, String name){
        this.name = name;
        this.id = id;
        left = null;
        right = null;
        parent = null;
    }
    /**
     *The method checks if the node's son is threaded or real.
     *The Time complexity is O(1)
     *The Space complexity is O(1)
     * @param  side the son you need to check
     * @return true if real or false if threaded
     */
    public boolean checkSon(String side){
        if (this == null || (side != "right" && side != "left")) {
            System.out.println("checkSon - input is null or without side"+side);
            return false;
        }
        if ((side == "right") && (this.right==null || this == this.right.parent)) return true;
        if ((side == "left") && (this.left==null || this == this.left.parent)) return true;
        return false;
    }
    /**
     *The method return the real right son of the node
     *The Time complexity is O(1)
     *The Space complexity is O(1)
     * @return the real right son (pointer) or null if there is no real son
     */
    public Node getRight(){
        if (this == null) {
            System.out.println("getRight-input is null");
            return null;
        }
        if (this.checkSon("right")) return this.right;
        else return null;
    }
    /**
     *The method return the real left son of the node
     *The Time complexity is O(1)
     *The Space complexity is O(1)
     * @return the real left son (pointer) or null if there is no real son
     */
    public Node getLeft(){
        if (this == null) {
            System.out.println("getLeft-input is null");
            return null;
        }
        if (this.checkSon("left")) return this.left;
        else return null;
    }
    /**
     *The method copy the data of x into this node.
     *The Time complexity is O(1)
     *The Space complexity is O(1)
     * @param  x the node you whant to copy the data from
     */
    public void copyData(Node x){
        if (x != null) {
            this.name = x.name;
            this.id = x.id;
        }   
    }
    /**
     * Return a string representation of the node.
     * for example:
     * ID: 12345678 , name: openU
     * @return String representation of the node.
     */
    public String toString(){
        String s = "ID: "+id+", Name: "+name;
        return new String(s);
    }
}

