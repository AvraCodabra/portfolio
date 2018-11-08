package mmn16;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	static ThreadedBTS tree = new ThreadedBTS();
	
    public static void main(String[] args) {
    	
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("Tal's Treaded Binary Search Tree");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        
        primaryStage.show();
    }


}