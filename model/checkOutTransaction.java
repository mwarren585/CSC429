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
    private Book nb;
    private Book b;
    private Book modifyBook;
    private Worker w;
    private String fName = "";
    private String lName = "";
    private int mode = 0;
    private String errorMessage;

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
        if (key.equals("StudentList") == true)
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
        else if (key.equals("searchMode")){
            return mode;
        }
        else if (key.equals("bookError")){
            return errorMessage;
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
        if(key.equals("search")){
            mode = (int)value;
        }
        else if(key.equals("checkOutTrans")){
            w = (Worker)value;
            createAndShowCheckOutBookView();
        }
        else
        if (key.equals("SearchStudents") == true && value != null)
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
        if (key.equals("BookSelected")){

            try {
                b = new Book((String) value);
                String status = (String) b.getState("status");
                createRental();
            }
            catch(InvalidPrimaryKeyException e){
                errorMessage = e.getMessage();
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
        r.stateChangeRequest("checkInWorkerID", "0");
        System.out.println("About to insert rental");
        r.update();
    }

    private void createRental() {
        String bar = (String)b.getState("barcode");
        try {
            Rental r = new Rental(bar);
            //create a view that says rental already exists
            } catch (InvalidPrimaryKeyException e) {
            errorMessage = e.getMessage();
            createAndShowRentBook();
        }
    }
    private void updateBook(){

        Properties p = new Properties();

        String barCode = (String)b.getState("barcode");
        String titl = (String)b.getState("title");
        String aut = (String)b.getState("author");
        String aut2 = (String)b.getState("author2");
        String aut3 = (String)b.getState("author3");
        String aut4 = (String)b.getState("author4");
        String pub = (String)b.getState("publisher");
        String py = (String)b.getState("pubYear");
        String isb = (String)b.getState("ISBN");
        String pr = (String)b.getState("price");
        String not = (String)b.getState("notes");
        String stat = (String)b.getState("status");




        p.setProperty("barcode", barCode);
        p.setProperty("title", titl);
        p.setProperty("author", aut);
        p.setProperty("author2", aut2);
        p.setProperty("author3", aut3);
        p.setProperty("author4", aut4);
        p.setProperty("publisher", pub);
        p.setProperty("pubYear", py);
        p.setProperty("ISBN", isb);
        p.setProperty("price", pr);
        p.setProperty("notes", not);
        p.setProperty("status", stat);
        p.setProperty("checkedOut", "Yes");


        modifyBook = new Book(p);
        modifyBook.setOldFlagTrue();
        modifyBook.save();
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
        View newView = ViewFactory.createView("StudentCollectionView", this); // USE VIEW FACTORY
        currentScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowBarcodeView()
    {
        Scene currentScene = null;

        // create our initial view
        View newView = ViewFactory.createView("SearchBookView", this); // USE VIEW FACTORY
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

        fName = z.getProperty("firstName");
        lName = z.getProperty("lastName");
        sc.findStudentsWithNameLike(fName, lName);
        createAndShowStudentSelectionView();
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
        dependencies.setProperty("BookSelected", "bookError");
    }

    @Override
    public void swapToView(IView viewName) {

    }




}