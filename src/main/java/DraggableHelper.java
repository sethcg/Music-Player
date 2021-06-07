import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DraggableHelper {

    public static void addDraggableListener(Stage stage, String selector) {
    	DraggableListener resizeListener = new DraggableListener(stage);
        stage.getScene().lookup(selector).addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
        stage.getScene().lookup(selector).addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
    }

    static class DraggableListener implements EventHandler<MouseEvent> {
    	private Stage stage;
    	private double x = 0;
    	private double y = 0;
    	
    	public DraggableListener(Stage stage) {
            this.stage = stage;
        }
    	
    	@Override
    	public void handle(MouseEvent mouseEvent) {
            EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
            if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType) == true) {
            	x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneY();
    		} else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) == true) {
    			stage.setX(mouseEvent.getScreenX() - x);
            	stage.setY(mouseEvent.getScreenY() - y);
    		}
    	}
    }
}