import java.io.File;
import java.io.FilenameFilter;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
	Data Model:	Model represents an object carrying data, in this case a List of Song Objects.
*/

public class SongList {

	private final static File folder = new File("./Music");
	
    private final ObservableList<Song> songList = FXCollections.observableArrayList(song -> new Observable[] {
    	song.getTitle(),
    	song.getArtist(),
    	song.getTotalDurationString(),
    	song.getMediaPlayer()
    });

    private final ObjectProperty<Song> currentSong = new SimpleObjectProperty<>(null);

    public ObjectProperty<Song> currentSongProperty() {
        return currentSong;
    }

    // Helper Method for Ease of Use
    public final boolean isCurrentSongEmpty(){
    	return currentSong.get() == null ? true : false;
    }
    
    // Getter For Current Song
    public final Song getCurrentSong() {
        return currentSongProperty().get();
    }

    // Setter For Current Song
    public final void setCurrentSong(Song song) {
        currentSongProperty().set(song);
    }

    public ObservableList<Song> getSongList() {
        return songList;
    }
    
    // Load Data From ./Music File
    public void loadMusicFromFolder(){
    	FilenameFilter filter = 
    			(directory, filename) -> filename.endsWith(".mp3");		// FileFilter for MP3 Files
    	for(File file : folder.listFiles(filter)){						// For Each File using the Filter add to SongList
    		songList.add(new Song(file.getPath()));
    	}
    }
    
    /* FOR IMPLMENTING A SAVING FEATURE IN FUTURE */
    //public void saveData(File file) { }
}