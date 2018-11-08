package mmn16;


/**
 * this is the Threaded Binary Search Tree class 
 * @author Tal Ben Avraham
 * @version 2018b
 */

public class ThreadedBTS{
    int numNode;
    Node root, med;
    String HelpMePrint = "";  // for the GUI only
    /**
     *constructor
     */
    public ThreadedBTS(){
        root = null;
        med = null;
        numNode = 0;
    }

    /**
     *The method insert the input data to the Threaded Binary Search Tree
     *The Time complexity is O(h) = h is the height of the tree.
     *The Space complexity is O(1)
     * @param  id  the student ID (the key for this Binary Search Tree)
     * @param  name  the student's name
     */
    public void insert(int id, String name){
        Node newNode = new Node(id,name);
        insert(newNode);
    }
    /**
     *The method insert a legal node to the Threaded Binary Search Tree
     *The Time complexity is O(h) = h is the height of the tree.
     *The Space complexity is O(1)
     * @param  z  the node for insertion.
     */
    public void insert(Node z){
        Node y = null;
        Node x = root;
        while (x != null){
            y = x;
             if (z.id < x.id) x = x.getLeft();
                    else      x = x.getRight(); 
        }
        z.parent = y;
        
        if (y == null) root = z;    //the tree is empty
        else if (z.id < y.id) {     // z is going to be a left son.. 
                    z.right = y;    // so the successor is his parent
                    z.left = y.left; // and the predecessor is his parent predecessor before the action
                    y.left = z;
        }  
                else {                 // z is going to be a right son.. 
                    z.right = y.right; // so the successor is his parent successor before the action
                    z.left = y;        // and the predecessor is his parent
                    y.right = z;
                    }

               // fix the median after insert
            if (numNode == 0) // tree was empty
                med = z;
            else if (numNode%2 == 0 && z.id>med.id) // the med is the successor
               med = successor(med);
            else if (numNode%2 == 1 && z.id<med.id) // the med is the predecessor
                med = predecessor(med);
                           
        numNode++; // one more node to the counter
        }

    /**
     *The method delete the node in the Threaded Binary Search Tree
     *the node MUST be in that tree
     *The Time complexity is O(h) = h is the height of the node you delete.
     *The Space complexity is O(1)
     * @param  z  the node for delete.
     */
    public void delete(Node z){
         //
            // saving the keys around the median because "delete" may change data of a node.
            int del = z.id;  // the key that will be delete
            int medKey = med.id;
            int preMedKey = Integer.MIN_VALUE;
            int sucMedKey = Integer.MAX_VALUE;
            if (predecessor(med) != null){
                preMedKey = predecessor(med).id;
            }
            if (successor(med) != null){
                sucMedKey = successor(med).id;
            }
         //                 
        Node y,x;
        if (z.getLeft() == null || z.getRight() == null)   y = z; 
                                                 else      y = successor(z);
        if (y.getLeft() != null) x = y.getLeft();
                            else x = y.getRight(); // x will be the son of y.parent
        if (x != null)  // y have one son 
            x.parent = y.parent;

        if (y.parent == null) root = x; // y is the root 
          else if ( y == y.parent.getLeft()) { // y is a left son
            y.parent.left = x;
            if (x == null)
                y.parent.left = y.left; // y is a left son and a leaf
            }
            else{                           // y is a right son
                y.parent.right = x;
                if (x == null)
                    y.parent.right = y.right; // y is a right son and a leaf
                }

        if (y != z) {z.copyData(y);}// the case z have tow son (copy y's data to z)

        if ((successor(y) != null) && !(successor(y).checkSon("left"))) // y's succsessor is treaded to y (by his left son)
            successor(y).left = predecessor(y); // so now he will treaded to y's predecessor

        if ((predecessor(y) != null) && !(predecessor(y).checkSon("right"))) // y's predecessor is treaded to y (by his right son)
            predecessor(y).right = successor(y); // so now he will treaded to y's succsessor

         //fixing the median
            if (del == medKey){ // delete the median
                if (numNode%2 == 1 ) med = search(preMedKey);
                else med = search(sucMedKey);
            } 
            else if (numNode%2 == 0 && del<medKey){ // delete under the median
                med = search(sucMedKey);
            }
            else if (numNode%2 == 1 && del>medKey){ // delete above the median
                med = search(preMedKey);
            }
            else med = search(medKey);
            numNode--;
    }

