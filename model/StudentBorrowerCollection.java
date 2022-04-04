package model;

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
public class StudentBorrowerCollection   extends EntityBase implements IView
{
    private static final String myTableName = "studentBorrower";

    private Vector<StudentBorrower> studentBorrowers;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------

    public StudentBorrowerCollection(){
        super(myTableName);
        studentBorrowers = new Vector<StudentBorrower>();
    }

    public StudentBorrowerCollection( StudentBorrower student) throws
            Exception
    {
        super(myTableName);

        if (student == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Missing student information", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: StudentBorrowerCollection.<init>: worker information is null");
        }

        String studentId = (String)student.getState("bannerId");

        if (studentId == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Data corrupted: Student has no id in database", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: StudentBorrowerCollection.<init>: Data corrupted: Worker has no id in repository");
        }

        String query = "SELECT * FROM " + myTableName + " WHERE (bannerId = " + studentId + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            studentBorrowers = new Vector<StudentBorrower>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextStudentData = (Properties)allDataRetrieved.elementAt(cnt);

                StudentBorrower student1 = new StudentBorrower(nextStudentData);

                if (student1 != null)
                {
                    addStudent(student1);
                }
            }

        }
        else
        {
            throw new InvalidPrimaryKeyException("No bannerId for student : "
                    + studentId + ". Name : " + student.getState("firstName"));
        }

    }
    public void findStudentsWithNameLike(String firstName, String lastName) {
        String query = "SELECT * FROM "+ myTableName+ " WHERE (firstName LIKE '%" + firstName + "%') AND (lastName LIKE '%" + lastName + "%')";
        System.out.println(query);
        queryHelper(query);
        System.out.println("Num of students in coll after query: " + studentBorrowers.size());

    }


    public void queryer(String d) throws InvalidPrimaryKeyException {
        Vector allDataRetrieved = getSelectQueryResult(d);

        if (allDataRetrieved.isEmpty() == false)
        {
            System.out.println("query is getting results");
            studentBorrowers = new Vector<StudentBorrower>();
            System.out.println("Data Size:" + " " + allDataRetrieved.size());
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextPatronData = (Properties)allDataRetrieved.elementAt(cnt);
                System.out.println(nextPatronData);

                StudentBorrower student = new StudentBorrower(nextPatronData);

                if (student != null)
                {
                    addStudent(student);
                }
            }

        }
        else
        {
            System.out.println("It is not getting results");
            throw new InvalidPrimaryKeyException("No books that match criteria");
        }
    }


    //----------------------------------------------------------------------------------
    protected void addStudent(StudentBorrower a)
    {
        //accounts.add(a);
        int index = findIndexToAdd(a);
        studentBorrowers.insertElementAt(a,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(StudentBorrower a)
    {
        //users.add(u);
        int low=0;
        int high = studentBorrowers.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            StudentBorrower midSession = studentBorrowers.elementAt(middle);

            int result = StudentBorrower.compare(a,midSession);

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
        if (key.equals("StudentBorrowers")) {
            return studentBorrowers;
        }
        else
        if (key.equals("StudentBorrowerList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------


    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
    public void queryHelper(String query) {
        studentBorrowers = new Vector<StudentBorrower>();

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextStudentData = (Properties)allDataRetrieved.elementAt(cnt);


                StudentBorrower student = new StudentBorrower(nextStudentData);

                if (studentBorrowers != null) {
                    addStudent(student);
                }
            }


        }
    }
}