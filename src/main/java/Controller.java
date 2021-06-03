import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/*
	Controller: Acts on the Model and View. Controls the flow of data.
 */

public class Controller {
	
	private final static String playShape = "M 20 20 L 20 80 L 80 50 L 20 20 Z";
	private final static String pauseShape = "M 20 20 L 30 20 L 30 80 L 20 80 L 20 20 M 40 20 L 50 20 L 50 80 L 40 80 Z";
	
	// Variables
	private SongList songlist;
	private Player player;
	
	@FXML private Button addSongButton;
	
	// Album Controls
	@FXML private ImageView albumCover;
	@FXML private Label albumLabel;
	
	
	// Duration Variables
	@FXML private Label currDuration;
	@FXML private Label totalDuration;
	@FXML private ProgressBar durationProgress;
	
	// Control Variables
	@FXML private Button backButton;
	@FXML private Button playButton;
	@FXML private Button nextButton;
	@FXML private Slider volumeSlider;
	
	@FXML private Label titleLabel;
	@FXML private Label artistLabel;

	// Initialize FXML Variables
    @FXML private void initialize() {
    	// Initialize Song Description
    	titleLabel.setText("Title");
    	artistLabel.setText("Artist");
    	
    	// Initialize Play Button
    	playButton.setStyle("-fx-shape: '" + playShape + "';");
    	
    	// Initialize AddMP3Button
    	addSongButton.setText("Add MP3 File");
    	
    	// Initialize Duration Bar
    	currDuration.setText("0:00");
    	totalDuration.setText("0:00");
    	durationProgress.setProgress(0.3);
    	
    	// Initialize Album Cover
    	albumLabel.setText("?");
    	albumLabel.getParent().setId("albumCoverDefaultColor");
    	
    	//Initialize Data Model (SongList)
		this.songlist = songlist;
    }
    
	// Handle MiddleButton SongRequests
	public EventHandler<ActionEvent> createSongRequestEvent(Song song) {
	    return new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent t) {
	        	if(player != null) {
	        		player.stop();
	        	}
		        player = new Player(song.getMediaPlayer().get());
		        player.mediaPlayer.play();
		        Image image = song.getAlbumCover().get();
		        setImage(image);
		        setSongDescription(song.getTitle().get(), song.getArtist().get());
	        }
	    };
	}
	
	// Update Title and Artist
	public void setSongDescription(String title, String artist) {
		titleLabel.setText(title);
		artistLabel.setText(artist);
	}
	
	// Update Album Image
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