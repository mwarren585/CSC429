
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

public class DelinquencyCheckTransaction implements IView, IModel, ISlideShow {

    private ModelRegistry myRegistry;
    private Hashtable<String, Scene> myViews;
    private Stage myStage;
    private Properties dependencies;
    private RentalCollection rc;
    private Rental r;
    private String id = "";
    private String bCode = "";
    private String lName = " ";
    private boolean check;


    protected DelinquencyCheckTransaction(){
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();
        myRegistry = new ModelRegistry("DelinquencyCheckTransaction");

        setDependencies();
    }


    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        if (key.equals("RentalList") == true)
        {
            return rc;
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
        if(key.equals("delCheck")) {

            check = runDelinquency();

            if (check == false) {
                databaseDelCheckError();
            } else {
                createAndShowDelinquencyCheckTransaction();
            }
        }
        else

        myRegistry.updateSubscribers(key, this);
    }

    private boolean runDelinquency() {
        boolean run;
        RentalCollection r = new RentalCollection();
        r.getDelinquencyCheck();
        Vector<Rental> check = (Vector)r.getState("Rentals");
        if(check.isEmpty() == true)
        {
            run = false;
        }
        else
            run = true;
            Vector<Rental> col = (Vector) r.getState("Rentals");
            for (int i = 0; i < col.size(); i++) {
                try {
                    StudentBorrower s = new StudentBorrower((String) col.elementAt(i).getState("borrowerID"));
                    s.stateChangeRequest("borrowerStatus", "Delinquent");
                    s.update();

                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
            }
        return run;
    }


    private void createAndShowDelinquencyCheckTransaction()
    {
        Scene currentScene = null;

        // create our initial view
        View newView = ViewFactory.createView("DelCheck", this); // USE VIEW FACTORY
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

    public void databaseDelCheckError(){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database");
        alert.setHeaderText("Ooops, there was an issue comleting your request.");
        alert.setContentText("There are no students eligible for delinquency status.");

        alert.showAndWait();
    }
}