package util;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;


import com.facebook.AccessToken;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.internal.SwipeToDismissTouchListener;


import io.fabric.sdk.android.Fabric;
import retrofit2.Call;


public class ConnectTwitter {
    private static final String TWITTER_KEY = "G1Reb2Zq5OTay56lVGY8Y4AYb";
    private static final String TWITTER_SECRET = "qqkRWfOFy00IEsmwr9G2KfPyNP2GSPxiY8S0DG0S6u9sEXtntB";
     private static Twitter twitter;


    public ConnectTwitter(Context context) {
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        TwitterAuthClient client = new TwitterAuthClient();

    }


    /**
     * Recupére le resultat de la connexion et traite les données
     * @param session
     * @param myName
     * @param myEmail
     */
    public void handlerSignInResultTwitter (TwitterSession session, final TextView myName, final TextView myEmail) {
        Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);
        userResult.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {

                User user = userResult.data;
                // String twitterImage = user.profileImageUrl;

                try {
                    myName.setText(user.name);
                    myEmail.setText(user.email);
                    Log.d("imageurl", user.profileImageUrl);
                    Log.d("name", user.name);
                    Log.d("email", user.email);
                    Log.d("des", user.description);
                    Log.d("followers ", String.valueOf(user.followersCount));
                    Log.d("createdAt", user.createdAt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(TwitterException e) {
                Log.d(ConnectTwitter.class.getSimpleName(), "User failed");
            }

        });
    }



}

