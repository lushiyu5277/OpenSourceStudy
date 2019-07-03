package com.shiyulu.opensource.study.di.module;

import com.shiyulu.opensource.study.MainActivity;

import dagger.Module;

@Module
public class MainActivityModule {
    private MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }
}
