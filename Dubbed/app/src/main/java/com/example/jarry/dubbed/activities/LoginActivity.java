package com.example.jarry.dubbed.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jarry.dubbed.R;
import com.example.jarry.dubbed.helpers.InputValidation;
import com.example.jarry.dubbed.model.Pets;
import com.example.jarry.dubbed.model.User;
import com.example.jarry.dubbed.sql.DatabaseHelper;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginActivity.this;


    private String email,password;
    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPassword;

    private Button ButtonLogin;

    private TextView textViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private Pets pet;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initListeners();
        initObjects();


    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        textInputEditTextEmail =  findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);

        ButtonLogin = (Button) findViewById(R.id.login_button);

        textViewLinkRegister = (TextView) findViewById(R.id.textViewLinkRegister);


    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        ButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_button) {
            verifyFromSQLite();
        }
        if(v.getId() == R.id.textViewLinkRegister) {
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, getString(R.string.error_message_password))) {
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {


            User user = databaseHelper.getUser(textInputEditTextEmail.getText().toString().trim());
            int user_id = user.getId();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("vet_id", user_id);
            editor.apply();
            Intent accountsIntent = new Intent(activity, MainActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);


        } else {
            // Snack Bar to show success message that record is wrong
            Toast.makeText(this, getString(R.string.toast_error_message), Toast.LENGTH_SHORT).show();
        }
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }


}
