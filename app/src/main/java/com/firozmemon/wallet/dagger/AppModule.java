package com.firozmemon.wallet.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.firozmemon.wallet.Constants;
import com.firozmemon.wallet.WalletApplication;
import com.firozmemon.wallet.models.Login;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesModel;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by firoz on 14/5/17.
 */
@Module
public class AppModule {

    private final WalletApplication application;

    public AppModule(WalletApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    Resources provideApplicationResource(Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreference(Context context) {
        return context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    SharedPreferences.Editor provideSharedPreferenceEditor(SharedPreferences preferences) {
        return preferences.edit();
    }

    @Provides
    Login providesLoginModel() {
        return new Login();
    }

    @Provides
    SharedPreferencesRepository providesSharedPreferencesRepository() {
        return new SharedPreferencesModel();
    }
}
