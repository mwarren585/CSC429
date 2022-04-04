package userinterface;

import javafx.beans.property.SimpleStringProperty;

import java.util.Vector;

public class BookTableModel  {
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty publisher;
    private final SimpleStringProperty puYear;
    private final SimpleStringProperty isbn;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty status;


    public BookTableModel(Vector<String> accountData) {
        this.barcode = new SimpleStringProperty(accountData.get(0));
        this.title = new SimpleStringProperty(accountData.get(1));
        this.author = new SimpleStringProperty(accountData.get(2));
        this.publisher = new SimpleStringProperty(accountData.get(3));
        this.puYear = new SimpleStringProperty(accountData.get(4));
        this.isbn = new SimpleStringProperty(accountData.get(5));
        this.notes = new SimpleStringProperty(accountData.get(6));
        this.status = new SimpleStringProperty(accountData.get(7));
    }




    //----------------------------------------------------------------------------
    // Getters
    //----------------------------------------------------------------------------
    public String getBarcode() {
        return barcode.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getPublisher() {
        return publisher.get();
    }

    public String getPuYear() {
        return puYear.get();
    }

    public String getIsbn() {
        return isbn.get();
    }

    public String getNotes() {
        return notes.get();
    }

    public String getStatus() {
        return status.get();
    }

    //----------------------------------------------------------------------------
    // Setters
    //----------------------------------------------------------------------------
    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public void setPuYear(String puYear) {
        this.puYear.set(puYear);
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}