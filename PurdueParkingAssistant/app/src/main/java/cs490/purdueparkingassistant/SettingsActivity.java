package cs490.purdueparkingassistant;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import cs490.purdueparkingassistant.APIClasses.ParkingRestClientUsage;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    AccountInfoValidator validator;
    EditText emailField, usernameField, passwordField, phoneNumberField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Account Settings");
        validator = new AccountInfoValidator();

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        emailField = (EditText) findViewById(R.id.emailField);
        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        CheckBox passwordCheckBox = (CheckBox) findViewById(R.id.passwordCheckBox);
        passwordCheckBox.setOnCheckedChangeListener(new PasswordCheckChangedListener(passwordField));
        phoneNumberField = (EditText) findViewById(R.id.phoneNumberField);
        phoneNumberField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        if (Global.localUser.getPhoneNumber() == null || Global.localUser.getPhoneNumber() == "") {
            phoneNumberField.setText("0000000000");
        } else {
            phoneNumberField.setText(Global.localUser.getPhoneNumber());
        }
        emailField.setText(Global.localUser.getEmail());
        usernameField.setText(Global.localUser.getUsername());
        passwordField.setText(Global.localUser.getPassword());

    }

    public void onClick(View v) {
        if (!validator.validateEmail(emailField.getText().toString()) || !validator.validatePassword(passwordField.getText().toString())
                || !validator.validatePhoneNumber(phoneNumberField.getText().toString()) || !validator.validateUsername(usernameField.getText().toString())) {
            Toast.makeText(v.getContext(), "Invalid Information", Toast.LENGTH_LONG).show();
        } else {
            User u = Global.localUser;
            u.setEmail(emailField.getText().toString());
            u.setPassword(passwordField.getText().toString());
            u.setPhoneNumber(phoneNumberField.getText().toString());
            u.setUsername(usernameField.getText().toString());
            Toast.makeText(v.getContext(), "Information Saved", Toast.LENGTH_LONG).show();
            ParkingRestClientUsage client = new ParkingRestClientUsage(v.getContext());
            try {
                client.editAccount();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private class PasswordCheckChangedListener implements CompoundButton.OnCheckedChangeListener {

        EditText passwordField;

        public PasswordCheckChangedListener(EditText passwordField) {
            this.passwordField = passwordField;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                passwordField.setTransformationMethod(null);
            } else {
                passwordField.setTransformationMethod(new PasswordTransformationMethod());
            }
        }
    }

}
