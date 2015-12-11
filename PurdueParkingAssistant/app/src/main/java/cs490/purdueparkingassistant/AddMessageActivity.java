package cs490.purdueparkingassistant;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cs490.purdueparkingassistant.APIClasses.ParkingRestClient;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Craig on 12/7/2015.
 */
public class AddMessageActivity extends AppCompatActivity {

    Button postMessage;
    EditText messageBody;
    CheckBox helpNeededCheck;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        c = this;

        postMessage = (Button) findViewById(R.id.completeMessage);
        messageBody = (EditText) findViewById(R.id.messageEditText);
        helpNeededCheck = (CheckBox) findViewById(R.id.helpNeededCheck);

        postMessage.setOnClickListener(new PostMessageClickListener());

    }

    public void postMessage(PublicMessage m) throws JSONException {

        JSONObject params = new JSONObject();
        params.put("username", m.getPoster());
        params.put("message", m.getContent());
        params.put("helpNeeded", m.isHelpNeeded());
        //params.put("resolved", m.isResolved());
        StringEntity entity = new StringEntity(params.toString(), ContentType.APPLICATION_JSON);

        ParkingRestClient.post(this, "addMessage", entity, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Add Message COmplete", response.toString());
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Post Error", errorResponse.toString());
                Toast.makeText(c, "Error Connecting to Server", Toast.LENGTH_LONG).show();
            }
        });

    }

    private class PostMessageClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (messageBody.getText().toString() == "") {
                Toast.makeText(v.getContext(), "Message Body Is Empty", Toast.LENGTH_SHORT).show();
            } else {
                boolean isHelpNeeded = helpNeededCheck.isChecked();

                PublicMessage m = new PublicMessage(messageBody.getText().toString(), Global.localUser.getUsername(), isHelpNeeded);
                try {
                    postMessage(m);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
