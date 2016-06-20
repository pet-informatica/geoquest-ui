package br.ufpe.cin.pet.geoquest.classes;

/**
 * Created by rbb3 on 19/06/16.
 */
public class Badge {

    String nome;
    String descricao;
    int id;

    public Badge(int id, String nome, String descript) {
        this.id = id;
        this.nome = nome;
        this.descricao = descript;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

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
}
