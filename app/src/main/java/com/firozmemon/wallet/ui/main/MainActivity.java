package com.firozmemon.wallet.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firozmemon.wallet.R;
import com.firozmemon.wallet.WalletApplication;
import com.firozmemon.wallet.database.DatabaseRepository;
import com.firozmemon.wallet.models.User_Credentials;
import com.firozmemon.wallet.ui.create.CreateCredentialsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements MainActivityView, MainActivityAdapter.AdapterItemClickListener {

    @Inject
    DatabaseRepository databaseRepository;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.noDataFound)
    TextView noDataFound;

    @OnClick(R.id.fab)
    public void fabClicked() {
        Snackbar.make(coordinatorLayout, "Create new setup", Snackbar.LENGTH_LONG)
                .setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Action Clicked", Toast.LENGTH_SHORT).show();
                    }
                }).show();
        presenter.addNewData();
    }

    MainActivityPresenter presenter;
    MainActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((WalletApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new MainActivityPresenter(this, databaseRepository, AndroidSchedulers.mainThread());
        presenter.loadData(((WalletApplication) getApplication()).getLoggedInUserId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void displayError(String msg) {
        Snackbar.make(coordinatorLayout, "Error: " + msg, Snackbar.LENGTH_LONG).show();
        noDataFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayNoCredentialsFound() {
        Snackbar.make(coordinatorLayout, "No Data Found", Snackbar.LENGTH_LONG).show();
        noDataFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayCredentials(List<User_Credentials> list) {
        Snackbar.make(coordinatorLayout, "Found: " + list.size(), Snackbar.LENGTH_LONG).show();
        noDataFound.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

        adapter = new MainActivityAdapter(MainActivity.this, list);
        adapter.setAdapterItemClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAdapterItemClick(View view, int position) {
        if (position >= 0) {
            Snackbar.make(coordinatorLayout,
                    "Adapter Item Clicked:" +
                            "\nPosition: " + position +
                            "\nItem: " + adapter.getItem(position).toString(),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void goToCreateCredentialActivity() {
        Intent intent = new Intent(MainActivity.this, CreateCredentialsActivity.class);
        startActivity(intent);
    }
}
