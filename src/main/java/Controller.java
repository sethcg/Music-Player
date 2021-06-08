import java.util.Random;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/*
	Controller: Acts on the Model and View. Controls the flow of data.
*/

public class Controller {
	
	private double x = 0;
	private double y = 0;
	private double xOffset = 0;
	private double yOffset = 0;
	private boolean canResize = false;
	private Song prevSong = null;

	private boolean hasPlayed = false;
	private final static String playShape = "M 20 20 L 20 80 L 80 50 L 20 20 Z";
	private final static String pauseShape = "M 20 20 L 30 20 L 30 80 L 20 80 L 20 20 M 40 20 L 50 20 L 50 80 L 40 80 Z";
	
	// Variables
	private SongList songlist = new SongList();
	private SongList playlist = new SongList();
	private MediaPlayer mediaPlayer;
	
	// On construction in App via .load(FXML) pointing to the controller, Create the DataModel (SongList)
	public Controller() {
        songlist.loadMusicFromFolder();
        playlist = createInorderPlaylist(songlist);
        //playlist = createRandomPlaylist(songlist);
	}

	
	@FXML private BorderPane root;
	@FXML private StackPane TopBar;
	@FXML private Label resizeButton;
	
	@FXML private Button SettingsButton;
	
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
	
	//@FXML private StackPane volumeStack;
	@FXML private Slider volumeSlider;
	
	// Song Description Variables
	@FXML private Label titleLabel;
	@FXML private Label artistLabel;

