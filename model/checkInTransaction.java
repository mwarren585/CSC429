package model;

//import com.sun.java.swing.plaf.windows.TMSchema;
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

import javax.rmi.PortableRemoteObject;
import java.util.Hashtable;
import java.util.Properties;

public class checkInTransaction implements IView, IModel, ISlideShow {

    private ModelRegistry myRegistry;
    private Hashtable<String, Scene> myViews;
    private Stage myStage;
    private Properties dependencies;
    private Rental r;
    private Worker w;
    private int mode;
    private String loginErrorMessage = "";

    protected checkInTransaction() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();
        myRegistry = new ModelRegistry("CheckInBookTransaction");

        setDependencies();
    }


    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        if (key.equals("worker")) {
            return w;
        }
        else if (key.equals(("rental"))) {
            return r;
        }
        else if (key.equals(("searchMode"))) {
            return mode;
        }
        else if(key.equals("BookError")){
            return loginErrorMessage;
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
        if (key.equals("checkInTrans")) {
            w = (Worker) value;
            createAndShowCheckOutBookView();
        }
        else if (key.equals("RentalModification")) {
                try {
                    r = new Rental((String) value);
                    createAndShowRentBook();
                }
                catch (InvalidPrimaryKeyException e){
                    loginErrorMessage = e.getMessage();
                }




        }
        else if (key.equals("InsertRental")) {
            updateRental((Properties) value);
            //STOPPPPPPED HERE DONT FORGET
        }
        else if (key.equals("search")){
            mode = (int)value;
        }

        myRegistry.updateSubscribers(key, this);
    }

    private void updateRental(Properties value) {
        Rental rental = new Rental(value);
        rental.setExistsTrue();
        rental.update();
    }
    /*private void createRental(Properties value){
        String barC = value.getProperty("");

            r = new Rental(value);

    }*/
    private void createAndShowCheckOutBookView() {
        Scene currentScene = null;

        // create our initial view
        View newView = ViewFactory.createView("SearchBookView", this); // USE VIEW FACTORY
        currentScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowRentBook() {
        Scene currentScene = null;

        // create our initial view
        View newView = ViewFactory.createView("CheckInBook", this); // USE VIEW FACTORY
        currentScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowTransactionChoiceView()
    {
        Scene currentScene = (Scene)myViews.get("TransactionChoiceView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("TransactionChoiceView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("TransactionChoiceView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }

    public void swapToView(Scene newScene) {


        if (newScene == null) {
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

    private void setDependencies() {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
        dependencies.setProperty("RentalModification", "BookError");
    }

    @Override
    public void swapToView(IView viewName) {

    }
}
