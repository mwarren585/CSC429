package model;

import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import java.util.Hashtable;
import java.util.Properties;

public class checkOutTransaction implements IView, IModel, ISlideShow {

    private ModelRegistry myRegistry;
    private Hashtable<String, Scene> myViews;
    private Stage myStage;
    private Properties dependencies;
    private StudentBorrowerCollection sc;
    private StudentBorrower sB;
    private Book b;
    private Worker w;
    private String fName = "";
    private String lName = "";

    protected checkOutTransaction(){
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();
        myRegistry = new ModelRegistry("CheckOutBookTransaction");

        setDependencies();
    }


    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        if (key.equals("StudentBorrowerList") == true)
        {
            return sc;
        }
        else if (key.equals("book") == true)
        {
            return b;
        }
        else if (key.equals("student") == true)
        {
            return sB;
        }
        else if (key.equals("worker") == true)
        {
            return w;
        }
        else
            return null;
    }

    @Override
    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    @Override
    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if(key.equals("checkOutTrans")){
            w = (Worker)value;
            createAndShowCheckOutBookView();
        }
        else
        if (key.equals("SelectStudentView") == true && value != null)
        {
            try {
                searchStudents((Properties)value);
            } catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
        }
        else
        if (key.equals("StudentSelected")){
            try{
                createAndShowBarcodeView();
                sB = new StudentBorrower((String)value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        if (key.equals("BookModification")){
            try{
                b = new Book((String)value);
                String status = (String) b.getState("status");
                if (status.equals("Active"))
                    createRental();
                else
                    databaseError();

            } catch (InvalidPrimaryKeyException e) {
                databaseError();
            }
        }
        else
        if (key.equals("RentBook")){
            try{
                b = new Book((String)value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        if (key.equals("InsertRental")){
            try{
                insertRental((Properties)value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        myRegistry.updateSubscribers(key, this);
    }

    private void insertRental(Properties p) {
        Rental r = new Rental(p);
        r.update();
        databaseUpdated();
    }

    private void createRental() {
        String bar = (String)b.getState("barcode");
        try {
            Rental r = new Rental(bar);
            //create a view that says rental already exists
            } catch (InvalidPrimaryKeyException e) {
            createAndShowRentBook();
        }
    }



    private void createAndShowCheckOutBookView()
    {
        Scene currentScene = null;

        // create our initial view
        View newView = ViewFactory.createView("CheckoutBook", this); // USE VIEW FACTORY
        currentScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowStudentSelectionView()
    {
        Scene currentScene = null;

        // create our initial view
        View newView = ViewFactory.createView("StudentSelectionView", this); // USE VIEW FACTORY
        currentScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowBarcodeView()
    {
        Scene currentScene = null;

        // create our initial view
        View newView = ViewFactory.createView("BarcodeSearchView", this); // USE VIEW FACTORY
        currentScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowRentBook()
    {
        Scene currentScene = null;

        // create our initial view
        View newView = ViewFactory.createView("RentBook", this); // USE VIEW FACTORY
        currentScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void searchStudents(Properties z) throws InvalidPrimaryKeyException {
        sc = new StudentBorrowerCollection();
        sc.getFirstAndLastName(z.getProperty("firstName"), z.getProperty("lastName"));
        createAndShowStudentSelectionView();
        fName = z.getProperty("firstName");
        lName = z.getProperty("lastName");

    }

    public void swapToView(Scene newScene)
    {


        if (newScene == null)
        {
            System.out.println("Teller.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
            return;
        }

        myStage.setScene(newScene);
        myStage.sizeToScene();


        //Place in center
        WindowPosition.placeCenter(myStage);

    }

    private void setDependencies(){
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }

    @Override
    public void swapToView(IView viewName) {

    }



    public void databaseUpdated(){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Database");
        alert.setHeaderText("Book Check Out Successful ");

        alert.showAndWait();
    }

    public void databaseError(){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database");
        alert.setHeaderText("Ooops, there was an error accessing the database.");
        alert.setContentText("Please make sure everything is filled out correctly and try again.");

        alert.showAndWait();
    }
}