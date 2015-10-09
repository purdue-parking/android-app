package cs490.purdueparkingassistant;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailField = (EditText) findViewById(R.id.emailField);
        TextView createButton = (TextView) findViewById(R.id.createAccount);
        createButton.setOnClickListener(new LoginPageClickListener(1));
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginPageClickListener(0));
        getSupportActionBar().hide();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://www.google.com", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        })

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

        public LoginPageClickListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            if (id == LOGINID) {
                Global.localUser = new User();
                Intent i = new Intent(v.getContext(), NavigationActivity.class);
                startActivity(i);

            } else if (id == CREATEID) {

            }

        }
    }

}
