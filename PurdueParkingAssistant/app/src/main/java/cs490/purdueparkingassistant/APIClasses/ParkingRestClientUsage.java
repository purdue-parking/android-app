package cs490.purdueparkingassistant.APIClasses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cs490.purdueparkingassistant.Car;
import cs490.purdueparkingassistant.Global;
import cs490.purdueparkingassistant.MainPageFragment;
import cs490.purdueparkingassistant.PublicMessage;
import cs490.purdueparkingassistant.Ticket;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * Created by Craig on 10/27/2015.
 */
public class ParkingRestClientUsage {

    Context c;

    public ParkingRestClientUsage(Context c) {
        this.c = c;
    }

    public void getVehicles(String username) throws JSONException {
        System.out.println(username);
        ParkingRestClient.get("vehiclecollection/" + username, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);

                try {
                    extractCars(response);
                    //Global.localUser.setCars(cars);
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(ListViewUpdateReceiver.NEW_UPDATE_BROADCAST);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    c.sendBroadcast(broadcastIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Array " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }
        });
    }

    public void extractCars(JSONObject response) throws JSONException {

        JSONArray items = response.getJSONArray("items");
        ArrayList<Car> cars = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                //System.out.println(items.get(i).toString());
                JSONObject carJSON = items.getJSONObject(i);
                System.out.println(carJSON);
                int id = Integer.parseInt(carJSON.getString("carID"));
                String model = carJSON.getString("model");
                String color = carJSON.getString("color");
                String plateNumber = carJSON.getString("plateNumber");
                String year = carJSON.getString("year");
                String make = carJSON.getString("make");
                String plateState = carJSON.getString("plateState");
                Car tempCar = new Car(id, plateNumber, plateState, make, model, year, color);
                cars.add(tempCar);
            }
            Global.localUser.setCars(cars);
    }

    public void postVehicle(Car car) throws JSONException {

        final Car carToAdd = car;

        JSONObject params = new JSONObject();
        //params.put("alt", "json");
        params.put("username", Global.localUser.getUsername());
        params.put("plateNumber", car.getLicensePlateNumber());
        params.put("plateState", car.getLicensePlateState());
        params.put("make", car.getMake());
        params.put("model", car.getModel());
        params.put("year", car.getYear());
        params.put("color", car.getColor());
        StringEntity entity = new StringEntity(params.toString(), ContentType.APPLICATION_JSON);


        ParkingRestClient.post(c, "addVehicle?", entity, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Global.localUser.addCar(carToAdd);
                Log.d("POST VEHICLE", "Success with object response");
                Log.d("POST VEHICLE", response.toString());
                try {
                    JSONObject key = response.getJSONObject("key");
                    int id = Integer.parseInt(key.getString("id"));
                    carToAdd.setId(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Global.localUser.addCar(carToAdd);
                Log.d("POST VEHICLE", "Success with array response");
                Log.d("POST VEHICLE", response.toString());
            }

        });
    }

    public void deleteVehicle(int id) throws JSONException {
        ParkingRestClient.delete("vehicle/" + id, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Success Object Response");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Success Object Array");
            }
        });

    }

    public void getMessages(int page, MainPageFragment fragment, final boolean add) throws JSONException {
        System.out.println("Gettings Message Page " + page);
        final MainPageFragment mpf = fragment;
        ParkingRestClient.get("messagecollection/" + page, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
                try {
                    extractMessages(response, add);
                    mpf.messagesView.invalidateViews();
                    mpf.maa.notifyDataSetChanged();

                    /*
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(ListViewUpdateReceiver.NEW_UPDATE_BROADCAST);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    c.sendBroadcast(broadcastIntent);
                    */
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Array " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }
        });
    }

    public void getMessagesSorted(int page, boolean ascending, final boolean add) throws JSONException {
        ParkingRestClient.put("messagecollection/" + page + "/" + ascending, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
                try {
                    extractMessages(response, add);
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(ListViewUpdateReceiver.NEW_UPDATE_BROADCAST);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    c.sendBroadcast(broadcastIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void extractMessages(JSONObject response, boolean add) throws JSONException{
        ArrayList<PublicMessage> messages = new ArrayList<PublicMessage>();
        JSONArray items = response.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            System.out.println(items.getJSONObject(i));
            JSONObject mes = items.getJSONObject(i);
            int id = Integer.parseInt(mes.getString("messageID"));
            String username = mes.getString("username");
            boolean helpNeeded = mes.getBoolean("helpNeeded");
            int votes = Integer.parseInt(mes.getString("votes"));
            String content = mes.getString("message");
            PublicMessage message = new PublicMessage(id, content, username, helpNeeded, votes);
            if (add) {
                Global.messageBoardContent.add(message);
            }
            messages.add(message);
        }
        if (!add) {
            Global.messageBoardContent = messages;
        }
        System.out.println(Global.messageBoardContent.size());

    }

    public void getTickets() throws JSONException {
        ParkingRestClient.get("ticketcollection/" + Global.localUser.getUsername(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
                try {
                    extractTickets(response);
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(ListViewUpdateReceiver.NEW_UPDATE_BROADCAST);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    c.sendBroadcast(broadcastIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Array " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }
        });
    }

    public void extractTickets(JSONObject response) throws JSONException {
        JSONArray items = response.getJSONArray("items");
        ArrayList<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject ticket = items.getJSONObject(i);
            String ticketNumber = ticket.getString("ticketNumber");
            String plateNumber = ticket.getString("plateNumber");
            String plateState = ticket.getString("plateState");
            String time = ticket.getString("time");
            String date = ticket.getString("date");
            String reason = ticket.getString("reason");
            Ticket t = new Ticket(ticketNumber,plateNumber,plateState,time,date,reason, "");
            tickets.add(t);
        }
        Global.tickets = tickets;
    }

    public void upVote(int id) throws JSONException {
        System.out.println("UPVOTE :" + id);
        ParkingRestClient client = new ParkingRestClient();
        client.post(c,"upvote/"+id, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Array " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }
        });
    }

    public void downVote(int id) throws JSONException {
        System.out.println("DOWNVOTE:" + id);
        ParkingRestClient client = new ParkingRestClient();
        client.post(c,"downvote/"+id, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Array " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }
        });
    }

    public void editAccount() throws JSONException {

        JSONObject params = new JSONObject();
        //params.put("alt", "json");
        params.put("username", Global.localUser.getUsername());
        params.put("name", Global.localUser.getName());
        params.put("email", Global.localUser.getEmail());
        params.put("phoneNumber", Global.localUser.getPhoneNumber());
        StringEntity entity = new StringEntity(params.toString(), ContentType.APPLICATION_JSON);
        ParkingRestClient client = new ParkingRestClient();
        client.post(c, "editAccount", entity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }

        });
    }
}
