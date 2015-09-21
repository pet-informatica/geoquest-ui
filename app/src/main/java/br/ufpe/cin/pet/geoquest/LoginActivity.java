package br.ufpe.cin.pet.geoquest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpe.cin.pet.geoquest.MainActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import java.util.HashMap;
import java.util.Map;


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
            if(Profile.getCurrentProfile() == null) {
                Profile.fetchProfileForCurrentAccessToken();
                waitProfileLoad();
            }else{
                loggedInCallback();
            }
        }else{
            Log.i("LoginActivity", "User is NOT logged in");
        }

        callbackManager = CallbackManager.Factory.create();

        authButton = (LoginButton) findViewById(R.id.auth_button);
        authButton.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        // Callback registration
        authButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i("FacebookLogin", "User logged in with success -> AccessToken:");
                System.out.println(AccessToken.getCurrentAccessToken().getToken());

                Profile.fetchProfileForCurrentAccessToken();

                progressDialog.show();

                Log.i("LoginActivity", "Registering user to server");
                waitRegister();

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

    private void waitRegister(){

        String url = "https://shielded-plains-2193.herokuapp.com/register";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LoginActivity", "User registered with success");
                waitProfileLoad();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginActivity", "Error on user registration");
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("Error", "TimeoutError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Error", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Error", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Error", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Error", "ParseError");
                }

                Log.e("Error", " Code " + error.networkResponse.data);

                Log.e("Error", " Code " + error.networkResponse.statusCode);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("access_token", AccessToken.getCurrentAccessToken().getToken());
                return params;
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestSingleton.getInstance(this).addToRequestQueue(sr);
    }

    private void waitProfileLoad(){

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
