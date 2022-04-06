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
import model.*;

import javax.swing.table.TableModel;
import java.util.Enumeration;
import java.util.Vector;

public class StudentCollectionView extends View
{
    protected TableView<StudentBorrowerTableModel> tableOfStudents;

    protected Button update;


    protected MessageView statusLog;


    //--------------------------------------------------------------------------
    public StudentCollectionView(IModel student)
    {

        super(student, "StudentCollectionView");
        System.out.println("-------------------------------------------------------------------------------------------");
        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        populateFields();
    }

    //--------------------------------------------------------------------------
    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<StudentBorrowerTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            StudentBorrowerCollection studentCollection = (StudentBorrowerCollection)myModel.getState("StudentList");

            Vector entryList = (Vector) studentCollection.getState("StudentList");
            System.out.println("worker coll size: " + entryList.size());
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements() == true)
            {
                System.out.println("Inside has more elements");
                StudentBorrower nextStudent = (StudentBorrower) entries.nextElement();
                System.out.println("Next worker for table" + nextStudent);
                Vector<String> view = nextStudent.getEntryListView();

                // add this list entry to the list
                StudentBorrowerTableModel nextTableRowData = new StudentBorrowerTableModel(view);
                tableData.add(nextTableRowData);
            }

            tableOfStudents.setItems(tableData);
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

        Text titleText = new Text(" Student Borrowers With Books Checked Out ");
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

        Text prompt = new Text("LIST OF SUDENTS");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        tableOfStudents = new TableView<StudentBorrowerTableModel>();
        tableOfStudents.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn bannerIDColumn = new TableColumn("bannerID") ;
        bannerIDColumn.setMinWidth(25);
        bannerIDColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("bannerID"));

        TableColumn fNameColumn = new TableColumn("firstName") ;
        fNameColumn.setMinWidth(25);
        fNameColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("firstName"));

        TableColumn lNameColumn = new TableColumn("lastName") ;
        lNameColumn.setMinWidth(100);
        lNameColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("lastName"));

        TableColumn phoneColumn = new TableColumn("phone") ;
        phoneColumn.setMinWidth(100);
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("phone"));

        TableColumn emailColumn = new TableColumn("email") ;
        emailColumn.setMinWidth(100);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("email"));

        TableColumn author3Column = new TableColumn("borrowerStatus") ;
        author3Column.setMinWidth(100);
        author3Column.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("borrowerStatus"));

        TableColumn latestBorrowerColumn = new TableColumn("dateOfLatestBorrower") ;
        latestBorrowerColumn.setMinWidth(100);
        latestBorrowerColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("dateOfLatestBorrower"));

        TableColumn regColumn = new TableColumn("dateOfRegistration") ;
        regColumn.setMinWidth(25);
        regColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("dateOfRegistration"));

        TableColumn notesColumn = new TableColumn("notes") ;
        notesColumn.setMinWidth(25);
        notesColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("notes"));

        TableColumn statusColumn = new TableColumn("status") ;
        statusColumn.setMinWidth(25);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("status"));

        tableOfStudents.getColumns().addAll(bannerIDColumn, fNameColumn, lNameColumn, phoneColumn, emailColumn, author3Column
                , latestBorrowerColumn, regColumn, notesColumn, statusColumn);


        tableOfStudents.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    processStudentSelected();
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfStudents);

        update = new Button("Back");
        update.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the inquiry
                processAction();
            }
        });



        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(update);
        //btnContainer.getChildren().add(cancelButton);

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
    protected void processAction()
    {
        myModel.stateChangeRequest("CancelTransaction", null);
    }

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


    protected void processStudentSelected()
    {
        StudentBorrowerTableModel selectedStudent = tableOfStudents.getSelectionModel().getSelectedItem();

        if(selectedStudent != null)
        {

            myModel.stateChangeRequest("StudentSelected", selectedStudent.getBannerID());
        }
    }

}