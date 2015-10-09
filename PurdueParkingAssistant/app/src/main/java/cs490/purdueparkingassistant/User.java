package cs490.purdueparkingassistant;

import java.util.ArrayList;

/**
 * Created by Craig on 10/2/2015.
 */
public class User {


    //Basic User Information
    private String email;
    private String phoneNumber;
    private String username;
    private String password;

    //App Preferences
    public Boolean receiveTicketPushNotifications = true;
    public Boolean receiveEmailNotifications = true;

    //Vehicles
    private ArrayList<Car> cars;

    //Generic User for debugging/testing purposes
    public User() {
        email = "email@website.com";
        phoneNumber = "0000000000";
        username = "anonymous";
        password = "password";
        cars = new ArrayList<Car>();
    }

    public User(String email, String phoneNumber, String username, String password) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
    }

    public ArrayList<Car> getCars() { return cars;}

    public void setCars(ArrayList<Car> cars) { this.cars = cars; }

    public void addCar(Car c) { this.cars.add(c); }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
