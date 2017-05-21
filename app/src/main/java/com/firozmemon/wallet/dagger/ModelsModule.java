package com.firozmemon.wallet.dagger;

import com.firozmemon.wallet.WalletApplication;
import com.firozmemon.wallet.models.Login;
import com.firozmemon.wallet.models.SignUp;
import com.firozmemon.wallet.models.User_Credentials;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesModel;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by firoz on 17/5/17.
 */
@Module
public class ModelsModule {

    private final WalletApplication application;

    public ModelsModule(WalletApplication application) {
        this.application = application;
    }


    @Provides
    @Singleton
    SharedPreferencesRepository providesSharedPreferencesRepository() {
        return new SharedPreferencesModel();
    }

    @Provides
    Login providesLoginModel() {
        return new Login();
    }

    @Provides
    SignUp providesSignUpModel() {
        return new SignUp();
    }

    @Provides
    @Singleton
    User_Credentials providesUserCredentialsModel() {
        return new User_Credentials();
    }

}
