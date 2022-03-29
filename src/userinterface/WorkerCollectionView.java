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
import model.Worker;
import model.WorkerCollection;

import java.util.Enumeration;
import java.util.Vector;

public class WorkerCollectionView extends View {
    // GUI components
    protected TableView<WorkerTableModel> tableOfWorkers;
    protected Button doneButton;


    // For showing error message
    protected MessageView statusLog;

    public WorkerCollectionView(IModel patron)
    {
        super(patron, "WorkerCollectionView");

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

        ObservableList<WorkerTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            WorkerCollection workerCollection = (WorkerCollection)myModel.getState("WorkerList");

            Vector entryList = (Vector)workerCollection.getState("Workers");
            System.out.println("worker coll size: " + entryList.size());
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements() == true)
            {
                System.out.println("Inside has more elements");
                Worker nextWorker = (Worker) entries.nextElement();
                System.out.println("Next worker for table" + nextWorker);
                Vector<String> view = nextWorker.getEntryListView();

                // add this list entry to the list
                WorkerTableModel nextTableRowData = new WorkerTableModel(view);
                tableData.add(nextTableRowData);

            }


            tableOfWorkers.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
        }
    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Library System ");
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

        Text prompt = new Text("LIST OF WORKERS");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        tableOfWorkers = new TableView<WorkerTableModel>();
        tableOfWorkers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn workerIdColumn = new TableColumn("Banner ID") ;
        workerIdColumn.setMinWidth(100);
        workerIdColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("bannerID"));

        TableColumn fNameColumn = new TableColumn("First Name") ;
        fNameColumn.setMinWidth(100);
        fNameColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("firstName"));

        TableColumn lNameColumn = new TableColumn("Last Name") ;
        lNameColumn.setMinWidth(100);
        lNameColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("lastName"));

        TableColumn phoneColumn = new TableColumn("Phone") ;
        phoneColumn.setMinWidth(100);
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("phone"));

        TableColumn emailColumn = new TableColumn("Email") ;
        emailColumn.setMinWidth(100);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("email"));

        TableColumn credColumn = new TableColumn("Credentials") ;
        credColumn.setMinWidth(100);
        credColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("credentials"));

        TableColumn dateCredColumn = new TableColumn("Date of Crendentials") ;
        dateCredColumn.setMinWidth(100);
        dateCredColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("dateOfLastCredentials"));

        TableColumn dateOfHireColumn = new TableColumn("Date Of Hire") ;
        dateOfHireColumn.setMinWidth(100);
        dateOfHireColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("dateOfHire"));



        /*tableOfPatrons.getColumns().addAll(patronIdColumn,
                nameColumn, titleColumn, publicationYearColumn, stateColumn, zipColumn, emailColumn, dateOfBirthColumn, statusColumn);*/

        tableOfWorkers.getColumns().addAll(workerIdColumn,
                fNameColumn, lNameColumn, phoneColumn, emailColumn, credColumn, dateCredColumn, dateOfHireColumn);

        tableOfWorkers.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    //processAccountSelected();
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfWorkers);



        //TODO need to look into switching this
        doneButton = new Button("Back");
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
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
            }
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


}