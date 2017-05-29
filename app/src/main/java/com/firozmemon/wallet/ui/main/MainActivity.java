package com.firozmemon.wallet.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.firozmemon.wallet.R;
import com.firozmemon.wallet.WalletApplication;
import com.firozmemon.wallet.database.DatabaseRepository;
import com.firozmemon.wallet.models.User_Credentials;
import com.firozmemon.wallet.ui.create.CreateCredentialsActivity;
import com.firozmemon.wallet.ui.details.CredentialDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements MainActivityView, MainActivityAdapter.AdapterItemClickListener, SearchView.OnQueryTextListener {

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
    private List<User_Credentials> list;

    @OnClick(R.id.fab)
    public void fabClicked() {
        presenter.addNewData();
    }

    MainActivityPresenter presenter;
    MainActivityAdapter adapter;

    SearchView searchView;

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

        if (searchView != null && !searchView.isIconified()) {
            searchView.setIconified(true);
        }

        int userId = ((WalletApplication) getApplication()).getLoggedInUserId();
        if (userId > 0) {
            presenter = new MainActivityPresenter(this, databaseRepository, AndroidSchedulers.mainThread());
            presenter.loadData(userId);
        } else {
            onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else
            super.onBackPressed();
    }

    @Override
    public void displayError(String msg) {
        Snackbar.make(coordinatorLayout, "Error: " + msg, Snackbar.LENGTH_LONG).show();
        noDataFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayNoCredentialsFound() {
        Snackbar.make(coordinatorLayout, getResources().getString(R.string.noDataFound), Snackbar.LENGTH_LONG).show();
        noDataFound.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void displayCredentials(List<User_Credentials> list) {
        this.list = list; // setting to global variable for filters (SearchView)

        noDataFound.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

        adapter = new MainActivityAdapter(MainActivity.this, list);
        adapter.setAdapterItemClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAdapterItemClick(View view, int position) {
        if (position >= 0) {
            presenter.adapterItemClicked(adapter.getItem(position));
        }
    }

    @Override
    public void goToCreateCredentialActivity() {
        Intent intent = new Intent(MainActivity.this, CreateCredentialsActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToCredentialDetailsActivity(User_Credentials credentials) {
        Intent intent = new Intent(MainActivity.this, CredentialDetailsActivity.class);
        intent.putExtra("DETAILS", credentials);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // SearchView collapsed
                        if (adapter != null)
                            adapter.setFilter(list);

                        // Hide keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                MainActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // SearchView expanded
                        return true; // Return true to expand action view
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (adapter != null) {
            List<User_Credentials> filteredUserCredentials = filter(list, query);

            adapter.setFilter(filteredUserCredentials);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (adapter != null) {
            List<User_Credentials> filteredUserCredentials = filter(list, newText);

            adapter.setFilter(filteredUserCredentials);
        }
        return true;
    }

    /**
     * Performing filter operation on existing list
     *
     * @param userCredentialsList
     * @param queryText
     * @return
     */
    private List<User_Credentials> filter(List<User_Credentials> userCredentialsList, String queryText) {
        List<User_Credentials> cred = new ArrayList<>();
        queryText = queryText.toLowerCase();
        for (User_Credentials user_credentials : userCredentialsList) {
            String site_name = user_credentials.getSite_name().toLowerCase();
            if (site_name.contains(queryText)) {
                cred.add(user_credentials);
            }
        }
        return cred;
    }

    @Override
    public void onAdapterItemClickForDelete(View view, final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.delete)
                .setMessage(R.string.deleteDialogMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        User_Credentials user_credentials = adapter.getItem(position);

                        presenter.performDelete(user_credentials);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void displayDeleteSuccess() {
        Snackbar.make(coordinatorLayout, R.string.credentialDeleteSuccess, Snackbar.LENGTH_SHORT)
                .show();
        onStart(); // Calling onStart to reset adapter
    }
}
