package br.ufpe.cin.pet.geoquest;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;

public class MainFragment extends Fragment{

	RelativeLayout app_ranking;
	RelativeLayout app_statistics;
	RelativeLayout app_myBadges;

    TextView userName;

	Button btn_play;

    private Profile profile;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        profile = Profile.getCurrentProfile();

		final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		getActivity().getActionBar().setTitle("Geoquest");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);

        userName = (TextView) rootView.findViewById(R.id.nameUserPerfil);
        userName.setText(profile.getName());

        ImageView userImage = (ImageView) rootView.findViewById(R.id.imgUserPerfil);

        new FacebookImageTask(userImage).execute(profile.getProfilePictureUri(150,150).toString());

		app_ranking = (RelativeLayout) rootView.findViewById (R.id.layoutRanking);
		app_ranking.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
				.replace(R.id.container, new RankingFragment()).commit();
			}
		});

		app_statistics = (RelativeLayout) rootView.findViewById (R.id.layoutEstatisticas);
		app_statistics.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//				Intent intent = new Intent(MainActivity.this, TestActivity.class);
				//				startActivity(intent);
			}
		});

		app_myBadges = (RelativeLayout) rootView.findViewById (R.id.layoutMeusBadges);
		app_myBadges.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
				.replace(R.id.container, new MyBadgesFragment()).commit();
			}
		});

		btn_play = (Button) rootView.findViewById (R.id.btnPlay);
		btn_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
				.replace(R.id.container, new QuestionFragment()).commit();
			}
		});

		return rootView;
	}

}
