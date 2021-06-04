import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/*
	View: View represents the visualization of the data that model contains.
 */

public class App extends Application {

	private final static int initialHeight = 480;
	private final static int initialWidth = 640;

	private static BorderPane root;

	@Override
	public void init() throws IOException{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
    	root = loader.load();
	}
	
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root, initialWidth, initialHeight);
        scene.setUserAgentStylesheet(new File("./DarkStyles.css").toURI().toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("MP3 Audio Player");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}