package com.firozmemon.wallet.ui.details;

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

public class CredentialDetailsActivityPresenter {

    private final CredentialDetailsActivityView view;
    private final DatabaseRepository databaseRepository;
    private final Scheduler mainScheduler;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CredentialDetailsActivityPresenter(CredentialDetailsActivityView view, DatabaseRepository databaseRepository, Scheduler mainScheduler) {
        this.view = view;
        this.databaseRepository = databaseRepository;
        this.mainScheduler = mainScheduler;
    }

    public void editClicked(boolean isInEditMode, User_Credentials credentials) {
        if (!isInEditMode) {
            view.enterEditMode();
        } else {
            // If already in editMode, save updatedData
            compositeDisposable.add(databaseRepository.updateCredentials(credentials)
                    .subscribeOn(Schedulers.io())
                    .observeOn(mainScheduler)
                    .subscribeWith(new DisposableSingleObserver<User_Credentials>() {
                        @Override
                        public void onSuccess(@NonNull User_Credentials user_credentials) {
                            if (user_credentials.equals(new User_Credentials())) {
                                view.displayError("Could not update!");
                            } else {
                                view.displaySuccess(user_credentials);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            view.displayError(e.getMessage());
                        }
                    }));
        }
    }

    public void unsubscribe() {
        compositeDisposable.clear();
    }
}
