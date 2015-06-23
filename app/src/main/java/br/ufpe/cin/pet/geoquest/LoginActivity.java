package br.ufpe.cin.pet.geoquest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.ufpe.cin.pet.geoquest.MainActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;


public class LoginActivity extends Activity {

    public final static String ACCESS_TOKEN = "br.ufpe.cin.pet.geoquest.ACCESS_TOKEN";

    private LoginButton authButton;

    private TextView username;

    private CallbackManager callbackManager;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        Log.i("LoginActivity", "User already logged in?");
        if(AccessToken.getCurrentAccessToken() != null){
            Log.i("LoginActivity", "User already logged.");
            Profile.fetchProfileForCurrentAccessToken();
            waitProfileLoad();
        }else{
            Log.i("LoginActivity", "User is NOT logged in");
        }

        callbackManager = CallbackManager.Factory.create();

        authButton = (LoginButton) findViewById(R.id.auth_button);
        authButton.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));



        // Callback registration
        authButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i("FacebookLogin", "User logged in with success -> AccessToken: " + AccessToken.getCurrentAccessToken().getToken());

                Profile.fetchProfileForCurrentAccessToken();
                waitProfileLoad();

            }

            @Override
            public void onCancel() {
                Log.i("FacebookLogin", "LoginCanceled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("FacebookLogin", "LoginError");
                Log.e("LoginActivity", exception.getMessage());
            }
        });


    }

    private void waitProfileLoad(){

        progressDialog = ProgressDialog.show(this, "",
                "Carregando", true);

        progressDialog.show();

        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.i("FacebookLogin", "User profile loaded.");
                loggedInCallback();
                progressDialog.hide();
            }
        };

        profileTracker.startTracking();

    }

    public void loggedInCallback(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
