package com.firozmemon.wallet.models.sharedpreferences;

import android.content.SharedPreferences;

import com.firozmemon.wallet.models.Login;

import io.reactivex.Single;

/**
 * Created by firoz on 14/5/17.
 */

public interface SharedPreferencesRepository {

    Single<Boolean> checkLoginCredentials(SharedPreferences sharedPreferences, Login login);

    Single<Boolean> checkIsUserLoggedIn(SharedPreferences sharedPreferences);

    Single<Boolean> updateUserLoggedIn(SharedPreferences.Editor sharedPreferencesEditor);

    Single<Boolean> updateUserLoggedOut(SharedPreferences.Editor sharedPreferencesEditor);
}
