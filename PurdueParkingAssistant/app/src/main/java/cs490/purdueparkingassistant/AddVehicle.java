package cs490.purdueparkingassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cs490.purdueparkingassistant.APIClasses.ParkingRestClient;
import cs490.purdueparkingassistant.APIClasses.ParkingRestClientUsage;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

public class AddVehicle extends AppCompatActivity {

    EditText licenseField, licenseStateField, carMakeField, carModelField, carColorField, carYearField;
    ArrayList<EditText> fields;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        Button save = (Button) findViewById(R.id.saveButton);
        licenseField = (EditText) findViewById(R.id.carLicense);
        licenseStateField = (EditText) findViewById(R.id.carState);
        carMakeField = (EditText) findViewById(R.id.carMake);
        carModelField = (EditText) findViewById(R.id.carModel);
        carColorField = (EditText) findViewById(R.id.carColor);
        carYearField = (EditText) findViewById(R.id.carYear);
        fields = new ArrayList<EditText>();
        fields.add(licenseField); fields.add(licenseStateField); fields.add(carMakeField);
        fields.add(carModelField); fields.add(carColorField); fields.add(carYearField);
        save.setOnClickListener(new SaveClickListener());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Exit Without Saving?");
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            adb.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_vehicle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SaveClickListener implements View.OnClickListener {

        public SaveClickListener() {
        }

        @Override
        public void onClick(View v) {
            boolean valid = true;
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).getText().toString().equals("")) {
                    valid = false;
                }
            }
            if (valid == true) {
                String l = licenseField.getText().toString();
                String ls = licenseStateField.getText().toString();
                String cm = carMakeField.getText().toString();
                String cmo = carModelField.getText().toString();
                String yr = carYearField.getText().toString();
                String co = carColorField.getText().toString();
                Car car = new Car(l, ls, cm, cmo, yr, co);
                //Global.localUser.addCar(car);
                //ParkingRestClientUsage client = new ParkingRestClientUsage(getApplicationContext());
                try {
                    postVehicle(car);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            } else {
                Toast.makeText(v.getContext(), "Fill out all fields", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void postVehicle(Car car) throws JSONException {
        /*
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Messages... Please wait...");
        dialog.show();
        */
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

        Global.localUser.addCar(carToAdd);
        ParkingRestClient.post(this, "addVehicle?", entity, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //dialog.dismiss();

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

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                //dialog.dismiss();
                Global.localUser.getCars().remove(carToAdd);
                System.out.println(errorResponse);
                Toast toast = Toast.makeText(getBaseContext(), "Error connecting to Server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }

        });
    }
}
