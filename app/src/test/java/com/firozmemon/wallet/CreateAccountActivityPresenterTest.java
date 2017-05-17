package com.firozmemon.wallet;

import android.content.SharedPreferences;

import com.firozmemon.wallet.models.SignUp;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesRepository;
import com.firozmemon.wallet.ui.signup.CreateAccountActivityPresenter;
import com.firozmemon.wallet.ui.signup.CreateAccountActivityView;

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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by firoz on 16/5/17.
 */

public class CreateAccountActivityPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    CreateAccountActivityView view;
    @Mock
    SharedPreferencesRepository preferencesRepository;
    @Mock
    SharedPreferences preferences;

    SignUp signUp;
    CreateAccountActivityPresenter presenter;

    @Before
    public void setUp() {
        presenter = new CreateAccountActivityPresenter(view, preferencesRepository,
                preferences, Schedulers.trampoline());

        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
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
    public void shouldDisplayUserExists(){
        when(preferencesRepository.checkLoginCredentials(preferences, signUp)).thenReturn(Single.just(Boolean.TRUE));

        presenter.performSignUp(signUp);

        verify(view).displayError("User already exists, please login!");
    }

    @Test
    public void shouldDisplaySignUpFailed(){
        when(preferencesRepository.checkLoginCredentials(preferences, signUp)).thenReturn(Single.just(Boolean.FALSE));

        when(preferencesRepository.createUser(preferences.edit(), signUp)).thenReturn(Single.just(Boolean.FALSE));

        presenter.performSignUp(signUp);

        verify(view).displayError("SignUp Failed, Please enter proper details.");
    }

    @Test
    public void shouldDisplaySignUpSuccessful(){
        when(preferencesRepository.checkLoginCredentials(preferences, signUp)).thenReturn(Single.just(Boolean.FALSE));

        when(preferencesRepository.createUser(preferences.edit(), signUp)).thenReturn(Single.just(true));

        presenter.performSignUp(signUp);

        verify(view).displaySuccessAndGoToLoginActivity("SignUp Successful. Please Login!");
    }
}
