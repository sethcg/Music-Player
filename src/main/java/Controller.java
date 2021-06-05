import com.sun.javafx.stage.StageHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
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
	}

	
	@FXML private BorderPane root;
	@FXML private StackPane TopBar;
	@FXML private Label resizeButton;
	
	
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
	
	//@FXML private StackPane volumeStack;
	@FXML private Slider volumeSlider;
	
	// Song Description Variables
	@FXML private Label titleLabel;
	@FXML private Label artistLabel;

	// Initialize FXML Variables
    @FXML private void initialize() {
    	// Add Song Buttons to Root (BorderPane)
    	root.setCenter(addCenter());
    	
    	// Initialize TopBar
        TopBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = event.getSceneX();
                y = event.getSceneY();
            }
        });
        TopBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	App.stage.setX(event.getScreenX() - x);
            	App.stage.setY(event.getScreenY() - y);
            }
        });
        
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
    	
    	// Initialize AddMP3Button
    	addSongButton.setText("Add MP3 File");
    	
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
    
    private void handleMiddleSongButton(Song song){
    	hasPlayed = true;
    	mediaPlayer.stop();
    	changeSong(song);
        mediaPlayer.play();
        playButton.setStyle("-fx-shape: '" + pauseShape + "';");
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
    	if(hasPlayed) {
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
    	if(hasPlayed) {
    		int currentSongIndex = playlist.getSongList().indexOf(playlist.getCurrentSong());
    		if(currentSongIndex - 1 >= 0) {
    			mediaPlayer.stop();
    			changeSong(playlist.getSongList().get(currentSongIndex - 1));
    			mediaPlayer.play();
    			playButton.setStyle("-fx-shape: '" + pauseShape + "';");
    		}
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
    	App.stage.close();
    }
    
    @FXML
    private void handleAddSongButton(){
    	System.out.println("add song button");
    }
    
    
    /* -------------- PLAYLIST METHODS ---------- */
	
	private SongList createInorderPlaylist(SongList songlist) {
		for(Song song : this.songlist.getSongList()){
			playlist.getSongList().add(song);
		}
		// Initialize a currentSong and Mediaplayer when playlist is created
		if(playlist.isCurrentSongEmpty()) {
			playlist.setCurrentSong(playlist.getSongList().get(0));
			mediaPlayer = new MediaPlayer(playlist.getCurrentSong().getMedia().get());
		}
		return playlist;
	}
    
    
    /* ----------- HELPER METHODS FOR HANDLING BUTTONS  --------- */
    
	public void changeSong(Song song){
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
	
	
	/* ----------- ADD SONG BUTTONS --------- */
	
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
        songButton.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent t) {
	        	handleMiddleSongButton(song);
	        }
	    });
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
        length.textProperty().bind(song.getTotalDurationString());
        
        // Adding the Labels to the correct column
        songInformation.add(title, 0, 0);
        songInformation.add(artist, 1, 0);
        songInformation.add(length, 2, 0);
        return songInformation;
    }
	
}