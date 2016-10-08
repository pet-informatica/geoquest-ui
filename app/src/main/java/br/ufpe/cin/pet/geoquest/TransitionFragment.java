package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by rbb3 on 03/10/16.
 */
public class TransitionFragment extends Fragment {

    static class ViewHolder {
        protected TextView txt1;
        protected TextView txt2;
        protected TextView txt3;
        protected TextView titulo;
        protected TextView nivel;
        protected ImageButton gofront;
        protected ImageButton goback;
        protected ProgressBar pb1;
        protected ProgressBar pb2;
        protected ProgressBar pb3;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transition, container, false);

        // make request for data
        // lets assume 17, 44, 77 percent of conclusion and no badges achieved

        populate(view);




        // if we have any badges they will appear as dialogs on screen


        return view;
    }

    void populate(View view) {

        ViewHolder vh = new ViewHolder();
        double v1 = 20, v2 = 77, v3 = 44;


        vh.txt1 = (TextView) view.findViewById(R.id.textView3);
        vh.txt2 = (TextView) view.findViewById(R.id.textView4);
        vh.txt3 = (TextView) view.findViewById(R.id.textView5);
        vh.pb1 = (ProgressBar) view.findViewById(R.id.progressBar);
        vh.pb2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        vh.pb3 = (ProgressBar) view.findViewById(R.id.progressBar3);
        vh.goback = (ImageButton) view.findViewById(R.id.returnMenoButton);
        //setar onclick
        vh.gofront = (ImageButton) view.findViewById(R.id.goFowardButton);
        //setar onclick
        vh.titulo = (TextView) view.findViewById(R.id.trasitionTitle);
        vh.nivel = (TextView) view.findViewById(R.id.transitionLevel);


        vh.txt1.setText(v1+"% de acertos no bloco");
        vh.txt2.setText(v2+"% da categoria concluída");
        vh.txt3.setText(v3+"% do jogo concluído");
        vh.pb1.setProgress((int)v1);
        vh.pb2.setProgress((int)v2);
        vh.pb3.setProgress((int)v3);
        vh.titulo.setText("Hidrografia"); //puxar da activity
        vh.nivel.setText("Nível "+1); //puxar da activity

        // inserir imagem (verm / amar / verde)
    }

}
