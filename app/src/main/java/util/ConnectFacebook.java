package util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.oolink.exo.connectrs.Home;

import org.json.JSONObject;




public class ConnectFacebook {

    private CallbackManager callbackManager;
    private String email, nom, prenom, pseudo;
    private Context context;

    /**
     *
     * @param context context de l'activité
     */
    public ConnectFacebook(Context context) {
        this.context=context;
        FacebookSdk.sdkInitialize(context);
    }

    /**
     * Appelle le service Facebook
     * Recupére le resultat de la connexion et traite les données
     *
     * @param button button login
     * @param myName nom user
     * @param myEmail email user
     * @param myProfil image du profile
     */

    public void handlerSignInResultFacebook(LoginButton button, final TextView myName, final TextView myEmail, final ImageView myProfil) {
        this.callbackManager = CallbackManager.Factory.create();
        button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(Home.class.getSimpleName(), "Auth Réussi! ");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                email = object.optString("email");
                                nom = object.optString("last_name");
                                prenom = object.optString("first_name");
                               String id = object.optString("id");
                                String myPicture= "http://graph.facebook.com/"+id+"/picture?type=large";
                                Glide.with(context).load(myPicture)
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(myProfil);
                                pseudo = object.optString("last_name") + " " + object.optString("first_name");
                                myEmail.setText(email);
                                myName.setText(pseudo);

                                try {
                                    MyAsyncTask myAsyncTask=new MyAsyncTask(context);
                                    myAsyncTask.inscriptionRs(pseudo,email);
                                }
                                catch (Exception e){
                                    Log.d(Home.class.getSimpleName(),"Error onclick button : "+ e.getMessage());
                                }

                            }
                        }
                );

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,last_name,first_name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast toast = Toast.makeText(context, "Action annulée", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast toast = Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    /**
     * getter
     * @return Callback
     */
    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public String getEmail() {
        return email;
    }

    public String getPseudo() {
        return pseudo;
    }
}
