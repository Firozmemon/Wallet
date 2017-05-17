package com.firozmemon.wallet.models.sharedpreferences;

import android.content.SharedPreferences;

import com.firozmemon.wallet.models.Login;
import com.firozmemon.wallet.models.SignUp;

import io.reactivex.Single;

/**
 * Created by firoz on 14/5/17.
 */

public interface SharedPreferencesRepository {

    Single<Boolean> checkLoginCredentials(SharedPreferences sharedPreferences, Login login);

    Single<Boolean> createUser(SharedPreferences.Editor editor, SignUp signUpData);
}
