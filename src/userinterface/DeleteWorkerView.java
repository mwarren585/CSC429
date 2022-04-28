// specify the package
package userinterface;
import java.time.LocalDate;
import javafx.scene.control.Alert.AlertType;

// system imports
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
import javafx.scene.control.TextArea;

import java.util.Properties;

// project imports
import impresario.IModel;
import model.Worker;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class DeleteWorkerView extends View
{
    // GUI components
    protected TextField bannerId;
    protected TextField firstName;
    protected TextField lastName;
    private Worker selectedWorker = (Worker)myModel.getState("Worker");



    //protected ComboBox statusBox;

    protected Button doneButton;
    protected Button backButton;
    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public DeleteWorkerView(IModel Worker)
    {
        super(Worker, "WorkerView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("TransactionErrorMessage", this);
    }


    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Brockport Library System");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text("WORKER INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        /*
        Text patronIdLabel = new Text(" Patron ID : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        patronIdLabel.setFont(myFont);
        patronIdLabel.setWrappingWidth(150);
        patronIdLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(patronIdLabel, 0, 1);

        patronId = new TextField();
        patronId.setEditable(false);
        grid.add(patronId, 1, 1);
         */

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        Text bannerIdLabel = new Text(" Banner Id : ");
        bannerIdLabel.setFont(myFont);
        bannerIdLabel.setWrappingWidth(150);
        bannerIdLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(bannerIdLabel, 0, 2);

        bannerId = new TextField();
        bannerId.setEditable(true);
        grid.add(bannerId, 1, 2);

        Text firstNameLabel = new Text(" First Name : ");
        firstNameLabel.setFont(myFont);
        firstNameLabel.setWrappingWidth(150);
        firstNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(firstNameLabel, 0, 3);

        firstName = new TextField();
        firstName.setEditable(true);
        grid.add(firstName, 1, 3);


        Text lastNameLabel = new Text(" Last Name : ");
        lastNameLabel.setFont(myFont);
        lastNameLabel.setWrappingWidth(150);
        lastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(lastNameLabel, 0, 4);

        lastName = new TextField();
        lastName.setEditable(true);
        grid.add(lastName, 1, 4);



        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);

        backButton = new Button("Cancel");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("CancelTransaction", null);
            }
        });


        doneButton = new Button("Submit");
        doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                Properties p = new Properties();

                String banid =  (String)selectedWorker.getState("bannerID");

                p.setProperty("bannerID", banid);
                p.setProperty("firstName", (String)selectedWorker.getState("firstName"));
                p.setProperty("lastName", (String)selectedWorker.getState("lastName"));
                p.setProperty("password", (String)selectedWorker.getState("password"));
                p.setProperty("phone", (String)selectedWorker.getState("phone"));
                p.setProperty("email", (String)selectedWorker.getState("email"));
                p.setProperty("credentials", (String)selectedWorker.getState("credentials"));
                p.setProperty("dateOfLatestCredentials", (String)selectedWorker.getState("dateOfLatestCredentials"));
                p.setProperty("dateOfHire", (String)selectedWorker.getState("dateOfHire"));
                p.setProperty("status", "Inactive");


                myModel.stateChangeRequest("InsertWorkerData", p);
                displayMessage("Worker with BannerId: "+ banid +" Deleted Successfully!");
                bannerId.clear();
                firstName.clear();
                lastName.clear();

            }
        });
        doneCont.getChildren().add(doneButton);
        doneCont.getChildren().add(backButton);


        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);


        return vbox;
    }


    // Create the status log field
    //-------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields()
    {
        Worker selectedWorker = (Worker)myModel.getState("Worker");
        bannerId.setText((String)selectedWorker.getState("bannerID"));
        firstName.setText((String)selectedWorker.getState("firstName"));
        lastName.setText((String)selectedWorker.getState("lastName"));


    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearErrorMessage();

        if (key.equals("TransactionErrorMessage") == true)
        {
            String val = (String)value;
            //serviceCharge.setText(val);
            if (val.startsWith("ERR"))
                displayErrorMessage(val);
            else
                displayMessage(val);
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
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }

    /**
     * Clear text
     */
    //----------------------------------------------------------
    public void clearText()
    {
        bannerId.clear();
        firstName.clear();
        lastName.clear();


        //statusBox.valueProperty().set("Active");
    }

}
