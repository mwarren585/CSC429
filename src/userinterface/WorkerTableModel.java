package userinterface;

import javafx.beans.property.SimpleStringProperty;

import java.util.Vector;

public class WorkerTableModel {
    private final SimpleStringProperty bannerID;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    private final SimpleStringProperty credentials;
    private final SimpleStringProperty dateOfLastCredentials;
    private final SimpleStringProperty dateOfHire;


    //----------------------------------------------------------------------------
    public WorkerTableModel(Vector<String> accountData)
    {
        bannerID =  new SimpleStringProperty(accountData.elementAt(0));
        firstName =  new SimpleStringProperty(accountData.elementAt(1));
        lastName =  new SimpleStringProperty(accountData.elementAt(2));
        phone =  new SimpleStringProperty(accountData.elementAt(3));
        email = new SimpleStringProperty(accountData.elementAt(4));
        credentials = new SimpleStringProperty(accountData.elementAt(5));
        dateOfLastCredentials = new SimpleStringProperty(accountData.elementAt(6));
        dateOfHire = new SimpleStringProperty(accountData.elementAt(7));


    }

    //----------------------------------------------------------------------------
    public String getWorkerId() {
        return bannerID.get();
    }

    //----------------------------------------------------------------------------
    public void setWorkerId(String number) { bannerID.set(number); }

    //----------------------------------------------------------------------------
    public String getfirstName() {
        return firstName.get();
    }

    //----------------------------------------------------------------------------
    public void setFirstName(String auth) { firstName.set(auth); }

    //----------------------------------------------------------------------------
    public String getLastName() {
        return lastName.get();
    }

    //----------------------------------------------------------------------------
    public void setLastName(String ti) {
        lastName.set(ti);
    }

    //----------------------------------------------------------------------------
    public String getPhone() { return phone.get(); }

    //----------------------------------------------------------------------------
    public void setPhone(String pubyear)
    {
        phone.set(pubyear);
    }
    //----------------------------------------------------------------------------
    public String getEmail() { return email.get(); }

    //----------------------------------------------------------------------------
    public void setEmail(String stat)
    {
        email.set(stat);
    }
    //----------------------------------------------------------------------------

    public String getCredentials() { return credentials.get(); }

    //----------------------------------------------------------------------------
    public void setCredentials(String stat)
    {
        credentials.set(stat);
    }

    //----------------------------------------------------------------------------
    public String getDateOfLastCredentials() { return dateOfLastCredentials.get(); }

    //----------------------------------------------------------------------------
    public void setDateOfLastCredentials(String stat)
    {
        dateOfLastCredentials.set(stat);
    }


    //----------------------------------------------------------------------------
    public String getDateOfHire() { return dateOfHire.get(); }

    //----------------------------------------------------------------------------
    public void setDateOfHire(String stat) { dateOfHire.set(stat); }


}
