package br.ufpe.cin.pet.geoquest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class LoginActivity extends Activity {

    //public final static String ACCESS_TOKEN = "br.ufpe.cin.pet.geoquest.ACCESS_TOKEN";

    private LoginButton authButton;

    private CallbackManager callbackManager;

    private TextView username;

    private ProgressDialog progressDialog;

    ProfileTracker tracker;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        Log.i("LoginActivity", "User already logged in?");
        Log.i("LoginActivity", "Config.key: " + Config.key);
        Log.i("LoginActivity", "LoggedIn: " + isLoggedIn());
        if (AccessToken.getCurrentAccessToken() != null) {
            Log.i("LoginActivity", "User already logged.");
            if (Profile.getCurrentProfile() == null) {
                Profile.fetchProfileForCurrentAccessToken();
                waitProfileLoad();
            } else {
                SharedPreferences sharedPref = getSharedPreferences("keyToken", Context.MODE_PRIVATE);
                Config.key = sharedPref.getString("key", "");

                loggedInCallback();
            }
        } else {
            Log.i("LoginActivity", "User is NOT logged in");

            authButton = (LoginButton) findViewById(R.id.auth_button);
            //Session session = Session.getActiveSession();
            //Log.i("LoginActivity", ""+session.getAccessToken().getToken());

            Log.i("LoginActivity", "" + AccessToken.getCurrentAccessToken());

            authButton.setReadPermissions(Arrays.asList("public_profile, email, user_friends, publish_actions"));

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Carregando...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);

            // Callback registration

            authButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {

                    Log.i("FacebookLogin", "User logged in with success -> AccessToken:");
                    Log.i("FacebookLogin", loginResult.getAccessToken().getToken());
                    Log.i("FacebookLogin", "User logged in with success -> UserID:");

                    Profile.fetchProfileForCurrentAccessToken();
                    AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                    Profile.setCurrentProfile(Profile.getCurrentProfile());

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

    }

    public static boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void waitRegister() {

        Log.i("FacebookLogin", "Enviando token para o servidor");

        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        String backurl = getResources().getString(R.string.facebook_auth_url);
                        Log.e("param[acess_token]", AccessToken.getCurrentAccessToken().getToken());
                        RequestBody formBody = new FormBody.Builder()
                                .add("access_token", AccessToken.getCurrentAccessToken().getToken())
                                .add("code", "login")
                                .build();
                        //RequestBody body = RequestBody.create(JSON, "{ 'access_token' : " + AccessToken.getCurrentAccessToken().getToken() + ", 'code' : 'login'}");
                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(backurl)
                                .post(formBody)
                                .build();

                        okhttp3.Response response = client.newCall(request).execute();
                        String json = response.body().string();
                        JSONObject j = new JSONObject(json);

                        SharedPreferences sharedPref = getSharedPreferences("keyToken", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("key", j.getString("key"));
                        editor.commit();
                        Config.key = j.getString("key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void v) {
                    waitProfileLoad();
                }
            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitProfileLoad() {

        Log.i("FacebookLogin", "Aguardando carregamento do perfil.");

        tracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.i("FacebookLogin", "User profile has changed.");
            }
        };


        progressDialog.hide();

        tracker.startTracking();
        loggedInCallback();
    }

    public void loggedInCallback() {
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

    @Override
    public void onDestroy() {
        Log.e("Login Activity", "onDestroy running");
        super.onDestroy();
        AccessToken.setCurrentAccessToken(null);
        Profile.setCurrentProfile(null);
        tracker.stopTracking();
    }
}
