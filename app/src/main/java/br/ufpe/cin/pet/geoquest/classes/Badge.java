package br.ufpe.cin.pet.geoquest.classes;

import android.graphics.Bitmap;

/**
 * Created by rbb3 on 19/06/16.
 */
public class Badge {

    String nome;
    String descricao;
    String id;
    boolean possui;
    Bitmap image;

    public Badge(String id, String nome, String descript, Bitmap img, boolean possui) {
        this.id = id;
        this.nome = nome;
        this.descricao = descript;
        this.image = img;
        this.possui = possui;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean temBadge() {return possui; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setDescricao(String desc) {
        this.descricao = desc;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setImage(Bitmap img) { this.image = img; }

    public Bitmap getImage() { return this.image; }

    public void setPossui(Boolean pos) { this.possui = pos; }

    public Boolean getPossui() { return this.possui; }
}