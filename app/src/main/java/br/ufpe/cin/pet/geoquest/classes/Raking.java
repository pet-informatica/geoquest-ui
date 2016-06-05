package br.ufpe.cin.pet.geoquest.classes;

import br.ufpe.cin.pet.geoquest.R;

/**
 * Created by rbb3 on 04/06/16.
 */
public class Raking {
    String number;
    String name;
    int color;

    public Raking(String number, String name) {
        this.number = number;
        this.name = name;
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
        if (this.number == "1") this.color = R.color.gold;
        else if (this.number == "2") this.color = R.color.silver;
        else if (this.number == "3") this.color = R.color.bronze;
        else this.color = R.color.grey;
    }

    public int getColor() {
        return this.color;
    }
}