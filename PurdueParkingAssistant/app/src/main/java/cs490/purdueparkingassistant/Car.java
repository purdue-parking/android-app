package cs490.purdueparkingassistant;

/**
 * Created by Craig on 10/7/2015.
 */
public class Car {

    private String licensePlateNumber;
    private String licensePlateState;
    private String make;
    private String model;
    private String year;
    private String color;
    private int id;



    public Car(String l, String ls, String m, String mo, String yr, String co) {
        licensePlateNumber = l;
        licensePlateState = ls;
        make = m;
        model = mo;
        year = yr;
        color = co;
    }

    public Car(int id, String l, String ls, String m, String mo, String yr, String co) {
        this.id = id;
        licensePlateNumber = l;
        licensePlateState = ls;
        make = m;
        model = mo;
        year = yr;
        color = co;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getLicensePlateState() {
        return licensePlateState;
    }

    public void setLicensePlateState(String licensePlateState) {
        this.licensePlateState = licensePlateState;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
