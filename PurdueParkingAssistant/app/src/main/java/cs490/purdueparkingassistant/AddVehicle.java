package cs490.purdueparkingassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddVehicle extends AppCompatActivity {

    EditText licenseField, licenseStateField, carMakeField, carModelField, carColorField, carYearField;
    ArrayList<EditText> fields;

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
                Global.localUser.addCar(car);
                finish();
            } else {
                Toast.makeText(v.getContext(), "Fill out all fields", Toast.LENGTH_LONG).show();
            }

        }

    }
}
