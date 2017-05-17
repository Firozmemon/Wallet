package com.firozmemon.wallet.models.sharedpreferences;

import android.content.SharedPreferences;

import com.firozmemon.wallet.models.Login;
import com.firozmemon.wallet.models.SignUp;

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
    public Single<Boolean> createUser(final SharedPreferences.Editor editor, final SignUp signUpData) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                if (signUpData != null) {
                    String username = signUpData.getUsername();
                    String email = signUpData.getEmail();
                    String password = signUpData.getPassword();

                    if (!("".equals(username)) && !("".equals(email)) && !("".equals(password))) {  // Flipped equals condition, to prevent NullPointerException
                        editor.putString("username", username);
                        editor.putString("email", email);
                        editor.putString("password", password);

                        return editor.commit();
                    }
                }
                return Boolean.FALSE;
            }
        });
    }


}
