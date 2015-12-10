package cs490.purdueparkingassistant;

import java.util.Date;

/**
 * Created by Craig on 10/3/2015.
 */
public class Ticket {

    private String id;
    private String plateNumber;
    private String plateState;
    private String time;
    private String date;
    private String reason;
    private String towAddress;
    private double amount;

    public Ticket(String id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public Ticket(String id, String plateNumber, String plateState, String time, String date, String reason, String towAddress) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.plateState = plateState;
        this.time = time;
        this.date = date;
        this.reason = reason;
        this.towAddress = towAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateState() {
        return plateState;
    }

    public void setPlateState(String plateState) {
        this.plateState = plateState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTowAddress() {
        return towAddress;
    }

    public void setTowAddress(String towAddress) {
        this.towAddress = towAddress;
    }
}
