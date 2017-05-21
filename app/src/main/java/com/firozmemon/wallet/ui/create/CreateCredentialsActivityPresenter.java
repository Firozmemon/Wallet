package com.firozmemon.wallet.ui.create;

import com.firozmemon.wallet.database.DatabaseRepository;
import com.firozmemon.wallet.models.User_Credentials;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firoz on 21/5/17.
 */

public class CreateCredentialsActivityPresenter {

    private final CreateCredentialsActivityView view;
    private final DatabaseRepository databaseRepository;
    private final Scheduler mainScheduler;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CreateCredentialsActivityPresenter(CreateCredentialsActivityView view, DatabaseRepository databaseRepository, Scheduler mainScheduler) {
        this.view = view;
        this.databaseRepository = databaseRepository;
        this.mainScheduler = mainScheduler;
    }

    public void createCredentials(int userId, User_Credentials credentials) {
        compositeDisposable.add(databaseRepository.createCredentials(userId, credentials)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        if (aBoolean)
                            view.displaySuccess();
                        else
                            view.displayError("Please enter proper input data");
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
