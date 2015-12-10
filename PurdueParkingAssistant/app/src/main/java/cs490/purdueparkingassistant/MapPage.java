package cs490.purdueparkingassistant;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapPage.
     */
    public static MapPage newInstance() {
        MapPage fragment = new MapPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MapPage() {
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
        View v = inflater.inflate(R.layout.fragment_map_page, container, false);
        WebView wv = (WebView) v.findViewById(R.id.mapView);
        wv.loadUrl("http://www.purdue.edu/pat/graphics/parking_map.jpg");
        wv.getSettings().setBuiltInZoomControls(true);
        //ZoomableImage view = (ZoomableImage) v.findViewById(R.id.mapView);
        //Bitmap b = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.parking_map);
        //view.setBitmap(b);
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
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
