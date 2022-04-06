// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.HPos;

// project imports
import impresario.IModel;

/** The class containing the Transaction Choice View  for the ATM application */
//==============================================================
public class TransactionChoiceView extends View
{

	// other private data
	private final int labelWidth = 120;
	private final int labelHeight = 25;

	// GUI components

	private Button addBookButton;
	private Button addWorkerButton;
	private Button addStudentBorrowerButton;
	private Button modifyWorkerButton;
	private Button modifyStudentBorrowerButton;
	private Button modifyBookButton;
    private Button deleteWorkerButton;
    private Button deleteStudentButton;
    private Button deleteBookButton;

	private Button cancelButton;

	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public TransactionChoiceView(IModel librarian)
	{
		super(librarian, "TransactionChoiceView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a title for this panel
		container.getChildren().add(createTitle());
		
		// how do you add white space?
		container.getChildren().add(new Label(" "));

		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContents());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		populateFields();

		myModel.subscribe("TransactionError", this);
	}

	// Create the labels and fields
	//-------------------------------------------------------------
	private VBox createTitle()
	{
		VBox container = new VBox(10);
		Text titleText = new Text("       Brockport Library          ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);

		String workerGreetingName = (String)myModel.getState("firstName");
		Text welcomeText = new Text("Welcome, " + workerGreetingName + "!");
		welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		welcomeText.setWrappingWidth(300);
		welcomeText.setTextAlignment(TextAlignment.CENTER);
		welcomeText.setFill(Color.DARKGREEN);
		container.getChildren().add(welcomeText);

		Text inquiryText = new Text("What do you wish to do today?");
		inquiryText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		inquiryText.setWrappingWidth(300);
		inquiryText.setTextAlignment(TextAlignment.CENTER);
		inquiryText.setFill(Color.BLACK);
		container.getChildren().add(inquiryText);
	
		return container;
	}


	// Create the navigation buttons
	//-------------------------------------------------------------
	private VBox createFormContents()
	{

		VBox container = new VBox(15);

		// create the buttons, listen for events, add them to the container
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		addBookButton = new Button("Add Book");
		addBookButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		addBookButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("AddBook", null);
            	     }
        	});

		grid.setHalignment(addBookButton, HPos.CENTER);
		grid.add(addBookButton, 0, 2);



		addStudentBorrowerButton = new Button("Add Student Borrower");
		addStudentBorrowerButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		addStudentBorrowerButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("AddStudent", null);
            	     }
        	});
		grid.setHalignment(addStudentBorrowerButton, HPos.CENTER);
		grid.add(addStudentBorrowerButton, 1, 2);


		addWorkerButton = new Button("Add Worker");
		addWorkerButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		addWorkerButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("Add Worker", null);
            	     }
        	});
		grid.setHalignment(addWorkerButton, HPos.CENTER);
		grid.add(addWorkerButton, 0, 3);


		modifyWorkerButton = new Button("Modify Worker");
		modifyWorkerButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		modifyWorkerButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("Search Worker", 1);
            	     }
        	});
		grid.setHalignment(modifyWorkerButton, HPos.CENTER);
		grid.add(modifyWorkerButton, 1, 3);



		modifyStudentBorrowerButton = new Button("Modify Student Borrower");
		modifyStudentBorrowerButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		modifyStudentBorrowerButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("Search Student", 1);
			}
		});
		grid.setHalignment(modifyStudentBorrowerButton, HPos.CENTER);
		grid.add(modifyStudentBorrowerButton, 0, 4);


		modifyBookButton = new Button("Modify Book");
		modifyBookButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		modifyBookButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("Search Book", 1);
			}
		});
		grid.setHalignment(modifyBookButton, HPos.CENTER);
		grid.add(modifyBookButton, 1, 4);


        deleteWorkerButton = new Button("Delete Worker");
        deleteWorkerButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deleteWorkerButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("Search Worker", 2);
            }
        });
		grid.setHalignment(deleteWorkerButton, HPos.CENTER);
		grid.add(deleteWorkerButton, 0, 5);

        deleteStudentButton = new Button("Delete Student Borrower");
        deleteStudentButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deleteStudentButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("Search Student", 2);
            }
        });
        grid.setHalignment(deleteStudentButton, HPos.CENTER);
        grid.add(deleteStudentButton, 1, 5);

        deleteBookButton = new Button("Delete Book");
        deleteBookButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deleteBookButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("Search Book", 2);
            }
        });
        grid.setHalignment(deleteBookButton, HPos.CENTER);
        grid.add(deleteBookButton, 0, 6);


		/*HBox iscCont = new HBox(10);
		iscCont.setAlignment(Pos.CENTER);
		imposeServiceChargeButton = new Button("Impose Service Charge");
		imposeServiceChargeButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		imposeServiceChargeButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	 myModel.stateChangeRequest("ImposeServiceCharge", null);
            	     }
        	});
		iscCont.getChildren().add(imposeServiceChargeButton);

		container.getChildren().add(iscCont);*/


		cancelButton = new Button("Logout");
		cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("Logout", null);    
            	     }
        	});
		grid.setHalignment(cancelButton, HPos.CENTER);
		grid.add(cancelButton, 1, 6);


		container.getChildren().add(grid);

		return container;
	}

	// Create the status log field
	//-------------------------------------------------------------
	private MessageView createStatusLog(String initialMessage)
	{

		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields()
	{

	}
	

	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		if (key.equals("TransactionError") == true)
		{
			// display the passed text
			//displayErrorMessage((String)value);
		}
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
}


