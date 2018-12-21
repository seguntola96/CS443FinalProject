package com.example.jarry.dubbed.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jarry.dubbed.R;
import com.example.jarry.dubbed.model.PetVisits;
import com.example.jarry.dubbed.model.Pets;
import com.example.jarry.dubbed.sql.DatabaseHelper;

import java.io.File;
import java.util.List;

public class ProfileActivity extends AppCompatActivity{
    private final AppCompatActivity activity = ProfileActivity.this;
    public static final String DEBUG_TAG = "ProfileActivity:";

    Pets pet;
    List<PetVisits> visitList;
    ListView listView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);

        db = new DatabaseHelper(activity);
        Intent intent = getIntent();
        int pet_id = intent.getIntExtra("PET", 0);
        visitList = db.getPetVisits(String.valueOf(pet_id));

        pet = db.getPet(String.valueOf(pet_id));

        TextView textViewName = (TextView) findViewById(R.id.profile_view_name);
        TextView textViewAge = (TextView) findViewById(R.id.profile_view_age);
        TextView textViewBreed = (TextView) findViewById(R.id.profile_view_breed);
        TextView textViewowner = (TextView) findViewById(R.id.profile_view_ownername);
        ImageView imageView = (ImageView) findViewById(R.id.profile_view_image);
        listView = findViewById(R.id.profile_visit_list);
        Button newVisit = findViewById(R.id.profile_view_new_visit);

        newVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddVisitsActivity.class);
                intent.putExtra("PET", pet.getPet_id());
                startActivity(intent);
            }
        });


        File imgFile = new  File( pet.getImagePath());
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imageView.setImageBitmap(myBitmap);
        textViewName.setText(pet.getName());
        textViewAge.setText(Integer.toString(pet.getPetAge()));
        textViewBreed.setText(pet.getBreed());
        textViewowner.setText(pet.getOwner_name());

        if(visitList != null) {
            VisitListAdapter listAdapter = new VisitListAdapter(this, visitList);
            listView.setAdapter(listAdapter);
        }

    }

}
