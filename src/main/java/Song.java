import java.awt.image.BufferedImage;
import java.io.File;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.MapChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/*
		Model: 	Model represents an object carrying data. 
				It can also have logic to update controller if its data changes.
 */

public class Song {
	
	private static final BufferedImage DEFAULT_ALBUM_COVER = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
	
	// JavaFX related Attributes
	private final String path;
	
	private final ObjectProperty<Media> media = new SimpleObjectProperty<Media>(this, "media", null);
	private final ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<MediaPlayer>(this, "mediaPlayer", null);
	private final StringProperty artist = new SimpleStringProperty(this, "artist", "");
	private final StringProperty title = new SimpleStringProperty(this, "title", "");
	private final StringProperty album = new SimpleStringProperty(this, "album", "");

	private final IntegerProperty length = new SimpleIntegerProperty(0);
	private final ObjectProperty<Image> albumCover = new SimpleObjectProperty<Image>(this, "album cover", SwingFXUtils.toFXImage(DEFAULT_ALBUM_COVER, null));

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
	public StringProperty getLengthString(){
		String seconds = (length.get() % 60) == 0 ? "00" : "" + (length.get() % 60);
		return new SimpleStringProperty("" + (length.get() / 60) + ":" + seconds);
	}
	public IntegerProperty getLength(){
		return length;
	}
}
