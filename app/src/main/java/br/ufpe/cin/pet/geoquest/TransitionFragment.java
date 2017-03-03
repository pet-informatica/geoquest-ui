package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudinary.Cloudinary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.pet.geoquest.Utils.BitmapFromURL;
import br.ufpe.cin.pet.geoquest.Utils.Cloud;
import br.ufpe.cin.pet.geoquest.classes.Badge;
import br.ufpe.cin.pet.geoquest.classes.Category;
import okhttp3.OkHttpClient;

/**
 * Created by rbb3 on 03/10/16.
 */
public class TransitionFragment extends Fragment {

    private String cat;
    private int lev;
    private int type;
    private Category category;
    private int fim;

    static class ViewHolder {
        protected TextView titulo;
        protected TextView nivel;
        protected ImageButton gofront;
        protected ImageButton goback;
        protected ImageView feedback;
        protected TextView nameBadge;
        protected TextView description;
    }

    public TransitionFragment(Category category, int level, int type) {
        this.category = category;
        this.cat = category.getName();
        this.lev = level;
        this.type = type;
    }

    private final OkHttpClient client = new OkHttpClient();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        if (type == 1) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new QuestionFragment(category, lev)).commit();
        }
        else
        {
            view = inflater.inflate(R.layout.fragment_transition, container, false);
            getData(view);
        }

        return view;
    }

    List<Badge> items = new ArrayList<>();
    private void getData(final View rootView){

        try {
            new AsyncTask<Void, Void, Void>() {


                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        items = run();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void v) {
                    populate(rootView);
                }
            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Badge> run() throws IOException {

        String url = "http://www.mocky.io/v2/58b8e8cb0f0000c503f09b5e";
        //String url = getResources().getString(R.string.base_url)+"badges/transition/";

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .header("Authorization", Config.getKey())
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        List<Badge> items = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(response.body().string());
            int end = obj.getInt("fim");
            fim = end;

            JSONArray badges = obj.getJSONArray("badges");
            int tam = badges.length();
            for (int i = 0; i < tam; i++) {
                JSONObject object = badges.getJSONObject(i);

                String name = object.getString("name");
                String description = object.getString("description");
                String image = object.getString("image");
                String id = object.getString("id");


                final String src = Cloud.cloudinary.url().generate(image);
                Bitmap bm = null;
                bm = new BitmapFromURL(src).getBitmapFromURL();

                items.add(new Badge(id, name, description, bm, true));

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return items;
    }

    void populate(View view) {
        ViewHolder vh = new ViewHolder();

        vh.goback = (ImageButton) view.findViewById(R.id.returnMenoButton);
        vh.gofront = (ImageButton) view.findViewById(R.id.goFowardButton);
        vh.titulo = (TextView) view.findViewById(R.id.trasitionTitle);
        vh.nivel = (TextView) view.findViewById(R.id.transitionLevel);
        vh.feedback = (ImageView) view.findViewById(R.id.feedbackImg);
        vh.nameBadge = (TextView) view.findViewById(R.id.nameBadge);
        vh.description = (TextView) view.findViewById(R.id.description);


        vh.goback.setImageResource(R.drawable.voltar);
        if (fim == 1) {
            vh.gofront.setImageResource(R.drawable.continuar);
        }
        vh.titulo.setText(cat);
        vh.nivel.setText("Nível " + lev);

        if (items.size() > 0) {
            vh.feedback.setImageBitmap(items.get(0).getImage());
            vh.nameBadge.setText(items.get(0).getNome());
            vh.description.setText(items.get(0).getDescricao());
        }

        vh.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new MainFragment());
                ft.addToBackStack("main_fragment");
                ft.commit();
            }
        });

        vh.gofront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fim == 1 && lev < 3) {
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new QuestionFragment(category, lev+1));
                    ft.addToBackStack("question_fragment");
                    ft.commit();
                } else if (fim == 0) {
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new QuestionFragment(category, lev));
                    ft.addToBackStack("question_fragment");
                    ft.commit();
                }
            }
        });
    }
}
