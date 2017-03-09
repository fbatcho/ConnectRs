package util;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.oolink.exo.connectrs.Home;



public class ConnectGoogle {

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private boolean visibility = false;
    private TextView myEmail, myName;
    private ImageView myProfile;
    private ProgressDialog mProgressDialog;
    private Context context;
    private String pseudo,email;


    /**
     * @param context contexte de l'activité
     * @param myEmail email user
     * @param myName nom user
     * @param myProfile image du profile
     */
    public ConnectGoogle(Context context,TextView myName, TextView myEmail,ImageView myProfile) {
        this.myEmail = myEmail;
        this.myName = myName;
        this.myProfile = myProfile;
        this.context = context;
    }

    /**
     * @param fragment FragmentActivity
     * @param failListener listener of Google api
     */
    public void signInServices(FragmentActivity fragment, GoogleApiClient.OnConnectionFailedListener failListener) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(fragment, failListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * Method to login with Google+
     *
     * @return intent of google api
     */
    public Intent signInGoogle() {
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    /**
     * Method to disconnected of Google+
     */
    public void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        visibility = false;
                    }
                });
    }

    /**
     *Call google services login
     */
    public void callGoogle() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(Home.class.getSimpleName(), "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResultGoogle(result);
        } else {
            showProgressDialog(context);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResultGoogle(googleSignInResult);
                }
            });
        }
    }

    /**
     * Give le result of connexion and set data
     * @param result parameter of connexion
     */

    public void handleSignInResultGoogle(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Log.d(Home.class.getSimpleName(), "handleSignInResult:" + result.isSuccess() + " Réussi!");
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            Log.e(Home.class.getSimpleName(), "display name: " + acct.getDisplayName());

            String personPhotoUrl = acct.getPhotoUrl().toString();
            Log.e(Home.class.getSimpleName(), "Name: " + myName + ", email: " + myEmail
                    + ", Image: " + personPhotoUrl);
             pseudo = acct.getDisplayName();
             email = acct.getEmail();
            myName.setText(pseudo);
            myEmail.setText(email);
            Glide.with(context).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myProfile);
            visibility = true;
            try {
                MyAsyncTask myAsyncTask=new MyAsyncTask(context);
                myAsyncTask.inscriptionRs(pseudo,email);


            }
            catch (Exception e){
                Log.d(Home.class.getSimpleName(),"Error onclick button : "+ e.getMessage());
            }
        } else {
            Log.d(Home.class.getSimpleName(), "handleSignInResult:" + result.isSuccess());
            visibility = false;
            Toast toast = Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    /**
     * Barre de chargement
     *
     * @param context context de l'activité
     */
    private void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Chargement");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    /**
     * getter
     * @return visibility
     */
    public boolean isVisibility() {
        return visibility;
    }

    /**
     * setter
     *
     * @return variable sign in
     */
    public int getRcSignIn() {
        return RC_SIGN_IN;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEmail() {
        return email;
    }
}
