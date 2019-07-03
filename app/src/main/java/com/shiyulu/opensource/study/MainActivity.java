package com.shiyulu.opensource.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shiyulu.opensource.study.di.component.DaggerMainActivityComponent;
import com.shiyulu.opensource.study.di.module.MainActivityModule;
import com.shiyulu.opensource.study.model.Student;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    Button btnInject;

    @Inject
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInject = findViewById(R.id.inject_button);
        DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        initListener();
    }

    private void initListener() {
        btnInject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, student.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
