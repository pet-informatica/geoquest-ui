package br.ufpe.cin.pet.geoquest.classes;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import br.ufpe.cin.pet.geoquest.R;

/**
 * Created by rbb3 on 04/06/16.
 */
public class Raking {
    String number;
    String name;
    int color;
    Bitmap picture;
    int pontuacao;

    public Raking(String number, String name, Bitmap bm, int points) {
        this.number = number;
        this.name = name;
        this.picture = bm;
        this.pontuacao = points;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor() {
        if (this.number.equals("1")) this.color = Color.rgb(255, 215, 0);
        else if (this.number.equals("2")) this.color = Color.rgb(169, 169, 169);
        else if (this.number.equals( "3")) this.color = Color.rgb(215, 127, 50);
        else this.color = Color.rgb(190, 190, 190);
    }

    public int getColor() {
        return this.color;
    }

    public void setPicture(Bitmap bm) {
        this.picture = bm;
    }

    public Bitmap getPicture() {
        return this.picture;
    }

    public int getPoints() {
        return this.pontuacao;
    }
}
