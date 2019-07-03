package com.shiyulu.opensource.study.di.module;

import com.shiyulu.opensource.study.MainActivity;
import com.shiyulu.opensource.study.model.Student;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    private MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    Student provideStudent(){
        return new Student();
    }
}
