import java.util.prefs.Preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SettingsHelper {

    static Preferences prefs = Preferences.userNodeForPackage(SettingsHelper.class);
    private static final String ACCENT_PREF = "accentColor";
    private static final String ACCENT_OFFSET_PREF = "accentOffsetColor";
    
    
    private static String accent = "";
    private static String accentOffset = "";
    
    private static int accentRedNum = 135;
    private static int accentGreenNum = 135;
    private static int accentBlueNum = 135;
    private static final String ACCENT_RED_PREF = "accentRedColor";
    private static final String ACCENT_GREEN_PREF = "accentGreenColor";
    private static final String ACCENT_BLUE_PREF = "accentBlueColor";
    
    private static int accentOffsetRedNum = 135;
    private static int accentOffsetGreenNum = 135;
    private static int accentOffsetBlueNum = 135;
    private static final String ACCENT_OFFSET_RED_PREF = "accentRedColor";
    private static final String ACCENT_OFFSET_GREEN_PREF = "accentGreenColor";
    private static final String ACCENT_OFFSET_BLUE_PREF = "accentBlueColor";
    
    private static BorderPane root;
    
    SettingsHelper(BorderPane root){
    	this.root = root;
    }

	public void setColorDefaults() {
		accent = prefs.get(ACCENT_PREF, "#275747");
		accentRedNum = Integer.parseInt(prefs.get(ACCENT_RED_PREF, "135"));
		accentGreenNum = Integer.parseInt(prefs.get(ACCENT_GREEN_PREF, "135"));
		accentBlueNum = Integer.parseInt(prefs.get(ACCENT_BLUE_PREF, "135"));
		
		accentOffset = prefs.get(ACCENT_OFFSET_PREF, "#276447");
		accentOffsetRedNum = Integer.parseInt(prefs.get(ACCENT_OFFSET_RED_PREF, "135"));
		accentOffsetGreenNum = Integer.parseInt(prefs.get(ACCENT_OFFSET_GREEN_PREF, "135"));
		accentOffsetBlueNum = Integer.parseInt(prefs.get(ACCENT_OFFSET_BLUE_PREF, "135"));
		
		root.setStyle("	-fx-accent: " + accent + ";" + "-fx-accent-offset: " + accentOffset + ";");
	}
	
	public static void saveColorPreference(){	
		prefs.put(ACCENT_PREF, accent);
		prefs.put(ACCENT_RED_PREF, (accentRedNum + ""));
		prefs.put(ACCENT_GREEN_PREF, (accentGreenNum + ""));
		prefs.put(ACCENT_BLUE_PREF, (accentBlueNum + ""));
		
	    prefs.put(ACCENT_OFFSET_PREF, accentOffset);
		prefs.put(ACCENT_OFFSET_RED_PREF, (accentOffsetRedNum + ""));
		prefs.put(ACCENT_OFFSET_GREEN_PREF, (accentOffsetGreenNum + ""));
		prefs.put(ACCENT_OFFSET_BLUE_PREF, (accentOffsetBlueNum + ""));
	}
	
	// Creates the BorderPane Center i.e. (Button, and Song Info)
    public static BorderPane addCenter(Controller controller) {
    	BorderPane center = new BorderPane();
    	center.setStyle("-fx-background-color: -fx-song-menu;");
    	
    	GridPane grid = addColorSliders();
        center.setCenter(grid);
        
        return center;
    }
    
    private static void changeAccentVisual(Pane colorVisual){
    	String accentHex = String.format("#%02x%02x%02x", accentRedNum, accentGreenNum, accentBlueNum);
    	colorVisual.setStyle("-fx-background-color: " + accentHex +";");
    	accent = accentHex;
    	root.setStyle("	-fx-accent: " + accentHex + ";" + "-fx-accent-offset: " + accentOffset + ";");
    }
    
    private static void changeAccentOffsetVisual(Pane colorVisual){
    	String accentOffsetHex = String.format("#%02x%02x%02x", accentOffsetRedNum, accentOffsetGreenNum, accentOffsetBlueNum);
    	colorVisual.setStyle("-fx-background-color: " + accentOffsetHex +";");
    	accentOffset = accentOffsetHex;
    	root.setStyle("-fx-accent-offset: " + accentOffsetHex + ";" + "	-fx-accent: " + accent + ";");
    }
    
    private static GridPane addColorSliders() {
    	GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10));
    	
    	
        HBox accentBox = new HBox();
        VBox accentSliders = new VBox();
        accentSliders.setAlignment(Pos.CENTER);
        accentSliders.setSpacing(15);
        
        Pane colorVisual = new Pane();
        colorVisual.setId("colorVisual");
    	colorVisual.setStyle("-fx-background-color: " + accent +";");

        Slider accentRed = new Slider(0, 255, accentRedNum);
        accentRed.setId("redSlider");
        accentRed.valueProperty().addListener(new ChangeListener<Number>() {
    		public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
    			String hex = String.format("#%02x%02x%02x", (int)accentRed.getValue(), 0, 0);
                String style = String.format("-fx-background-color: " + hex +";",
                		newValue.intValue(), newValue.intValue());
                accentRed.lookup(".track").setStyle(style);
                accentRedNum = (int)accentRed.getValue();
                changeAccentVisual(colorVisual);
            }
        });
        Slider accentGreen = new Slider(0, 255, accentGreenNum);
        accentGreen.setId("greenSlider");
        accentGreen.valueProperty().addListener(new ChangeListener<Number>() {
    		public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
    			String hex = String.format("#%02x%02x%02x", 0, (int)accentGreen.getValue(), 0);
                String style = String.format("-fx-background-color: " + hex +";",
                		newValue.intValue(), newValue.intValue());
                accentGreen.lookup(".track").setStyle(style);
                accentGreenNum = (int)accentGreen.getValue();
                changeAccentVisual(colorVisual);
            }
        });
        Slider accentBlue = new Slider(0, 255, accentBlueNum);
        accentBlue.setId("blueSlider");
        accentBlue.valueProperty().addListener(new ChangeListener<Number>() {
    		public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
    			String hex = String.format("#%02x%02x%02x", 0, 0, (int)accentBlue.getValue());
                String style = String.format("-fx-background-color: " + hex +";",
                		newValue.intValue(), newValue.intValue());
                accentBlue.lookup(".track").setStyle(style);
                accentBlueNum = (int)accentBlue.getValue();
                changeAccentVisual(colorVisual);
            }
        });
        accentSliders.getChildren().addAll(accentRed, accentGreen, accentBlue);
        accentBox.getChildren().addAll(colorVisual, accentSliders);
        
        HBox accentOffsetBox = new HBox();
        VBox accentOffsetSliders = new VBox();
        accentOffsetSliders.setAlignment(Pos.CENTER);
        accentOffsetSliders.setSpacing(15);
        
        Pane colorVisualOffset = new Pane();
        colorVisualOffset.setId("colorVisual");
    	colorVisualOffset.setStyle("-fx-background-color: " + accent +";");

        Slider accentOffsetRed = new Slider(0, 255, accentRedNum);
        accentOffsetRed.setId("redSlider");
        accentOffsetRed.valueProperty().addListener(new ChangeListener<Number>() {
    		public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
    			String hex = String.format("#%02x%02x%02x", (int)accentOffsetRed.getValue(), 0, 0);
                String style = String.format("-fx-background-color: " + hex +";",
                		newValue.intValue(), newValue.intValue());
                accentOffsetRed.lookup(".track").setStyle(style);
                accentOffsetRedNum = (int)accentOffsetRed.getValue();
                changeAccentOffsetVisual(colorVisualOffset);
            }
        });
        Slider accentOffsetGreen = new Slider(0, 255, accentGreenNum);
        accentOffsetGreen.setId("greenSlider");
        accentOffsetGreen.valueProperty().addListener(new ChangeListener<Number>() {
    		public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
    			String hex = String.format("#%02x%02x%02x", 0, (int)accentOffsetGreen.getValue(), 0);
                String style = String.format("-fx-background-color: " + hex +";",
                		newValue.intValue(), newValue.intValue());
                accentOffsetGreen.lookup(".track").setStyle(style);
                accentOffsetGreenNum = (int)accentOffsetGreen.getValue();
                changeAccentOffsetVisual(colorVisualOffset);
            }
        });
        Slider accentOffsetBlue = new Slider(0, 255, accentBlueNum);
        accentOffsetBlue.setId("blueSlider");
        accentOffsetBlue.valueProperty().addListener(new ChangeListener<Number>() {
    		public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
    			String hex = String.format("#%02x%02x%02x", 0, 0, (int)accentOffsetBlue.getValue());
                String style = String.format("-fx-background-color: " + hex +";",
                		newValue.intValue(), newValue.intValue());
                accentOffsetBlue.lookup(".track").setStyle(style);
                accentOffsetBlueNum = (int)accentOffsetBlue.getValue();
                changeAccentOffsetVisual(colorVisualOffset);
            }
        });
        accentOffsetSliders.getChildren().addAll(accentOffsetRed, accentOffsetGreen, accentOffsetBlue);
        accentOffsetBox.getChildren().addAll(colorVisualOffset, accentOffsetSliders);
        
       	VBox accentVBox = new VBox();
       	Label accentLabel = new Label("Accent Color:");
       	accentVBox.getChildren().addAll(accentLabel, accentBox);
       	
       	VBox accentOffsetVBox = new VBox();  	
       	Label accentOffsetLabel = new Label("Accent Offset Color:");
       	accentOffsetVBox.getChildren().addAll(accentOffsetLabel, accentOffsetBox);
       	
       	VBox colorSliders = new VBox();
       	colorSliders.setMinWidth(250);
       	colorSliders.getChildren().addAll(accentVBox, accentOffsetVBox);
       	
       	
       	
       	VBox colorSchemes = new VBox();
       	VBox colorSchemeButtons = new VBox();
       	colorSchemeButtons.setSpacing(15);
       	
       	// Accent: #c95140 Accent Offset: #a34334
       	Button redScheme = new Button("Red");
       	redScheme.setId("redScheme");
       	redScheme.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent event) {
	        	accentRedNum = 201;
	        	accentGreenNum = 81;
	        	accentBlueNum = 64;
	        	accentRed.valueProperty().set(accentRedNum);
	        	accentGreen.valueProperty().set(accentGreenNum);
	        	accentBlue.valueProperty().set(accentBlueNum);
	        	accentOffsetRedNum = 163;
	        	accentOffsetGreenNum = 67;
	        	accentOffsetBlueNum = 52;
	        	accentOffsetRed.valueProperty().set(accentOffsetRedNum);
	        	accentOffsetGreen.valueProperty().set(accentOffsetGreenNum);
	        	accentOffsetBlue.valueProperty().set(accentOffsetBlueNum);
	        	changeAccentVisual(colorVisual);
	        	changeAccentVisual(colorVisualOffset);
	        }
	    });
    	
       	// Accent: #3db861 Accent Offset: #28783f
    	Button greenScheme = new Button("Green");
       	greenScheme.setId("greenScheme");
    	greenScheme.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent event) {
	        	accentRedNum = 61;
	        	accentGreenNum = 184;
	        	accentBlueNum = 97;
	        	accentRed.valueProperty().set(accentRedNum);
	        	accentGreen.valueProperty().set(accentGreenNum);
	        	accentBlue.valueProperty().set(accentBlueNum);
	        	accentOffsetRedNum = 40;
	        	accentOffsetGreenNum = 120;
	        	accentOffsetBlueNum = 63;
	        	accentOffsetRed.valueProperty().set(accentOffsetRedNum);
	        	accentOffsetGreen.valueProperty().set(accentOffsetGreenNum);
	        	accentOffsetBlue.valueProperty().set(accentOffsetBlueNum);
	        	changeAccentVisual(colorVisual);
	        	changeAccentVisual(colorVisualOffset);
	        }
	    });
    	
    	// Accent: #4d9cc4 Accent Offset: #38728f
    	Button blueScheme = new Button("Blue");
    	blueScheme.setId("blueScheme");
    	blueScheme.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent event) {
	        	accentRedNum = 77;
	        	accentGreenNum = 156;
	        	accentBlueNum = 196;
	        	accentRed.valueProperty().set(accentRedNum);
	        	accentGreen.valueProperty().set(accentGreenNum);
	        	accentBlue.valueProperty().set(accentBlueNum);
	        	accentOffsetRedNum = 56;
	        	accentOffsetGreenNum = 114;
	        	accentOffsetBlueNum = 143;
	        	accentOffsetRed.valueProperty().set(accentOffsetRedNum);
	        	accentOffsetGreen.valueProperty().set(accentOffsetGreenNum);
	        	accentOffsetBlue.valueProperty().set(accentOffsetBlueNum);
	        	changeAccentVisual(colorVisual);
	        	changeAccentVisual(colorVisualOffset);
	        }
	    });
    	
       	// Accent: #7a599e  Accent Offset: #4f3a66
    	Button purpleScheme = new Button("Purple");
       	purpleScheme.setId("purpleScheme");
    	purpleScheme.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent event) {
	        	accentRedNum = 122;
	        	accentGreenNum = 89;
	        	accentBlueNum = 158;
	        	accentRed.valueProperty().set(accentRedNum);
	        	accentGreen.valueProperty().set(accentGreenNum);
	        	accentBlue.valueProperty().set(accentBlueNum);
	        	accentOffsetRedNum = 79;
	        	accentOffsetGreenNum = 58;
	        	accentOffsetBlueNum = 102;
	        	accentOffsetRed.valueProperty().set(accentOffsetRedNum);
	        	accentOffsetGreen.valueProperty().set(accentOffsetGreenNum);
	        	accentOffsetBlue.valueProperty().set(accentOffsetBlueNum);
	        	changeAccentVisual(colorVisual);
	        	changeAccentVisual(colorVisualOffset);
	        }
	    });
    	colorSchemeButtons.getChildren().addAll(redScheme, greenScheme, blueScheme, purpleScheme);
       	
    	Label schemeLabel = new Label("Preset Color Schemes:");
    	colorSchemes.getChildren().addAll(schemeLabel, colorSchemeButtons);
    	
       	
    	grid.add(colorSliders, 0, 0);
    	grid.add(colorSchemes, 1, 0);
       	
       	return grid;
    }
}
