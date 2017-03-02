package util;

import android.content.Context;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;


import io.fabric.sdk.android.Fabric;


public class ConnectTwitter {
    private static final String TWITTER_KEY = "l2TCeZ6YvEwU8z2reBqA9nQjy";
    private static final String TWITTER_SECRET = "pUW4LhThuDHHHhxwkJEMTqEc3XUiNNhpWzZz6tHXS86ZGgY06R";


    public ConnectTwitter(Context context) {
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
    }

}

