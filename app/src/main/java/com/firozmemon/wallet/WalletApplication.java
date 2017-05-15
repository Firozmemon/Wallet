package com.firozmemon.wallet;

import android.app.Application;

import com.firozmemon.wallet.dagger.AppComponent;
import com.firozmemon.wallet.dagger.AppModule;
import com.firozmemon.wallet.dagger.DaggerAppComponent;

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
                .build();

        appComponent.inject(this);
    }
}
