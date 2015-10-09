package cs490.purdueparkingassistant;

import java.util.Date;

/**
 * Created by Craig on 10/3/2015.
 */
public class Ticket {

    private String id;
    private Date dateReceived;
    private double amount;

    public Ticket(String id, Date dateReceived, double amount) {
        this.id = id;
        this.dateReceived = dateReceived;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
