package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.oolink.exo.connectrs.Home;

import cz.msebera.android.httpclient.Header;


public class MyAsyncTask {

    private Context context;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private  static     String URL_CREATE = "http://www.alertcar.ovh/android_php/user/create_user.php";
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
    final Boolean isExist;
    RequestParams params= new RequestParams();
    params.put("Pseudo",pseudo);
    params.put("Email",email);
    params.put("Password"," ");
    params.put("Telephone"," ");

    client.post(URL_CREATE,params, new AsyncHttpResponseHandler() {

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
            Log.d(Home.class.getSimpleName(), "Success "+"status code :"+statusCode+" email et pseudo:"+email+" "+pseudo);
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            Log.d(Home.class.getSimpleName(), "Error http Request failed status: "+statusCode);
            mProgressDialog.dismiss();
        }

        @Override
        public void onRetry(int retryNo) {
            Log.d(Home.class.getSimpleName(), "Retry "+retryNo);
        }
    });
}

}
