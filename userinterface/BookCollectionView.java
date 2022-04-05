package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Book;
import model.BookCollection;


import java.util.Enumeration;
import java.util.Vector;

public class BookCollectionView extends View {
    // GUI components
    protected TableView<BookTableModel> tableOfBooks;
    protected Button doneButton;


    // For showing error message
    protected MessageView statusLog;

    public BookCollectionView(IModel Book)
    {
        super(Book, "BookCollectionView");

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


        myModel.subscribe("TransactionError", this);
    }
    //--------------------------------------------------------------------------
    protected void populateFields()
    {

        getEntryTableModelValues();
    }
    protected void getEntryTableModelValues()
    {


        ObservableList<BookTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            BookCollection bookCollection = (BookCollection)myModel.getState("BookList");

            Vector entryList = (Vector)bookCollection.getState("BookList");
            System.out.println("Book coll size: " + entryList.size());
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements())
            {
                System.out.println("Inside hasMoreElements");
                Book nextBook = (Book) entries.nextElement();
                System.out.println("Next element: " + nextBook);
                Vector<String> view = nextBook.getEntryListView();

                // add this list entry to the list'
                BookTableModel nextTableRowContext = new BookTableModel(view);
                tableData.add(nextTableRowContext);

            }


            tableOfBooks.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
            System.out.println(e);
            e.printStackTrace();
        }
    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Book Collection ");
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

        Text prompt = new Text("LIST OF Books");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        tableOfBooks = new TableView<BookTableModel>();
        tableOfBooks.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        /*TableColumn bookIdColumn = new TableColumn("Book ID") ;
        bookIdColumn.setMinWidth(100);
        bookIdColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("bookID"));*/

        TableColumn barcodeColumn = new TableColumn("Barcode") ;
        barcodeColumn.setMinWidth(100);
        barcodeColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("barcode"));

        TableColumn titleColumn = new TableColumn("Book Title") ;
        titleColumn.setMinWidth(100);
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("bookTitle"));

        TableColumn authColumn = new TableColumn("Author") ;
        authColumn.setMinWidth(100);
        authColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("author"));

        TableColumn auth2Column = new TableColumn("Author2") ;
        auth2Column.setMinWidth(100);
        auth2Column.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("author2"));

        TableColumn auth3Column = new TableColumn("Author3") ;
        auth3Column.setMinWidth(100);
        auth3Column.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("author3"));

        TableColumn auth4Column = new TableColumn("Author4") ;
        auth4Column.setMinWidth(100);
        auth4Column.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("author4"));

        TableColumn publisherColumn = new TableColumn("Publisher") ;
        publisherColumn.setMinWidth(100);
        publisherColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("publisher"));

        TableColumn pubYearColumn = new TableColumn("Publication Year") ;
        pubYearColumn.setMinWidth(100);
        pubYearColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("pubYear"));

        TableColumn iSBNColumn = new TableColumn("ISBN") ;
        iSBNColumn.setMinWidth(100);
        iSBNColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("ISBN"));

        TableColumn priceColumn = new TableColumn("Price") ;
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("price"));
        TableColumn notesColumn = new TableColumn("Notes") ;
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("notes"));

        tableOfBooks.getColumns().addAll(barcodeColumn, titleColumn, authColumn, auth2Column, auth3Column, auth4Column,
                publisherColumn, pubYearColumn, iSBNColumn, priceColumn, notesColumn);


        tableOfBooks.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    provessBookHighlighted();
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfBooks);



        //TODO need to look into switching this
        doneButton = new Button("Back");
        doneButton.setOnAction(e -> {
            /**
             * Process the Cancel button.
             * The ultimate result of this action is that the transaction will tell the teller to
             * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
             * It simply tells its model (controller) that the transaction was canceled, and leaves it
             * to the model to decide to tell the teller to do the switch back.
             */
            //----------------------------------------------------------
            clearErrorMessage();
            myModel.stateChangeRequest("CancelTransaction", null);
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(doneButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }



    //--------------------------------------------------------------------------
    public void updateState(String key, Object value)
    {
    }

    //--------------------------------------------------------------------------
    /*protected void processAccountSelected()
    {
        BookTableModel selectedItem = tableOfBooks.getSelectionModel().getSelectedItem();
        if(selectedItem != null)
        {
            String selectedAcctNumber = selectedItem.getAccountNumber();
            myModel.stateChangeRequest("AccountSelected", selectedAcctNumber);
        }
    }*/

    //--------------------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
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
    protected void provessBookHighlighted()
    {
        BookTableModel bookChosen = tableOfBooks.getSelectionModel().getSelectedItem();

        if(bookChosen != null)
        {

            myModel.stateChangeRequest("BookSelected", bookChosen.getBarcode());
        }
    }


}