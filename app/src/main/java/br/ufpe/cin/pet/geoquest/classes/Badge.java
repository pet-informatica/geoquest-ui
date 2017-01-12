package br.ufpe.cin.pet.geoquest.classes;

import android.graphics.Bitmap;

/**
 * Created by rbb3 on 19/06/16.
 */
public class Badge {

    private String nome;
    private String descricao;
    private int id;
    private boolean possui;
    private Bitmap image;
    private String str;

    public Badge(int id, String nome, String descript, Bitmap img, boolean possui, String str) {
        this.id = id;
        this.nome = nome;
        this.descricao = descript;
        this.image = img;
        this.possui = possui;
        this.str = str;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public boolean temBadge() {return this.possui; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void setDescricao(String desc) {
        this.descricao = desc;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setImage(Bitmap img) { this.image = img; }

    public Bitmap getImage() { return this.image; }

    public void setPossui(Boolean pos) { this.possui = pos; }

    public Boolean getPossui() { return this.possui; }

    public void setStr(String str) {
        this.str = str;
    }

    public String getStr() {
        return this.str;
    }
}
