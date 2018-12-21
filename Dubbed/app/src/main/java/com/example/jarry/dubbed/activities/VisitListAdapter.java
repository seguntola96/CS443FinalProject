package com.example.jarry.dubbed.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jarry.dubbed.R;
import com.example.jarry.dubbed.model.PetVisits;

import java.util.List;

public class VisitListAdapter extends ArrayAdapter<PetVisits> {
    public static final String DEBUG_TAG = "VisitListAdapter: ";
    //the list values in the List of type visit
    List<PetVisits> visitList;
    Context context;


    //constructor initializing the values 
    public VisitListAdapter(Context context, List<PetVisits> visits) {
        super(context, R.layout.custom_list_view, visits);
        this.visitList = visits;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        //we need to get the view of the xml for our list item
        //And for this we need a layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        /* getting the view */
        View view = layoutInflater.inflate(R.layout.custom_visit_view, parent, false);

        //getting the view elements of the list from the view
        PetVisits visit = getItem(position);
        Log.d(DEBUG_TAG, visit.toString());
        TextView textViewReason = view.findViewById(R.id.custom_reason4visit);
        TextView textViewNotes = view.findViewById(R.id.custom_visit_notes);


        textViewReason.setText(visit.getCause_of_visit());
        textViewNotes.setText(visit.getDate_of_visit());


        return view;
    }

    @Override
    public PetVisits getItem(int i) {
        return visitList.get(i);
    }
}