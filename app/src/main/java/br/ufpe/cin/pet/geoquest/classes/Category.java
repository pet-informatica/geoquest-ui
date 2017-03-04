package br.ufpe.cin.pet.geoquest.classes;

/**
 * Created by Tomer Simis on 19/04/2015.
 */
public class Category {

    private String name;

    private String description;

    private int done;

    private int total;

    private int max_level;

    private int min_level;

    public Category(String name, String description, int done, int total, int min_level, int max_level){
        this.name = name;
        this.description = description;
        this.done = done;
        this.total = total;
        this.max_level = max_level;
        this.min_level = min_level;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDone() { return done;}

    public void setDone(int done) {
        this.done = done;
    }

    public int getTotal() {return total;}

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean is_unavailable (int level) {
        return level > this.max_level;
    }

    public boolean is_completed (int level) {
        return level < min_level;
    }

    public void setMin_level(int level) {
        this.min_level = level;
    }
}
