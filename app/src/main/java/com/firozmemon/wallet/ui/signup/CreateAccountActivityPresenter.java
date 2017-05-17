package com.firozmemon.wallet.ui.signup;

import android.content.SharedPreferences;

import com.firozmemon.wallet.models.SignUp;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesRepository;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firoz on 16/5/17.
 */

public class CreateAccountActivityPresenter {

    private CreateAccountActivityView view;
    private SharedPreferencesRepository sharedPreferencesRepository;
    private SharedPreferences preferences;
    private Scheduler mainScheduler;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CreateAccountActivityPresenter(CreateAccountActivityView view, SharedPreferencesRepository sharedPreferencesRepository, SharedPreferences preferences, Scheduler scheduler) {
        this.view = view;
        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.preferences = preferences;
        mainScheduler = scheduler;
    }

    public void performSignUp(final SignUp signUp) {
        compositeDisposable.add(sharedPreferencesRepository.checkLoginCredentials(preferences, signUp)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .map(new Function<Boolean, String>() {
                    @Override
                    public String apply(@NonNull Boolean aBoolean) throws Exception {
                        String result = null;
                        if (!aBoolean) {
                            result =  sharedPreferencesRepository.createUser(preferences.edit(), signUp)
                                    .to(new Function<Single<Boolean>, String>() {
                                        @Override
                                        public String apply(@NonNull Single<Boolean> booleanSingle) throws Exception {
                                            if(booleanSingle.blockingGet())
                                                return "1";     // to display success
                                            else
                                                return "-1";    // to display signup failed
                                        }
                                    });
                        } else
                            result = "0";   // to display user already exists

                        return result;
                    }
                }).subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(@NonNull String s) {
                        if ("0".equals(s)) {
                            view.displayError("User already exists, please login!");
                        } else if ("-1".equals(s)) {
                            view.displayError("SignUp Failed, Please enter proper details.");
                        } else /*if ("1".equals(s))*/ {
                            view.displaySuccessAndGoToLoginActivity("SignUp Successful. Please Login!");
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.displayError("Some Error Occurred. Please try again later!" + e.toString());
                    }
                }));
    }

    public void unsubscribe() {
        compositeDisposable.clear();
    }
}
