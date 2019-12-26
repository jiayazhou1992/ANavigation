package com.ya.anavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.ya.anavigationlib.core.ANavigation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("hello", "say hello");
                ANavigation.findNavController(MainActivity.this).navigation("/main/fragment1", bundle);
            }
        });
        if (savedInstanceState == null) {
//            ANavigation.findNavController(this, R.id.host).navigation("/main/fragment1");
//            ANavigation.findNoController(this).navigation("/main/fragment1");
        }
    }
}
