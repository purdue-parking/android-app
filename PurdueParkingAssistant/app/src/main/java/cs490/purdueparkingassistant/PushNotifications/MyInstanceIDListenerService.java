package cs490.purdueparkingassistant.PushNotifications;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

import cs490.purdueparkingassistant.PushNotifications.RegistrationIntentService;

/**
 * Created by Craig on 10/8/2015.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
