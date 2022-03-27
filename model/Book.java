package model;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Book extends EntityBase {

    private boolean oldFlag = true;
    private static final String myTableName = "book";
    protected Properties dependencies;
    private String updateStatusMessage = "";

    public Book(String bookID) throws exception.InvalidPrimaryKeyException {
        super(myTableName);

        setDependencies();

        String query = "SELECT * FROM " + myTableName + " WHERE (barcode = " + bookID + ")";
        Vector allDataRetrieved = getSelectQueryResult(query);
        // You must get one Book at least

        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();
            // There should be EXACTLY one Book. More than that is an error
            if (size != 1) {
                throw new exception.InvalidPrimaryKeyException("Multiple Objects matching id : "
                        + bookID + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedAccountData = (Properties) allDataRetrieved.elementAt(0);
                persistentState = new Properties();
                Enumeration allKeys = retrievedAccountData.propertyNames();
                while (allKeys.hasMoreElements() == true) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedAccountData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
                oldFlag = true;
            }
        }
        // If no book found for this id, throw an src.exception
        else {
            throw new exception.InvalidPrimaryKeyException("No book matching id : "
                    + bookID + " found.");
        }
    }

    public Book(Properties props) {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);
            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
        oldFlag = false;
    }

    public Book() {

        super(myTableName);

        setDependencies();
        persistentState = new Properties();

        oldFlag = false;
    }

    public void save() {
        updateStateInDatabase();
    }

    private void updateStateInDatabase() // should be private? Should this be invoked directly or via the 'sCR(...)' method always?
    {
        try {
            if (oldFlag == true) {
                System.out.println("This is duplicated book");
                Properties whereClause = new Properties();
                whereClause.setProperty("barcode",
                        persistentState.getProperty("barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Book data for bookID number : " + persistentState.getProperty("barcode") + " updated successfully in src.database!";
            } else {
                //System.out.println("This should be a new book");
                Integer bookID = insertPersistentState(mySchema, persistentState);
                persistentState.setProperty("barcode", "" + bookID.intValue());
                updateStatusMessage = "Book data for new book : " + persistentState.getProperty("barcode")
                        + " installed successfully in src.database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing Book data in database!";
            System.out.println(ex);
            ex.printStackTrace();
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }
    //Todo: dependenices are yes
    private void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("Update", "UpdateStatusMessage");
        dependencies.setProperty("ServiceCharge", "UpdateStatusMessage");

        //myRegistry.setDependencies(dependencies);
    }

    @Override
    public String toString() {
        return "Title: " + persistentState.getProperty("title") + "; Author: " +
                persistentState.getProperty("author") + "; Year: " +
                persistentState.getProperty("pubYear");
    }

    public void display() {
        System.out.println(this);
    }
    /**
     * TODO:Work on getting the compare functions into proper use and intergration.
    **/
    public static int compare(Book a, Book b) {
        String aNum = (String) a.getState("barcode");
        String bNum = (String) b.getState("barcode");

        return aNum.compareTo(bNum);
    }

    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage") == true) {
            return updateStatusMessage;
        }
        return persistentState.getProperty(key);
    }

    public void stateChangeRequest(String key, Object value) {
        if (value != null) {
            persistentState.setProperty(key, (String) value);
        }
    }

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
//Todo:Figure out where this would be useful
    public void processNewBook(Properties p) {

        /*persistentState.setProperty("bookTitle", );
        persistentState.setProperty("author", );
        persistentState.setProperty("pubYear", );
        persistentState.setProperty("status", );
        this.save();
        */
    }

    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("title"));
        v.addElement(persistentState.getProperty("author"));
        v.addElement(persistentState.getProperty("publisher"));
        v.addElement(persistentState.getProperty("pubYear"));
        v.addElement(persistentState.getProperty("ISBN"));
        v.addElement(persistentState.getProperty("price"));
        v.addElement(persistentState.getProperty("notes"));

        return v;
    }
}