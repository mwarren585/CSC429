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
import model.Worker;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class ModifyBookView extends View
{
    // GUI components
    protected TextField barcode;
    protected TextField bookTitle;
    protected TextField author;
    protected TextField author2;
    protected TextField author3;
    protected TextField author4;
    protected TextField publisher;
    protected TextField pubYear;
    protected TextField ISBN;
    protected TextField price;
    protected TextField notes;
    protected ComboBox statusBox;

    protected Button doneButton;
    protected Button backButton;
    // For showing error message
    protected MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ModifyBookView(IModel Worker)
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

        Text prompt = new Text("BOOK INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text barLab = new Text(" Barcode : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        barLab.setFont(myFont);
        barLab.setWrappingWidth(150);
        barLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(barLab, 0, 1);

        barcode = new TextField();
        barcode.setEditable(true);
        grid.add(barcode, 1, 1);

        Text titlLab = new Text(" Title : ");
        titlLab.setFont(myFont);
        titlLab.setWrappingWidth(150);
        titlLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(titlLab, 0, 2);

        bookTitle = new TextField();
        bookTitle.setEditable(true);
        grid.add(bookTitle, 1, 2);

        Text authLab = new Text(" Author : ");
        authLab.setFont(myFont);
        authLab.setWrappingWidth(150);
        authLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(authLab, 0, 3);

        author = new TextField();
        author.setEditable(true);
        grid.add(author, 1, 3);

        Text auth2Lab = new Text(" Author #2 : ");
        auth2Lab.setFont(myFont);
        auth2Lab.setWrappingWidth(150);
        auth2Lab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(auth2Lab, 0, 4);

        author2 = new TextField();
        author2.setEditable(true);
        grid.add(author2, 1, 4);

        Text auth3Lab = new Text(" Author #3 : ");
        auth3Lab.setFont(myFont);
        auth3Lab.setWrappingWidth(150);
        auth3Lab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(auth3Lab, 0, 5);

        author3 = new TextField();
        author3.setEditable(true);
        grid.add(author3, 1, 5);

        Text auth4Lab = new Text(" Author #4 : ");
        auth4Lab.setFont(myFont);
        auth4Lab.setWrappingWidth(150);
        auth4Lab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(auth4Lab, 0, 6);

        author4 = new TextField();
        author4.setEditable(true);
        grid.add(author4, 1, 6);


        Text publishLab = new Text(" Publisher : ");
        publishLab.setFont(myFont);
        publishLab.setWrappingWidth(150);
        publishLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(publishLab, 0, 7);

        publisher = new TextField();
        publisher.setEditable(true);
        grid.add(publisher, 1, 7);

        Text pYearLab = new Text(" Publish Year : ");
        pYearLab.setFont(myFont);
        pYearLab.setWrappingWidth(150);
        pYearLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(pYearLab, 0, 8);

        pubYear = new TextField();
        pubYear.setEditable(true);
        grid.add(pubYear, 1, 8);

        Text ISBNLab = new Text(" ISBN : ");
        ISBNLab.setFont(myFont);
        ISBNLab.setWrappingWidth(150);
        ISBNLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(ISBNLab, 0, 9);

        ISBN = new TextField();
        ISBN.setEditable(true);
        grid.add(ISBN, 1, 9);

        Text priceLab = new Text(" Price : ");
        priceLab.setFont(myFont);
        priceLab.setWrappingWidth(150);
        priceLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(priceLab, 0, 10);

        price = new TextField();
        price.setEditable(true);
        grid.add(price, 1, 10);

        Text notesLab = new Text(" Notes : ");
        notesLab.setFont(myFont);
        notesLab.setWrappingWidth(150);
        notesLab.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLab, 0, 11);

        notes = new TextField();
        notes.setEditable(true);
        grid.add(notes, 1, 11);

        Text sta = new Text(" Book Status : ");
        sta.setFont(myFont);
        sta.setWrappingWidth(150);
        sta.setTextAlignment(TextAlignment.RIGHT);
        grid.add(sta, 0, 12);

        statusBox = new ComboBox();
        statusBox.getItems().addAll("Active", "Inactive");
        statusBox.getSelectionModel().selectFirst();
        grid.add(statusBox, 1, 12);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);

        backButton = new Button("Cancel");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("back", null);
            }
        });

        doneButton = new Button("Submit");
        doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                Properties p = new Properties();

                String barCode = barcode.getText();
                p.setProperty("barcode", barCode);
                p.setProperty("title", bookTitle.getText());
                p.setProperty("author", author.getText());
                p.setProperty("author2", author2.getText());
                p.setProperty("author3", author3.getText());
                p.setProperty("author4", author4.getText());
                p.setProperty("publisher", publisher.getText());
                p.setProperty("pubYear", pubYear.getText());
                p.setProperty("ISBN", ISBN.getText());
                p.setProperty("price", price.getText());
                p.setProperty("notes", notes.getText());
                p.setProperty("status", (String)statusBox.getValue());


                clearText();
                myModel.stateChangeRequest("InsertBookData", p);
                displayMessage("Book with Barcode: "+ barCode +" Modified Succcessfully!");
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
        Book selectedBook = (Book)myModel.getState("Book");
        barcode.setText((String)selectedBook.getState("barcode"));
        bookTitle.setText((String)selectedBook.getState("title"));
        author.setText((String)selectedBook.getState("author"));
        author2.setText((String)selectedBook.getState("author2"));
        author3.setText((String)selectedBook.getState("author3"));
        author4.setText((String)selectedBook.getState("author4"));
        publisher.setText((String)selectedBook.getState("publisher"));
        pubYear.setText((String)selectedBook.getState("pubYear"));
        ISBN.setText((String)selectedBook.getState("ISBN"));
        price.setText((String)selectedBook.getState("price"));
        notes.setText((String)selectedBook.getState("notes"));

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
        barcode.setText("");
        bookTitle.setText("");
        author.setText("");
        author2.setText("");
        author3.setText("");
        author4.setText("");
        publisher.setText("");
        pubYear.setText("");
        ISBN.setText("");
        price.setText("");
        notes.setText("");
    }

}
