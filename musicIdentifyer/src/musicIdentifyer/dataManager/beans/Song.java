package musicIdentifyer.dataManager.beans;

/**
 * Created by Lambda on 2016/10/29.
 */
public class Song {
    private int id;
    private String name;

    public Song() {
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
