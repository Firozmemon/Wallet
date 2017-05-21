package com.firozmemon.wallet.database;

import com.firozmemon.wallet.models.Login;
import com.firozmemon.wallet.models.SignUp;
import com.firozmemon.wallet.models.User_Credentials;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by firoz on 19/5/17.
 */

public interface DatabaseRepository {

    Single<Integer> checkLoginCredentials(Login login);

    Single<Boolean> createUser(SignUp signUpData);

    Single<List<User_Credentials>> getCredentials(int userId);

    Single<Boolean> createCredentials(int userId, User_Credentials credentials);
}
