package com.shiyulu.opensource.study.di.component;

import com.shiyulu.opensource.study.MainActivity;
import com.shiyulu.opensource.study.di.module.MainActivityModule;

import dagger.Component;

@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
