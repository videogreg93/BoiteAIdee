package gregoryfournier.boiteaidee.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import gregoryfournier.boiteaidee.Data.IdeasManager;
import gregoryfournier.boiteaidee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewIdeaFragment extends Fragment {
    Button addNewIdeaButton;
    EditText NewIdeaEditText;


    public NewIdeaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_idea, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the views
        addNewIdeaButton = (Button) getActivity().findViewById(R.id.bAddNewIdea);
        NewIdeaEditText = (EditText) getActivity().findViewById(R.id.etNewIdea);

        addNewIdeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // add the idea
                String idea = NewIdeaEditText.getText().toString();
                IdeasManager.addIdea(idea);

                // End Activity
                getActivity().onBackPressed();
            }
        });
    }
}
