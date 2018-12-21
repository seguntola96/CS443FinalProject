package com.example.jarry.dubbed.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jarry.dubbed.R;
import com.example.jarry.dubbed.model.Pets;

import java.io.File;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Pets> {
    public static final String DEBUG_TAG = "ListAdapter: ";
    //the list values in the List of type pet
    List<Pets> petList;
    Context context;


    //constructor initializing the values 
    public ListAdapter(Context context, List<Pets> petList) {
        super(context, R.layout.custom_list_view, petList);
        this.petList = petList;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        //we need to get the view of the xml for our list item
        //And for this we need a layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        /* getting the view */
        View view = layoutInflater.inflate(R.layout.custom_list_view, parent, false);

        //getting the view elements of the list from the view
        Pets pet = getItem(position);
        Log.d(DEBUG_TAG, pet.toString());
        ImageView imageView = view.findViewById(R.id.custom_list_image);
        TextView textViewName = view.findViewById(R.id.custom_list_name);
        TextView textViewAge = view.findViewById(R.id.custom_list_age);
        TextView textViewOwner = view.findViewById(R.id.custom_list_owner);
        TextView textViewLastVisit = view.findViewById(R.id.custom_list_last_visit);

        //getting the hero of the specified position
        String filename = null;
        String[] split = pet.getImagePath().split("\\.");
        if (split.length > 0) {
            //filename = split[0];
            //Log.d(DEBUG_TAG, "getView:................ found.... " + filename);
            //final int resourceId = context.getResources().getIdentifier(filename, "drawable", context.getPackageName());
            File imgFile = new  File( pet.getImagePath());
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
            textViewName.setText(pet.getName());
            textViewAge.setText(Integer.toString(pet.getPetAge()));
            textViewOwner.setText(pet.getOwner_name());
            textViewLastVisit.setText(pet.getLastVisit());
        }else {
            Log.d(DEBUG_TAG, "getView:................not found");
        }
        return view;
    }

    @Override
    public Pets getItem(int i) {
        return petList.get(i);
    }
}