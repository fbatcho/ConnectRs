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

public class MyAsyncTask {

    private Context context;
    private String url = "http://www.ovh.net/www/android_php/user/create_user.php";
    private AsyncHttpClient client = new AsyncHttpClient();

    public MyAsyncTask(Context context, final String pseudo, final String email) {
        this.context = context;
        RequestParams params= new RequestParams();
        params.put("Pseudo",email);
        params.put("Email",pseudo);
        params.put("Password","Essaie");
        params.put("Telephone","Essaie");

        client.post(url, new AsyncHttpResponseHandler() {


            @Override
            public void onStart() {
                //Toast.makeText(context, "command sent", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d(Home.class.getSimpleName(), "Success ");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(Home.class.getSimpleName(), "Error http Request failed status: "+statusCode+" params: "+pseudo+" "+email );
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(Home.class.getSimpleName(), "Retry "+retryNo);
            }
        });
    }



}
