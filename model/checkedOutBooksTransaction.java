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
import java.util.Vector;

public class checkedOutBooksTransaction implements IView, IModel, ISlideShow {

    private ModelRegistry myRegistry;
    private Hashtable<String, Scene> myViews;
    private Stage myStage;
    private Properties dependencies;
    private BookCollection bc;
    private Rental r;
    private String id = "";
    private String bCode = "";
    private String lName = " ";
    private int check;


    protected checkedOutBooksTransaction(){
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();
        myRegistry = new ModelRegistry("GetCheckedOutBooksTransaction");

        setDependencies();
    }


    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        if (key.equals("BookList") == true)
        {
            return bc;
        }
        else if (key.equals("rental") == true)
        {
            return r;
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
        if(key.equals("checkedOutBooks")) {
            boolean check = getBooksFromRentals();
            if (check == false) {
                databaseCheckedOutError();
            } else {
                createAndShowBookCollectionView();
            }
        }
        else
            myRegistry.updateSubscribers(key, this);
    }

    private boolean getBooksFromRentals() {
        boolean run;
        RentalCollection r = new RentalCollection();
        r.getCheckedOutRentals();
        Vector<Rental> check = (Vector)r.getState("Rentals");
        if(check.isEmpty() == true)
        {
            run = false;
        }
        else {
            run = true;
            bc = new BookCollection();
            Vector<Rental> col = (Vector) r.getState("Rentals");
            for (int i = 0; i < col.size(); i++) {
                try {
                    Book b = new Book((String) col.elementAt(i).getState("bookID"));
                    bc.addBook(b);
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
            }
        }
        return run;
    }

    private void createAndShowBookCollectionView()
    {
        Scene currentScene = null;

        // create our initial view
        View newView = ViewFactory.createView("BookCollectionView", this); // USE VIEW FACTORY
        currentScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(currentScene);
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

    public void databaseCheckedOutError(){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database");
        alert.setHeaderText("Ooops, there was an issue completing your request.");
        alert.setContentText("There are no books that are checked out.");

        alert.showAndWait();
    }
}
