package gregoryfournier.boiteaidee.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;

import gregoryfournier.boiteaidee.Data.Idea;
import gregoryfournier.boiteaidee.Data.IdeasManager;
import gregoryfournier.boiteaidee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllIdeasFragment extends Fragment {
    public static final String ONLY_USER_IDEAS = "ONLY_USER_IDEAS" ;
    // Views
    ListView allIdeas;
    Button bDeleteItems;
    Spinner filterSpinner;

    boolean showOnlyUserIdeas = false;




    public AllIdeasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_ideas, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get bundle data
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            showOnlyUserIdeas = bundle.getBoolean(ONLY_USER_IDEAS, false);
        }

        // Get the Views
        allIdeas = (ListView) getActivity().findViewById(R.id.lvAllIdeas);
        bDeleteItems = (Button) getActivity().findViewById(R.id.bAllItemsDelete);
        filterSpinner = (Spinner) getActivity().findViewById(R.id.all_ideas_filter_spinner);

        // Setup delete button
        bDeleteItems.setVisibility(View.INVISIBLE);
        bDeleteItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get all checked items and delete them
                for (int i = 0; i < allIdeas.getChildCount(); i++ ) {
                    View itemView = allIdeas.getChildAt(i);
                    CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.itemAdapterCheckbox);
                    if (checkBox.isChecked()) {
                        String idea = (String) allIdeas.getAdapter().getItem(i);
                        Log.d("allItemsView","removing " + idea);
                        //IdeasManager.removeIdea(idea, getActivity());
                    }
                }

            }
        });

       // ArrayAdapter<String> ideaAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, IdeasManager.getAllIdeas());
        final ListIdeaAdapter ideaAdapter = new ListIdeaAdapter(getContext(),R.layout.list_item_adapter_layout, IdeasManager.getAllIdeas(), allIdeas, showOnlyUserIdeas);
        allIdeas.setAdapter(ideaAdapter);
        allIdeas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO this is hack just to be able to delete items
                Idea idea = (Idea) ideaAdapter.getItem(position);
                Log.d("allItemsView","removing " + idea);
                IdeasManager.removeIdea(idea, getActivity());
                //allIdeas.removeView(view);
                return false;
            }
        });

        // make it so the list updates when new ideas come in
        IdeasManager.setAdapterForChanges(ideaAdapter);

        // Populate filter spinner
        // Populate the spinner with the categories
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

        // Filter listview when filterSpinner changes value
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = spinnerValues[position];
                ideaAdapter.getFilter().filter(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ideaAdapter.getFilter().filter("ALL");
            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        IdeasManager.setAdapterForChanges(null);
    }
}
