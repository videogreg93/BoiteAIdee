package gregoryfournier.boiteaidee.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import gregoryfournier.boiteaidee.Data.Idea;
import gregoryfournier.boiteaidee.Data.IdeasManager;
import gregoryfournier.boiteaidee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    Button mainButton;
    Spinner filterSpinner;
    int indexFilter = 0; // 0 = all


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the views
        mainButton = (Button) getActivity().findViewById(R.id.bmainButton);
        filterSpinner = (Spinner) getActivity().findViewById(R.id.main_fragment_spinner);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Idea idea = IdeasManager.getRandomIdea(indexFilter);
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // 2. Chain together various setter methods to set the dialog characteristics
                if (idea == null) {
                    builder.setMessage("Il n'éxiste pas d'activité dans la catégorie " + filterSpinner.getSelectedItem().toString())
                            .setTitle("Erreur")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                } else {
                    builder.setMessage(idea.getIdea())
                            .setTitle("Votre activité!")
                            .setPositiveButton("Parfait", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Retirer l'idée de la liste", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            IdeasManager.removeIdea(idea, getActivity());
                        }
                    });
                }


                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Populate the filter spinner
        final String[] spinnerValues = new String[Idea.CATEGORY.values().length + 1];
        spinnerValues[0] = "ALL";
        for (int i = 0; i < Idea.CATEGORY.values().length; i++) {
            spinnerValues[i+1] = Idea.CATEGORY.values()[i].toString();
        }
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                spinnerValues
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        // Set on spinner change listener
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexFilter = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                indexFilter = 0;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MainFragment:OnResume", "Resumed");

        try {
            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            //woops
        }
    }
}
