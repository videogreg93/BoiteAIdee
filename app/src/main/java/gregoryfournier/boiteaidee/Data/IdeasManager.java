package gregoryfournier.boiteaidee.Data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import io.paperdb.Paper;

/**
 * Created by Gregory on 9/26/2017.
 */

public class IdeasManager {
    private static ArrayList<String> allIdeas;
    private static boolean hasBeenInitialized = false;
    private static final String ALL_IDEAS_DB_STRING = "allIdeas";

    private IdeasManager() {

    }

    public static void Init() {
        //bdeleteAllIdeas();
        allIdeas = Paper.book().read(ALL_IDEAS_DB_STRING, new ArrayList<String>());
        Log.d("Paper", "Loaded " + allIdeas.size() + " ideas from storage");
        hasBeenInitialized = true;
    }

    public static void addIdea(String newIdea) {
        if (hasBeenInitialized) {
            allIdeas.add(newIdea);
            Log.d("Paper", "Added " + newIdea + " to local ideas");
            saveAllIdeas();
        }
            else {
            Log.w("Paper", "Trying to add idea when DB has not been initialized");
        }
    }

    public static void removeIdea(String idea) {
        if (hasBeenInitialized && !allIdeas.isEmpty()) {
            allIdeas.remove(idea);
            Log.d("Paper", "Removed " + idea + " from local ideas");
            saveAllIdeas();
        } else {
            Log.w("Paper", "Trying to remove idea when DB has not been initialized or is empty");
        }
    }

    public static void saveAllIdeas() {
        Paper.book().write(ALL_IDEAS_DB_STRING, allIdeas);
        Log.d("Paper", "Saved " + allIdeas.size() + " ideas to storage");
    }

    public static void deleteAllIdeas() {
        Paper.book().write(ALL_IDEAS_DB_STRING, new ArrayList<String>());
        Log.d("Paper", "Deleted all ideas from local storage");
    }

    public static String getRandomIdea() {
        if (hasBeenInitialized && !allIdeas.isEmpty()) {
            Random random = new Random();
            return allIdeas.get(random.nextInt(allIdeas.size()));
        } else {
            Log.d("Paper", "Trying to get idea when DB has not been initialized or is empty");
            return " ";
        }
    }

    public static ArrayList<String> getAllIdeas() {
        return allIdeas;
    }
}
