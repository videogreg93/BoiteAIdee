package gregoryfournier.boiteaidee.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.Dictionary;
import java.util.HashMap;

import gregoryfournier.boiteaidee.Data.IdeasManager;
import gregoryfournier.boiteaidee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllIdeasFragment extends Fragment {
    // Views
    ListView allIdeas;
    Button bDeleteItems;




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

        // Get the Views
        allIdeas = (ListView) getActivity().findViewById(R.id.lvAllIdeas);
        bDeleteItems = (Button) getActivity().findViewById(R.id.bAllItemsDelete);

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
        final ListIdeaAdapter ideaAdapter = new ListIdeaAdapter(getContext(),R.layout.list_item_adapter_layout, IdeasManager.getAllIdeas());
        allIdeas.setAdapter(ideaAdapter);
        allIdeas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //((ListIdeaAdapter)allIdeas.getAdapter()).showCheckboxes = true;
                //ideaAdapter.notifyDataSetChanged();
                //bDeleteItems.setVisibility(View.VISIBLE);
                // TODO this is hack just to be bale to delete items
                String idea = (String) ideaAdapter.getItem(position);
                Log.d("allItemsView","removing " + idea);
                IdeasManager.removeIdea(idea, getActivity());
                allIdeas.removeView(view);
                return false;
            }
        });

        // make it so the list updates when new ideas come in
        IdeasManager.setAdapterForChanges(ideaAdapter);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        IdeasManager.setAdapterForChanges(null);
    }
}
