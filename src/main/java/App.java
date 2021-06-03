import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/*
	View: View represents the visualization of the data that model contains.
 */

public class App extends Application {

	private final static int initialHeight = 480;
	private final static int initialWidth = 640;
	
	private static SongList songlist = new SongList();
	private static BorderPane root;
	private Controller controller;

	@Override
	public void init() throws IOException{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
    	root = loader.load();
    	controller = loader.getController();
    	
        songlist.loadMusicFromFolder();
	}
	
    @Override
    public void start(Stage primaryStage) {
    	
    	root.setCenter(addCenter());		//Add SongButtons
        
        Scene scene = new Scene(root, initialWidth, initialHeight);
        scene.setUserAgentStylesheet(new File("./DarkStyles.css").toURI().toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("MP3 Audio Player");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    

    //HELPER METHODS:

    
    // Creates the BorderPane Center i.e. (Button, and Song Info)
    public ScrollPane addCenter() {
    	GridPane grid = new GridPane();
    	grid.setId("SongMenu");

    	ColumnConstraints songColumn = new ColumnConstraints();
        songColumn.setPercentWidth(100);
        grid.getColumnConstraints().addAll(songColumn);
    	
        // Add each Song Button with Labels
        for(int i = 0; i < songlist.getSongList().size(); i++){
        	// Place SongLabel and SongButton on top of each other
        	StackPane buttonStack = new StackPane();
        	buttonStack.setId("SongButton");
        	buttonStack.getChildren().add(addSongLabel(songlist.getSongList().get(i)));
        	buttonStack.getChildren().add(addSongButton(songlist.getSongList().get(i)));
        	grid.add(buttonStack, 0, i);
        }

        // SongMenu ScrollBar
        ScrollPane songScroll = new ScrollPane();
        songScroll.setId("SongScrollPane");
        songScroll.setContent(grid);
        songScroll.setFitToWidth(true);
        
        return songScroll;
    }
    
    // Add Button to Complex Song Button
    public Button addSongButton(Song song){
        Button songButton = new Button();
        songButton.setOnAction(controller.createSongRequestEvent(song));
        songButton.setPrefWidth(Double.MAX_VALUE);
        songButton.setPrefHeight(32);
        return songButton;
    }
    
    // Add Label to Complex (Confusing :P) Song Button
    public GridPane addSongLabel(Song song){
    	GridPane songInformation = new GridPane();
    	songInformation.setId("SongButtonLabel");
    	
    	// Constraints for the sizing of the Columns i.e. (60%, 30%, 10%)
        ColumnConstraints titleColumn = new ColumnConstraints();
        titleColumn.setPercentWidth(60);
        titleColumn.setHalignment(HPos.LEFT);
        
        ColumnConstraints artistColumn = new ColumnConstraints();
        artistColumn.setPercentWidth(30);
        artistColumn.setHalignment(HPos.LEFT);
        
        ColumnConstraints lengthColumn = new ColumnConstraints();
        lengthColumn.setPercentWidth(10);
        lengthColumn.setHalignment(HPos.RIGHT);
        
        // Adding the Constraints to the grid
        songInformation.getColumnConstraints().addAll(titleColumn, artistColumn, lengthColumn);
        
        Label title = new Label();
        Label artist = new Label();
        Label length = new Label();
        
        // Bind the textProperty to get rid of Loading Bugs*
        title.textProperty().bind(song.getTitle());
        artist.textProperty().bind(song.getArtist());
        length.textProperty().bind(song.getLengthString());
        
        // Adding the Labels to the correct column
        songInformation.add(title, 0, 0);
        songInformation.add(artist, 1, 0);
        songInformation.add(length, 2, 0);
        return songInformation;
    }
    
}