package userinterface;


// system imports
import java.text.NumberFormat;
import java.util.Properties;

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

// project imports
import impresario.IModel;
import model.StudentBorrower;
import model.Worker;

/** The class containing the Teller View  for the ATM application */
//==============================================================
public class DelinquencyCheckCompleteView extends View
{

    // GUI stuff
    private TextField userid;
    private PasswordField password;
    private Button submitButton;
    private Button back;

    private TextField nameT;
    private TextField banT;

    // For showing error message
    private MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public DelinquencyCheckCompleteView( IModel librarian)
    {

        super(librarian, "Librarian");

        // create a container for showing the contents
        VBox container = new VBox(10);

        container.setPadding(new Insets(15, 5, 5, 5));

        // create a Node (Text) for showing the title
        container.getChildren().add(createTitle());

        // create a Node (GridPane) for showing data entry fields
        container.getChildren().add(createFormContents());

        // Error message area
        container.getChildren().add(createStatusLog("                          "));

        getChildren().add(container);

        populateFields();

    }

    // Create the label (Text) for the title of the screen
    //-------------------------------------------------------------
    private Node createTitle()
    {

        Text titleText = new Text("       Delinquency Check         ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);


        return titleText;
    }

    // Create the main form contents
    //-------------------------------------------------------------
    private GridPane createFormContents()
    {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        Text name = new Text(" Student borrower table updated with necessary delinquency status. ");
        name.setFont(myFont);
        name.setWrappingWidth(300);
        name.setTextAlignment(TextAlignment.CENTER);
        grid.add(name, 0, 0);


        submitButton = new Button("Ok");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });

        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        grid.add(btnContainer, 0, 2);

        return grid;
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

    // This method processes events generated from our GUI components.
    // Make the ActionListeners delegate to this method
    //-------------------------------------------------------------
    public void processAction(Event evt) {

        myModel.stateChangeRequest("CancelTransaction", null);

    }
    /**
     * Process userid and pwd supplied when Submit button is hit.
     * Action is to pass this info on to the teller object
     */
    //----------------------------------------------------------




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

    @Override
    public void updateState(String key, Object value) {

    }

}