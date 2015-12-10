package cs490.purdueparkingassistant.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by Craig on 11/4/2015.
 */
public abstract class CustomArrayAdapter<T> extends ArrayAdapter {

    public abstract void fillAdapter();

    public CustomArrayAdapter(Context context) {
        super(context, -1);
    }


}
