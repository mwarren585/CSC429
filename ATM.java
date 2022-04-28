
// specify the package

// system imports

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;

// project imports
import event.Event;

import model.Librarian;
import model.Teller;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;


/** The class containing the main program  for the ATM application */
//==============================================================
public class ATM extends Application
{

	private Librarian myLibrarian;		// the main behavior for the application

	/** Main frame of the application */
	private Stage mainStage;


	// start method for this class, the main application object
	//----------------------------------------------------------
	public void start(Stage primaryStage)
	{
	   System.out.println("Brockport Library");
	   System.out.println("Copyright 2022 Matthew Warren");

           // Create the top-level container (main frame) and add contents to it.
	   MainStageContainer.setStage(primaryStage, "Brockport Bank ATM Version 3.00");
	   mainStage = MainStageContainer.getInstance();

	   // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED USING THE TOP RIGHT
	   // 'X' IN THE WINDOW), and show it.
           mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
           });

           try
	   {
		myLibrarian = new Librarian();
	   }
	   catch(Exception exc)
	   {
		System.err.println("ATM.ATM - could not create Librarian!");
		new Event(Event.getLeafLevelClassName(this), "ATM.<init>", "Unable to create Teller object", Event.ERROR);
		exc.printStackTrace();
	   }


  	   WindowPosition.placeCenter(mainStage);

           mainStage.show();
	}


	/** 
	 * The "main" entry point for the application. Carries out actions to
	 * set up the application
	 */
	//----------------------------------------------------------
    	public static void main(String[] args)
	{

		launch(args);
	}

}
