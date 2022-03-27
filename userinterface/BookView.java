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

/** The class containing the Account View  for the ATM application */
//==============================================================
public class BookView extends View
{
    // GUI components
    protected TextField barcode;
    protected TextField bookTitle;
    protected TextField author;
    protected TextField publisher;
    protected TextField pubYear;
    protected TextField ISBN;
    protected TextField price;
    protected TextField notes;
    //protected ComboBox statusBox;

    protected Button doneButton;
    protected Button backButton;
    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public BookView(IModel Book)
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

        Text titlLab = new Text(" Barcode : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        titlLab.setFont(myFont);
        titlLab.setWrappingWidth(150);
        titlLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(titlLab, 0, 1);

        barcode = new TextField();
        barcode.setEditable(true);
        grid.add(barcode, 1, 1);

        Text authLab = new Text(" Title : ");
        authLab.setFont(myFont);
        authLab.setWrappingWidth(150);
        authLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(authLab, 0, 2);

        bookTitle = new TextField();
        bookTitle.setEditable(true);
        grid.add(bookTitle, 1, 2);

        Text pubLab = new Text(" Author : ");
        pubLab.setFont(myFont);
        pubLab.setWrappingWidth(150);
        pubLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(pubLab, 0, 3);

        author = new TextField();
        author.setEditable(true);
        grid.add(author, 1, 3);

        Text publishLab = new Text(" Publisher : ");
        publishLab.setFont(myFont);
        publishLab.setWrappingWidth(150);
        publishLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(publishLab, 0, 4);

        publisher = new TextField();
        publisher.setEditable(true);
        grid.add(publisher, 1, 4);

        Text pYearLab = new Text(" Publish Year : ");
        pYearLab.setFont(myFont);
        pYearLab.setWrappingWidth(150);
        pYearLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(pYearLab, 0, 5);

        pubYear = new TextField();
        pubYear.setEditable(true);
        grid.add(pubYear, 1, 5);

        Text ISBNLab = new Text(" ISBN : ");
        ISBNLab.setFont(myFont);
        ISBNLab.setWrappingWidth(150);
        ISBNLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(ISBNLab, 0, 6);

        ISBN = new TextField();
        ISBN.setEditable(true);
        grid.add(ISBN, 1, 6);

        Text priceLab = new Text(" Price : ");
        priceLab.setFont(myFont);
        priceLab.setWrappingWidth(150);
        priceLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(priceLab, 0, 7);

        price = new TextField();
        price.setEditable(true);
        grid.add(price, 1, 7);

        Text notesLab = new Text(" Notes : ");
        notesLab.setFont(myFont);
        notesLab.setWrappingWidth(150);
        notesLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLab, 0, 8);

        notes = new TextField();
        notes.setEditable(true);
        grid.add(notes, 1, 8);

        /*statusBox = new ComboBox();
        statusBox.getItems().addAll("Active", "Inactive");
        statusBox.getSelectionModel().selectFirst();

        grid.add(statusBox, 1, 11);*/

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);

        backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("back", null);
            }
        });
        doneCont.getChildren().add(backButton);

        doneButton = new Button("Submit");
        doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                Properties p = new Properties();


                p.setProperty("barcode", barcode.getText());
                p.setProperty("title", bookTitle.getText());
                p.setProperty("author", author.getText());
                p.setProperty("publisher", publisher.getText());
                p.setProperty("pubYear", pubYear.getText());
                p.setProperty("ISBN", ISBN.getText());
                p.setProperty("price", price.getText());
                p.setProperty("notes", notes.getText());


                clearText();
                myModel.stateChangeRequest("BookData", p);
            }
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
        barcode.clear();
        bookTitle.clear();
        author.clear();
        publisher.clear();
        pubYear.clear();
        ISBN.clear();
        price.clear();
        notes.clear();
        //statusBox.valueProperty().set("Active");
    }

}