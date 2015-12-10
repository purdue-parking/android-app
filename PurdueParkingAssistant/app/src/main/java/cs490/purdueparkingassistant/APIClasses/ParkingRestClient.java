package cs490.purdueparkingassistant.APIClasses;


import android.content.Context;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ContentType;

/**
 * Created by Craig on 10/27/2015.
 */
public class ParkingRestClient {

    private static final String BASE_URL = "https://purdue-parking.appspot.com/_ah/api/purdueParking/1/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteURL(url), params, responseHandler);
    }
//removed RequestParams params from constructor
    public static void post(Context context, String url, HttpEntity entity, AsyncHttpResponseHandler responseHandler) {
        //client.post(getAbsoluteURL(url), params, responseHandler);
        client.post(context, getAbsoluteURL(url), entity, "application/json", responseHandler);
    }

    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.delete(getAbsoluteURL(url), params, responseHandler);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(getAbsoluteURL(url), params, responseHandler);
    }

    private static String getAbsoluteURL(String relativeUrl) {
        return BASE_URL+relativeUrl;
    }
}
