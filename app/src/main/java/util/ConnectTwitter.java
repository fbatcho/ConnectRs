package util;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oolink.exo.connectrs.Home;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;


import io.fabric.sdk.android.Fabric;
import retrofit2.Call;


public class ConnectTwitter {
    private static final String TWITTER_KEY = "G1Reb2Zq5OTay56lVGY8Y4AYb";
    private static final String TWITTER_SECRET = "qqkRWfOFy00IEsmwr9G2KfPyNP2GSPxiY8S0DG0S6u9sEXtntB";
    private TextView myEmail, myName;
    private ImageView myProfil;
    private Context context;
    private String email, pseudo;

    /**
     *
     * @param context
     */
    public ConnectTwitter(Context context) {
        this.context = context;
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));

    }

    /**
     *
     * @param myName
     * @param myEmail
     * @param myProfil
     * @return
     */

    public Callback CallTwitter(TextView myName,TextView myEmail, ImageView myProfil) {
        this.myEmail = myEmail;
        this.myName = myName;
        this.myProfil = myProfil;

        final Callback twitterCallback = new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(Home.class.getSimpleName(), "---Réussi ---");
                handlerSignInResultTwitter(result);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d(Home.class.getSimpleName(), "--- Fail! ---");
            }
        };
        return twitterCallback;
    }

    /**
     *  Recupére le resultat de la connexion et traite les données
     * @param result
     */
    private void handlerSignInResultTwitter(final Result<TwitterSession> result) {

   //Creating a twitter session with result's data
        final TwitterSession session = result.data;


        //Getting the username from session
        final String username = session.getUserName();
        //final String email=result.data.email;

        //This code will fetch the profile image URL
        //Getting the account service of the user logged in
        Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);

        userResult.enqueue(new Callback<User>() {
            @Override
            public void failure(TwitterException e) {
                Toast toast = Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void success(Result<User> userResult) {
                //If it succeeds creating a User object from userResult.data
                User user = userResult.data;
                //Getting the profile image url
                String profileImage = user.profileImageUrl.replace("_normal", "");
                pseudo=user.name;
                myName.setText(pseudo);
                Glide.with(context).load(profileImage)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(myProfil);

                final TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                         email = result.data.toString();
                        myEmail.setText(email);
                        Log.d("Yes", "email--> " + email);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d("Oh No!", "Email failed");

                    }
                });
                Log.d("done", "name--> " + username + " url-->" + profileImage);
                try {
                    MyAsyncTask myAsyncTask=new MyAsyncTask(context);
                    myAsyncTask.execute(pseudo,email);


                }
                catch (Exception e){
                    Log.d(Home.class.getSimpleName(),"Error onclick button : "+ e.getMessage());
                }
            }
        });

    }


    public void sigOutTwitter() {
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
    }

    public String getEmail() {
        return email;
    }

    public String getPseudo() {
        return pseudo;
    }
}

