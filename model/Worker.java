package model;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import impresario.IView;
import org.w3c.dom.ls.LSOutput;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


public class Worker extends EntityBase {
    private static final String myTableName = "worker";

    protected Properties dependencies;

    private String updateStatusMessage = "";


    //Constructor for if we know the id number which is a private key so there should only be one unique id of that number
    public Worker(String id) throws InvalidPrimaryKeyException
    {
        super(myTableName);
        String query = "SELECT * FROM " + myTableName + " WHERE (bannerID = " + id + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null){
            int size = allDataRetrieved.size();

            // There should be EXACTLY one patron. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple workers matching id : "
                        + id + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedAccountData = (Properties)allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedAccountData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedAccountData.getProperty(nextKey);

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no patron found for this user name, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No worker matching id : "
                    + id + " found.");
        }
    }

    //Constructor for it we do not know the specific id number to look up
    public Worker(Properties props)
            throws InvalidPrimaryKeyException, PasswordMismatchException {
        super(myTableName);

        String idToQuery = props.getProperty("ID");

        String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + idToQuery + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple accounts matching user id : "
                        + idToQuery + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedCustomerData = (Properties) allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedCustomerData.propertyNames();
                while (allKeys.hasMoreElements() == true) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedCustomerData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no account found for this user name, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No account matching user id : "
                    + idToQuery + " found.");
        }

        String password = props.getProperty("Password");

        String accountPassword = persistentState.getProperty("password");

        if (accountPassword != null)
        {
            boolean passwordCheck = accountPassword.equals(password);
            if (passwordCheck == false)
            {
                throw new PasswordMismatchException("Password mismatch");
            }

        }
        else
        {
            throw new PasswordMismatchException("Password missing for account");
        }
    }


    public void save(){
        updateStateInDatabase();
    }

    public void updateStateInDatabase(){
        try
        {
            if(persistentState.getProperty("bannerID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("bannerID",
                        persistentState.getProperty("bannerID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Worker data for Worker Id : " + persistentState.getProperty("bannerID") + " updated successfully in database!";
            }
            else
            {
                Integer patronId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("bannerID", "" + patronId.intValue());
                updateStatusMessage = "Worker data for new worker : " +  persistentState.getProperty("bannerID")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing worker data in database!";
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }
    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("Update", "UpdateStatusMessage");
        dependencies.setProperty("ServiceCharge", "UpdateStatusMessage");

        myRegistry.setDependencies(dependencies);
    }
    @Override
    public String toString() {
        return "Worker First Name: " + persistentState.getProperty("firstName") + "; Last Name: " +
                persistentState.getProperty("lastName")  + "; Phone#: " +
                persistentState.getProperty("phone") + "; Email: "+
                persistentState.getProperty("email") + "; Credentials: " +
                persistentState.getProperty("credentials") + "; dateOfHire: " +
                persistentState.getProperty("dateOfHire")
                ;
    }

    // compare
    public void display(){
        System.out.println(toString());
    }

    
    public static int compare(Worker a, Worker b)
    {
        String aNum = (String)a.getState("bannerID");
        String bNum = (String)b.getState("bannerID");

        return aNum.compareTo(bNum);
    }
    
    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage") == true)
        {
            return updateStatusMessage;
        }
        return persistentState.getProperty(key);
    }

    public void stateChangeRequest(String key, Object value) {
        if (value != null) {
            persistentState.setProperty(key, (String)value);
        }
    }
    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }

    //-----------------------------------------------------------------------------------
    public boolean matchPassword(String givenPassword) {

        String dbPassword = persistentState.getProperty("password");
        if (givenPassword.equals(dbPassword) == true)
            return true;
        else
            return false;
    }

    //------------------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("bannerID"));
        v.addElement(persistentState.getProperty("firstName"));
        v.addElement(persistentState.getProperty("lastName"));
        v.addElement(persistentState.getProperty("phone"));
        v.addElement(persistentState.getProperty("email"));
        v.addElement(persistentState.getProperty("credentials"));
        v.addElement(persistentState.getProperty("dateOfLastCredentials"));
        v.addElement(persistentState.getProperty("dateOfHire"));

        return v;
    }
}
