package model;

import exception.InvalidPrimaryKeyException;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class StudentBorrower extends EntityBase {
    private static final String myTableName = "studentBorrower";
    protected Properties dependencies;
    private String updateStatusMessage = "Update Successful";
    private boolean exists = true;

    public StudentBorrower(String bannerId) throws InvalidPrimaryKeyException {
        super(myTableName);
        this.setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (bannerID = " + bannerId + ")";
        Vector allDataFromDB = this.getSelectQueryResult(query);
        if (allDataFromDB != null) {
            int dataLen = allDataFromDB.size();
            if (dataLen != 1) {
                throw new InvalidPrimaryKeyException("Multiple StudentBorrowerIds matching id : " + bannerId + " found.");
            } else {
                Properties borrowerIdData = (Properties)allDataFromDB.elementAt(0);
                this.persistentState = new Properties();
                Enumeration borrowerKeys = borrowerIdData.propertyNames();

                while(borrowerKeys.hasMoreElements()) {
                    String nextKey = (String)borrowerKeys.nextElement();
                    String nextValue = borrowerIdData.getProperty(nextKey);
                    if (nextValue != null) {
                        this.persistentState.setProperty(nextKey, nextValue);
                    }
                }
                exists = true;
            }
        }
    }

    public void setExistsTrue()
    {
        exists = true;
    }

    public StudentBorrower(Properties props) {
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
        return "StudentBorrower: ID: " + getState("bannerID") + " name: " + getState("firstName") + " email: " + getState("email");
    }

    public static int compare(StudentBorrower a, StudentBorrower b) {
        String ba = (String)a.getState("firstName");
        String bb = (String)b.getState("firstName");
        return ba.compareTo(bb);
    }

    public static int compareBanner(StudentBorrower a, StudentBorrower b) {
        String banid = (String)a.getState("bannerID");
        String banid2 = (String)b.getState("bannerID");
        return banid.compareTo(banid2);
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
                whereClause.setProperty("bannerID", persistentState.getProperty("bannerID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "BannerID data for borrower number : " + persistentState.getProperty("bannerID") + " updated successfully in database!";
            }
            else
            {
                Integer studentId = insertPersistentState(mySchema, persistentState);
                persistentState.setProperty("bannerID", "" + studentId.intValue());
                updateStatusMessage = "StudentBorrower data for new StudentBorrower : " +  persistentState.getProperty("bannerID")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            System.out.println("Error: " + ex.toString());
            updateStatusMessage = "Error adding Student Borrower data in database!";
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

        v.addElement(persistentState.getProperty("bannerID"));
        v.addElement(persistentState.getProperty("firstName"));
        v.addElement(persistentState.getProperty("lastName"));
        v.addElement(persistentState.getProperty("phone"));
        v.addElement(persistentState.getProperty("email"));
        v.addElement(persistentState.getProperty("dateOfLatestBorrower"));
        v.addElement(persistentState.getProperty("dateOfRegistration"));
        v.addElement(persistentState.getProperty("notes"));
        v.addElement(persistentState.getProperty("status"));

        return v;
    }

}