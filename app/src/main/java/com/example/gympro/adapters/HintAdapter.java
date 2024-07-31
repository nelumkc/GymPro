package com.example.gympro.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HintAdapter extends ArrayAdapter<String> {

    public HintAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0; // Disable the first item from Spinner
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        if (position == 0) {
            textView.setTextColor(getContext().getColor(android.R.color.darker_gray)); // Set the hint text color
        } else {
            textView.setTextColor(getContext().getColor(android.R.color.black)); // Set the regular text color
        }
        return view;
    }
}
