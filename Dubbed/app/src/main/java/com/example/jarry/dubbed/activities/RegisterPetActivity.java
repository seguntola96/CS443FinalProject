package com.example.jarry.dubbed.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarry.dubbed.model.Pets;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.example.jarry.dubbed.R;
import com.example.jarry.dubbed.helpers.InputValidation;
import com.example.jarry.dubbed.model.User;
import com.example.jarry.dubbed.sql.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static com.example.jarry.dubbed.activities.MainActivity.DEBUG_TAG;

public class RegisterPetActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterPetActivity.this;

    private Button uploadPhoto;

    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;

    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private EditText textInputEditTextPetName;
    private EditText textInputEditTextAnimal;
    private EditText textInputEditTextBreed;
    private EditText textInputEditTextOwner;
    private EditText textInputEditTextAge;

    private Button ButtonRegister;
    private TextView TextViewLoginLink;

    private ImageView profileImageView;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    private Pets pet;
    private ProgressDialog progressbar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private boolean hasImageChanged = false;
    private String path;
    DatabaseHelper db;
    Bitmap thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);

        requestMultiplePermissions();
        initViews();
        initListeners();
        initObjects();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        textInputEditTextPetName = (EditText) findViewById(R.id.textInputEditTextPetName);
        textInputEditTextAnimal = (EditText) findViewById(R.id.textInputEditTextAnimal);
        textInputEditTextBreed = (EditText) findViewById(R.id.textInputEditTextBreed);
        textInputEditTextAge = (EditText) findViewById(R.id.textInputEditTextAge);
        textInputEditTextOwner = (EditText) findViewById(R.id.textInputEditTextOwner);
        profileImageView = findViewById(R.id.pet_photo);

        uploadPhoto = (Button) findViewById(R.id.upload_button);

        ButtonRegister = findViewById(R.id.register_pet_button);


    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        uploadPhoto.setOnClickListener(this);
        ButtonRegister.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
        pet = new Pets();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.upload_button:
                showPictureDialog();
                break;

            case R.id.register_pet_button:
                postDataToSQLite();
                break;
        }
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    path = saveImage(bitmap);
                    Log.d(DEBUG_TAG, "path: " + path);

                    Toast.makeText(RegisterPetActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    profileImageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterPetActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profileImageView.setImageBitmap(thumbnail);
            path = saveImage(thumbnail);
            Toast.makeText(RegisterPetActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPetName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextBreed, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextAnimal, getString(R.string.error_message_contact))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextAge, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextOwner, getString(R.string.error_message_email))) {
            return;
        }

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final int user;
        user =(mSharedPreference.getInt("vet_id", 0));

            pet.setName(textInputEditTextPetName.getText().toString().trim());
            pet.setPet_vet_id(user);
            pet.setBreed(textInputEditTextBreed.getText().toString().trim());
            pet.setPetAge(Integer.parseInt(textInputEditTextAge.getText().toString().trim()));
            pet.setOwner_name(textInputEditTextOwner.getText().toString().trim());
            pet.setAnimal(textInputEditTextAnimal.getText().toString().trim());
            pet.setImagePath(path);

            databaseHelper.addPet(pet);

            // Snack Bar to show success message that record saved successfully
            Toast.makeText(this, getString(R.string.success_message), Toast.LENGTH_LONG).show();
            Intent accountsIntent = new Intent(activity, MainActivity.class);
            emptyInputEditText();
            startActivity(accountsIntent);


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextPetName.setText(null);
        textInputEditTextAnimal.setText(null);
        textInputEditTextBreed.setText(null);
        textInputEditTextAge.setText(null);
        textInputEditTextOwner.setText(null);
    }


}
