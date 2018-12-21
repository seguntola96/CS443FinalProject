package com.example.jarry.dubbed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jarry.dubbed.R;
import com.example.jarry.dubbed.helpers.InputValidation;
import com.example.jarry.dubbed.model.User;
import com.example.jarry.dubbed.sql.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private EditText textInputEditTextFirstName;
    private EditText textInputEditTextLastName;
    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPhoneNumber;
    private EditText textInputEditTextPassword;
    private EditText textInputEditTextConfirmPassword;

    private Button ButtonRegister;
    private TextView TextViewLoginLink;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        textInputEditTextFirstName = (EditText) findViewById(R.id.textInputEditTextFirstName);
        textInputEditTextLastName = (EditText) findViewById(R.id.textInputEditTextLastName);
        textInputEditTextEmail = (EditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (EditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextPhoneNumber = (EditText) findViewById(R.id.textInputEditTextPhoneNumber);
        textInputEditTextConfirmPassword = (EditText) findViewById(R.id.textInputEditTextConfirmPassword);

        ButtonRegister = (Button) findViewById(R.id.register_button);

        TextViewLoginLink = (TextView) findViewById(R.id.textViewLinkSignIn);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        ButtonRegister.setOnClickListener(this);
        TextViewLoginLink.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.register_button:
                postDataToSQLite();
                break;

            case R.id.textViewLinkSignIn:
                finish();
                break;
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextFirstName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextLastName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPhoneNumber, getString(R.string.error_message_contact))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword, getString(R.string.error_message_password_match))) {
            return;
        }

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextFirstName.getText().toString().trim() + " " + textInputEditTextLastName.getText().toString().trim());
            Log.d("ADebugTag", "Value: " + textInputEditTextFirstName.getText().toString());

            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());
            user.setContact(textInputEditTextPhoneNumber.getText().toString().trim());

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Toast.makeText(this, getString(R.string.success_message), Toast.LENGTH_LONG).show();
            Intent accountsIntent = new Intent(activity, ProfileActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);


        } else {
            // Snack Bar to show error message that record already exists
            Toast.makeText(this, getString(R.string.email_exist), Toast.LENGTH_LONG).show();
        }


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextFirstName.setText(null);
        textInputEditTextLastName.setText(null);
        textInputEditTextPhoneNumber.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}
