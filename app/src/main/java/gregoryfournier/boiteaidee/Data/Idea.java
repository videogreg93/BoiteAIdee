package gregoryfournier.boiteaidee.Data;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by gregoryfournier on 2017-12-31.
 */

public class Idea {
    String idea;
    String author;
    CATEGORY category;

    public Idea() {
        idea = "Empty Idea";
        author = "N/A";
        category = CATEGORY.NONE;
    }

    public Idea(String idea, String author, CATEGORY category) {
        this.idea = idea;
        this.author = author;
        this.category = category;
    }

    // GETTERS AND SETTERS

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public void setCategory(CATEGORY category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return idea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Idea idea1 = (Idea) o;
        return Objects.equals(idea, idea1.idea) &&
                Objects.equals(author, idea1.author) &&
                category == idea1.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idea, author, category);
    }

    public enum CATEGORY {
        NONE,
        FOOD,
        ACTIVITY
    }
}
