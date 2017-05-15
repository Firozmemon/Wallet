package com.firozmemon.wallet.models.sharedpreferences;

import android.content.SharedPreferences;

import com.firozmemon.wallet.models.Login;

import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Created by firoz on 14/5/17.
 */

public class SharedPreferencesModel implements SharedPreferencesRepository {

    @Override
    public Single<Boolean> checkLoginCredentials(final SharedPreferences sharedPreferences,
                                                 final Login login) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (login != null) {
                    String username = sharedPreferences.getString("username", "");
                    String password = sharedPreferences.getString("password", "");

                    if (username.equals("") || password.equals(""))
                        return Boolean.FALSE;

                    if (username.equals(login.getUsername()) && password.equals(login.getPassword())) {
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }

                } else {
                    return Boolean.FALSE;
                }
            }
        });
    }

    @Override
    public Single<Boolean> checkIsUserLoggedIn(final SharedPreferences sharedPreferences) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                // TODO: 14/5/17 Check Username & Password, instead of isLoggedIn Flag
                return Boolean.valueOf(sharedPreferences.getBoolean("isLoggedIn", false));
            }
        });
    }

    @Override
    public Single<Boolean> updateUserLoggedIn(final SharedPreferences.Editor sharedPreferencesEditor) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return Boolean.valueOf(sharedPreferencesEditor.putBoolean("isLoggedIn", true)
                        .commit());
            }
        });
    }

    @Override
    public Single<Boolean> updateUserLoggedOut(final SharedPreferences.Editor sharedPreferencesEditor) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return Boolean.valueOf(sharedPreferencesEditor.putBoolean("isLoggedIn", false)
                        .commit());
            }
        });
    }
}
