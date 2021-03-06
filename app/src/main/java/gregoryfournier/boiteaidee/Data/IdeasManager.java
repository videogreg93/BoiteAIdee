package gregoryfournier.boiteaidee.Data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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
    private static ArrayList<Idea> allIdeas = new ArrayList<Idea>();
    private static boolean hasBeenInitialized = false;
    private static final String ALL_IDEAS_LOCAL_DB_STRING = "allIdeas";
    //private static final String IDEAS_DB_STRING = "Ideas";
    private static final String IDEAS_DB_STRING = "DevelopmentIdeas";
    private static ListIdeaAdapter adapterForChanges;

    private IdeasManager() {

    }

    public static void Init() {
        allIdeas = new ArrayList<Idea>();
        Log.d("Paper", "Loaded " + allIdeas.size() + " ideas from storage");
        setupRealtimeIdeasDatabase();
    }

    /**
     * Uploads an idea to the database. Database callbacks will ensure that
     * the new data is reflected locally
     * @param newIdea Idea to be added
     * @param activity The calling activity. This is to show a toast upon success
     */
    public static void addIdea(Idea newIdea, final Activity activity) {
        if (hasBeenInitialized) {
            // Upload idea to firebase. The callback will add it locally
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(IDEAS_DB_STRING);
            myRef.push().setValue(newIdea).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            else {
            Log.w("firebase", "Trying to add idea when DB has not been initialized");
        }
    }

    /**
     * Remove idea from the database. Database callback will
     * ensure that the changes are reflected locally.
     * @param idea Idea to be removed
     * @param activity The calling activity. This is to show a toast upon success
     */
    public static void removeIdea(final Idea idea, final Activity activity) {
        if (hasBeenInitialized && !allIdeas.isEmpty()) {

            // TODO Actually implement removal in a non hacky way
        } else {
            Log.w("Paper", "Trying to remove idea when DB has not been initialized or is empty");
        }
    }


    /**
     * Uploads all ideas to a backup database
     * @param activity The calling activity. This is to show a toast upon success
     */
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

    /**
     * Get a random idea. User story.
     * @return The random idea
     */
    public static Idea getRandomIdea(int indexFilter) {
        if (hasBeenInitialized && !allIdeas.isEmpty()) {
            Random random = new Random();
            ArrayList<Idea> possibleIdeas = new ArrayList<>();
            if (indexFilter == 0) {
                possibleIdeas = allIdeas;
            } else {
                Idea.CATEGORY category = Idea.CATEGORY.values()[indexFilter-1];
                for (Idea i : allIdeas) {
                    if (i.getCategory().equals(category))
                        possibleIdeas.add(i);
                }
            }
            if (possibleIdeas.isEmpty())
                return null;
            return possibleIdeas.get(random.nextInt(possibleIdeas.size()));
        } else {
            Log.d("firebase", "Trying to get idea when DB has not been initialized or is empty");
            return new Idea();
        }
    }

    public static ArrayList<Idea> getAllIdeas() {
        return allIdeas;
    }

    /**
     * Adds the callbacks for data changes. Handles data being added, removed and changed.
     */
    private static void setupRealtimeIdeasDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(IDEAS_DB_STRING);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                allIdeas.add(dataSnapshot.getValue(Idea.class));
                Log.d("firebase", "new idea incoming");
                if (adapterForChanges != null) {
                    adapterForChanges.notifyDataSetChanged();
                    Log.d("adapter", "NotifyingDataSetChanged");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Idea removedIdea = dataSnapshot.getValue(Idea.class);
                allIdeas.remove(removedIdea);
                Log.d("firebase", "removed '" + removedIdea + "' idea");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        hasBeenInitialized = true;
    }

    /**
     * Must be called so that the listView in the allIdea view can be
     * made aware of data changes
     * @param adapterForChanges adapter that handles data in the listview
     */
    public static void setAdapterForChanges(ListIdeaAdapter adapterForChanges) {
        IdeasManager.adapterForChanges = adapterForChanges;
    }
}
