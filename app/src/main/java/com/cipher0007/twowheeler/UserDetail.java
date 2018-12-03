package com.cipher0007.twowheeler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetail extends AppCompatActivity {
    private EditText editTextfirstName, editTextlastName, editTextemail;
    private FloatingActionButton SubmitButtonUser;
    private Button btnAadharCard, btnDrivingLicence;
    CircleImageView imgAadharCard, imgDrivingLicence;
    String chckLicence = null;

    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        editTextfirstName = findViewById(R.id.FirstNameUser);
        editTextlastName = findViewById(R.id.LastNameUser);
        editTextemail = findViewById(R.id.EmailIdUser);
        SubmitButtonUser = findViewById(R.id.SubmitButtonUser);

        btnAadharCard = findViewById(R.id.btnAadharCard);
        btnDrivingLicence = findViewById(R.id.btnDrivingLicence);
        imgAadharCard = findViewById(R.id.imgAadhar);
        imgDrivingLicence = findViewById(R.id.imgDrivingLicnce);
        imgDrivingLicence.setVisibility(View.GONE);
        imgAadharCard.setVisibility(View.GONE);

        btnAadharCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectImageClick(view, "a");
            }
        });

        btnDrivingLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectImageClick(view, "d");
            }
        });

        SubmitButtonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!validation()) {
                    new SharedPrefManager(getApplicationContext()).saveLoginDetails(editTextfirstName.getText().toString().trim(), editTextlastName.getText().toString().trim(), editTextemail.getText().toString().trim(), true);
                    Intent i = new Intent(UserDetail.this, MapActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    // close this activity
                    finish();

                }
            }
        });
    }


    private boolean validation() {
        boolean check = false;
        if (imgAadharCard.getDrawable() == null) {
            Toast.makeText(this, "Upload Supporting Document Aadhar card", Toast.LENGTH_SHORT).show();
            check = true;
        }
        if (imgDrivingLicence.getDrawable() == null) {
            Toast.makeText(this, "Upload Supporting Document Driving Licence", Toast.LENGTH_SHORT).show();
            check = true;
        }
        if (editTextfirstName.getText().toString().trim().isEmpty()) {
            editTextfirstName.setError("First Name Cannot be empty");

            check = true;
        }
        if (editTextlastName.getText().toString().trim().isEmpty()) {
            editTextlastName.setError("First Name Cannot be empty");
            check = true;
        }
        if (editTextemail.getText().toString().trim().isEmpty()) {
            editTextemail.setError("First Name Cannot be empty");
            check = true;
        }
        if (!isValidEmail(editTextemail.getText().toString().trim())) {
            editTextemail.setError("Invalid Email");
            check = true;
        }

        return check;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void onSelectImageClick(View view, String licence) {
        CropImage.startPickImageActivity(this);
        chckLicence = licence;
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (chckLicence.equalsIgnoreCase("a")) {
                    imgAadharCard.setImageURI(result.getUri());
                    imgAadharCard.setVisibility(View.VISIBLE);
                } else {
                    imgDrivingLicence.setImageURI(result.getUri());
                    imgDrivingLicence.setVisibility(View.VISIBLE);
                }
                //((ImageButton) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (new SharedPrefManager(getApplicationContext()).isLoggedIn()) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
