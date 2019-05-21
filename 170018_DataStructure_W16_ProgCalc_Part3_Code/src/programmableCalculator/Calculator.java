package programmableCalculator;


import programmableCalculator.UserInterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/******
 * <p> Title: Calculator Class. </p>
 * 
 * <p> Description: A JavaFX demonstration application and baseline for a sequence of projects </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2016 </p>
 * 
 * @author Lynn Robert Carter
 *         Sumit Singh and Shivam Singhal
 * @author Garvit
 * 
 * @version 4.40	2018-02-24 The mainline of a JavaFX-based GUI implementation of a double calculator with error terms
 * @version 5.00	2019-02-26 The mainline of a JavaFX-based GUI implementation of a scientific calculator with variables and constants
 */

public class Calculator extends Application {
	
	public final static double WINDOW_WIDTH = 850;
	public final static double WINDOW_HEIGHT = 600;
	public final static double WINDOW_WIDTH2 = 970;
	public UserInterface theGUI;
	public DefinitionsUserInterface theGUI1;
	/**********
	 * This is the start method that is called once the application has been loaded into memory and is 
	 * ready to get to work.
	 * 
	 * In designing this application I have elected to IGNORE all opportunities for automatic layout
	 * support and instead have elected to manually position each GUI element and its properties in
	 * order to exercise complete control over the user interface look and feel.
	 * 
	 */
	
	@Override
	public void start(Stage theStage) throws Exception {
		theStage.setTitle("Calculator");		
		Pane theRoot1 = new Pane();							// Create a pane within the window
		Pane thePane1 = new Pane();																			// Label the stage (a window)
		theGUI = new UserInterface(theRoot1);					// Create the Graphical User Interface
		theGUI1 = new DefinitionsUserInterface(thePane1);
		Tab newTab = new Tab("Calculator");
		Tab newTab1 = new Tab("Load");	
		TabPane TabPane = new TabPane();
		Scene theScene = new Scene(TabPane, WINDOW_WIDTH2, WINDOW_HEIGHT);
		TabPane.getTabs().addAll(newTab,newTab1);
		newTab1.setOnSelectionChanged(e->{theStage.setWidth(WINDOW_WIDTH2-300);});
		newTab.onSelectionChangedProperty().set(e->{theStage.setWidth(WINDOW_WIDTH2);});;
		newTab.setContent(theRoot1);
		newTab1.setContent(thePane1);
		theStage.setScene(theScene);													// Set the scene on the stage
		theStage.show();
	}
		public static void main(String[] args) {										// This method may not be required
		launch(args);																	// for all JavaFX applications using
	}																					// other IDEs.
}
