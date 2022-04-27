
package model;
import exception.InvalidPrimaryKeyException;
import java.util.Properties;
import java.sql.SQLException;
import java.util.*;


public class Rental extends EntityBase {

    private static final String myTableName = "Rental";
    protected Properties dependencies;
    private String updateStatusMessage = "";
    private boolean exists = true;

    public Rental(String barcode) throws InvalidPrimaryKeyException {
        super(myTableName);
        this.setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (bookID = " + barcode + ") AND checkInDate IS NULL";
        Vector allDataFromDB = this.getSelectQueryResult(query);
            System.out.println("This didnt fail yet 1");
        if (allDataFromDB != null) {
                System.out.println("This didn't fail yet 2");
            int dataLen = allDataFromDB.size();
            if (dataLen > 1) {
                System.out.println("This failed : " + dataLen);
                throw new InvalidPrimaryKeyException("Multiple rentals matching id : " + barcode + " found.");
            }
            else if (dataLen < 1)
                {
                    throw new InvalidPrimaryKeyException("No book matching id : "
                            + barcode + " found.");
                }
            else {
                Properties bookData = (Properties) allDataFromDB.elementAt(0);
                this.persistentState = new Properties();
                Enumeration bookKeys = bookData.propertyNames();

                while (bookKeys.hasMoreElements()) {
                    String nextKey = (String) bookKeys.nextElement();
                    String nextValue = bookData.getProperty(nextKey);
                    if (nextValue != null) {
                        this.persistentState.setProperty(nextKey, nextValue);
                    }
                }

                exists = true;

            }
        }
        else {
            //System.out.println("This failed here 3");
            throw new InvalidPrimaryKeyException("No Books matching: " + barcode + " found.");
        }

    }

    public Rental(Properties props) {
        super(myTableName);
        this.setDependencies();
        this.persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();

        while(allKeys.hasMoreElements()) {
            String one = (String)allKeys.nextElement();
            String two = props.getProperty(one);
            if (two != null) {
                this.persistentState.setProperty(one, two);
            }
        }
        exists = false;
    }

    public Rental() {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        exists = false;
    }

    public void setExistsTrue()
    {
        exists = true;
    }

    private void setDependencies(){
        this.dependencies = new Properties();
        this.myRegistry.setDependencies(this.dependencies);
    }

    @Override
    public Object getState(String key) {
        return persistentState.getProperty(key);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
            persistentState.setProperty(key, (String)value);
    }

    public String toString()
    {
        return "Book: barcode: " + getState("barcode") + " Title: " + getState("title");
    }

    public static int compare(Rental a, Rental b) {
        String ba = (String)a.getState("ID");
        String bb = (String)b.getState("ID");
        return ba.compareTo(bb);
    }


    //-----------------------------------------------------------------------------------
    public void update()
    {
        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {

        try
        {
            if (exists == true)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Rental data for book barcode number : " + persistentState.getProperty("bookID") + " updated successfully in database!";
            }
            else
            {
                int idRet = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", idRet + "");
                updateStatusMessage = "Rental data for new Book : " +  persistentState.getProperty("bookID")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            System.out.println("Error: " + ex.toString());
            updateStatusMessage = "Error in installing Rental data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }

    @Override
    //------------------------------------------------------------------------------------
    protected void initializeSchema(String tableName) {

        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }

    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();
/*
        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("title"));
        //v.addElement(persistentState.getProperty("discipline"));
        v.addElement(persistentState.getProperty("author"));
        v.addElement(persistentState.getProperty("author2"));
        v.addElement(persistentState.getProperty("author3"));
        v.addElement(persistentState.getProperty("status"));
        v.addElement(persistentState.getProperty("author4"));
        v.addElement(persistentState.getProperty("publisher"));
        v.addElement(persistentState.getProperty("pubYear"));
        v.addElement(persistentState.getProperty("ISBN"));
        //v.addElement(persistentState.getProperty("condition"));
        v.addElement(persistentState.getProperty("price"));
        v.addElement(persistentState.getProperty("notes"));
*/

        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("borrowerID"));
        v.addElement(persistentState.getProperty("bookID"));
        v.addElement(persistentState.getProperty("checkOutDate"));
        v.addElement(persistentState.getProperty("checkOutWorkerID"));
        v.addElement(persistentState.getProperty("dueDate"));
        v.addElement(persistentState.getProperty("checkInDate"));
        v.addElement(persistentState.getProperty("checkInWorkerID"));
        return v;
    }

}