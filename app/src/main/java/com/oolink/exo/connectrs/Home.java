package com.oolink.exo.connectrs;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import util.ConnectFacebook;
import util.ConnectGoogle;
import util.ConnectTwitter;


public class Home extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {


    private  TextView myName, myEmail;
    private ImageView myProfil;
    private LinearLayout myProfileLayout;

    private final FragmentActivity fragmentActivity = this;
    private final Context context = this;
    private final GoogleApiClient.OnConnectionFailedListener listener = this;
    private int logRs=0;

    //Google
    private SignInButton ggin;
    private Button ggout;
    private ConnectGoogle connectGoogle;


    //Facebook
    private LoginButton fb;
    private ConnectFacebook connectFacebook;


    //Twitter
    private TwitterLoginButton ttin;
    private Button ttout;
    private ConnectTwitter connectTwitter;
    private TwitterSession session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectFacebook = new ConnectFacebook(this);
        connectTwitter = new ConnectTwitter(this);
        setContentView(R.layout.activity_home);


        myName = (TextView) findViewById(R.id.myName);
        myEmail = (TextView) findViewById(R.id.myEmail);
        myProfil = (ImageView) findViewById(R.id.myProfil);
        myProfileLayout = (LinearLayout) findViewById(R.id.myProfileLayout);


        connectGoogle = new ConnectGoogle(context, myName, myEmail, myProfil);

        //For login Google
        ggin = (SignInButton) findViewById(R.id.ggin);
        ggout = (Button) findViewById(R.id.ggout);
        connectGoogle.signInServices(fragmentActivity, listener);

        //Connexion Google
        ggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(connectGoogle.signInGoogle(), connectGoogle.getRcSignIn());
                logRs = 1;
            }
        });
        //Deconnexion Google
        ggout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectGoogle.signOutGoogle();
                updateUI(connectGoogle.isVisibility());
                logRs = 0;
            }
        });

        //For login Facebook
        fb = (LoginButton) findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectFacebook.callFacebook(fb, myName, myEmail);
                logRs = 2;
            }
        });


        //For login Twitter
        ttin = (TwitterLoginButton) findViewById(R.id.tt);
        ttout = (Button) findViewById(R.id.ttout);

        ttin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logRs=3;
                updateUI(true);
                ttin.setVisibility(View.VISIBLE);

            }
        });

        final Callback twitterCallback = new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(Home.class.getSimpleName(), "---Réussi ---");
                //session = result.data;
                //String username = session.getUserName();
                //myName.setText("Hi " + username);
                // connectTwitter.handlerSignInResultTwitter(session, myName, myEmail);
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d(Home.class.getSimpleName(), "--- Fail! ---");
                myName.setText("Login failed");
            }
        };

        ttin.setCallback(twitterCallback);


        ttout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Twitter.getSessionManager().clearActiveSession();
                Twitter.logOut();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(Home.class.getSimpleName(), "onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (logRs == 1) {
            connectGoogle.callGoogle();
        }
        updateUI(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Home.class.getSimpleName(), "---Type de RS: " + logRs + " ---");
        //si Connexion via google +
        if (requestCode == connectGoogle.getRcSignIn() && logRs == 1) {
            Log.d(Home.class.getSimpleName(), " Connexion Google");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            connectGoogle.handleSignInResultGoogle(result);
            updateUI(connectGoogle.isVisibility());
        }
        //si Connexion via Facebook
         if (logRs == 2) {
            Log.d(Home.class.getSimpleName(), " Connexion Facebook");
            connectFacebook.getCallbackManager().onActivityResult(requestCode, resultCode, data);
            updateUI(true);
        }
        //Si connexion via Twitter
         if (logRs == 3) {
            Log.d(Home.class.getSimpleName(), " Connexion Twitter");
            ttin.onActivityResult(requestCode, resultCode, data);


        }
    }

    /**
     * Visibilite des widgets et des layouts
     *
     * @param isSignedIn
     */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            if(logRs==1) {
                ggin.setVisibility(View.GONE);
                ggout.setVisibility(View.VISIBLE);
            }
            myProfileLayout.setVisibility(View.VISIBLE);
        } else {
            if(logRs==1||logRs==0) {
                ggin.setVisibility(View.VISIBLE);
                ggout.setVisibility(View.GONE);
            }
            myProfileLayout.setVisibility(View.GONE);
        }
    }

    public void login(Result<TwitterSession> result) {

//Creating a twitter session with result's data
        TwitterSession session = result.data;


        //Getting the username from session
        final String username = session.getUserName();

        //This code will fetch the profile image URL
        //Getting the account service of the user logged in
        Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);

        userResult.enqueue(new Callback<User>() {
            @Override
            public void failure(TwitterException e) {
                //If any error occurs handle it here
            }

            @Override
            public void success(Result<User> userResult) {
                //If it succeeds creating a User object from userResult.data
                User user = userResult.data;

                //Getting the profile image url
                String profileImage = user.profileImageUrl.replace("_normal", "");
                myName.setText(username);
                myEmail.setText(user.email);
                Glide.with(context).load(profileImage)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(myProfil);
                Log.d("done", "name--> " + username + " url-->" + profileImage);

            }
        });
    }

}


