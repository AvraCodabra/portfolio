package mmn16;


import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class MyController {
	
	//Buttons
	public Button buttonInsert;
	public Button buttonDelete;
	public Button buttonSuccessor;
	public Button buttonPredecessor;
	public Button buttonSearch;
	public Button buttonMaximum;
	public Button buttonMinimum;
	public Button buttonMedian;
	public Button buttonInorder;
	public Button buttonPreorder;
	public Button buttonPostorder;
	
	
	//Fields
	public TextField tf_ID;
	public TextField tf_name;
	public TextField tf_outPut;
	public TextArea ta_outPut;
	//label
	//public Label tf_outPut;
	
	//On Click's function
	public void onInsertClick() {
		if (isInteger(tf_ID.getText())) {
			int id = Integer.parseInt(tf_ID.getText());
			String name = tf_name.getText();
				if (Main.tree.search(id) == null) {
				Main.tree.insert(id, name);
				tf_outPut.setText("Insert ID: "+id+"  name: "+name);
				tf_name.setText("");
				tf_ID.setText("");
			} else tf_outPut.setText("this ID Already exists");
		}
		else tf_outPut.setText("the ID value is not integer");
	}
	
	public void onDeleteClick() {
		if (isInteger(tf_ID.getText())) {
			int id = Integer.parseInt(tf_ID.getText());
			Node del = Main.tree.search(id);	
			if (del != null) {
				Main.tree.delete(del);
				tf_outPut.setText("Delete ID: "+del.id+"  name: "+del.name);
				tf_name.setText("");
				tf_ID.setText("");
			}
		}
		else tf_outPut.setText("the ID value is not integer");
	}

	public void onSuccessorClick() {
		if (isInteger(tf_ID.getText())) {
			int id = Integer.parseInt(tf_ID.getText());
			Node x = Main.tree.search(id);	
			if (x != null) tf_outPut.setText("the Successor is: "+ Main.tree.successor(x));
			tf_name.setText("");
			tf_ID.setText("");
		}
		else tf_outPut.setText("the ID value is not integer");
	}
	
	public void onPredecessorClick() {
		if (isInteger(tf_ID.getText())) {
			int id = Integer.parseInt(tf_ID.getText());
			Node x = Main.tree.search(id);	
			if (x != null)  tf_outPut.setText("the Predecessor is: "+ Main.tree.predecessor(x));
			tf_name.setText("");
			tf_ID.setText("");
		}
		else tf_outPut.setText("the ID value is not integer");
	}
	

	public void onSearchClick() {
		if (isInteger(tf_ID.getText())) {
			int id = Integer.parseInt(tf_ID.getText());
			Node x = Main.tree.search(id);	
			if (x != null)  tf_outPut.setText("this ID exists");
			else tf_outPut.setText("this ID dosent exists");
			tf_name.setText("");
			tf_ID.setText("");
		}
		else tf_outPut.setText("the ID value is not integer");
	}
	
	public void onMaximumClick() {
		if (Main.tree.root == null) tf_outPut.setText("the tree is empty");
		else tf_outPut.setText("the Maximum is: "+ Main.tree.maximum());
	}
	
	public void onMinimumClick() {
		if (Main.tree.root == null) tf_outPut.setText("the tree is empty");
		else tf_outPut.setText("the Minimum is: "+ Main.tree.minimum());
	}
	
	public void onMedianClick() {
		if (Main.tree.root == null) tf_outPut.setText("the tree is empty");
		else tf_outPut.setText("the Median is: "+ Main.tree.median());
	}
	
	public void onInorderClick() {
		Main.tree.inorderTW();
		ta_outPut.setText(Main.tree.HelpMePrint);

	}
	public void onPreorderClick() {
		Main.tree.preorderTW();
		ta_outPut.setText(Main.tree.HelpMePrint);
	}
	public void onPostorderClick() {
		Main.tree.postorderTW();
		ta_outPut.setText(Main.tree.HelpMePrint);
		
	}



	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}

}