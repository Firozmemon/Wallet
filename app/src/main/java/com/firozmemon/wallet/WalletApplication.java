package com.firozmemon.wallet;

import android.app.Application;

import com.firozmemon.wallet.dagger.AppComponent;
import com.firozmemon.wallet.dagger.AppModule;
import com.firozmemon.wallet.dagger.DaggerAppComponent;
import com.firozmemon.wallet.dagger.ModelsModule;

/**
 * Created by firoz on 14/5/17.
 */

public class WalletApplication extends Application {

    AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initDagger();
    }

    private void initDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .modelsModule(new ModelsModule(this))
                .build();

        appComponent.inject(this);
    }
}
