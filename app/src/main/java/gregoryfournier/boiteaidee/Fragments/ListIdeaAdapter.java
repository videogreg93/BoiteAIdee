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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gregoryfournier.boiteaidee.Data.Idea;
import gregoryfournier.boiteaidee.R;

/**
 * Created by gregoryfournier on 2017-12-12.
 */

public class ListIdeaAdapter extends ArrayAdapter<Idea> implements Filterable {
    boolean showCheckboxes = false;
    private List<Idea> filteredData = null;
    private List<Idea> originalData = null;
    private IdeaFilter filter = new IdeaFilter();

    // for checkboxes
    HashMap<Integer,Boolean> isChecked = new HashMap<>();

    public ListIdeaAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Idea> objects) {
        super(context, resource, objects);
        originalData = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // return super.getView(position, convertView, parent);


        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_adapter_layout, parent, false);


        // Get the item
        Idea idea = getItem(position);

        // Get the views
        TextView textView = (TextView) convertView.findViewById(R.id.itemAdapterIdeaText);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.itemAdapterCheckbox);

        textView.setText(idea.getIdea());
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

    private class IdeaFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final List<Idea> list = originalData;
            List<Idea> newList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint.equals("ALL")) {
                results.values = list;
                results.count = list.size();
                return results;
            }


            for (Idea idea : list) {
                if (idea.getCategory().toString().equals(constraint)) {
                    newList.add(idea);
                }
            }


            results.values = newList;
            results.count = newList.size();

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Idea>) results.values;
            notifyDataSetChanged();
        }
    }
}
