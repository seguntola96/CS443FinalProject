package com.example.jarry.dubbed.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jarry.dubbed.model.User;
import com.example.jarry.dubbed.sql.DatabaseHelper;
import com.example.jarry.dubbed.model.Pets;
import com.example.jarry.dubbed.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "MainActivity: ";


    // Views
    ListView listView;
    // Helpers
    DatabaseHelper myDb;
    List<Pets> petList;
    User mUser = new User();
    // Constants
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_view);

        Log.d(DEBUG_TAG, "onCreate: initializing view........................");

        init();
        Log.d(DEBUG_TAG, "onCreate: list view populated.........................");
        Log.d(DEBUG_TAG, "onCreate: save path is + ...................................\n");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                return true;
            case R.id.menu_register:
                Intent intent2 = new Intent(this, RegisterPetActivity.class);
                startActivity(intent2);
                return true;
            case R.id.menu_setting:
                Intent intent3 = new Intent(this, SettingsActivity.class);
                startActivity(intent3);
                return true;
            case R.id.menu_about_us:
                Intent intent4 = new Intent(this, AboutActivity.class);
                startActivity(intent4);
                return true;
            case R.id.menu_logout:
                Intent intent5 = new Intent(this, LoginActivity.class);
                startActivity(intent5);
                // Do something
                return true;
            default:
                // Do Something
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        // Helpers
        myDb = new DatabaseHelper(this);
        petList = new ArrayList<>();

        // Views
        listView = (ListView) findViewById(R.id.home_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pets pet = petList.get(position);
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                intent.putExtra("PET", pet.getPet_id());
                startActivity(intent);
            }
        });
        populateListView();
    }

    private void populateListView() {

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final int user;
        user =(mSharedPreference.getInt("vet_id", 0));

        /*petList.add(new Pet(1, "otterhound.jpg", "Rufus", "otterhound", 7, "12/09/2015 05:13 AM", "Simba Gilmore"));
        petList.add(new Pet(2, "mexicanhairless.jpg", "Sophie", "mexicanhairless", 4, "11/22/2010 09:52 AM", "Sandy Benton"));
        petList.add(new Pet(3, "beagle.jpg", "Hank", "beagle", 4, "04/26/2013 04:26 PM", "Mac Jacobson"));
        petList.add(new Pet(4, "pinscher_miniature.jpg", "Kona", "pinscher-miniature", 4, "12/06/2010 08:19 PM", "Mimi Vega"));
        petList.add(new Pet(5, "samoyed.jpg", "Harley", "samoyed", 4, "02/27/2013 04:39 AM", "Olive Briggs"));
        petList.add(new Pet(6, "mexicanhairless.jpg", "Sophie", "mexicanhairless", 4, "11/22/2010 09:52 AM", "Sandy Benton"));
        petList.add(new Pet(7, "beagle.jpg", "Hank", "beagle", 4, "04/26/2013 04:26 PM", "Mac Jacobson"));
        petList.add(new Pet(8, "mexicanhairless.jpg", "Sophie", "mexicanhairless", 4, "11/22/2010 09:52 AM", "Sandy Benton"));
        petList.add(new Pet(9, "beagle.jpg", "Hank", "beagle", 4, "04/26/2013 04:26 PM", "Mac Jacobson"));*/
        Log.d(DEBUG_TAG, "user id: " + user);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                petList.clear();
                petList.addAll(myDb.getAllPet(String.valueOf(user)));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();

        ListAdapter listAdapter = new ListAdapter(this, petList);
        listView.setAdapter(listAdapter);

    }
}