package com.ya.anavigationlib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ya.anavigationlib.core.ANavigation;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String path = intent.getStringExtra(ANoController.KEY_FRAGMENT_PATH);
            Bundle args = intent.getBundleExtra(ANoController.KEY_FRAGMENT_ARGS);

            ANavigation.findNavController(this, R.id.fragment).navigation(path, args);
        }
    }
}