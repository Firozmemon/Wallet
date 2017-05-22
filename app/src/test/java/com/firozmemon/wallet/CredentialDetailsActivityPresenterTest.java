package com.firozmemon.wallet;

import com.firozmemon.wallet.database.DatabaseRepository;
import com.firozmemon.wallet.models.User_Credentials;
import com.firozmemon.wallet.ui.details.CredentialDetailsActivityPresenter;
import com.firozmemon.wallet.ui.details.CredentialDetailsActivityView;

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
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by firoz on 16/5/17.
 */

public class CredentialDetailsActivityPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    CredentialDetailsActivityView view;
    @Mock
    DatabaseRepository databaseRepository;

    User_Credentials credentials;
    CredentialDetailsActivityPresenter presenter;

    int userId = 1;
    boolean isInEditMode = false;

    @Before
    public void setUp() {
        presenter = new CredentialDetailsActivityPresenter(view, databaseRepository, Schedulers.trampoline());

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
    public void shouldEnterEditMode() {
        presenter.editClicked(isInEditMode, credentials);

        verify(view).enterEditMode();
    }

    @Test
    public void shouldFailUpdateCredentials() {
        isInEditMode = true;
        credentials = null;
        when(databaseRepository.updateCredentials(credentials)).thenReturn(Single.just(new User_Credentials()));

        presenter.editClicked(isInEditMode, credentials);

        verify(view).displayError(anyString());
    }

    @Test
    public void shouldPassUpdateCredentials() {
        isInEditMode = true;
        credentials = new User_Credentials();
        credentials.setUser_id(String.valueOf(userId)); // setting User_Credentials with custom value to Pass Test

        when(databaseRepository.updateCredentials(credentials)).thenReturn(Single.just(credentials));

        presenter.editClicked(isInEditMode, credentials);

        verify(view).displaySuccess(credentials);
    }

    @Test
    public void shouldHandleCrashUpdateCredentials() {
        isInEditMode = true;

        when(databaseRepository.updateCredentials(credentials)).thenReturn(Single.<User_Credentials>error(new Throwable("Some error occurred")));

        presenter.editClicked(isInEditMode, credentials);

        verify(view).displayError(anyString());
    }

}
