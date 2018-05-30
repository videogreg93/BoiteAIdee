package gregoryfournier.boiteaidee.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.auth.api.signin.GoogleSignIn;

import gregoryfournier.boiteaidee.Data.Idea;
import gregoryfournier.boiteaidee.Data.IdeasManager;
import gregoryfournier.boiteaidee.MainActivity;
import gregoryfournier.boiteaidee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewIdeaFragment extends Fragment {
    Button addNewIdeaButton;
    EditText NewIdeaEditText;
    Spinner categorySpinner;


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
        categorySpinner = (Spinner) getActivity().findViewById(R.id.spNewIdeaCategory);

        addNewIdeaButton.setEnabled(false);

        // add an editText listener to assure the idea is valid

        NewIdeaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addNewIdeaButton.setEnabled(!s.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        addNewIdeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // add the idea
                String message = NewIdeaEditText.getText().toString();
                String author = "N/A";
                try {
                    author = GoogleSignIn.getLastSignedInAccount(getActivity()).getDisplayName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Idea.CATEGORY category = (Idea.CATEGORY) categorySpinner.getSelectedItem();
                Idea idea = new Idea(message,author,category);

                IdeasManager.addIdea(idea, getActivity());

                // End Activity
                ((MainActivity) getActivity()).superBackPress();
            }
        });

        // Populate the spinner with the categories
        ArrayAdapter<Idea.CATEGORY> spinnerAdapter = new ArrayAdapter<Idea.CATEGORY>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                Idea.CATEGORY.values()
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

    }


    public EditText getNewIdeaEditText() {
        return NewIdeaEditText;
    }


}
