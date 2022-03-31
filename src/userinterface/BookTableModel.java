package userinterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

import java.util.Vector;

public class BookTableModel  {
    private final SimpleStringProperty bookID;
    private final SimpleStringProperty bookTitle;
    private final SimpleStringProperty author;
    private final SimpleStringProperty publisher;
    private final SimpleStringProperty pubYear;
    private final SimpleStringProperty ISBN;
    private final SimpleStringProperty price;
    private final SimpleStringProperty notes;


    public BookTableModel(Vector<String> accountData) {

    bookID =  new SimpleStringProperty(accountData.elementAt(0));
    bookTitle =  new SimpleStringProperty(accountData.elementAt(1));
    author =  new SimpleStringProperty(accountData.elementAt(2));
    publisher =  new SimpleStringProperty(accountData.elementAt(3));
    pubYear = new SimpleStringProperty(accountData.elementAt(4));
    ISBN = new SimpleStringProperty(accountData.elementAt(5));
    price = new SimpleStringProperty(accountData.elementAt(6));
    notes = new SimpleStringProperty(accountData.elementAt(7));


    }

    //----------------------------------------------------------------------------
    public String getBookId() {
        return bookID.get();
    }

    //----------------------------------------------------------------------------
    public void setBookId(String number) { bookID.set(number); }

    //----------------------------------------------------------------------------
    public String getBookTitle() {
        return bookTitle.get();
    }

    //----------------------------------------------------------------------------
    public void setBookTitle(String name) { bookTitle.set(name); }

    //----------------------------------------------------------------------------
    public String getAuthor() {
        return author.get();
    }

    //----------------------------------------------------------------------------

    public void setAuthor(String name) { author.set(name); }

    //----------------------------------------------------------------------------
    public String getPublisher() {
        return publisher.get();
    }

    //----------------------------------------------------------------------------

    public void setPublisher(String name) { publisher.set(name); }

    //----------------------------------------------------------------------------
    public String getPubYear() {
        return pubYear.get();
    }

    //----------------------------------------------------------------------------

    public void setPubYear(String name) { pubYear.set(name); }

    //----------------------------------------------------------------------------
    public String getISBN() {
        return ISBN.get();
    }

    //----------------------------------------------------------------------------

    public void setISBN(String name) { ISBN.set(name); }

    //----------------------------------------------------------------------------
    public String getPrice() {
        return price.get();
    }

    //----------------------------------------------------------------------------

    public void setPrice(String name) { price.set(name); }

    //----------------------------------------------------------------------------
    public String getNotes() {
        return notes.get();
    }

    //----------------------------------------------------------------------------

    public void setNotes(String name) { notes.set(name); }

    //----------------------------------------------------------------------------

}
