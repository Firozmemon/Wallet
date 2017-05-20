package com.firozmemon.wallet.ui.login;

/**
 * Created by firoz on 14/5/17.
 */

public interface LoginActivityView {

    void goToCreateAccountActivity();

    void goToNextActivity(int userId);

    void displayError(String text);
}
