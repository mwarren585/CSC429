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
import model.Book;

public class DeleteBookView extends View{
    // GUI components
    protected TextField barcodeField;
    private Book boookSelction = (Book)myModel.getState("Book");


    //protected ComboBox statusBox;

    protected Button doneButton;
    protected Button backButton;
    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public DeleteBookView(IModel Book)
    {
        super(Book, "BookView");

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

        Text prompt = new Text("BOOK INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);


        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        Text bannerIdLabel = new Text(" Barcode : ");
        bannerIdLabel.setFont(myFont);
        bannerIdLabel.setWrappingWidth(150);
        bannerIdLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(bannerIdLabel, 0, 2);

        barcodeField = new TextField();
        barcodeField.setEditable(true);
        grid.add(barcodeField, 1, 2);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);

        backButton = new Button("Cancel");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setOnAction(e -> {
            clearErrorMessage();
            myModel.stateChangeRequest("CancelTransaction", null);
        });


        doneButton = new Button("Submit");
        doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e){
                clearErrorMessage();

                Properties p = new Properties();


                p.setProperty("barcode", (String) boookSelction.getState("barcode"));
                p.setProperty("title", (String) boookSelction.getState("title"));
                p.setProperty("author", (String) boookSelction.getState("author"));
                p.setProperty("publisher", (String) boookSelction.getState("publisher"));
                p.setProperty("pubYear", (String) boookSelction.getState("pubYear"));
                p.setProperty("ISBN", (String) boookSelction.getState("ISBN"));
                p.setProperty("price", (String) boookSelction.getState("price"));
                p.setProperty("notes", (String) boookSelction.getState("notes"));
                p.setProperty("status", "Inactive");


                myModel.stateChangeRequest("InsertBookData", p);
                displayMessage("Book with barcode: " + (String) boookSelction.getState("barcode")+ "Deleted Successfully!!!");
                barcodeField.clear();
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
        Book boookSelction = (Book)myModel.getState("Book");
        barcodeField.setText((String)boookSelction.getState("barcode"));

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
        barcodeField.clear();
        //statusBox.valueProperty().set("Active");
    }

}