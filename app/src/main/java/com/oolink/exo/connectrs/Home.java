package com.oolink.exo.connectrs;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import util.ConnectGoogle;


public class Home extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {


    private TextView myName, myEmail;
    private ImageView myProfil;
    private LinearLayout myProfileLayout;


    //Google
    private SignInButton ggin;
    private Button ggout;
    private ConnectGoogle connectGoogle;


    //Facebook
    private LoginButton fb;
    private CallbackManager callbackManager;

    //Twitter
    private TwitterLoginButton tt;
    private static final String TWITTER_KEY = "l2TCeZ6YvEwU8z2reBqA9nQjy";
    private static final String TWITTER_SECRET = "pUW4LhThuDHHHhxwkJEMTqEc3XUiNNhpWzZz6tHXS86ZGgY06R";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_home);

        myName = (TextView) findViewById(R.id.myName);
        myEmail = (TextView) findViewById(R.id.myEmail);
        myProfil = (ImageView) findViewById(R.id.myProfil);
        myProfileLayout = (LinearLayout) findViewById(R.id.myProfileLayout);


//For login Google
        ggin = (SignInButton) findViewById(R.id.ggin);
        ggout = (Button) findViewById(R.id.ggout);
        ggin.setOnClickListener(this);
        ggout.setOnClickListener(this);

        connectGoogle = new ConnectGoogle(this, this, this, myName, myEmail, myProfil);

        //For login Facebook
        fb = (LoginButton) findViewById(R.id.fb);
        fb.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                myName.setText("Reussi!");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                myEmail.setText(object.optString("email"));
                                myName.setText(object.optString("last_name"));

                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                myName.setText("Login annulé.");
            }

            @Override
            public void onError(FacebookException error) {
                myName.setText("Login échoué.");
            }
        });


        //For login Twitter
        tt = (TwitterLoginButton) findViewById(R.id.tt);
        tt.setOnClickListener(this);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        tt.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                myName.setText(result.data.getUserName() + " Action réussi!");
            }

            @Override
            public void failure(TwitterException exception) {
                myName.setText("Erreur login Twitter");
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.ggin:
                //connectGoogle.signIn();
                startActivityForResult(connectGoogle.signIn(), connectGoogle.getRcSignIn());

                break;
            case R.id.ggout:
                connectGoogle.signOut();
                break;
            case R.id.fb:
                break;
            case R.id.tt:
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(Home.class.getSimpleName(), "onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectGoogle.CallGoogle();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == connectGoogle.getRcSignIn()) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            connectGoogle.handleSignInResult(result);
            updateUI(connectGoogle.isVisibility());
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        tt.onActivityResult(requestCode, resultCode, data);
    }

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


