package com.firozmemon.wallet;

import com.firozmemon.wallet.database.DatabaseRepository;
import com.firozmemon.wallet.models.User_Credentials;
import com.firozmemon.wallet.ui.create.CreateCredentialsActivityPresenter;
import com.firozmemon.wallet.ui.create.CreateCredentialsActivityView;

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
 * Created by firoz on 16/5/17.
 */

public class CreateCredentialsActivityPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    CreateCredentialsActivityView view;
    @Mock
    DatabaseRepository databaseRepository;

    User_Credentials credentials;
    CreateCredentialsActivityPresenter presenter;

    int userId = 1;

    @Before
    public void setUp() {
        presenter = new CreateCredentialsActivityPresenter(view, databaseRepository, Schedulers.trampoline());

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
    public void shouldPassCreateCredentials(){
        when(databaseRepository.createCredentials(userId, credentials)).thenReturn(Single.just(Boolean.TRUE));

        presenter.createCredentials(userId, credentials);

        verify(view).displaySuccess();
    }

    @Test
    public void shouldFailCreateCredentials(){
        when(databaseRepository.createCredentials(userId, credentials)).thenReturn(Single.just(Boolean.FALSE));

        presenter.createCredentials(userId, credentials);

        verify(view).displayError(anyString());
    }

    @Test
    public void shouldHandleCrash(){
        when(databaseRepository.createCredentials(userId, credentials)).thenReturn(Single.<Boolean>error(new Throwable("something weird happened")));

        presenter.createCredentials(userId, credentials);

        verify(view).displayError(anyString());
    }
}
