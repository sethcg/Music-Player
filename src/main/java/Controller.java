import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/*
		Controller: Controller acts on both model and view. 
					It controls the data flow (Songs Played) and updates the view (Buttons) 
					It keeps view and model separate.
 */

public class Controller {
	
	// Variables
	private SongList songlist;
	private Player player;
	
	@FXML
	private ImageView albumCover;
	@FXML private Label albumLabel;

    @FXML 
    private void initialize() {
    	
    	// Initialize Album Cover
    	albumLabel.setText("?");
    	albumLabel.getParent().setId("albumCoverDefaultColor");
    	
    	//Initialize Data Model (SongList)
		this.songlist = songlist;
    }
    
	// Method for Handling MiddleButton SongRequests
	public EventHandler<ActionEvent> createSongRequestEvent(Song song) {
	    return new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent t) {
	        	//if(player != null && player.mediaPlayer.getStatus().equals(Status.PLAYING)) {
	        	//	player.mediaPlayer.stop();
	        	//}
	        	if(player != null) {
	        		player.stop();
	        	}
		        player = new Player(song.getMediaPlayer().get());
		        player.mediaPlayer.play();
		        Image image = song.getAlbumCover().get();
		        setImage(image);
	        }
	    };
	}
	
	// Helper Method for Handling MiddleButton SongRequests
	public void setImage(Image image) {
		if(image == null){
			albumCover.setImage(image);
			albumLabel.getParent().setId("albumCoverDefaultColor");
    		albumLabel.setText("?");
    		return;
    	}
    	albumCover.setImage(image);
    	albumLabel.setText("");
    	albumLabel.getParent().setId("");
    }
	
}