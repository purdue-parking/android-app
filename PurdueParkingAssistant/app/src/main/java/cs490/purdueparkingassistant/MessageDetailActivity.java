package cs490.purdueparkingassistant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

import cs490.purdueparkingassistant.APIClasses.ListViewUpdateReceiver;
import cs490.purdueparkingassistant.APIClasses.ParkingRestClient;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MessageDetailActivity extends AppCompatActivity {

    ProgressDialog dialog;
    private int id;
    private ArrayList<MessageComment> comments;
    private ListView commentList;
    Button addComment;
    Button resolve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        comments = new ArrayList<>();

        TextView messageView = (TextView) findViewById(R.id.messageContentDetail);
        TextView authorView = (TextView) findViewById(R.id.authorViewDetail);
        TextView votesView = (TextView) findViewById(R.id.votesDetail);
        commentList = (ListView) findViewById(R.id.commentList);

        commentList.setAdapter(new CommentArrayAdapter(this, comments));

        addComment = (Button) findViewById(R.id.addComment);
        addComment.setOnClickListener(new AddCommentClickListener());

        resolve = (Button) findViewById(R.id.resolve);


        Bundle b = getIntent().getExtras();
        id = b.getInt("id");
        String message = b.getString("message", "Error");
        String author = b.getString("author", "Unknown");
        int votes = b.getInt("votes", 0);

        messageView.setText(message);
        authorView.setText(author);
        votesView.setText("" + votes);

        if (!author.equals(Global.localUser.getUsername())) {
            resolve.setVisibility(View.GONE);
        } else {
            resolve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        try {
            getComments();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class CommentArrayAdapter extends ArrayAdapter<MessageComment> {

        List<MessageComment> objects;

        public CommentArrayAdapter(Context context, List<MessageComment> objects) {
            super(context, -1, objects);
            this.objects = objects;

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("ListView", "Printing----------------------------------------");
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.comment_adapter, null);
            }

            MessageComment comment = objects.get(position);
            TextView content = (TextView) v.findViewById(R.id.commentContent);
            TextView author = (TextView) v.findViewById(R.id.commentAuthorView);

            content.setText(comment.getContent() + "\n\n\n\n\n");
            author.setText("Posted By " + comment.getAuthor());

            return v;
        }

    }

    public void getComments() throws JSONException {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Comments... Please wait...");
        dialog.show();
        ParkingRestClient.get("commentcollection/" + id, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);

                try {
                    extractComments(response);
                    commentList.setAdapter(new CommentArrayAdapter(getBaseContext(), comments));
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

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialog.dismiss();
                System.out.println(errorResponse);
                Toast toast = Toast.makeText(getBaseContext(), "Error connecting to Server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        });
    }

    public void extractComments(JSONObject response) throws JSONException{
        JSONArray objects = response.getJSONArray("items");
        for (int i = 0; i < objects.length(); i++) {
            JSONObject comment = objects.getJSONObject(i);
            String username = comment.getString("username");
            String content = comment.getString("comment");
            String parentString = comment.getString("parent");
            int parent = Integer.parseInt(parentString);
            comments.add(new MessageComment(content, username, parent));
        }

    }

    private class AddCommentClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Add Comment");

            final EditText input = new EditText(v.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MessageComment m = new MessageComment(input.getText().toString(), Global.localUser.getUsername(), id);
                    try {
                        addComment(m);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

        }

    }

    public void addComment(MessageComment m) throws JSONException {

        comments.add(m);

        JSONObject params = new JSONObject();
        params.put("username", m.getAuthor());
        params.put("comment", m.getContent());
        params.put("parent", m.getParent());
        StringEntity entity = new StringEntity(params.toString(), ContentType.APPLICATION_JSON);

        ParkingRestClient.post(this, "addComment?", entity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                commentList.setAdapter(new CommentArrayAdapter(getBaseContext(), comments));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Post Comment Error", errorResponse.toString());
                Toast.makeText(getBaseContext(), "Error Posting Comment", Toast.LENGTH_LONG).show();
                comments.remove(comments.size()-1);

            }
        });
    }

}
