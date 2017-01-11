package br.ufpe.cin.pet.geoquest.classes;

/**
 * Created by Tomer Simis on 19/04/2015.
 */
public class Category {

    private String name;

    private String description;

    private int done;

    private int total;

    private int id;

    private int max_level;

    public Category(String name, String description, int done, int total, int id, int level){
        this.name = name;
        this.description = description;
        this.done = done;
        this.total = total;
        this.id = id;
        this.max_level = level;
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

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public boolean is_available (int level) {
        return (level <= this.max_level);
    }
}
