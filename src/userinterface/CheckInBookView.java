package userinterface;

import impresario.IModel;
//import javafx.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Book;
import java.util.Properties;
// project imports


public class CheckInBookView extends View {
    // GUI components
    protected TextField barcodeField;
    //private Book boookSelction = (Book)myModel.getState("barcode");


    //protected ComboBox statusBox;

    protected Button doneButton;
    protected Button backButton;
    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public CheckInBookView(IModel Book)
    {
        super(Book, "CheckInBookView");
         System.out.println("Help I am trapped in a view");
        // create a container for showing the conCents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        System.out.println("Help I am trapped in a diwb here agan");
        // add the container to the scene
        getChildren().add(container);


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
//        createTitle();
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

        backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setOnAction(e -> {
            clearErrorMessage();
            myModel.stateChangeRequest("BookCollection", null);
        });
        doneCont.getChildren().add(backButton);
        doneButton = new Button("Submit");
        doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        doneButton.setOnAction(e -> {
            System.out.println("BUTTON PRESSED");
            clearErrorMessage();
            Properties p = new Properties();
            //String status = "inactive";
            p.setProperty("bookID", barcodeField.getText());
            System.out.println("Barcode: " + barcodeField.getText());
            clearText();
            myModel.stateChangeRequest("FindBook", p);
            myModel.stateChangeRequest("done", null);
            checkInBook();
        });
        doneCont.getChildren().add(doneButton);

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
    public void checkInBook() {
        String input = barcodeField.getText();
        Properties p = new Properties();
        p.setProperty("bookID", input);
        myModel.stateChangeRequest("FindBook", p);
        System.out.println(p);

}
    //-------------------------------------------------------------
    public void populateFields()
    {
        System.out.println("I am trapped on populateFields");
        //Book b = (Book) myModel.getState("Book");
        //barcodeField.setText(b.getBarcode());
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
