package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;


public class StudentBorrowerTableModel{

    private final SimpleStringProperty bannerID;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    private final SimpleStringProperty dateOfLatestBorrower;
    private final SimpleStringProperty dateOfRegistration;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty status;


    //----------------------------------------------------------------------------
    public StudentBorrowerTableModel(Vector<String> bookData)
    {
        bannerID =  new SimpleStringProperty(bookData.elementAt(0));
        firstName =  new SimpleStringProperty(bookData.elementAt(1));
        lastName =  new SimpleStringProperty(bookData.elementAt(2));
        phone =  new SimpleStringProperty(bookData.elementAt(3));
        email =  new SimpleStringProperty(bookData.elementAt(4));
        dateOfLatestBorrower =  new SimpleStringProperty(bookData.elementAt(5));
        dateOfRegistration =  new SimpleStringProperty(bookData.elementAt(6));
        notes =  new SimpleStringProperty(bookData.elementAt(7));
        status =  new SimpleStringProperty(bookData.elementAt(8));
    }

    //----------------------------------------------------------------------------
    public String getBannerID() {return bannerID.get(); }

    //----------------------------------------------------------------------------
    public void setBannerID(String number) { bannerID.set(number); }

    //----------------------------------------------------------------------------
    public String getFirstName() {
        return firstName.get();
    }

    //----------------------------------------------------------------------------
    public void setFirstName(String name) {
        firstName.set(name);
    }

    //----------------------------------------------------------------------------
    public String getLastName() {
        return lastName.get();
    }

    //----------------------------------------------------------------------------
    public void setLastName(String name)
    {
        lastName.set(name);
    }

    //----------------------------------------------------------------------------
    public String getPhone()
    {
        return phone.get();
    }

    //----------------------------------------------------------------------------
    public void setPhone(String phone)
    {
        this.phone.set(phone);
    }

    //----------------------------------------------------------------------------
    public String getEmail()
    {
        return email.get();
    }

    //----------------------------------------------------------------------------
    public void setEmail(String em)
    {
        email.set(em);
    }

    //----------------------------------------------------------------------------
    public String getDateOfLatestBorrowerStatus()
    {
        return dateOfLatestBorrower.get();
    }

    //----------------------------------------------------------------------------
    public void setDateOfLatestBorrowerStatus(String dob)
    {
        dateOfLatestBorrower.set(dob);
    }

    //----------------------------------------------------------------------------
    public String getDateOfRegistration() {
        return dateOfRegistration.get();
    }

    //----------------------------------------------------------------------------
    public void setDateOfRegistration(String hire)
    {
        dateOfRegistration.set(hire);
    }

    public String getNotes() {
        return notes.get();
    }

    //----------------------------------------------------------------------------
    public void setNotes(String stat)
    {
        notes.set(stat);
    }

    public String getStatus() {
        return status.get();
    }

    //----------------------------------------------------------------------------
    public void setStatus(String stat)
    {
        status.set(stat);
    }
}