package cs490.purdueparkingassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs490.purdueparkingassistant.APIClasses.ParkingRestClient;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CreateAccountActivity extends AppCompatActivity {

    EditText usernameField, fullNameField, passwordField, emailField, phoneField;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        usernameField = (EditText) findViewById(R.id.createUsername);
        fullNameField = (EditText) findViewById(R.id.createName);
        passwordField = (EditText) findViewById(R.id.createPassword);
        emailField = (EditText) findViewById(R.id.createEmail);
        phoneField = (EditText) findViewById(R.id.createPhoneNumber);
        submit = (Button) findViewById(R.id.createRegister);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basicValidation()) {
                    //submit account to server
                    try {
                        addAccount();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void addAccount() throws JSONException {

        JSONObject params = new JSONObject();
        params.put("username", usernameField.getText().toString());
        params.put("name", fullNameField.getText().toString());
        params.put("email", emailField.getText().toString());
        params.put("phoneNumber", phoneField.getText().toString());
        params.put("acctType", "CITIZEN");
        params.put("ticketEmail", true);
        params.put("helpEmail", true);
        params.put("responseEmail", true);

        StringEntity entity = new StringEntity(params.toString(), ContentType.APPLICATION_JSON);


        ParkingRestClient.post(this, "addAccount?", entity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String u = usernameField.getText().toString();
                String n = fullNameField.getText().toString();
                String e = emailField.getText().toString();
                String p = phoneField.getText().toString();
                String pass = passwordField.getText().toString();
                Global.localUser = new User(n,e,p,u,pass);
                Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(i);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Post Error", errorResponse.toString());
                Toast.makeText(getBaseContext(), "Error Connecting to Server", Toast.LENGTH_LONG).show();
            }
        });
        }

    public boolean basicValidation() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String fullName = fullNameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();

        if (username.length() > 20) {
            Toast.makeText(this, "Username is too long.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 5 || password.length() > 20) {
            Toast.makeText(this, "Password must be between 5 and 20 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }

        int spaceCount = 0;
        for (int i = 0; i < fullName.length(); i++) {
            if (fullName.charAt(i) == ' ') {
                spaceCount++;
            }
        }

        if (spaceCount < 1) {
            Toast.makeText(this, "Use first and last name.", Toast.LENGTH_SHORT).show();
            return false;
        }


        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if(!mat.matches()) {
            Toast.makeText(this, "Email is invalid.", Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }

}
