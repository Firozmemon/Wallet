package com.firozmemon.wallet.dagger;

import com.firozmemon.wallet.WalletApplication;
import com.firozmemon.wallet.ui.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by firoz on 14/5/17.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(WalletApplication application);

    void inject(LoginActivity loginActivity);

}
