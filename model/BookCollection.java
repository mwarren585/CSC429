package model;

import java.util.Properties;
import java.util.Vector;

public class BookCollection extends EntityBase {
    private static final String myTableName = "book";

    private Vector<Book> books;

    public BookCollection(){
        super(myTableName);
        books = new Vector<Book>();
    }

    public void queryHelper(String query) {
        books = new Vector<Book>();

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {


            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextBookData = (Properties) allDataRetrieved.elementAt(cnt);

                Book bk = new Book(nextBookData);

                if (bk != null) {
                    addBook(bk);
                }
            }

        }
    }
    public void findBooksOlderThanDate (String year) {


        String query = "SELECT * FROM " + myTableName + " WHERE (pubYear < " + year + ")";
        System.out.println(query);
        queryHelper(query);

    }

    public void findBooksNewerThanDate (String year){

        String query = "SELECT * FROM " + myTableName + " WHERE (pubYear > " + year + ")";
        System.out.println(query);
        queryHelper(query);

    }

    public void  findBooksWithTitleLike(String title){

        String query = "SELECT * FROM " + myTableName + " WHERE (title LIKE '%"+ title +"%')";
        System.out.println(query);
        queryHelper(query);
        System.out.println("Book Collection size right after query: " + books.size());

    }

    public void findBooksWithAuthorLike(String author){

        String query = "SELECT * FROM " + myTableName + " WHERE (author LIKE '%" + author + "%')";
        System.out.println(query);
        queryHelper(query);

    }

    private void addBook (Book b)
    {
        //users.add(u);
        int index = findIndexToAdd(b);
        books.insertElementAt(b, index); // To build up a collection sorted on some key
    }

    public Object getState(String key) {
        if (key.equals("Books"))
            return books;
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
    }

    private int findIndexToAdd(Book a)
    {
        //users.add(u);
        int low=0;
        int high = books.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Book midSession = books.elementAt(middle);

            int result = Book.compare(a, midSession);

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

    public void display() {
        if (books != null) {
            for (int cnt = 0; cnt < books.size(); cnt++) {
                Book b = books.get(cnt);
                System.out.println("--------");
                System.out.println(b.toString());
            }
        }
    }
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }


}