	// Initialize FXML Variables
    @FXML private void initialize() {
    	
    	// Initialize the Middle Songs
    	root.setCenter(SongButtonHelper.addCenter(songlist, this));
    	
    	SettingsHelper helper = new SettingsHelper(root);
    	helper.setColorDefaults();
        
        // Resize Screen if MousePress is within 12px of bottom left corner
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getX() > App.stage.getWidth() - 12
                	&& event.getX() < App.stage.getWidth() + 12
                	&& event.getY() > App.stage.getHeight() - 12
                	&& event.getY() < App.stage.getHeight() + 12){
                    canResize = true;
                    x = App.stage.getWidth() - event.getX();
                    y = App.stage.getHeight() - event.getY();
                } else {
                    canResize = false;
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (canResize == true) {
                	App.stage.setWidth(event.getX() + x);
                	App.stage.setHeight(event.getY() + y);
                }
            }
        });
    	
    	// Initialize Volume Slider
    	volumeSlider.setMax(100);
    	volumeSlider.setValue(50);		// Start at 50% Volume (Gradient must also be set in CSS Styles)
    	volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
    		public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                String style = String.format("-fx-background-color: linear-gradient(to right, -fx-accent %d%%, -fx-slider-track %d%%);",
                		newValue.intValue(), newValue.intValue());
                volumeSlider.lookup(".track").setStyle(style);
                mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
            }
        });
    	
    	// Initialize Song Description
    	titleLabel.setText("Title");
    	artistLabel.setText("Artist");
    	
    	// Initialize Play Button
    	playButton.setStyle("-fx-shape: '" + playShape + "';");
    	
    	// Initialize Duration Bar
    	currDuration.setText("0:00");
    	totalDuration.setText("0:00");
    	
    	// Initialize durationBar
    	durationProgress.setProgress(0.0);
    	
    	// Initialize Album Cover
    	albumLabel.setText("?");
    	albumLabel.getParent().setId("albumStackDefault");
    }
    
    
    /* METHODS FOR HANDLING BUTTONS */
    
    protected void handlePlaySongButton(Song song){    	
    	hasPlayed = true;
    	mediaPlayer.stop();
    	changeSong(song);
        mediaPlayer.play();
        playButton.setStyle("-fx-shape: '" + pauseShape + "';");
    }
    
    protected void deleteSong(Song song){
    	if(prevSong == song) {
    		mediaPlayer.stop();
    		prevSong = null;
    	}
    	songlist.getSongList().remove(song);
    	songlist.deleteSong(song);
    	playlist.getSongList().remove(song);
    	root.setCenter(SongButtonHelper.addCenter(songlist, this));
    }
    
    
    @FXML
    private void handlePlayButton(){
    	switch(mediaPlayer.getStatus()) {
    		case PAUSED:
    			mediaPlayer.play();
    			playButton.setStyle("-fx-shape: '" + pauseShape + "';");
    			break;
    		case PLAYING:
    			mediaPlayer.pause();
    			playButton.setStyle("-fx-shape: '" + playShape + "';");
    			break;
    		default:
    			break;
    	}
    }
    
    @FXML
    private void handleNextButton(){
    	if(prevSong != null) {
    		int currentSongIndex = playlist.getSongList().indexOf(playlist.getCurrentSong());
    		if(currentSongIndex + 1 < playlist.getSongList().size()) {
    			mediaPlayer.stop();
    			changeSong(playlist.getSongList().get(currentSongIndex + 1));
    			mediaPlayer.play();
    			playButton.setStyle("-fx-shape: '" + pauseShape + "';");
    		}
    	}
    }
    
    @FXML
    private void handleBackButton(){
    	if(prevSong != null) {
    		int currentSongIndex = playlist.getSongList().indexOf(playlist.getCurrentSong());
    		if(currentSongIndex - 1 >= 0 && (int) mediaPlayer.getCurrentTime().toSeconds() <= 3) {
    			currentSongIndex--;
    		}
    		mediaPlayer.stop();
    		changeSong(playlist.getSongList().get(currentSongIndex));
    		mediaPlayer.play();
    		playButton.setStyle("-fx-shape: '" + pauseShape + "';");
    	}
    }
    
    @FXML
    private void handleMinButton(){
    	App.stage.setIconified(true);
    }
    
    @FXML
    private void handleMaxButton(){
    	if(App.stage.isFullScreen()) {
    		App.stage.setFullScreen(false);
    	}else {
        	App.stage.setFullScreenExitHint("");
        	App.stage.setFullScreen(true);
    	}
    }
    
    @FXML
    private void handleCloseButton(){
    	// Save Color Choice Before Closing
		SettingsHelper.saveColorPreference();
    	App.stage.close();
    }
    
    @FXML
    private void handleSettingsButton(){
    	if(root.getCenter().getId() == "SongScrollPane") {
    		root.setCenter(SettingsHelper.addCenter(this));
    	}else {
    		root.setCenter(SongButtonHelper.addCenter(songlist, this));
    	}
    }
      
    /* -------------- PLAYLIST METHODS ---------- */
	
	private SongList createInorderPlaylist(SongList songlist) {
		for(Song song : this.songlist.getSongList()){
			playlist.getSongList().add(song);
		}
		initializeSong(playlist.getSongList().get(0));
		return playlist;
	}
	
	private SongList createRandomPlaylist(SongList songlist) {
		playlist = songlist;
        Random r = new Random();
        
        for (int index = songlist.getSongList().size() - 1; index > 1; index--) {

            int newSpot = r.nextInt(index);
            
            Song temp = playlist.getSongList().get(index);
            playlist.getSongList().set(index, playlist.getSongList().get(newSpot));
            playlist.getSongList().set(newSpot, temp);
        }
     	initializeSong(playlist.getSongList().get(0));
        return playlist;
	}
	
	// Initialize a currentSong and Mediaplayer when playlist is created
	private void initializeSong(Song song) {
     	if(playlist.isCurrentSongEmpty()) {
     		playlist.setCurrentSong(song);
     		mediaPlayer = new MediaPlayer(playlist.getCurrentSong().getMedia().get());
     	}
	}
    
    
    /* ----------- HELPER METHODS FOR HANDLING BUTTONS  --------- */
    
	public void changeSong(Song song){
		// Update the previous SongButton and Current SongButton Style
    	StackPane songStack = song.getSongStack();
    	if(prevSong != null && prevSong.getSongStack().lookup("#SongButtonLabelPlaying") != null) {
    		prevSong.getSongStack().lookup("#SongButtonLabelPlaying").setId("SongButtonLabel");
    	}
    	songStack.lookup("#SongButtonLabel").setId("SongButtonLabelPlaying");
    	prevSong = song;
    	
    	// Change Song in MediaPlayer
    	playlist.setCurrentSong(song);
    	mediaPlayer = new MediaPlayer(playlist.getCurrentSong().getMedia().get());
        setImage(song);
        setSongDescription(song);
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
				double progress = mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis();
				durationProgress.setProgress(progress);
				int seconds = (int) mediaPlayer.getCurrentTime().toSeconds();
				currDuration.setText(("" + (seconds / 60) + ":" + ((seconds % 60) <= 9 ? "0" + (seconds % 60) : "" + (seconds % 60))));
			}
   	    });
	}
	
	// Update Title and Artist
	public void setSongDescription(Song song) {
		totalDuration.setText(song.getTotalDurationString().get());
		titleLabel.setText(song.getTitle().get());
		artistLabel.setText(song.getArtist().get());
	}
	
	// Update Album Image
	public void setImage(Song song) {
		Image image = song.getAlbumCover().get();
		if(image == null){
			albumCover.setImage(image);
			albumLabel.getParent().setId("albumStackDefault");
    		albumLabel.setText("?");
    		return;
    	}
    	albumCover.setImage(image);
    	albumLabel.setText("");
    	albumLabel.getParent().setId("");
    }
	
}