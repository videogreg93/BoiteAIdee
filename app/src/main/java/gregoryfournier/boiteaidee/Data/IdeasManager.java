package gregoryfournier.boiteaidee.Data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import gregoryfournier.boiteaidee.Fragments.ListIdeaAdapter;
import io.paperdb.Paper;

/**
 * Created by Gregory on 9/26/2017.
 */

public class IdeasManager {
    private static ArrayList<String> allIdeas;
    private static boolean hasBeenInitialized = false;
    private static final String ALL_IDEAS_LOCAL_DB_STRING = "allIdeas";
    private static final String IDEAS_DB_STRING = "Ideas";
    private static ListIdeaAdapter adapterForChanges;

    private IdeasManager() {

    }

    public static void Init() {
        //deleteAllIdeas();
        allIdeas = Paper.book().read(ALL_IDEAS_LOCAL_DB_STRING, new ArrayList<String>());
        Log.d("Paper", "Loaded " + allIdeas.size() + " ideas from storage");
        // Upload to database
        //uploadAllIdeas();
        setupRealtimeIdeasDatabase();
        hasBeenInitialized = true;
    }

    public static void addIdea(String newIdea, final Activity activity) {
        if (hasBeenInitialized) {
            allIdeas.add(newIdea);
            Log.d("Paper", "Added " + newIdea + " to local ideas");
            saveAllIdeas(activity);
        }
            else {
            Log.w("Paper", "Trying to add idea when DB has not been initialized");
        }
    }

    public static void removeIdea(String idea, final Activity activity) {
        if (hasBeenInitialized && !allIdeas.isEmpty()) {
            allIdeas.remove(idea);
            Log.d("Paper", "Removed " + idea + " from local ideas");
            saveAllIdeas(activity); // TODO change upload message to removed message
        } else {
            Log.w("Paper", "Trying to remove idea when DB has not been initialized or is empty");
        }
    }

    public static void saveAllIdeas(final Activity activity) {
        Paper.book().write(ALL_IDEAS_LOCAL_DB_STRING, allIdeas);
        uploadAllIdeas(activity);
        Log.d("Paper", "Saved " + allIdeas.size() + " ideas to storage");
    }

    public static void uploadAllIdeas(final Activity activity) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(IDEAS_DB_STRING);

        myRef.setValue(allIdeas).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(activity, "Upload successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Could not backup ideas: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void uploadallIdeasToBackup(final Activity activity) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(IDEAS_DB_STRING + "BACKUP");

        myRef.setValue(allIdeas).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(activity, "Upload successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Could not backup ideas: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void deleteAllIdeas() {
        Paper.book().write(ALL_IDEAS_LOCAL_DB_STRING, new ArrayList<String>());
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

    public static void setupRealtimeIdeasDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(IDEAS_DB_STRING);

        // Read from the database
        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                allIdeas = dataSnapshot.getValue(t);
                if (adapterForChanges != null) {
                    adapterForChanges.notifyDataSetChanged();
                    Log.d("adapter", "NotifyingDataSetChanged");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public static void setAdapterForChanges(ListIdeaAdapter adapterForChanges) {
        IdeasManager.adapterForChanges = adapterForChanges;
    }
}
