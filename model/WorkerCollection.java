package model;

import java.util.Properties;
import java.util.Vector;

public class WorkerCollection extends EntityBase{
    private static final String myTableName= "worker";
    private Vector<Worker> workers;

    public WorkerCollection()
    {
        super(myTableName);
        workers = new Vector<Worker>();
    }

    public void findPatronsOlderThan(String date) throws Exception{
        if(date == null){
            throw new Exception("UNEXPECTED ERROR: date is null");
        }

        String query = "SELECT * FROM "+ myTableName+ "WHERE (dateOfBirth > "+date+")";

        queryHelper(query);
    }
    public void findPatronsYoungerThan(String date)throws Exception{
        if(date == null){
            throw new Exception("UNEXPECTED ERROR: date is null");
        }

        String query = "SELECT * FROM "+ myTableName+ "WHERE (dateOfBirth < "+date+")";

        queryHelper(query);
    }
    public void findPatronsAtZipCode(String zip){
        /*if(zip == null){
            throw new Exception("UNEXPECTED ERROR: zip is null");
        }*/

        String query = "SELECT * FROM "+ myTableName+ " WHERE (zip = "+zip+")";
        System.out.println(query);
        queryHelper(query);
    }
    public void findWorkersWithNameLike(String firstName, String lastName)throws Exception{
        if((firstName == null)||(lastName == null)){
            throw new Exception("UNEXPECTED ERROR: name is null");
        }

        String query = "SELECT * FROM "+ myTableName+ "WHERE (firstName LIKE '%"+firstName+"%') AND (lastName LIKE '%"+lastName+"%')";

        queryHelper(query);
    }

    public void queryHelper(String query) {
        workers = new Vector<Worker>();

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextPatronData = (Properties) allDataRetrieved.elementAt(cnt);

                //Need to fix this
               /*Worker worker = new Worker(nextPatronData);

                if (workers != null) {
                    addWorker(worker);
                }*/
            }


        }
    }

    //----------------------------------------------------------------------------------
    private void addWorker(Worker a)
    {
        //users.add(u);
        int index = findIndexToAdd(a);
        workers.insertElementAt(a,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Worker a)
    {
        //users.add(u);
        int low=0;
        int high = workers.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Worker midSession = (Worker) workers.elementAt(middle);

            int result = Worker.compare(a,midSession);

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


    public Object getState(String key) {
      if(key.equals("Workers")) {
          return workers;
      }
      else{
          return null;
      }
    }
    
    public void stateChangeRequest(String key, Object value) {
    }

    public void display() {
        if (workers != null) {
            for (int cnt = 0; cnt < workers.size(); cnt++) {
                Worker p = workers.get(cnt);
                System.out.println("--------");
                System.out.println(p.toString());
            }
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
}
