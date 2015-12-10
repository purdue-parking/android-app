package cs490.purdueparkingassistant.APIClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cs490.purdueparkingassistant.MainPageFragment;

/**
 * Created by Craig on 10/28/2015.
 */
public class ListViewUpdateReceiver extends BroadcastReceiver{

    public static final String NEW_UPDATE_BROADCAST = "cs490.purdueparkingassistant";
    public ListView listView;

    public ListViewUpdateReceiver(ListView lv) {
        listView = lv;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Updating ListView");
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
        System.out.println("ListView Updated");
    }
}
