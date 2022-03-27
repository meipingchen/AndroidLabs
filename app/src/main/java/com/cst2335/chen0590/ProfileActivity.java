package com.cst2335.chen0590;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {
    public final static String TAG = "PROFILE_ACTIVITY";
    ImageView imgView;
    ActivityResultLauncher<Intent> myPictureTakerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            ,new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap imgbitmap = (Bitmap) data.getExtras().get("data");
                        imgView.setImageBitmap(imgbitmap);
                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED)
                        Log.i(TAG, "User refused to capture a picture.");
                }
            } );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton btn = (ImageButton) findViewById(R.id.imageButton2);
        EditText emailEditText = (EditText) findViewById(R.id.editTextTextPersonName3);
        Intent fromMain = getIntent();
        String email = fromMain.getStringExtra("email");
        emailEditText.setText(email);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        myPictureTakerLauncher.launch(takePictureIntent);
                    }

                }

            });

        Button chatButton = (Button) findViewById(R.id.button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToFile = new Intent(ProfileActivity.this,ChatRoomActivity.class);
                startActivity(goToFile);
            }
        });

        Button toolbarButton = (Button) findViewById(R.id.button2);
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToFile = new Intent(ProfileActivity.this,TestToolbar.class);
                startActivity(goToFile);
            }
        });

        }

}
