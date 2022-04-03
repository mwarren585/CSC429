// specify the package
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
public class DeleteStudentView extends View
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
    public DeleteStudentView( IModel librarian)
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

        Text titleText = new Text("       Verify Delete          ");
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

        Text name = new Text(" Name : ");
        name.setFont(myFont);
        name.setWrappingWidth(80);
        name.setTextAlignment(TextAlignment.CENTER);
        grid.add(name, 0, 0);

        nameT = new TextField();
        nameT.setEditable(false);
        grid.add(nameT, 1, 0);

        Text ban = new Text(" Banner ID : ");
        ban.setFont(myFont);
        ban.setWrappingWidth(80);
        ban.setTextAlignment(TextAlignment.CENTER);
        grid.add(ban, 0, 2);

        banT = new TextField();
        banT.setEditable(false);
        grid.add(banT, 1, 2);

        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });

        back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {


                myModel.stateChangeRequest("SelectStudentView", null);
            }
        });

        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        grid.add(btnContainer, 0, 4);

        HBox btnContainer2 = new HBox(10);
        btnContainer2.setAlignment(Pos.CENTER);
        btnContainer2.getChildren().add(back);
        grid.add(btnContainer2, 1, 4);

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
        StudentBorrower wc = (StudentBorrower)myModel.getState("Student");
        String ban = (String)wc.getState("bannerId");
        String fName = (String)wc.getState("firstName");
        String lName = (String)wc.getState("lastName");

        banT.setText(ban);
        nameT.setText(fName + " " +lName);

    }

    // This method processes events generated from our GUI components.
    // Make the ActionListeners delegate to this method
    //-------------------------------------------------------------
    public void processAction(Event evt) {
        StudentBorrower wc = (StudentBorrower)myModel.getState("Student");
        String ban = (String)wc.getState("bannerId");
        String fName = (String)wc.getState("firstName");
        String lName = (String)wc.getState("lastName");
        String pho = (String)wc.getState("contactPhone");
        String eml = (String)wc.getState("email");
        String credentials = (String)wc.getState("borrowerStatus");
        String latestCred = (String)wc.getState("dateOfLatestBorrowerStatus");
        String dateHire =(String)wc.getState("dateOfRegistration");
        String notes =(String)wc.getState("notes");
        String stat = "Inactive";

        System.out.println(fName + " " + lName + " " + pho + " " + eml + " " + credentials + " " + latestCred + " " + dateHire + " " + notes + " " + stat);

        Properties p1 = new Properties();
        p1.setProperty("bannerId", ban);
        p1.setProperty("firstName", fName);
        p1.setProperty("lastName", lName);
        p1.setProperty("contactPhone", pho);
        p1.setProperty("email", eml);
        p1.setProperty("borrowerStatus", credentials);
        p1.setProperty("dateOfLatestBorrowerStatus", latestCred);
        p1.setProperty("dateOfRegistration", dateHire);
        p1.setProperty("notes", notes);
        p1.setProperty("status", stat);

        databaseRemoved();

        myModel.stateChangeRequest("insertStudentModification", p1);
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

    public void databaseRemoved(){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Database");
        alert.setHeaderText("Your request was complete!");
        alert.setContentText("Student was set to 'Inactive' in database.");

        alert.showAndWait();
    }
}