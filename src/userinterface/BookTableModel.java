package userinterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

import java.util.Vector;

public class BookTableModel  {

    private final SimpleStringProperty barcode;
    private final SimpleStringProperty bookTitle;
    private final SimpleStringProperty author;
    private final SimpleStringProperty author4;
    private final SimpleStringProperty author2;
    private final SimpleStringProperty author3;
    private final SimpleStringProperty publisher;
    private final SimpleStringProperty pubYear;
    private final SimpleStringProperty ISBN;
    private final SimpleStringProperty price;
    private final SimpleStringProperty notes;


    public BookTableModel(Vector<String> accountData) {

        barcode = new SimpleStringProperty(accountData.elementAt(0));
        bookTitle =  new SimpleStringProperty(accountData.elementAt(1));
        author =  new SimpleStringProperty(accountData.elementAt(2));
        author2 =  new SimpleStringProperty(accountData.elementAt(3));
        author3 =  new SimpleStringProperty(accountData.elementAt(4));
        author4 =  new SimpleStringProperty(accountData.elementAt(5));
        publisher =  new SimpleStringProperty(accountData.elementAt(6));
        pubYear = new SimpleStringProperty(accountData.elementAt(7));
        ISBN = new SimpleStringProperty(accountData.elementAt(8));
        price = new SimpleStringProperty(accountData.elementAt(9));
        notes = new SimpleStringProperty(accountData.elementAt(10));


    }

    //----------------------------------------------------------------------------
    public String getBarcode() {
        return barcode.get();
    }

    //----------------------------------------------------------------------------
    public void setBarcode(String number) { barcode.set(number); }

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

    public String getAuthor2() {
        return author2.get();
    }

    //----------------------------------------------------------------------------

    public void setAuthor2(String name) { author2.set(name); }
    //----------------------------------------------------------------------------

    public String getAuthor3() {
        return author3.get();
    }

    //----------------------------------------------------------------------------

    public void setAuthor3(String name) { author3.set(name); }
    //----------------------------------------------------------------------------

    public String getAuthor4() {
        return author4.get();
    }

    //----------------------------------------------------------------------------

    public void setAuthor4(String name) { author4.set(name); }

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