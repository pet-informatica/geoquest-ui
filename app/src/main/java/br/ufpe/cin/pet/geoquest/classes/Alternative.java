package br.ufpe.cin.pet.geoquest.classes;

/**
 * Created by rbb3 on 12/01/17.
 */
public class Alternative {

    private String text;
    private String sequence;

    public Alternative(String text, String sequence) {
        this.text = text;
        this.sequence = sequence;
    }

    public void setSequence(String sequence) { this.sequence = sequence; }

    public String getSequence() { return this.sequence; }

    public void setText(String text) { this.text = text; }

    public String getText() { return this.text; }

}
