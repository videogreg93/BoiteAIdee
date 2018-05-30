package gregoryfournier.boiteaidee;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import gregoryfournier.boiteaidee.Data.IdeasManager;
import gregoryfournier.boiteaidee.Fragments.AllIdeasFragment;
import gregoryfournier.boiteaidee.Fragments.MainFragment;
import gregoryfournier.boiteaidee.Fragments.NewIdeaFragment;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton fab;

    int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init PaperDB
        Paper.init(getApplicationContext());
        // Load ideas
        IdeasManager.Init();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNewIdeaFragment();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup Authentification
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.OAuthWebKey))
                .requestEmail()
                .build();

        // Sign in with Google
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("firebase", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebase", "firebaseAuthWithGoogle:" + acct.getId());
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("firebase", "signInWithCredential:success");

                            // Load Main Content
                            goToMainFragment();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("firebase", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void goToMainFragment() {
        MainFragment fragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    private void goToNewIdeaFragment() {
        fab.setVisibility(View.INVISIBLE);
        NewIdeaFragment fragment = new NewIdeaFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, "newIdea");
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    private void goToIdeaListFragment(boolean onlyUserIdeas) {
        AllIdeasFragment fragment = new AllIdeasFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(AllIdeasFragment.ONLY_USER_IDEAS, onlyUserIdeas);
        fragment.setArguments(bundle);
        fab.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final NewIdeaFragment myFragment = (NewIdeaFragment) getSupportFragmentManager().findFragmentByTag("newIdea");
            if (myFragment != null && myFragment.isVisible()) {
                if (!myFragment.getNewIdeaEditText().getText().toString().isEmpty()) {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Are you sure you want to stop adding a new idea?")
                            .setTitle("Abandoning Idea :(");

                    builder.setPositiveButton("Fuck this idea", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            myFragment.getNewIdeaEditText().getText().clear();
                            onBackPressed();
                        }
                    });
                    builder.setNegativeButton("Nah, I like it finally", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    public void superBackPress() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.drawer_all_idea_list:
                goToIdeaListFragment(false);
                break;
            case R.id.drawer_my_idea_list:
                goToIdeaListFragment(true);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity:OnResume", "Resumed");
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
        }
    }
}
