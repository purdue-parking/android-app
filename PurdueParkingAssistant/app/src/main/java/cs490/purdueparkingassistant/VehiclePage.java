package cs490.purdueparkingassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

import cs490.purdueparkingassistant.APIClasses.ListViewUpdateReceiver;
import cs490.purdueparkingassistant.APIClasses.ParkingRestClient;
import cs490.purdueparkingassistant.APIClasses.ParkingRestClientUsage;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VehiclePage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VehiclePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehiclePage extends Fragment {

    private static final int ADD_VEHICLE = 0;
    private static final int DELETE_VEHICLE = 1;
    private OnFragmentInteractionListener mListener;
    private View fragmentView;
    private ListView lv;
    private ListViewUpdateReceiver updateReceiver;
    private VehicleArrayAdapter vaa;
    private ProgressDialog dialog;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VehiclePage.
     */
    // TODO: Rename and change types and number of parameters
    public static VehiclePage newInstance() {
        VehiclePage fragment = new VehiclePage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public VehiclePage() {
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
        System.out.println("Creating View");
        // Inflate the layout for this fragment
        vaa = new VehicleArrayAdapter(getActivity(), Global.localUser.getCars());
        fragmentView = inflater.inflate(R.layout.fragment_vehicle_page, container, false);
        lv = (ListView) fragmentView.findViewById(R.id.vehicleList);
        lv.setAdapter(vaa);
        lv.setOnItemClickListener(new VehicleClickListener(Global.localUser.getCars()));
        Button addVehicle = (Button) fragmentView.findViewById(R.id.addVehicle);
        addVehicle.setOnClickListener(new VehicleButtonListener(ADD_VEHICLE));
        Button deleteVehicle = (Button) fragmentView.findViewById(R.id.deleteVehicle);
        deleteVehicle.setOnClickListener(new VehicleButtonListener(DELETE_VEHICLE));

        IntentFilter filter = new IntentFilter(ListViewUpdateReceiver.NEW_UPDATE_BROADCAST);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        updateReceiver = new ListViewUpdateReceiver(lv);
        getContext().registerReceiver(updateReceiver, filter);

        //ParkingRestClientUsage client = new ParkingRestClientUsage(getContext());
        try {
            getVehicles(Global.localUser.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    public void onResume() {
        super.onResume();
        //lv.invalidateViews();
        Log.d("RESUME ", "ACTIVITY RESUMED");
        VehicleArrayAdapter adapter = new VehicleArrayAdapter(getContext(), Global.localUser.getCars());
        lv.setAdapter(adapter);
        /*
        try {
            getVehicles(Global.localUser.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
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

    private class VehicleButtonListener implements View.OnClickListener {

        private int id;

        public VehicleButtonListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            if (id == ADD_VEHICLE) {
                Intent i = new Intent(getContext(), AddVehicle.class);
                startActivity(i);
                System.out.println("NUMBER OF VEHICLES : " + Global.localUser.getCars().size());
                //lv.invalidateViews();
                //lv.refreshDrawableState();


            } else if (id == DELETE_VEHICLE) {
            }

        }
    }

    private class VehicleClickListener implements AdapterView.OnItemClickListener {
        List<Car> objects;

        public VehicleClickListener(List<Car> objects) {
            this.objects = objects;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setTitle("Remove Vehicle?");
            adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ParkingRestClientUsage client = new ParkingRestClientUsage(getContext());
                    try {
                        Car car = Global.localUser.getCars().get(position);
                        System.out.println(car.getMake() + car.getModel());
                        System.out.println(car.getId());
                        client.deleteVehicle(Global.localUser.getCars().get(position).getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Global.localUser.getCars().remove(objects.get(position));
                    Global.localUser.getCars().remove(position);
                    lv.invalidateViews();
                }
            });
            adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            adb.show();
        }
    }


    private class VehicleArrayAdapter extends ArrayAdapter<Car> {

        List<Car> objects;

        public VehicleArrayAdapter(Context context, List<Car> objects) {
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
                v = vi.inflate(R.layout.vehicle_adapter, null);
            }

            TextView license = (TextView) v.findViewById(R.id.licenseField);
            TextView lstate = (TextView) v.findViewById(R.id.stateField);
            TextView make = (TextView) v.findViewById(R.id.makeField);
            TextView model = (TextView) v.findViewById(R.id.modelField);
            TextView year = (TextView) v.findViewById(R.id.yearField);
            TextView color = (TextView) v.findViewById(R.id.colorField);

            Car car = objects.get(position);
            license.setText(car.getLicensePlateNumber());
            lstate.setText(car.getLicensePlateState());
            make.setText(car.getMake());
            model.setText(car.getModel());
            year.setText(car.getYear());
            color.setText(car.getColor());

            return v;
        }

    }

    public void getVehicles(String username) throws JSONException {
        System.out.println(username);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading Messages... Please wait...");
        dialog.show();
        ParkingRestClient.get("vehiclecollection/" + username, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Object " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
                dialog.dismiss();

                try {
                    extractCars(response);
                    VehicleArrayAdapter adapter = new VehicleArrayAdapter(getContext(), Global.localUser.getCars());
                    lv.setAdapter(adapter);
                    //Global.localUser.setCars(cars);
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                dialog.dismiss();
                System.out.println("Array " + statusCode);
                System.out.println("-----------------------------------");
                System.out.println(response);
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

    public void extractCars(JSONObject response) throws JSONException {

        JSONArray items = response.getJSONArray("items");
        ArrayList<Car> cars = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            //System.out.println(items.get(i).toString());
            JSONObject carJSON = items.getJSONObject(i);
            System.out.println(carJSON);
            int id = Integer.parseInt(carJSON.getString("carID"));
            String model = carJSON.getString("model");
            String color = carJSON.getString("color");
            String plateNumber = carJSON.getString("plateNumber");
            String year = carJSON.getString("year");
            String make = carJSON.getString("make");
            String plateState = carJSON.getString("plateState");
            Car tempCar = new Car(id, plateNumber, plateState, make, model, year, color);
            cars.add(tempCar);
        }
        Global.localUser.setCars(cars);
    }
}
