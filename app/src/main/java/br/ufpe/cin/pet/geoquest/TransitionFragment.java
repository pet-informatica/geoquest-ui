package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.pet.geoquest.Utils.BitmapFromURL;
import br.ufpe.cin.pet.geoquest.Utils.Cloud;
import br.ufpe.cin.pet.geoquest.Utils.ProgressGenerator;
import br.ufpe.cin.pet.geoquest.classes.Badge;
import br.ufpe.cin.pet.geoquest.classes.Category;
import okhttp3.OkHttpClient;

/**
 * Created by rbb3 on 03/10/16.
 */
public class TransitionFragment extends Fragment {

    private int lev;
    private int type;
    private int right;
    private int size;
    private Category category;
    private int fim;

    static class ViewHolder {
        protected TextView titulo;
        protected TextView nivel;
        protected ImageView appBack;
    }

    public TransitionFragment(Category category, int level, int right, int size, int type) {
        this.category = category;
        this.lev = level;
        this.type = type;
        this.right = right;
        this.size = size;
    }

    private final OkHttpClient client = new OkHttpClient();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().hide();
        if (type == 1) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new QuestionFragment(category, lev)).commit();
        }
        else
        {
            view = inflater.inflate(R.layout.fragment_transition, container, false);
            populate(view);
            //getData(view);
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

        //String url = "http://www.mocky.io/v2/58b8e8cb0f0000c503f09b5e";

        String categoryId = "category=" + this.category.getName() + "&level=" + lev;
        String url = getResources().getString(R.string.base_url)+"badges/transition?"+categoryId;

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
    ViewHolder vh;
    void populate(View view) {
        vh = new ViewHolder();

        vh.titulo = (TextView) view.findViewById(R.id.trasitionTitle);
        vh.nivel = (TextView) view.findViewById(R.id.questions);
        vh.appBack = (ImageView) view.findViewById(R.id.app_back);
        final LinearProgressButton button = (LinearProgressButton) view.findViewById(R.id.next);
        int progressColor = getResources().getColor(R.color.mb_purple);
        int color = getResources().getColor(R.color.mb_gray);
        int progressCornerRadius = (int) getResources().getDimension(R.dimen.mb_corner_radius_4);
        int width = (int) getResources().getDimension(R.dimen.mb_width_200);
        int height = (int) getResources().getDimension(R.dimen.mb_height_8);

        button.blockTouch(); // prevent user from clicking while button is in progress
        ProgressGenerator generator = new ProgressGenerator(new ProgressGenerator.OnCompleteListener() {
            @Override
            public void onComplete() {
                button.unblockTouch();
                morphToSuccess(button);
            }
        });
        button.morphToProgress(color, progressColor, progressCornerRadius, width, height, 0);
        double right = this.right;
        double size = this.size;
        generator.start(button, (int)((right / size) * 100));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new CategoryFragment());
                    ft.addToBackStack("category_fragment");
                    ft.commit();
            }
        });

        vh.titulo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cooper-black.ttf"));
        vh.titulo.setText(this.category.getName());
        vh.nivel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Calibri.ttf"));
        vh.nivel.setText(this.right + "/" + this.size + " acertos");


        vh.appBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new MainFragment());
                ft.addToBackStack("main_fragment");
                ft.commit();
            }
        });
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(getResources().getInteger(R.integer.mb_animation))
                .cornerRadius((int) getResources().getDimension(R.dimen.mb_height_56))
                .width((int) getResources().getDimension(R.dimen.mb_height_56))
                .height((int) getResources().getDimension(R.dimen.mb_height_56))
                .color(getResources().getColor(R.color.mb_green))
                .colorPressed(getResources().getColor(R.color.mb_green_dark))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }
}
