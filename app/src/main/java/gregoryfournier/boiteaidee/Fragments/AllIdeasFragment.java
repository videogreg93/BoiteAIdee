package gregoryfournier.boiteaidee.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import gregoryfournier.boiteaidee.Data.IdeasManager;
import gregoryfournier.boiteaidee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllIdeasFragment extends Fragment {
    ListView allIdeas;


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

        ArrayAdapter<String> ideaAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, IdeasManager.getAllIdeas());
        allIdeas.setAdapter(ideaAdapter);
        allIdeas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO something here gosh I dont know what
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
