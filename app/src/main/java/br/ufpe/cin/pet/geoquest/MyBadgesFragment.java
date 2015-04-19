package br.ufpe.cin.pet.geoquest;

import java.util.Vector;

import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyBadgesFragment extends Fragment {
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView = inflater.inflate(R.layout.fragment_badges, container, false);
		
		String name = "Maria Gabriela";
		getActivity().getActionBar().setTitle(name);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
        Vector<String> badges = new Vector<String>();
        badges.add("caseiro");
        badges.add("flash");
        badges.add("lerdo");
        badges.add("dev");
             
        
        for (int i = 0; i < badges.size(); ++i){
        	final ImageView img = (ImageView) rootView.findViewById(getIdBadge(badges.get(i)));
        	img.setAlpha(1f);
        	final String desc = getBadgeDesc(badges.get(i));
        	
        	img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            		dialog.setMessage(desc);
            		dialog.show();
                }
            });  
        }     
        
        return rootView;
    }
	
	public int getIdBadge(String x){
		if (x.equals("caseiro")) return R.id.badge1;
		if (x.equals("dev")) return R.id.badge2;
		if (x.equals("badge3")) return R.id.badge3;
		if (x.equals("badge4")) return R.id.badge4;
		if (x.equals("flash")) return R.id.badge5;
		if (x.equals("badge6")) return R.id.badge6;
		if (x.equals("badge7")) return R.id.badge7;
		if (x.equals("lerdo")) return R.id.badge8;
		if (x.equals("badge9")) return R.id.badge9;
		return 0;
	}
	
	public static int getIdBadgeImg(String badge){
		if (badge.equals("caseiro")) return R.drawable.badge_caseiro;
		if (badge.equals("flash")) return R.drawable.badge_flash;
		if (badge.equals("lerdo")) return R.drawable.badge_lerdo;
		if (badge.equals("dev")) return R.drawable.badge_dev;
		return 0;
	}
	
	public static String getNomeBadge(String badge){
		if (badge.equals("caseiro")) return "\"Caseiro\"";
		if (badge.equals("flash")) return "\"The Flash\"";
		if (badge.equals("lerdo")) return "\"Lerdo\"";
		if (badge.equals("dev")) return "\"Dev\"";
		return "";
	}
	
	
	public String getBadgeDesc(String badge){
		if (badge.equals("caseiro")) return "Caseiro: participou de uma sa�da onde n�o houve sugest�es.";
		if (badge.equals("flash")) return "Flash: clicou rapidamente.";
		if (badge.equals("lerdo")) return "Lerdo: clicou lentamente.";
		if (badge.equals("dev")) return "Desenvolvedor: participou do desenvolvimento do Geoquest.";	
		return "";
	}
	
}