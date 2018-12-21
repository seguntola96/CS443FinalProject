package com.example.jarry.dubbed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarry.dubbed.R;
import com.example.jarry.dubbed.helpers.InputValidation;
import com.example.jarry.dubbed.model.PetVisits;
import com.example.jarry.dubbed.model.Pets;
import com.example.jarry.dubbed.model.User;
import com.example.jarry.dubbed.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddVisitsActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = AddVisitsActivity.this;


    private DatePicker mDatePicker;
    private EditText causeOfVisitEditText;
    private Button addVisits;
    private TextView dateText;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    private Pets mPets;
    private PetVisits mPetVisits;
    private Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_visits);

        initViews();
        initListeners();
        initObjects();
        dateText.setText(new Date().toString());

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_visits_button:
                postDataToSQLite();
                break;
        }
    }


    /**
     * This method is to initialize views
     */
    private void initViews() {

        causeOfVisitEditText = (EditText) findViewById(R.id.causeOfVisitEditText);
        addVisits = (Button) findViewById(R.id.add_visits_button);
        dateText = findViewById(R.id.dateTextView);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        addVisits.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
        mPetVisits = new PetVisits();

    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {

        if (!inputValidation.isInputEditTextFilled(causeOfVisitEditText, getString(R.string.error_message_text))) {
            return;
        }
        Intent intent = getIntent();
        int pet_id = intent.getIntExtra("PET", 0);
        mPetVisits.setPet_id(pet_id);
        mPetVisits.setCause_of_visit(causeOfVisitEditText.getText().toString().trim());
        mPetVisits.setDate_of_visit(dateText.getText().toString().trim());

        databaseHelper.addVisits(mPetVisits);

        Toast.makeText(this, getString(R.string.succesful_visit), Toast.LENGTH_LONG).show();
        Intent accountsIntent = new Intent(activity, ProfileActivity.class);
        accountsIntent.putExtra("PET",mPetVisits.getPet_id());
        emptyInputEditText();
        startActivity(accountsIntent);


        }
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        causeOfVisitEditText.setText(null);
    }

}
