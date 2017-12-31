package gregoryfournier.boiteaidee.Data;

import java.util.ArrayList;

/**
 * Created by gregoryfournier on 2017-12-31.
 */

public class Idea {
    String idea;
    String author;
    CATEGORY category;

    public Idea(String idea, String author, CATEGORY category) {
        this.idea = idea;
        this.author = author;
        this.category = category;
    }





    public enum CATEGORY {
        NONE,
        FOOD,
        ACTIVITY
    }
}
