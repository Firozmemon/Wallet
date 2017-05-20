package com.firozmemon.wallet.ui.main;

import com.firozmemon.wallet.database.DatabaseRepository;
import com.firozmemon.wallet.models.User_Credentials;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firoz on 18/5/17.
 */

public class MainActivityPresenter {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MainActivityView view;
    private DatabaseRepository databaseRepository;
    private Scheduler mainScheduler;

    public MainActivityPresenter(MainActivityView view, DatabaseRepository databaseRepository, Scheduler mainScheduler) {
        this.view = view;
        this.databaseRepository = databaseRepository;
        this.mainScheduler = mainScheduler;
    }

    public void loadData(int userId) {
        compositeDisposable.add(databaseRepository.getCredentials(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<User_Credentials>>() {
                    @Override
                    public void onSuccess(@NonNull List<User_Credentials> user_credentialses) {
                        if (user_credentialses.isEmpty())
                            view.displayNoCredentialsFound();
                        else
                            view.displayCredentials(user_credentialses);
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
