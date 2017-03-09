package util;

import android.app.ProgressDialog;
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
    private String url = "http://www.alertcar.ovh/android_php/user/create_user.php";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private ProgressDialog mProgressDialog;


    public MyAsyncTask(Context context) {
        this.context = context;

    }

    /**
     * Inscription user par Réseaux sociaux
     * @param pseudo pseudo user
     * @param email email user
     */
    public void inscriptionRs(final String pseudo, final String email)
{
    RequestParams params= new RequestParams();
    params.put("Pseudo",pseudo);
    params.put("Email",email);
    params.put("Password"," ");
    params.put("Telephone"," ");

    client.post(url,params, new AsyncHttpResponseHandler() {

        @Override
        public void onStart() {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage("Chargement");
                mProgressDialog.setIndeterminate(true);
            }
            mProgressDialog.show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            Log.d(Home.class.getSimpleName(), "Success "+"status code :"+response+" email et pseudo:"+email+" "+pseudo);
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            Log.d(Home.class.getSimpleName(), "Error http Request failed status: "+statusCode+" params: "+pseudo+" "+email );
            mProgressDialog.dismiss();
        }

        @Override
        public void onRetry(int retryNo) {
            Log.d(Home.class.getSimpleName(), "Retry "+retryNo);
        }
    });
}

}
