package cs490.purdueparkingassistant;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cs490.purdueparkingassistant.APIClasses.ParkingRestClient;
import cz.msebera.android.httpclient.Header;
//import com.loopj.android.http.*;


public class LoginActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        TextView createButton = (TextView) findViewById(R.id.createAccount);
        createButton.setOnClickListener(new LoginPageClickListener(this, 1));
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginPageClickListener(this, 0));
        getSupportActionBar().hide();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    class LoginPageClickListener implements View.OnClickListener {

        private static final int LOGINID = 0;
        private static final int CREATEID = 1;
        private int id;
        private Context c;

        public LoginPageClickListener(Context c, int id) {
            this.id = id;
            this.c = c;
        }

        @Override
        public void onClick(View v) {
            if (id == LOGINID) {
                Global.localUser = new User();
                Global.localUser.setUsername("cbrentz");
                Global.localUser.setName("Craig");
                login();



            } else if (id == CREATEID) {
                Intent i = new Intent(v.getContext(), CreateAccountActivity.class);
                startActivity(i);

            }

        }
    }

    public void login() {
        ParkingRestClient.get("login/" + usernameField.getText().toString() + "/" + passwordField.getText().toString(),null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Login", response.toString());
                try {
                    JSONObject error = response.getJSONObject("error");
                } catch (JSONException e) {
                    //login is good
                    try {
                        setLoginInfo(response);
                        Log.d("USER", Global.localUser.getUsername());
                        Log.d("USER", Global.localUser.getPassword());
                        Intent i = new Intent(getBaseContext(), NavigationActivity.class);
                        startActivity(i);
                    } catch (JSONException e2) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), "Invalid Information", Toast.LENGTH_LONG).show();
            }

        });

    }

    public void setLoginInfo(JSONObject response) throws JSONException {
        JSONObject properties = response.getJSONObject("properties");
        String username = properties.getString("username");
        String phone = properties.getString("phoneNumber");
        String password = properties.getString("password");
        boolean ticketEmail = properties.getBoolean("ticketEmail");
        boolean helpEmail = properties.getBoolean("helpEmail");
        boolean responseEmail = properties.getBoolean("responseEmail");
        String name = properties.getString("name");
        String email = properties.getString("email");
        User u = new User(name,email,phone,username,password);
        u.receiveEmailNotifications = responseEmail;
        u.receiveHelpNotifications = helpEmail;
        u.receiveTicketPushNotifications = ticketEmail;
        Global.localUser = u;
    }



}
