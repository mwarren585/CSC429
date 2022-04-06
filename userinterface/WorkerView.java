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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

// project imports
import impresario.IModel;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class WorkerView extends View
{
    // GUI components
    protected TextField bannerId;
    protected TextField firstName;
    protected TextField lastName;
    protected TextField password;
    protected TextField contactPhone;
    protected TextField email;
    protected ComboBox credentials;
    protected TextField dateOfLastCredentialsStatus;
    protected TextField dateOfHire;
    protected ComboBox statusBox;

    protected Button doneButton;
    protected Button backButton;
    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public WorkerView(IModel Worker)
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

        Text passwordLabel = new Text(" Password : ");
        passwordLabel.setFont(myFont);
        passwordLabel.setWrappingWidth(150);
        passwordLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(passwordLabel, 0, 5);

        password = new TextField();
        password.setEditable(true);
        grid.add(password, 1, 5);

        Text contactPhoneLabel = new Text(" Contact Phone : ");
        contactPhoneLabel.setFont(myFont);
        contactPhoneLabel.setWrappingWidth(150);
        contactPhoneLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(contactPhoneLabel, 0, 6);

        contactPhone = new TextField();
        contactPhone.setEditable(true);
        grid.add(contactPhone, 1, 6);

        Text emailLabel = new Text(" Email : ");
        emailLabel.setFont(myFont);
        emailLabel.setWrappingWidth(150);
        emailLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(emailLabel, 0, 7);

        email = new TextField();
        email.setEditable(true);
        grid.add(email, 1, 7);

        Text credentialsLabel = new Text(" Credentials : ");
        credentialsLabel.setFont(myFont);
        credentialsLabel.setWrappingWidth(150);
        credentialsLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(credentialsLabel, 0, 8);

        credentials = new ComboBox();
        credentials.getItems().addAll(
                "Ordinary",
                "Administrator"
        );

        credentials.setValue("Ordinary");
        grid.add(credentials, 1, 8);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        Text dateOfLastCredentialsStatusLabel = new Text(" Date of Last Credential Status : ");
        dateOfLastCredentialsStatusLabel.setFont(myFont);
        dateOfLastCredentialsStatusLabel.setWrappingWidth(150);
        dateOfLastCredentialsStatusLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateOfLastCredentialsStatusLabel, 0, 9);

        dateOfLastCredentialsStatus = new TextField();
        dateOfLastCredentialsStatus.setEditable(false);
        dateOfLastCredentialsStatus.setText(dtf.format(now));
        grid.add(dateOfLastCredentialsStatus, 1, 9);

        Text dateOfHireLabel = new Text(" Date of Hire : ");
        dateOfHireLabel.setFont(myFont);
        dateOfHireLabel.setWrappingWidth(150);
        dateOfHireLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateOfHireLabel, 0, 10);

        dateOfHire = new TextField();
        dateOfHire.setEditable(false);
        dateOfHire.setText(dtf.format(now));
        grid.add(dateOfHire, 1, 10);

        Text sta = new Text(" Worker Status : ");
        sta.setFont(myFont);
        sta.setWrappingWidth(150);
        sta.setTextAlignment(TextAlignment.RIGHT);
        grid.add(sta, 0, 11);

        statusBox = new ComboBox();
        statusBox.getItems().addAll("Active", "Inactive");
        statusBox.getSelectionModel().selectFirst();

        grid.add(statusBox, 1, 11);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);

        backButton = new Button("Back");
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

                String bannerID = bannerId.getText();

                Properties p = new Properties();

                p.setProperty("bannerID", bannerId.getText());
                p.setProperty("firstName", firstName.getText());
                p.setProperty("lastName", lastName.getText());
                p.setProperty("password", password.getText());
                p.setProperty("phone", contactPhone.getText());
                p.setProperty("email", email.getText());
                p.setProperty("credentials", (String)credentials.getValue());
                p.setProperty("dateOfLatestCredentials", dateOfLastCredentialsStatus.getText());
                p.setProperty("dateOfHire", dateOfHire.getText());
                p.setProperty("status", (String)statusBox.getValue());

                if(bannerID.length() != 9){
                    displayErrorMessage("bannerID needs to be exactly 9 numbers long!");
                }
                else {
                    myModel.stateChangeRequest("WorkerData", p);
                    displayMessage("Worker Successful Added!!");
                    clearText();
                }
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
        //accountNumber.setText((String)myModel.getState("AccountNumber"));
        //acctType.setText((String)myModel.getState("Type"));
        //balance.setText((String)myModel.getState("Balance"));
        //serviceCharge.setText((String)myModel.getState("ServiceCharge"));
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
        password.clear();
        contactPhone.clear();
        email.clear();
        //credentials.clear();
        dateOfLastCredentialsStatus.clear();
        dateOfHire.clear();
        //statusBox.valueProperty().set("Active");
    }

}

//---------------------------------------------------------------
//	Revision History:
//
