package cs490.purdueparkingassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cs490.purdueparkingassistant.APIClasses.ListViewUpdateReceiver;
import cs490.purdueparkingassistant.APIClasses.ParkingRestClient;
import cs490.purdueparkingassistant.APIClasses.ParkingRestClientUsage;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public static int nextPage = 1;
    private ListViewUpdateReceiver updateReceiver;
    public ListView messagesView;
    public MessageArrayAdapter maa;
    public MainPageFragment mpf = this;
    private ProgressDialog dialog;
    private int savedPosition = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageFragment newInstance() {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MainPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_page, container, false);
        messagesView = (ListView) v.findViewById(R.id.messageListView);
        //messagesView.setOnItemClickListener(new MessageClickListener());

        Button sortByCount = (Button) v.findViewById(R.id.sortByScore);
        sortByCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParkingRestClientUsage client = new ParkingRestClientUsage(v.getContext());
                try {
                    nextPage = 1;
                    getMessagesSorted(1, true, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Button sortByTime = (Button) v.findViewById(R.id.sortByTime);
        sortByTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nextPage = 1;
                    loadMessages(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button postMessage = (Button) v.findViewById(R.id.postMessage);
        postMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open intent for posting message
                Intent i = new Intent(v.getContext(), AddMessageActivity.class);
                startActivity(i);
            }
        });
        //maa = new MessageArrayAdapter(getContext(), Global.messageBoardContent);
        //messagesView.setAdapter(new MessageArrayAdapter(getContext(), Global.messageBoardContent));
        //messagesView.setAdapter(maa);
        IntentFilter filter = new IntentFilter(ListViewUpdateReceiver.NEW_UPDATE_BROADCAST);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        updateReceiver = new ListViewUpdateReceiver(messagesView);
        getContext().registerReceiver(updateReceiver, filter);
        //maa.fillAdapter();

        Button loadMore = (Button) v.findViewById(R.id.loadMessages);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ParkingRestClientUsage client = new ParkingRestClientUsage(v.getContext());
                try {
                    //getMessagesSorted(Global.page, true, true);
                    nextPage++;
                    loadMessages(nextPage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Button myPosts = (Button) v.findViewById(R.id.myPosts);
        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getMyMessages();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            nextPage++;
            loadMessages(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (updateReceiver != null){
            getContext().unregisterReceiver(updateReceiver);
            updateReceiver = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        messagesView.invalidateViews();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class MessageArrayAdapter extends ArrayAdapter<PublicMessage> {

        public ArrayList<PublicMessage> messages;

        public MessageArrayAdapter(Context context, ArrayList<PublicMessage> messages) {
            super(context, -1, messages);
            this.messages = messages;
        }

        public void setMessages(ArrayList<PublicMessage> listOfMessages) {
            messages = listOfMessages;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            System.out.println(position);
            View v = convertView;
            PublicMessage m = messages.get(position);

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.message, null);
            }


            TextView content = (TextView) v.findViewById(R.id.messageContent);
            TextView author = (TextView) v.findViewById(R.id.authorView);
            final TextView votes = (TextView) v.findViewById(R.id.votes);
            ImageButton upvote = (ImageButton) v.findViewById(R.id.upvote_button);
            ImageButton downvote = (ImageButton) v.findViewById(R.id.downvote_button);
            RelativeLayout clickableView = (RelativeLayout) v.findViewById(R.id.clickableWindow);
            clickableView.setOnClickListener(new MCListener(m));
            //upvote.setOnClickListener(new VoteClickListener(true, m.getId(), v));
            upvote.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int current = Integer.parseInt(votes.getText().toString());
                   votes.setText("" + (current + 1));
                   //ParkingRestClientUsage client = new ParkingRestClientUsage(v.getContext());
                   try {
                       savedPosition = position;
                       upVote(Global.messageBoardContent.get(position).getId());
                       Global.messageBoardContent.get(position).setVotes(current + 1);
                       MessageArrayAdapter adapter = new MessageArrayAdapter(getContext(), Global.messageBoardContent);
                       messagesView.setAdapter(adapter);

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
            });
            //downvote.setOnClickListener(new VoteClickListener(false, m.getId(), v));
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current = Integer.parseInt(votes.getText().toString());
                    votes.setText("" + (current - 1));
                    //ParkingRestClientUsage client = new ParkingRestClientUsage(v.getContext());
                    try {
                        savedPosition = position;
                        downVote(Global.messageBoardContent.get(position).getId());
                        Global.messageBoardContent.get(position).setVotes(current - 1);
                        MessageArrayAdapter adapter = new MessageArrayAdapter(getContext(), Global.messageBoardContent);
                        messagesView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            votes.setText("" + m.getVotes());
            author.setText("Posted By " + m.getPoster());
            if (m.isHelpNeeded()) {
                content.setText("HELP NEEDED:\n" + m.getContent());
            } else {
                content.setText(m.getContent());
            }

            return v;
        }

        private class MCListener implements View.OnClickListener {

            private PublicMessage pm;

            public MCListener(PublicMessage pm) {
                this.pm = pm;
            }


            @Override
            public void onClick(View v) {
                Log.d("CLICK", "CLICKED");
                Intent i = new Intent(v.getContext(), MessageDetailActivity.class);
                Bundle b = new Bundle();
                //PublicMessage pm = Global.messageBoardContent.get(position);
                b.putString("message", pm.getContent());
                b.putString("author", pm.getPoster());
                b.putInt("votes", pm.getVotes());
                b.putInt("id", pm.getId());
                i.putExtras(b);
                startActivity(i);
            }

        }
    }


    public void loadMessages(final int page) throws JSONException {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading Messages... Please wait...");
        dialog.show();
        Log.d("Load Messages", "Loading page " + page + " of messages");
        ParkingRestClient.get("usermessagecollection/" + page, null, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
                try {
                    if (page == 1) {
                        extractMessages(response, false);
                    } else {
                        extractMessages(response, true);
                    }
                    MessageArrayAdapter adapter = new MessageArrayAdapter(getContext(), Global.messageBoardContent);
                    messagesView.setAdapter(adapter);
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
            public void onFailure(int statusCode, Header[] headers, String s, Throwable throwable) {
                dialog.dismiss();
                Log.d("ERROR", s);
                Toast toast = Toast.makeText(getContext(), "Error connecting to Server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialog.dismiss();
                System.out.println(errorResponse);
                Toast toast = Toast.makeText(getContext(), "Error connecting to Server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        });
    }

    public void getMessagesSorted(int page, boolean ascending, final boolean add) throws JSONException {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading Messages... Please wait...");
        dialog.show();
        ParkingRestClient.put("usermessagecollection/" + page + "/" + ascending, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);

                try {
                    extractMessages(response, add);
                    MessageArrayAdapter adapter = new MessageArrayAdapter(getContext(), Global.messageBoardContent);
                    messagesView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialog.dismiss();
                System.out.println(errorResponse);
                Toast toast = Toast.makeText(getContext(), "Error connecting to Server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                dialog.dismiss();
                System.out.println(response);
                Toast toast = Toast.makeText(getContext(), "Error connecting to Server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        });
    }

    public void upVote(int id) throws JSONException {
        System.out.println("UPVOTE :" + id);
        ParkingRestClient client = new ParkingRestClient();
        client.post(getContext(), "upvote/" + id, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
                //messagesView.scrollToPosition(savedPosition);
                messagesView.setSelection(savedPosition);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Array " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast toast = Toast.makeText(getContext(), "Error connecting to Server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        });
    }

    public void downVote(int id) throws JSONException {
        System.out.println("DOWNVOTE:" + id);
        ParkingRestClient client = new ParkingRestClient();
        client.post(getContext(), "downvote/" + id, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
                //messagesView.smoothScrollToPosition(savedPosition);
                messagesView.setSelection(savedPosition);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Array " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast toast = Toast.makeText(getContext(), "Error connecting to Server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        });
    }

    public void getMyMessages() throws JSONException {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading Messages... Please wait...");
        dialog.show();

        ParkingRestClient.get("usermessagecollection/1/" + Global.localUser.getUsername(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                try {
                    extractMessages(response, false);
                    MessageArrayAdapter adapter = new MessageArrayAdapter(getContext(), Global.messageBoardContent);
                    messagesView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Error", errorResponse.toString());
                dialog.dismiss();
                Toast toast = Toast.makeText(getContext(), "Error connecting to Server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        });
    }

    public void extractMessages(JSONObject response, boolean add) throws JSONException{
        Log.d("Extract MEssages", response.toString());
        ArrayList<PublicMessage> messages = new ArrayList<PublicMessage>();
        JSONArray items = response.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            System.out.println(items.getJSONObject(i));
            JSONObject mes = items.getJSONObject(i);
            int id = Integer.parseInt(mes.getString("messageID"));
            String username = mes.getString("username");
            //boolean resolved = mes.getBoolean("resolved");
            boolean helpNeeded = mes.getBoolean("helpNeeded");
            int votes = Integer.parseInt(mes.getString("votes"));
            String content = mes.getString("message");
            //PublicMessage message = new PublicMessage(id, content, username, helpNeeded, resolved, votes);
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

}
