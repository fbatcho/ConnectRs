package util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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


/**
 * Created by Antoine on 02/03/2017.
 */

public class ConnectFacebook {

    private CallbackManager callbackManager;

    public ConnectFacebook(Context context) {
        FacebookSdk.sdkInitialize(context);
    }

    public void callFacebook(LoginButton button, final TextView myName, final TextView myEmail) {
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
                                myEmail.setText(object.optString("email"));
                                myName.setText(object.optString("last_name")+" "+object.optString("first_name"));

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
                myName.setText("Login annulé.");
            }

            @Override
            public void onError(FacebookException error) {
                myName.setText("Login échoué.");
            }
        });

    }


    public CallbackManager getCallbackManager() {
        return callbackManager;
    }
}
