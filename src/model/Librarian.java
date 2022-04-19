// specify the package
package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;


import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.*;

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
    private Worker selectedWorker;
    private Worker modifyWorker;
    private Book selectedBook;
    private Book modifyBook;
    private StudentBorrower selectedStudent;
    private StudentBorrower modifyStudent;
    private Rental myRental;


    private WorkerCollection myWorkers;
    private BookCollection myBooks;
    private StudentBorrowerCollection myStudents;

    private DelinquencyCheckTransaction dCt;

    private CheckInBookView clt;

    // GUI Components
    private Hashtable<String, Scene> myViews;
    private Stage	  	myStage;

    private String loginErrorMessage = "";
    private String transactionErrorMessage = "";

    private int searchMode = 1;

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
        if (key.equals("firstName") == true)
        {
            if (myWorker != null)
            {
                return myWorker.getState("firstName");
            }
            else
                return "Undefined";
        }
        else
        if(key.equals("WorkerList")){
            return myWorkers;
        }
        else
        if(key.equals("BookList")){
            return myBooks;
        }
        else if(key.equals("StudentList")){
            return myStudents;
        }
        else if(key.equals("searchMode")){
            return searchMode;
        }
        else if (key.equals("Worker")){
            return selectedWorker;
        }
        else if (key.equals("Book")){
            return selectedBook;
        }
        else if (key.equals("Student")){
            return selectedStudent;
        }
        else
            return "";
    }

    //----------------------------------------------------------------

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
                else{

                }
            }
        }
        else if(key.equals("Logout")){
            createAndShowLibrarianView();
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




        else if (key.equals("Add Worker") == true)
        {

            createAndShowAddWorkerView();

        }
        else if(key.equals("WorkerData")){
            Properties p = (Properties)value;
            createNewWorker(p);

        }
        else if(key.equals("Search Worker")){
            searchMode = (int)value;
            createAndShowSearchWorkerView();
        }
        else if(key.equals("Search Book")){
            searchMode = (int)value;
            createAndShowSearchBookView();
        }
        else if(key.equals("Search Student")){
            searchMode = (int)value;
            createAndShowSearchStudentView();
        }
        else if(key.equals("FindWorkers")){
            Properties p = (Properties)value;
            String first = p.getProperty("firstName");
            String last = p.getProperty("lastName");
            myWorkers = new WorkerCollection();
            myWorkers.findWorkersWithNameLike(first, last);

            createAndShowWorkerCollectionView();
        }
        else if(key.equals("FindBooks")){
            Properties p = (Properties)value;
            String barcode = p.getProperty("barcode");
            myBooks = new BookCollection();
            myBooks.findBookWithBarcodeLike(barcode);
            createAndShowBookCollectionView();
        }
        else if(key.equals("FindBook")){
            Properties p = (Properties)value;
            String barcode = p.getProperty("barcode");
            try {
                myRental = new Rental(barcode);
                myRental.setCheckInDate();
                myRental.setCheckInWorkerId(myWorker.getWorkerId());
                myRental.setNullDueDate();
                myRental.update();
            }catch(Exception e){
                System.out.println("Rental not found");
            }

        }
        else if(key.equals("FindStudents")){
            Properties p = (Properties)value;
            String first = p.getProperty("firstName");
            String last = p.getProperty("lastName");
            myStudents = new StudentBorrowerCollection();
            myStudents.findStudentsWithNameLike(first, last);

            createAndShowStudentCollectionView();
        }
        else if (key.equals("WorkerSelected"))
        {
            if(searchMode == 1){
                try {
                    getWorker((String)value);
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
                createAndShowModifyWorkerView();
            }
            else if(searchMode == 2){
                try {
                    getWorker((String)value);
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
                createAndShowDeleteWorkerView();
            }
        }
        else if (key.equals("BookSelected"))
        {
            if(searchMode == 1){
                try {
                    getBook((String)value);
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
                createAndShowModifyBookView();
            }
            else if(searchMode == 2){
                try {
                    getBook((String)value);
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
                createAndShowDeleteBookView();
            }
        }
        else if (key.equals("StudentSelected"))
        {
            if(searchMode == 1){
                try {
                    getStudent((String)value);
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
                createAndShowModifyStudentBorrowerView();
            }
            else if(searchMode == 2){
                try {
                    getStudent((String)value);
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
                createAndShowDeleteStudentBorrowerView();
            }
        }
        else if(key.equals("InsertWorkerData")){
            Properties p = (Properties)value;
            modifyWorker = new Worker(p);
            modifyWorker.setOldFlagTrue();
            modifyWorker.save();
            modifyWorker.getState("UpdateStatusMessage");
        }
        else if(key.equals("InsertBookData")){
            Properties p = (Properties)value;
            modifyBook = new Book(p);
            //modifyBook.setOldFlagTrue();
            modifyBook.save();
            modifyBook.getState("UpdateStatusMessage");
        }
        else if(key.equals("InsertStudentData")){
            Properties p = (Properties)value;
            modifyStudent = new StudentBorrower(p);
            modifyStudent.setExistsTrue();
            modifyStudent.update();
            modifyStudent.getState("UpdateStatusMessage");
        }
        else if(key.equals("Delinquency Check")){
            dCt = new DelinquencyCheckTransaction();
            dCt.subscribe("CancelTransaction", this);
            dCt.stateChangeRequest("delCheck", null);
        }
        else if (key.equals("checkInView")){
           System.out.println("checkInView");
            createAndShowCheckInBookView();

        }
        else if(key.equals("checkInBook")){
            Properties p = (Properties)value;
            String barcode = p.getProperty("barcode");
            myBooks = new BookCollection();
            myBooks.findBookWithBarcodeLike(barcode);
            createAndShowBookCollectionView();

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
//        catch (PasswordMismatchException ex)
//        {
//            loginErrorMessage = "ERROR: " + ex.getMessage();
//            return false;
//        }

    }

    private void getWorker(String id)throws InvalidPrimaryKeyException {
        selectedWorker = new Worker(id);
    }
    private void getBook(String id)throws InvalidPrimaryKeyException {
        selectedBook = new Book(id);
    }
    private void getStudent(String id)throws InvalidPrimaryKeyException {
        selectedStudent = new StudentBorrower(id);
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
    private void createAndShowSearchBookView(){
        Scene currentScene = (Scene)myViews.get("SearchBookView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchBookView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("SearchBookView", currentScene);
        }

        swapToView(currentScene);

    }
    private void createAndShowSearchStudentView(){
        Scene currentScene = (Scene)myViews.get("SearchStudentView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchStudentView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("SearchStudentView", currentScene);
        }

        swapToView(currentScene);

    }
    private void createAndShowBookCollectionView(){
        Scene currentScene = (Scene)myViews.get("BookCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("BookCollectionView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("BookCollectionView", currentScene);
        }

        swapToView(currentScene);

    }
    private void createAndShowStudentCollectionView(){
        Scene currentScene = (Scene)myViews.get("StudentCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("StudentCollectionView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("StudentCollectionView", currentScene);
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
    private void createAndShowModifyWorkerView()
    {
        Scene currentScene = (Scene)myViews.get("ModifyWorkerView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ModifyWorkerView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("ModifyWorkerView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }
    private void createAndShowModifyBookView()
    {
        Scene currentScene = (Scene)myViews.get("ModifyBookView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ModifyBookView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("ModifyBookView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }
    private void createAndShowModifyStudentBorrowerView()
    {
        Scene currentScene = (Scene)myViews.get("ModifyStudentView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ModifyStudentView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("ModifyStudentView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }
    private void createAndShowDeleteWorkerView()
    {
        Scene currentScene = (Scene)myViews.get("DeleteWorkerView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteWorkerView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("DeleteWorkerView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }
    private void createAndShowDeleteBookView()
    {
        Scene currentScene = (Scene)myViews.get("DeleteBookView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteBookView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("DeleteBookView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }
    private void createAndShowCheckInBookView(){
        Scene currentScene = (Scene)myViews.get("CheckInBookView");
        System.out.println("EWJDCVHBJKCSDABHJCASBJKHC");
        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("CheckInBookView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("CheckInBookView", currentScene);
        }
        swapToView(currentScene);
    }

    private void createAndShowDeleteStudentBorrowerView()
    {
        Scene currentScene = (Scene)myViews.get("DeleteStudentView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteStudentView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("DeleteStudentView", currentScene);
        }

        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowCheckOutBookView()
    {
        Scene currentScene = (Scene)myViews.get("CheckOutBookView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("CheckOutBookView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("CheckOutBookView", currentScene);
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
