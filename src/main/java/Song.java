
import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/*
	Model: Model represents an object carrying data. It can also have logic to update controller if its data changes.
*/

public class Song{

	// JavaFX related Attributes
	private final String path;
	
	private StackPane songStack = new StackPane();
	
	private final ObjectProperty<Media> media = new SimpleObjectProperty<Media>(this, "media", null);
	private final ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<MediaPlayer>(this, "mediaPlayer", null);
	private final StringProperty artist = new SimpleStringProperty(this, "artist", "");
	private final StringProperty title = new SimpleStringProperty(this, "title", "");
	private final StringProperty album = new SimpleStringProperty(this, "album", "");

	private final StringProperty totalDurationString = new SimpleStringProperty(this, "totalDurationString", "0:00");
	
	private final ObjectProperty<Image> albumCover = new SimpleObjectProperty<Image>(this, "album cover", null);	// Null album cover handled in Controller

    //Song Constructor
	public Song(String fileLocation){
		this.path = fileLocation;
   		this.media.set(new Media(new File(path).toURI().toString()));
   		this.media.get().getMetadata().addListener(new MapChangeListener<String, Object>() {
   			@Override
   			public void onChanged(Change<? extends String, ? extends Object> change) {
   	    		if (!change.wasAdded()) {
   	        		return;
   	        	}
   	        	bindProperty(change.getKey(), change.getValueAdded());
   	        }
   	    });
   	    this.mediaPlayer.set(new MediaPlayer(media.get()));

   	    // Bind the totalDuration
   	    this.mediaPlayer.get().totalDurationProperty().addListener(new ChangeListener<Duration>() {
   	    	public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
   	    		int seconds = (int) mediaPlayer.get().getTotalDuration().toSeconds();
   	    		totalDurationString.set("" + (seconds / 60) + ":" + ((seconds % 60) == 0 ? "00" : "" + (seconds % 60)));
			}
	    });
   	}
	
	private void bindProperty(String key, Object value){
		switch (key){
			case "title":
				title.setValue((String) value);
				break;
			case "artist":
				artist.setValue((String) value);
				break;
			case "album":
				album.setValue((String) value);
				break;
			case "image":
				albumCover.setValue((Image) value);
				break;
		}
	}
	
	//Getters
	public ObjectProperty<MediaPlayer> getMediaPlayer() {
		return mediaPlayer;
	}
	public ObjectProperty<Media> getMedia() {
		return media;
	}
	public StringProperty getArtist(){
		return artist;
	}
	public StringProperty getAlbum(){
		return album;
	}
	public StringProperty getTitle(){
		return title;
	}
	public ObjectProperty<Image> getAlbumCover(){
		return albumCover;
	}
	public StringProperty getTotalDurationString(){
		return totalDurationString;
	}
	public StackPane getSongStack() {
		return songStack;
	}
	public void setSongStack(StackPane newStack) {
		songStack = newStack;
	}
	public String getPath(){
		return path;
	}
}
