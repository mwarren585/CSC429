// specify the package
package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

/** The class containing the Teller  for the ATM application */
//==============================================================
public class Librarian implements IView, IModel
// This class implements all these interfaces (and does NOT extend 'EntityBase')
// because it does NOT play the role of accessing the back-end database tables.
// It only plays a front-end role. 'EntityBase' objects play both roles.
{
    // For Impresario
    private Properties dependencies;
    private ModelRegistry myRegistry;

    private Worker myWorker;
    private WorkerCollection myWorkers;

    // GUI Components
    private Hashtable<String, Scene> myViews;
    private Stage	  	myStage;

    private String loginErrorMessage = "";
    private String transactionErrorMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public Librarian()
    {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        // STEP 3.1: Create the Registry object - if you inherit from
        // EntityBase, this is done for you. Otherwise, you do it yourself
        myRegistry = new ModelRegistry("Librarian");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "Librarian",
                    "Could not instantiate Registry", Event.ERROR);
        }

        // STEP 3.2: Be sure to set the dependencies correctly
        setDependencies();

        // Set up the initial view
        createAndShowLibrarianView();
    }

    //-----------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("Login", "LoginError");
        dependencies.setProperty("WorkerData", "TransactionError");
        dependencies.setProperty("StudentData", "TransactionError");
        dependencies.setProperty("Transfer", "TransactionError");
        dependencies.setProperty("BalanceInquiry", "TransactionError");
        dependencies.setProperty("ImposeServiceCharge", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * Method called from client to get the value of a particular field
     * held by the objects encapsulated by this object.
     *
     * @param	key	Name of database column (field) for which the client wants the value
     *
     * @return	Value associated with the field
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("LoginError") == true)
        {
            return loginErrorMessage;
        }
        else
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        else
        if (key.equals("Name") == true)
        {
            if (myWorker != null)
            {
                return myWorker.getState("firstName");
            }
            else
                return "Undefined";
        }
        else
        if(key.equals("WorkerList") == true){
            return myWorkers;
        }
        else
            return "";
    }

    //----------------------------------------------------------------
    /*public void stateChangeRequest(String key, Object value)
    {
        // STEP 4: Write the sCR method component for the key you
        // just set up dependencies for
        // DEBUG System.out.println("Teller.sCR: key = " + key);

        if (key.equals("Login") == true)
        {
            if (value != null)
            {
                loginErrorMessage = "";

                boolean flag = loginWorker((Properties)value);
                if (flag == true)
                {
                    createAndShowTransactionChoiceView();
                }
            }
        }
        else
        if (key.equals("CancelTransaction") == true)
        {
            createAndShowTransactionChoiceView();
        }
        else
        if ((key.equals("Add Book") == true) || (key.equals("Add Student Borrower") == true) ||
                (key.equals("Add Worker") == true) || (key.equals("Modify Worker") == true) ||
                (key.equals("ImposeServiceCharge") == true))
        {
            String transType = key;

            if (myWorker != null)
            {
                doTransaction(transType);
            }
            else
            {
                transactionErrorMessage = "Transaction impossible: Customer not identified";
            }

        }
        else
        if (key.equals("Logout") == true)
        {
            myWorker = null;
            myViews.remove("TransactionChoiceView");

            createAndShowLibrarianView();
        }

        myRegistry.updateSubscribers(key, this);
    }*/
   public void createNewWorker(Properties props){
        Worker worker = new Worker(props);
        worker.save();
        transactionErrorMessage = (String)worker.getState("UpdateStatusMessage");

    }
    public void createNewStudent(Properties props){
        StudentBorrower student = new StudentBorrower(props);
        student.update();
        transactionErrorMessage = (String)student.getState("UpdateStatusMessage");

    }
    public void createNewBook(Properties props){
        Book book = new Book(props);
        book.save();
        transactionErrorMessage = (String)book.getState("UpdateStatusMessage");

    }
    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        // STEP 4: Write the sCR method component for the key you
        // just set up dependencies for
        // DEBUG System.out.println("Teller.sCR: key = " + key);

        if (key.equals("Login") == true)
        {
            if (value != null)
            {
                loginErrorMessage = "";

                boolean flag = loginWorker((Properties)value);
                if (flag == true)
                {
                    createAndShowTransactionChoiceView();
                }
            }
        }
        else
        if (key.equals("CancelTransaction") == true)
        {
            createAndShowTransactionChoiceView();
        }

        else if (key.equals("AddStudent") == true)
        {
            createAndShowStudentView();
        }
        else if (key.equals("StudentData")== true){
            Properties p = (Properties)value;
            createNewStudent(p);
        }
        else if (key.equals("AddBook") == true)
        {
            createAndShowBookView();
        }
        else if (key.equals("BookData") == true)
        {
            Properties p = (Properties)value;
            createNewBook(p);
        }
        else if (key.equals("Search Books") == true){
            //createAndShowSearchBooksView();
        }
        else if (key.equals("Search Workers") == true)
        {
          //  createAndShowSearchPatronsView();
        }

        else if (key.equals("Add Worker") == true)
        {

            createAndShowAddWorkerView();

        }
        else if(key.equals("WorkerData")){
            Properties p = (Properties)value;
            createNewWorker(p);

        }
        else if(key.equals("FindBooks")){
            Properties p = (Properties)value;
            String titl = p.getProperty("bookTitle");
          //  myBooks = new BookCollection();
           // myBooks.findBooksWithTitleLike(titl);
            //createAndShowBookCollectionView();
        }
        else if(key.equals("WorkerCollection")){
            createAndShowWorkerCollectionView();
        }
        else if(key.equals("FindWorkers")){
            Properties p = (Properties)value;
            String first = p.getProperty("firstName");
            String last = p.getProperty("lastName");
            myWorkers = new WorkerCollection();
            myWorkers.findWorkersWithNameLike(first, last);

            createAndShowWorkerCollectionView();

        }
        else if(key.equals("Modify Worker")){
            createAndShowSearchWorkerView();
        }
        else if (key.equals("back") == true)
        {
            createAndShowTransactionChoiceView();

        }



        myRegistry.updateSubscribers(key, this);
    }


    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        // DEBUG System.out.println("Teller.updateState: key: " + key);

        stateChangeRequest(key, value);
    }

    /**
     * Login AccountHolder corresponding to user name and password.
     */
    //----------------------------------------------------------
    public boolean loginWorker(Properties props)
    {
        String givenBannerId = props.getProperty("bannerID");
        String givenPassword = props.getProperty("password");
        try
        {
            myWorker = new Worker(givenBannerId);
            // DEBUG System.out.println("Account Holder: " + myAccountHolder.getState("Name") + " successfully logged in");
            boolean logInSuccessFull = myWorker.matchPassword(givenPassword);
            if (logInSuccessFull == true)
                return true;
            else
                return false;
        }
        catch (InvalidPrimaryKeyException ex)
        {
            loginErrorMessage = "ERROR: " + ex.getMessage();
            return false;
        }

    }


    /**
     * Create a Transaction depending on the Transaction type (deposit,
     * withdraw, transfer, etc.). Use the AccountHolder holder data to do the
     * create.
     */
    //----------------------------------------------------------
    public void doTransaction(String transactionType)
    {
        try
        {
            Transaction trans = TransactionFactory.createTransaction(
                    transactionType, myWorker);

            trans.subscribe("CancelTransaction", this);
            trans.stateChangeRequest("DoYourJob", "");
        }
        catch (Exception ex)
        {
            transactionErrorMessage = "FATAL ERROR: TRANSACTION FAILURE: Unrecognized transaction!!";
            new Event(Event.getLeafLevelClassName(this), "createTransaction",
                    "Transaction Creation Failure: Unrecognized transaction " + ex.toString(),
                    Event.ERROR);
        }
    }

    //----------------------------------------------------------
    private void createAndShowWorkerCollectionView()
    {
        Scene currentScene = (Scene)myViews.get("WorkerCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("WorkerCollectionView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("WorkerCollectionView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }

    //------------------------------------------------------------
    private void createAndShowLibrarianView()
    {
        Scene currentScene = (Scene)myViews.get("LibrarianView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("LibrarianView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("LibrarianView", currentScene);
        }

        swapToView(currentScene);

    }

    private void createAndShowSearchWorkerView(){
        Scene currentScene = (Scene)myViews.get("SearchWorkerView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchWorkerView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("SearchWorkerView", currentScene);
        }

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
    private void createAndShowAddWorkerView()
    {
        Scene currentScene = (Scene)myViews.get("WorkerView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("WorkerView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("WorkerView", currentScene);
        }

        swapToView(currentScene);

    }
    private void createAndShowBookView()
    {
        Scene currentScene = (Scene)myViews.get("BookView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("BookView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("BookView", currentScene);
        }

        swapToView(currentScene);

    }


    //------------------

    private void createAndShowStudentView()
    {
        Scene currentScene = (Scene)myViews.get("StudentView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("StudentView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("StudentView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }

    /** Register objects to receive state updates. */
    //----------------------------------------------------------
    public void subscribe(String key, IView subscriber)
    {
        // DEBUG: System.out.println("Cager[" + myTableName + "].subscribe");
        // forward to our registry
        myRegistry.subscribe(key, subscriber);
    }

    /** Unregister previously registered objects. */
    //----------------------------------------------------------
    public void unSubscribe(String key, IView subscriber)
    {
        // DEBUG: System.out.println("Cager.unSubscribe");
        // forward to our registry
        myRegistry.unSubscribe(key, subscriber);
    }



    //-----------------------------------------------------------------------------
    public void swapToView(Scene newScene)
    {


        if (newScene == null)
        {
            System.out.println("Librarian.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
            return;
        }

        myStage.setScene(newScene);
        myStage.sizeToScene();


        //Place in center
        WindowPosition.placeCenter(myStage);

    }

}

