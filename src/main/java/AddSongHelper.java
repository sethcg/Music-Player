import java.io.File;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;

public class AddSongHelper {
	
	// Creates the BorderPane Center i.e. (Button, and Song Info)
    public static BorderPane addCenter(SongList songlist) {
    	BorderPane dragDropPane = new BorderPane();
    	dragDropPane.setId("dragDropPane");
    	
    	Label dragDropLabel = new Label("Drag and Drop Mp3 Files");
    	dragDropLabel.setId("dragDropLabel");
    	dragDropPane.setCenter(dragDropLabel);
    	
    	dragDropPane.setOnDragOver(new EventHandler<DragEvent>() {
    		@Override
    		public void handle(DragEvent event) {
    			Dragboard dragBoard = event.getDragboard();
    			if (dragBoard.hasFiles()) {
    				event.acceptTransferModes(TransferMode.COPY);
    			}else{
    				event.consume();
    			}
    		}
    	});
    	
    	dragDropPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
            	Dragboard dragBoard = event.getDragboard();
            	boolean success = false;
            	if (dragBoard.hasFiles()) {
            		success = true;
            		String fileLocation = null;
            		for (File file:dragBoard.getFiles()) {
            			fileLocation = file.getAbsolutePath();
            			System.out.println("Adding: " + fileLocation);
            			songlist.addSong(fileLocation);
            		}
            	}
            	event.setDropCompleted(success);
            	event.consume();
            }
    	});
    	
		return dragDropPane;
    }

}
