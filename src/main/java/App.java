import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/*
	View: View represents the visualization of the data that model contains.
*/

public class App extends Application {

	private final static int initialHeight = 480;
	private final static int initialWidth = 640;
	private double x, y = 0;

	private static BorderPane root;
	public static Stage stage;
	public static Scene scene;

	@Override
	public void init() throws IOException{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
    	root = loader.load();
	}
	
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(root, initialWidth, initialHeight);
        scene.setUserAgentStylesheet(new File("./DarkStyles.css").toURI().toString());
        stage.setScene(scene);
        this.scene = scene;
    	DraggableHelper.addDraggableListener(stage, "#TopBar");		// Add Drag ability to TopBar
        stage.setTitle("MP3 Audio Player");
        stage.initStyle(StageStyle.UNDECORATED);
        this.stage = stage;
        stage.show();
       
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.out.println("set something on close");
				System.exit(0);
			}	
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}