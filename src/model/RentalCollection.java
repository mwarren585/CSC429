package model;

import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Vector;


// system imports
        import java.util.Properties;
        import java.util.Vector;
        import javafx.scene.Scene;

// project imports
        import exception.InvalidPrimaryKeyException;
        import event.Event;
        import database.*;

        import impresario.IView;

        import userinterface.View;
        import userinterface.ViewFactory;
public class RentalCollection   extends EntityBase implements IView
{
    private static final String myTableName = "Rental";

    private Vector<Rental> rentals;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------

    public RentalCollection(){
        super(myTableName);
        rentals = new Vector<Rental>();
    }

    public RentalCollection( Rental rental) throws
            Exception
    {
        super(myTableName);

        if (rental == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Missing rental information", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: RentalCollection.<init>: rental information is null");
        }

        String id = (String)rental.getState("id");

        if (id == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Data corrupted: Rental has no id in database", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: RentalCollection.<init>: Data corrupted: Rental has no id in repository");
        }

        String query = "SELECT * FROM " + myTableName + " WHERE (rentalId = " + id + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            rentals = new Vector<Rental>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextRentalData = (Properties)allDataRetrieved.elementAt(cnt);

                Rental rental1 = new Rental(nextRentalData);

                if (rental1 != null)
                {
                    addRental(rental1);
                }
            }

        }
        else
        {
            throw new InvalidPrimaryKeyException("No id for rental : "
                    + id + ". BorrowerID : " + rental.getState("borrowerId"));
        }

    }

    public void getDelinquencyCheck() {

        String query = "SELECT * FROM " + myTableName + " WHERE dueDate < " + "CURRENT_DATE()" + " AND checkinDate IS " + "NULL";
        System.out.println(query);

        try {
            queryer(query);
        } catch (Exception x) {
            System.out.println("Error: " + x);
        }
    }

    public void getCheckedOutRentals () {

        String query = "SELECT * FROM " + myTableName + " WHERE checkinDate IS " + "NULL";
        System.out.println(query);

        try {
            queryer(query);
        } catch (Exception x) {
            System.out.println("Error: " + x);
        }
    }

    public void queryer(String d) throws InvalidPrimaryKeyException {
        Vector allDataRetrieved = getSelectQueryResult(d);
        System.out.println(allDataRetrieved.size() + " is the length of the rental vector");
        if (allDataRetrieved.isEmpty() == false)
        {
            System.out.println("query is getting results");
            rentals = new Vector<Rental>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextPatronData = (Properties)allDataRetrieved.elementAt(cnt);
                System.out.println(nextPatronData);

                Rental rental = new Rental(nextPatronData);

                if (rental != null)
                {
                    addRental(rental);
                }
            }

        }
        else
        {
            System.out.println("It is not getting results");
            throw new InvalidPrimaryKeyException("No rentals that match criteria");
        }
    }


    //----------------------------------------------------------------------------------
    private void addRental(Rental a)
    {
        //accounts.add(a);
        int index = findIndexToAdd(a);
        rentals.insertElementAt(a,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Rental a)
    {
        //users.add(u);
        int low=0;
        int high = rentals.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Rental midSession = rentals.elementAt(middle);

            int result = Rental.compare(a,midSession);

            if (result ==0)
            {
                return middle;
            }
            else if (result<0)
            {
                high=middle-1;
            }
            else
            {
                low=middle+1;
            }


        }
        return low;
    }


    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("Rentals")) {
            return rentals;
        }
        else
        if (key.equals("RentalList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Rental retrieve(String accountNumber)
    {
        Rental retValue = null;
        for (int cnt = 0; cnt < rentals.size(); cnt++)
        {
            Rental nextRental = rentals.elementAt(cnt);
            String nextRentalId = (String)nextRental.getState("bannerID");
            if (nextRentalId.equals(accountNumber) == true)
            {
                retValue = nextRental;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------
    protected void createAndShowView()
    {

        Scene localScene = myViews.get("WorkerSelectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("WorkerSelectionView", this);
            localScene = new Scene(newView);
            myViews.put("WorkerSelectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);

    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}