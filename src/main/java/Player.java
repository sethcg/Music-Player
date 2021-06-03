import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class Player{
	protected MediaPlayer mediaPlayer;
	
	public Player(MediaPlayer mediaPlayer){
		this.mediaPlayer = mediaPlayer;
		mediaPlayer.play();
	}
	
	protected void stop() {
		if(mediaPlayer.getStatus().equals(Status.PLAYING)) {
			mediaPlayer.stop();
		}
	}
}