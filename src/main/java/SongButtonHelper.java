import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class SongButtonHelper {
	
	// Creates the BorderPane Center i.e. (Button, and Song Info)
    public static ScrollPane addCenter(SongList songlist, Controller controller) {
    	GridPane grid = new GridPane();
    	grid.setId("SongMenu");

    	ColumnConstraints songColumn = new ColumnConstraints();
        songColumn.setPercentWidth(100);
        grid.getColumnConstraints().addAll(songColumn);
    	
        // Add each Song Button with Labels
        for(int i = 0; i < songlist.getSongList().size(); i++){
        	// Place SongLabel and SongButton on top of each other
        	StackPane buttonStack = new StackPane();
        	buttonStack.setId("SongStack");
        	buttonStack.getChildren().add(addSongLabel(songlist.getSongList().get(i)));
        	buttonStack.getChildren().add(addSongButton(songlist.getSongList().get(i), controller));
        	grid.add(buttonStack, 0, i);
        	
        	// Update the referenced songStack in song
        	songlist.getSongList().get(i).setSongStack(buttonStack);
        }

        // SongMenu ScrollBar
        ScrollPane songScroll = new ScrollPane();
        songScroll.setId("SongScrollPane");
        songScroll.setContent(grid);
        songScroll.setFitToWidth(true);
        
        return songScroll;
    }
    
    // Add Button to Complex Song Button
    public static Button addSongButton(Song song, Controller controller){
        Button songButton = new Button();
        songButton.setId("SongButton");
        songButton.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent event) {
	        	controller.handleMiddleSongButton(song);
	        }
	    });
        songButton.setPrefWidth(Double.MAX_VALUE);
        songButton.setPrefHeight(32);
        return songButton;
    }
    
    // Add Label to Complex (Confusing :P) Song Button
    public static GridPane addSongLabel(Song song){
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
        length.textProperty().bind(song.getTotalDurationString());
        
        // Adding the Labels to the correct column
        songInformation.add(title, 0, 0);
        songInformation.add(artist, 1, 0);
        songInformation.add(length, 2, 0);
        return songInformation;
    }
}
