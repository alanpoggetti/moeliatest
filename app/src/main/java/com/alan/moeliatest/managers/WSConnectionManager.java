package com.alan.moeliatest.managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.alan.moeliatest.R;
import com.alan.moeliatest.utils.CallbackMethod;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
/**
 * This class will manage our connections
 */
public class WSConnectionManager {


    /**
     * By calling this method, it will connect via GET to a given url and if the connection was
     * successful it will call the provided CallbackMethod's success method, or failed in case of
     * connection error
     *
     * @param url
     * @param callbackMethod
     * @param context
     */
    public static void performAction(String url, final CallbackMethod callbackMethod, Context context) {

        //Client setup
        AsyncHttpClient client = new AsyncHttpClient();

        client.setMaxRetriesAndTimeout(10, 120000);

        //ProgressDialog setup and launch
        final ProgressDialog pDialog = new ProgressDialog(context);

        pDialog.setTitle(context.getString(R.string.progress_dialog_title));
        pDialog.setMessage(context.getString(R.string.progress_dialog_message));

        pDialog.setCancelable(false);

        pDialog.show();

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                pDialog.dismiss();

                String response = "";

                Log.e("Movies", "Response: " + response);

                //We convert our response into String
                try {
                    response = responseBody == null ? null : new String(responseBody, this.getCharset());
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

                callbackMethod.success(response);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                pDialog.dismiss();

                String response = "";

                //We convert our response into String

                try {
                    response = responseBody == null ? null : new String(responseBody, this.getCharset());
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
                callbackMethod.failed(response);
            }
        });
    }


}
