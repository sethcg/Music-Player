import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;

public class AddSongHelper {
	
	private File folder = new File("./Music/");
	
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
            			Path sourcePath = file.toPath();
            		    String fileName = file.getName();
            		    Path targetPath = Paths.get("./Music/", fileName);
            		    try {
							Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							System.out.println("Copy Failed");
						}
            		    songlist.addSong(targetPath.toString());
            		}
            	}
            	event.setDropCompleted(success);
            	event.consume();
            }
    	});
    	
		return dragDropPane;
    }

}
