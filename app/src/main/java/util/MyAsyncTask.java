package util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.oolink.exo.connectrs.Home;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fanny on 07/03/2017.
 */

public class MyAsyncTask extends AsyncTask<String, Integer, Double> {

    private Context context;
    private String url = "ftp://ftp.cluster020.hosting.ovh.net/www/android_php/user/create_user.php";

    private static AsyncHttpClient syncHttpClient = new SyncHttpClient();
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();


    public MyAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Double doInBackground(String... params) {
        post(params);

        return null;
    }

    protected void onPostExecute(Double result) {
        Toast.makeText(context, "command sent", Toast.LENGTH_LONG).show();
    }

    protected void onProgressUpdate(Integer... progress) {

    }


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(url, params, responseHandler);
    }

    private void post(final String ... params) {
        RequestParams parameters = new RequestParams();
        parameters.put("Pseudo",params[0]);
        parameters.put("Email",params[1]);
        parameters.put("Password","Essaie");
        parameters.put("Telephone","Essaie");

        getClient().post(url, parameters, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(Home.class.getSimpleName(), "Success ");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(Home.class.getSimpleName(), "Error http Request failed status: "+statusCode+" params: "+params[0]+params[1] );
               // Toast toast = Toast.makeText(context, "Erreur post data", Toast.LENGTH_LONG);
                //toast.show();
            }

        });


    }
    private static AsyncHttpClient getClient()
    {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }
}
