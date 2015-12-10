package cs490.purdueparkingassistant;

import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import cs490.purdueparkingassistant.APIClasses.ParkingRestClientUsage;

/**
 * Created by Craig on 11/5/2015.
 */
public class VoteClickListener implements View.OnClickListener {

    private int id;
    private boolean up;
    private View view;

    public VoteClickListener(boolean up, int id, View view) {
        this.id = id;
        this.up = up;
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        ParkingRestClientUsage client = new ParkingRestClientUsage(v.getContext());
        try {
            client.upVote(id);
            TextView votes = (TextView) view.findViewById(R.id.votes);
            int current = Integer.parseInt(votes.getText().toString());
            if (up) {
                votes.setText(current++);
            } else {
                votes.setText(current--);
            }
            } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
