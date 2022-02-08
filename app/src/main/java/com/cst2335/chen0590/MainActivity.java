package com.cst2335.chen0590;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);

    );

        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toastMessage = MainActivity.this.getResources().getString(R.string.toast_message);
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_LONG).show();

            }
        });

        Switch mySwitch = (Switch) findViewById(R.id.switch1);
        String switchNow = MainActivity.this.getResources().getString(R.string.switch_now);
        String undo = MainActivity.this.getResources().getString(R.string.undo);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean b) {
                if (b == true) {
                    String on = MainActivity.this.getResources().getString(R.string.on);
                    Snackbar.make(btn, switchNow + on, Snackbar.LENGTH_LONG).setAction( undo, click -> cb.setChecked(!b)).show();

                } else if (b == false) {
                    String off = MainActivity.this.getResources().getString(R.string.off);
                    Snackbar.make(btn, switchNow + off, Snackbar.LENGTH_LONG).setAction( undo, click -> cb.setChecked(!b)).show();
                }

            }
        });


    }
}