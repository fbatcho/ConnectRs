package com.oolink.exo.connectrs;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.FacebookSdk;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;

import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import util.ConnectFacebook;
import util.ConnectGoogle;
import util.ConnectTwitter;


public class Home extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {


    private TextView myName, myEmail;
    private ImageView myProfil;
    private LinearLayout myProfileLayout;

    private final FragmentActivity fragmentActivity = this;
    private final Context context = this;
    private final GoogleApiClient.OnConnectionFailedListener listener = this;
    private int logRs;

    //Google
    private SignInButton ggin;
    private Button ggout;
    private ConnectGoogle connectGoogle;


    //Facebook
    private LoginButton fb;
    private ConnectFacebook connectFacebook;


    //Twitter
    private TwitterLoginButton tt;
    private ConnectTwitter connectTwitter;
    private TwitterSession session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
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


        ggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(connectGoogle.signInGoogle(), connectGoogle.getRcSignIn());
                logRs = 1;
            }
        });

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
        connectFacebook = new ConnectFacebook();
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectFacebook.callFacebook(fb, myName, myEmail);
                logRs = 2;
            }
        });


        //For login Twitter
        tt = (TwitterLoginButton) findViewById(R.id.tt);
        tt.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(Home.class.getSimpleName(), "---Réussi ---");
                session = result.data;
                String username = session.getUserName();
                Long  userid = session.getUserId();
                myName.setText("Hi " + username);
                logRs = 3;
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d(Home.class.getSimpleName(), "--- Fail! ---");
                myName.setText("Login failed");
                logRs = 0;
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
        //si Connexion via google +
        if (requestCode == connectGoogle.getRcSignIn() && logRs == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            connectGoogle.handleSignInResultGoogle(result);
            updateUI(connectGoogle.isVisibility());
            Log.d(Home.class.getSimpleName(), " Connexion Google");
        }
        //si Connexion via Facebook
        else if (logRs == 2) {
            Log.d(Home.class.getSimpleName(), " Connexion Facebook");
            connectFacebook.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
        //Si connexion via Twitter
        else if (logRs == 3) {
            Log.d(Home.class.getSimpleName(), " Connexion Twitter");
            tt.onActivityResult(requestCode, resultCode, data);
            myName.setText(requestCode);
        }
    }

    /**
     * Visibilite des widgets et des layouts
     *
     * @param isSignedIn
     */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            ggin.setVisibility(View.GONE);
            ggout.setVisibility(View.VISIBLE);
            myProfileLayout.setVisibility(View.VISIBLE);
        } else {
            ggin.setVisibility(View.VISIBLE);
            ggout.setVisibility(View.GONE);
            myProfileLayout.setVisibility(View.GONE);
        }
    }


}


