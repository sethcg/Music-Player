import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.media.MediaPlayer;
/*
		Controller: Controller acts on both model and view. 
					It controls the data flow (Songs Played) and updates the view (Buttons) 
					It keeps view and model separate.
 */

public class Controller {
	private SongList songlist ;
	private static MediaPlayer mediaplayer;

	public void initializeSongList(SongList songlist) {
		if (this.songlist != null) {
			throw new IllegalStateException("SongList can only be initialized once!");
		}
			this.songlist = songlist;
	}

	// Handles SongRequestEvent Made By Middle SongButtons
	public static EventHandler<ActionEvent> createSongRequestEvent(Song song) {
	    return new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent t) {
	            // HANDLE SONG REQUEST FROM PRESSING ONE OF MIDDLE SONG BUTTON
	        	System.out.println("Playing: " + song.getTitle().get());
	        	mediaplayer = song.getMediaPlayer().get();
	        	mediaplayer.play();
	        }
	    };
	}	
	
}