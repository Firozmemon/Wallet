package com.firozmemon.wallet.ui.login;

import android.content.SharedPreferences;

import com.firozmemon.wallet.models.Login;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesRepository;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firoz on 14/5/17.
 */

public class LoginActivityPresenter {

    private LoginActivityView view;
    private SharedPreferencesRepository preferencesRepository;
    private SharedPreferences preferences;
    private Scheduler mainScheduler;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginActivityPresenter(LoginActivityView view, SharedPreferencesRepository preferencesRepository, SharedPreferences preferences, Scheduler mainScheduler) {
        this.view = view;
        this.preferencesRepository = preferencesRepository;
        this.preferences = preferences;
        this.mainScheduler = mainScheduler;
    }

    public void createAccountClicked() {
        view.goToCreateAccountActivity();
    }

    public void signInClicked(Login login) {
        compositeDisposable.add(preferencesRepository.checkLoginCredentials(preferences,login)
                .subscribeOn(Schedulers.computation())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        if (aBoolean)
                            view.goToNextActivity();
                        else
                            view.displayError("Invalid Credentials");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.displayError(e.getMessage());
                    }
                }));
    }

    public void unsubscribe() {
        compositeDisposable.clear();
    }
}
