package com.cst2335.chen0590;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private Button loginButton;
    private EditText emailAddress;

    @Override
    protected void onPause() {
        super.onPause();

        prefs = getSharedPreferences("Lab3",MODE_PRIVATE);
        String email = prefs.getString("email","");
        emailAddress.setText(email);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_login);

        emailAddress = (EditText) findViewById(R.id.editTextTextPersonName2);
        loginButton = (Button) findViewById(R.id.button3);

        onPause();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailAddress.getText().toString();
                prefs = getSharedPreferences("Lab3",MODE_PRIVATE);
                edit = prefs.edit();
                edit.putString("email", email);
                edit.commit();

                Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
                goToProfile.putExtra("email",email);
                startActivity(goToProfile);
            }



        });


}

}