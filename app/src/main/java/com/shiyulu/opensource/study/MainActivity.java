package com.shiyulu.opensource.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shiyulu.opensource.study.di.component.DaggerMainActivityComponent;
import com.shiyulu.opensource.study.di.module.MainActivityModule;
import com.shiyulu.opensource.study.model.Student;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button btnInject;
    Button btnRxjava;
    Button btnRxjust;
    Button btnEventBus;

    TextView tvContent;

    @Inject
    Student student;

    StringBuilder mRxTextSb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInject = findViewById(R.id.inject_btn);
        btnRxjava = findViewById(R.id.rxjava_btn);
        btnRxjust = findViewById(R.id.rxjust_btn);
        btnEventBus = findViewById(R.id.eventbus_btn);
        tvContent = findViewById(R.id.content_tv);
        DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initListener() {
        btnInject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, student.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnRxjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        Log.e(TAG, "subscribe thread: " + Thread.currentThread().getName());
                        mRxTextSb.append("Observable emit 1" + "\n");
                        Log.e(TAG, "Observable emit 1" + "\n");
                        emitter.onNext(1);
                        mRxTextSb.append("Observable emit 2" + "\n");
                        Log.e(TAG, "Observable emit 2" + "\n");
                        emitter.onNext(2);
                        mRxTextSb.append("Observable emit 3" + "\n");
                        Log.e(TAG, "Observable emit 3" + "\n");
                        emitter.onNext(3);
                        emitter.onComplete();
                        mRxTextSb.append("Observable emit 4" + "\n");
                        Log.e(TAG, "Observable emit 4" + "\n");
                        emitter.onNext(4);
                    }
                }).subscribeOn(Schedulers.io())
                        .doOnNext(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                integer = integer * 10;
                                Log.e(TAG, "doOnNext: " + integer);
                                Log.e(TAG, "doOnNext thread: " + Thread.currentThread().getName() );
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Integer>() {

                            private int i;
                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                mRxTextSb.append("onSubscribe : " + d.isDisposed() + "\n");
                                Log.e(TAG, "onSubscribe : " + d.isDisposed() + "\n");
                                disposable = d;
                                Log.e(TAG, "onSubscribe Thread: " + Thread.currentThread().getName());
                            }

                            @Override
                            public void onNext(Integer integer) {
                                mRxTextSb.append("onNext : value : " + integer + "\n");
                                Log.e(TAG, "onNext : value : " + integer + "\n");
                                Log.e(TAG, "onNext thread: " + Thread.currentThread().getName());
//                                i++;
//                                if (i == 2) {
//                                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
//                                    disposable.dispose();
//                                    mRxTextSb.append("onNext : isDisposable : " + disposable.isDisposed() + "\n");
//                                    Log.e(TAG, "onNext : isDisposable : " + disposable.isDisposed() + "\n");
//                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mRxTextSb.append("onError : value : " + e.getMessage() + "\n");
                                Log.e(TAG, "onError : value : " + e.getMessage() + "\n");
                            }

                            @Override
                            public void onComplete() {
                                mRxTextSb.append("onComplete" + "\n");
                                Log.e(TAG, "onComplete" + "\n");
                                tvContent.setText(mRxTextSb.toString());
                            }
                        });
            }
        });

        btnRxjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.just(1, 2, 3).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "just " + integer);
                    }
                });
            }
        });

        btnEventBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}
