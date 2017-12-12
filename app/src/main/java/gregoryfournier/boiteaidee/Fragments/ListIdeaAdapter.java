package gregoryfournier.boiteaidee.Fragments;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import gregoryfournier.boiteaidee.R;

/**
 * Created by gregoryfournier on 2017-12-12.
 */

public class ListIdeaAdapter extends ArrayAdapter<String> {
    boolean showCheckboxes = false;

    // for checkboxes
    HashMap<Integer,Boolean> isChecked = new HashMap<>();

    public ListIdeaAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // return super.getView(position, convertView, parent);


        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_adapter_layout, parent, false);


        // Get the item
        String idea = getItem(position);

        // Get the views
        TextView textView = (TextView) convertView.findViewById(R.id.itemAdapterIdeaText);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.itemAdapterCheckbox);

        textView.setText(idea);
        if (showCheckboxes)
            checkBox.setVisibility(View.VISIBLE);
        else
            checkBox.setVisibility(View.INVISIBLE);

        if (this.isChecked.containsKey(position))
            checkBox.setChecked(this.isChecked.get(position));
        else
            checkBox.setChecked(false);

        return convertView;

    }

    public void addItemIsChecked(int index, boolean isChecked) {
        this.isChecked.put(index, isChecked);
    }
}