    /**
     *The method return the successor of a given node
     *The Time complexity is O(h) = h is the height of the node's right son.
     *The Space complexity is O(1)
     * @param  x  the node your are looking for his successor
     * @return the successor
     */
    public Node successor(Node x){
        if (!(x.checkSon("right"))) return x.right; //his right son threaded to his successor
        else return minimum(x.right);
    }

    /**
     *The method return the predecessor of a given node
     *The Time complexity is O(h) = h is the height of the node's left son.
     *The Space complexity is O(1)
     * @param  x  the node your are looking for his predecessor
     * @return the predecessor
     */
    public Node predecessor(Node x){
        if (!(x.checkSon("left"))) return x.left; //his left son threaded to his predecessor
        else return maximum(x.left);
    }

    /**
     *The method gets an key (ID), and check if it's in the tree
     *The Time complexity is O(h) - h is the height of tree.
     *The Space complexity is O(1)
     * @param  id  the key you need to find in the tree
     * @return the key if found. if not, return null
     */
    public Node search(int id){
        Node tmp = search(id, root);
        if (tmp != null) return tmp;
        System.out.println("not found");
        return null;
    }

    /**
     *this is a private method that make a search for the given key (ID) in the sub-tree of x.
     *The Time complexity is O(h) 
     *The Space complexity is O(1)
     * @param  id  the key you need to find in the sub-tree
     * @param  x  the root of the sub-tree.
     */
    private Node search(int id, Node x){
        while (x != null && id != x.id){
            if (id < x.id) x = x.getLeft();
            else x = x.getRight();
       }
        return x;
    }
    /**
     *The method return the minimum key in the tree
     *The Time complexity is O(h) - h is the height of tree.
     *The Space complexity is O(1)
     * @return the minimum ID in the tree
     */
    public Node minimum(){
        if (root != null) return minimum(root);
        System.out.println("you asked for a minimum of an empty tree");
        return null;
    }
    /**
     *this is a private method that return the minimum key in a given sub-tree.
     * @param  x  the root of the sub-tree.
     * @return the minimum ID in the sub-tree.
     */
    private Node minimum(Node x){
        if (x == null) return null;
        while (x.getLeft() != null) {x = x.getLeft();};
        return x;
    }
    /**
     *The method return the maximum key in the tree
     *The Time complexity is O(h) - h is the height of tree.
     *The Space complexity is O(1)
     * @return the maximum ID in the tree
     */
    public Node maximum(){
        if (root != null) return maximum(root);
        System.out.println("you asked for a maximum of an empty tree");
        return null;
    }
    /**
     *this is a private method that return the maximum key in a given sub-tree.
     * @param  x  the root of the sub-tree.
     * @return the maximum ID in the sub-tree.
     */
    private Node maximum(Node x){
        if (x == null) return null;
        while (x.getRight() != null) {x = x.getRight();};
        return x;
    }

    /**
     *this is method return the median node (according to the key) in a the threaded tree.
     *The Time complexity is O(1) 
     *The Space complexity is O(1)
     * @return the median node in the tree.
     */
    public Node median(){
        return med;
    }

    //        TREE   WALKs
    /**
     *Given binary tree, this method print its nodes inorder
     *The Time complexity is O(n)
     * @param  node  the root of the tree.
     */
    public void inorderTW(Node node){
        Node x = minimum(node);
        	while (x != null) {
            System.out.println(x);
            HelpMePrint = HelpMePrint+x+"\n"; // for the GUI only
            x = successor(x);
        }
    }
    /**
     *Given a binary tree, this method print its nodes preorder
     *The Time complexity is O(n)
     * @param  node  the root of the tree.
     */
    public void preorderTW(Node node){
        if (node == null) return;
        System.out.println(node);
        HelpMePrint = HelpMePrint+node+"\n"; // for the GUI only
        preorderTW(node.getLeft());
        preorderTW(node.getRight());
        } 
    /**
     *Given a binary tree, this method print its nodes postorder
     *The Time complexity is O(n)
     * @param  node  the root of the tree.
     */
    public void postorderTW(Node node){
        if (node == null) return;
        postorderTW(node.getLeft());
        postorderTW(node.getRight());
        System.out.println(node);
        HelpMePrint = HelpMePrint+node+"\n"; // for the GUI only
    }
    
    
    
    // for the GUI only
    public void inorderTW() {
    	HelpMePrint ="";
    	inorderTW(root);
    }
    public void preorderTW() {
    	HelpMePrint ="";
    	preorderTW(root);
    }
    public void postorderTW() {
    	HelpMePrint ="";
    	postorderTW(root);
    }
   
}
