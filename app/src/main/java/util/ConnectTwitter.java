package util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;


import io.fabric.sdk.android.Fabric;


public class ConnectTwitter {
    private static final String TWITTER_KEY = "l2TCeZ6YvEwU8z2reBqA9nQjy";
    private static final String TWITTER_SECRET = "pUW4LhThuDHHHhxwkJEMTqEc3XUiNNhpWzZz6tHXS86ZGgY06R";


    public ConnectTwitter(Context context) {
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
    }

    public void handlerSignInResultTwitter () {



    }





}

