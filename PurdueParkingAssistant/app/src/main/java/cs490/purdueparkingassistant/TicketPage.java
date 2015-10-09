package cs490.purdueparkingassistant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View fragmentView;

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TicketPage.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketPage newInstance() {
        TicketPage fragment = new TicketPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TicketPage() {
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
        fragmentView = inflater.inflate(R.layout.fragment_vehicle_info_page, container, false);
        ListView ticketList = (ListView) fragmentView.findViewById(R.id.ticketList);
        List<Ticket> ticks = new ArrayList<Ticket>();
        for (int i = 0; i < 4; i++) {
            ticks.add(new Ticket("039458294502",new Date(), 20));
        }
        ticketList.setAdapter(new TicketArrayAdapter(getActivity(), ticks));
        ticketList.setOnItemClickListener(new TicketClickListener(ticks));
        return fragmentView;
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

    private class TicketClickListener implements AdapterView.OnItemClickListener {

        List<Ticket> objects;

        public TicketClickListener(List<Ticket> objects) {
            this.objects = objects;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            Log.d("Click", "Click");
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.ticket_popup);
            Ticket t = objects.get(position);

            TextView idView = (TextView) dialog.findViewById(R.id.popupId);
            idView.setText(t.getId());
            TextView dateView = (TextView) dialog.findViewById(R.id.popupDate);
            dateView.setText("" + t.getDateReceived());
            TextView costView = (TextView) dialog.findViewById(R.id.popupCost);
            costView.setText("$"+t.getAmount());
            TextView link = (TextView) dialog.findViewById(R.id.popupLink);
            link.setMovementMethod(LinkMovementMethod.getInstance());
            dialog.show();
        }
    }

    private class TicketArrayAdapter extends ArrayAdapter<Ticket> {

        List<Ticket> objects;

        public TicketArrayAdapter(Context context, List<Ticket> objects) {
            super(context, -1, objects);
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.ticket_adapter, null);
            }

            Ticket t = objects.get(position);
            TextView title = (TextView) v.findViewById(R.id.ticketId);
            TextView date = (TextView) v.findViewById(R.id.dateView);
            TextView cost = (TextView) v.findViewById(R.id.costView);

            title.setText(t.getId());
            date.setText(t.getDateReceived().toString());
            cost.setText("$"+t.getAmount());

            return v;
        }
    }

}
