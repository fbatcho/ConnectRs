package util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;




 class MyAsync extends AsyncTask<String, Integer, Double> {

    @Override
    protected Double doInBackground(String... params) {
        // TODO Auto-generated method stub
        return null;
    }

    protected void onPostExecute(Double result, Context context){

        Toast.makeText(context, "command sent", Toast.LENGTH_LONG).show();
    }
    protected void onProgressUpdate(Integer... progress){

    }


    public void insertData(final String pseudo, final String email){
       final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date date=new Date();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("ftp://ftp.cluster020.hosting.ovh.net/www/android_php/user/create_user.php", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                RequestParams params = new RequestParams();
                params.put("Pseudo", pseudo);
                params.put("Email",email);
                params.put("Password","***");
                params.put("Telephone","***");
                params.put("DateCreation",dateFormat.format(date));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
