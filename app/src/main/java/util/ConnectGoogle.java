package util;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView myProfil;
    private ProgressDialog mProgressDialog;
    private Context context;


    /**
     *
     * @param context
     * @param myEmail
     * @param myName
     * @param myProfil
     */
    public ConnectGoogle(Context context, TextView myEmail, TextView myName, ImageView myProfil) {
        this.myEmail = myEmail;
        this.myName = myName;
        this.myProfil = myProfil;
        this.context = context;
    }

    /**
     *
     * @param fragment
     * @param failListener
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
     * Méthode pour se logger avec Google+
     * @return
     */
    public Intent signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        return signInIntent;
    }

    /**
     * Méthode pour se déconnecter de Google+
     */
    public void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        visibility = false;
                    }
                });
    }

    /**
     *
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
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResultGoogle(googleSignInResult);
                }
            });
        }
    }

    /**
     * Recupére le resultat de la connexion et traite les données
     * @param result
     */

    public void handleSignInResultGoogle(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Log.d(Home.class.getSimpleName(), "handleSignInResult:" + result.isSuccess() + " Réussi!");
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(Home.class.getSimpleName(), "display name: " + acct.getDisplayName());

            String personPhotoUrl = acct.getPhotoUrl().toString();
            Log.e(Home.class.getSimpleName(), "Name: " + myName + ", email: " + myEmail
                    + ", Image: " + personPhotoUrl);

            myName.setText(acct.getDisplayName());
            myEmail.setText(acct.getEmail());
            Glide.with(context).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myProfil);


            visibility = true;
        } else {
            Log.d(Home.class.getSimpleName(), "handleSignInResult:" + result.isSuccess());
            visibility = false;
        }
    }


    /**
     * Barre de chargement
     * @param context
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
     * @return
     */
    public int getRcSignIn() {
        return RC_SIGN_IN;
    }
}
