package br.ufpe.cin.pet.geoquest;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

public class MainActivity extends Activity implements CategoryFragment.OnFragmentInteractionListener{

    private Bitmap userImage;
	private String userName;
    private ProgressDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new MainFragment()).commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onNavigateUp() {
		getFragmentManager().beginTransaction()
		.replace(R.id.container, new MainFragment()).commit();
		return true;
	}

	public void onBackPressed()
	{
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new MainFragment()).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.menu_help) {
			getFragmentManager().beginTransaction()
			.replace(R.id.container, new HelpFragment()).commit();
			return true;
		} else if (id == R.id.menu_credits) {
			getFragmentManager().beginTransaction()
			.replace(R.id.container, new CreditsFragment()).commit();
			return true;
		} else if (id == R.id.arrow){
			getFragmentManager().beginTransaction()
			.replace(R.id.container, new MainFragment()).commit();
			return true;
		}else if(id == R.id.menu_logout){
            LoginManager.getInstance().logOut();
            Intent loginIntent = new Intent(getApplication(), LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
        }

		return super.onOptionsItemSelected(item);
	}

	/*
    public void onCategorySelected(String id){
        Log.i("CategoryFragment", "Selected category " + id);

        Bundle b = new Bundle();
        b.putString("category_id", id);

        Fragment f = new QuestionFragment();
        f.setArguments(b);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, f).commit();

    }
    */


    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

	public String getUserName() { return this.userName; }

	public void setUserName(String userName) {this.userName = userName; }

}
