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
    private boolean oldFlag = true;


    //Constructor for if we know the id number which is a private key so there should only be one unique id of that number
    public Worker(String id) throws InvalidPrimaryKeyException
    {
        super(myTableName);
        String query = "SELECT * FROM " + myTableName + " WHERE (bannerID = " + id + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null){
            int size = allDataRetrieved.size();

            // There should be EXACTLY one patron. More than that is an error
            if (size > 1)
            {
                throw new InvalidPrimaryKeyException("Multiple workers matching id : "
                        + id + " found.");
            }
            else if (size < 1)
            {
                throw new InvalidPrimaryKeyException("No worker matching id : "
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
                oldFlag = true;

            }
        }
        // If no patron found for this user name, throw an exception

    }

    //Constructor for it we do not know the specific id number to look up
    public Worker(Properties props){
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
        oldFlag = false;

    }

    public Worker(){
        super(myTableName);
        oldFlag = false;
    }

    public void save(){
        updateStateInDatabase();
    }

    public void updateStateInDatabase(){
        try
        {
            if(oldFlag == true)
            {

                System.out.println("getting here");
                Properties whereClause = new Properties();
                whereClause.setProperty("bannerID",
                        persistentState.getProperty("bannerID"));
                System.out.print(persistentState.getProperty("bannerID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Worker data for Worker Id : " + persistentState.getProperty("bannerID") + " updated successfully in database!";
            }
            else
            {
                Integer workerID = insertPersistentState(mySchema, persistentState);
                persistentState.setProperty("bannerID", "" + workerID.intValue());
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
        return "bannerID" + persistentState.getProperty("bannerID") + "Worker First Name: " + persistentState.getProperty("firstName") + "; Last Name: " +
                persistentState.getProperty("lastName")  + "; Phone#: " +
                persistentState.getProperty("phone") + "; Email: "+
                persistentState.getProperty("email") + "; Credentials: " +
                persistentState.getProperty("credentials") + "; dateOfHire: " +
                persistentState.getProperty("dateOfHire") + "; status: " +
                persistentState.getProperty("status")
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
    public boolean matchPassword(String givenPassword) throws PasswordMismatchException {

        String dbPassword = persistentState.getProperty("password");
        if (givenPassword.equals(dbPassword) == true) {
            return true;
        }
        else{
            throw new PasswordMismatchException("Wrong Password! Try Again!");
        }



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
        v.addElement(persistentState.getProperty("dateOfLatestCredentials"));
        v.addElement(persistentState.getProperty("dateOfHire"));
        v.addElement(persistentState.getProperty("status"));

        return v;
    }
    public void setOldFlagTrue(){
        oldFlag = true;
    }
}
