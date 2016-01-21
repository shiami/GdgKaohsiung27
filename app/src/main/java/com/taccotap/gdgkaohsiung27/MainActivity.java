package com.taccotap.gdgkaohsiung27;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.taccotap.gdgkaohsiung27.api.GithubClient;
import com.taccotap.gdgkaohsiung27.api.model.Repo;
import com.taccotap.gdgkaohsiung27.databinding.MainActivityBinding;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MainActivityViewModel mMainActivityViewModel;
    private MainActivityBinding mMainActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mMainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mMainActivityViewModel = new MainActivityViewModel();

        example1();
        example2();
        example3();
        example4();
        example5();
        example6();
    }

    private void example1() {
        // numbers: 1, 2, 3, 5, 7, 9, 11, 12, 13
        // multiply 2 and get the largest number less than 10.
        solution1();
        solution2();
    }

    private void solution1() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 5, 7, 9, 11, 12, 13);

        // multiply 2
        ArrayList<Integer> aResults = new ArrayList<>();
        for (int a : numbers) {
            int aResult = a * 2;
            aResults.add(aResult);
        }

        // filter number less than 10
        ArrayList<Integer> bResults = new ArrayList<>();
        for (int b : aResults) {
            if (b < 10) {
                bResults.add(b);
            }
        }

        // find largest number
        int largestNumber = Integer.MIN_VALUE;
        for (int h : bResults) {
            if (h > largestNumber) {
                largestNumber = h;
            }
        }
        Log.i(TAG, "solution1, largestNumber = " + largestNumber);
    }

    private void solution2() {
        Observable.just(1, 2, 3, 5, 7, 9, 11, 12, 13)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer * 2;
                    }
                })
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return (integer < 10);
                    }
                })
                .reduce(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer > integer2 ? integer : integer2;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG, "solution2, largestNumber = " + integer);
                    }
                });
    }

    private void example2() {
        // separated

        // Observable
        Observable<String> observable = Observable.just("input1", "input2");

        // Subscriber (Observer)
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "example2, result1 = " + s);
            }
        };

        // To subscribe
        observable.subscribe(action1);

        // combined


        Observable.just("input3", "input4")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(TAG, "example2, result2 = " + s);
                    }
                });
    }

    private void example3() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);

        Observable.from(numbers)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG, "example3, result = " + integer);
                    }
                });
    }

    private void example4() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.i(TAG, "example4, result = " + integer);
            }
        });
    }

    private void example5() {
        final List<Integer> numbers = Arrays.asList(1, 2, 3, 10, 11);

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int n : numbers) {
                        if (n >= 10) {
                            throw new Exception("example5, Number is >= 10");
                        }
                        subscriber.onNext(n);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .compose(this.<Integer>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "example5, onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "example5, onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "example5, onNext: " + integer);
                    }
                });
    }

    private void example6() {
        GithubClient.Api().getOrgRepos("ReactiveX")
                .compose(this.<ArrayList<Repo>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Repo>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "example6, onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i(TAG, "example6, onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Repo> repos) {
                        Log.i(TAG, "example6, onNext: " + repos);
                        handleRepoApiResponse(repos);
                    }
                });
    }

    private void handleRepoApiResponse(ArrayList<Repo> repos) {
        Repo repo = repos.get(0);
        bindFirstRepo(repo);
    }

    private void bindFirstRepo(Repo repo) {
        mMainActivityBinding.setRepo(repo);
        mMainActivityBinding.setViewModel(mMainActivityViewModel);
    }
}
