package com.firozmemon.wallet;

import com.firozmemon.wallet.database.DatabaseRepository;
import com.firozmemon.wallet.models.SignUp;
import com.firozmemon.wallet.models.User_Credentials;
import com.firozmemon.wallet.ui.main.MainActivityPresenter;
import com.firozmemon.wallet.ui.main.MainActivityView;
import com.firozmemon.wallet.ui.signup.CreateAccountActivityPresenter;
import com.firozmemon.wallet.ui.signup.CreateAccountActivityView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

public class MainActivityPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    MainActivityView view;
    @Mock
    DatabaseRepository databaseRepository;

    SignUp signUp;
    MainActivityPresenter presenter;

    int userId = 1;

    @Before
    public void setUp() {
        presenter = new MainActivityPresenter(view, databaseRepository, Schedulers.trampoline());

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
    public void shouldPassCredentialsToView(){
        List<User_Credentials> list = Arrays.asList(new User_Credentials(),new User_Credentials(),new User_Credentials());

        when(databaseRepository.getCredentials(userId)).thenReturn(Single.just(list));

        presenter.loadData(userId);

        verify(view).displayCredentials(list);
    }

    @Test
    public void shouldPassNoCredentialsFoundToView(){
        when(databaseRepository.getCredentials(userId)).thenReturn(Single.just(Collections.<User_Credentials>emptyList()));

        presenter.loadData(userId);

        verify(view).displayNoCredentialsFound();
    }

    @Test
    public void shouldPassErrorToView(){
        when(databaseRepository.getCredentials(userId)).thenReturn(Single.<List<User_Credentials>>error(new Throwable("Some error occured!")));

        presenter.loadData(userId);

        verify(view).displayError(anyString());
    }
}
