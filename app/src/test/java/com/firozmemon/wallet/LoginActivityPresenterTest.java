package com.firozmemon.wallet;

import android.content.SharedPreferences;

import com.firozmemon.wallet.models.Login;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesRepository;
import com.firozmemon.wallet.ui.login.LoginActivityPresenter;
import com.firozmemon.wallet.ui.login.LoginActivityView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by firoz on 14/5/17.
 */

public class LoginActivityPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    LoginActivityView view;
    @Mock
    SharedPreferencesRepository preferencesRepository;
    @Mock
    SharedPreferences preferences;

    Login login;
    LoginActivityPresenter presenter;

    @Before
    public void setUp() {
        presenter = new LoginActivityPresenter(view, preferencesRepository,
                preferences, Schedulers.trampoline());

        RxJavaPlugins.setComputationSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @After
    public void cleanUp() {
        RxJavaPlugins.reset();
    }

    @Test
    public void shouldPassLoginCredentials() {
        when(preferencesRepository.checkLoginCredentials(preferences, login)).thenReturn(Single.just(Boolean.TRUE));

        presenter.signInClicked(login);

        verify(view).goToNextActivity();
    }

    @Test
    public void shouldFailLoginCredentials() {
        when(preferencesRepository.checkLoginCredentials(preferences, login)).thenReturn(Single.just(Boolean.FALSE));

        presenter.signInClicked(login);

        verify(view).displayError(anyString());
    }

    @Test
    public void shouldGoToCreateAccountActivity() {
        presenter.createAccountClicked();

        verify(view).goToCreateAccountActivity();
    }
}
