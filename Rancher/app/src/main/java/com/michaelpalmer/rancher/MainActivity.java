package com.michaelpalmer.rancher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.michaelpalmer.rancher.container.ContainerFragment;
import com.michaelpalmer.rancher.schema.Container;
import com.michaelpalmer.rancher.schema.Host;
import com.michaelpalmer.rancher.schema.Service;
import com.michaelpalmer.rancher.schema.Stack;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        StacksListFragment.OnStackListFragmentInteractionListener,
        ServicesListFragment.OnServiceListFragmentInteractionListener,
        ContainerListFragment.OnContainerListFragmentInteractionListener,
        HostsListFragment.OnHostsListFragmentInteractionListener,
        HostFragment.OnHostFragmentInteractionListener,
        ContainerFragment.OnContainerFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private String rancher_url = null, project_id = "1a5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get preferences and register on change listener
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        // Get settings
        rancher_url = preferences.getString("rancher_url", null);
        final String rancher_access_key = preferences.getString("rancher_access_key", null);
        final String rancher_secret_key = preferences.getString("rancher_secret_key", null);

        // Check that settings have been configured
        if (rancher_url == null || rancher_access_key == null || rancher_secret_key == null) {
            Toast.makeText(this, "Rancher settings need to be configured.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(rancher_access_key, rancher_secret_key.toCharArray());
                }
            });
        }

        try {
            getSupportActionBar().setSubtitle(R.string.action_stacks);
        } catch (NullPointerException e) {
            // Let it go
        }

        // Load stacks fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_layout, new StacksListFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_stacks) {
            try {
                getSupportActionBar().setSubtitle(R.string.action_stacks);
            } catch (NullPointerException e) {
                // Let it go
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, new StacksListFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_hosts) {
            try {
                getSupportActionBar().setSubtitle(R.string.action_hosts);
            } catch (NullPointerException e) {
                // Let it go
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, new HostsListFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_containers) {
            try {
                getSupportActionBar().setSubtitle(R.string.action_containers);
            } catch (NullPointerException e) {
                // Let it go
            }

            // Instantiate
            ContainerListFragment containerListFragment = ContainerListFragment.newInstance(rancher_url, project_id);
            if (containerListFragment == null) {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Unable to load containers. Confirm Rancher settings are configured.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return false;
            }

            // Load fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, containerListFragment)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals("rancher_access_key") && !key.equals("rancher_secret_key")) {
            return;
        }

        final String username = sharedPreferences.getString("rancher_access_key", null);
        final String password = sharedPreferences.getString("rancher_secret_key", null);

        if (username != null && password != null) {
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            });
        }
    }

    /**
     * Load services for this stack
     *
     * @param item Stack
     */
    @Override
    public void onStackListFragmentInteraction(Stack item) {
        try {
            getSupportActionBar().setSubtitle(R.string.action_services);
        } catch (NullPointerException e) {
            // Let it go
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_layout, ServicesListFragment.newInstance(item))
                .addToBackStack(null)
                .commit();
    }

    /**
     * Load containers for this service
     *
     * @param item Service
     */
    @Override
    public void onServiceListFragmentInteraction(Service item) {
        try {
            getSupportActionBar().setSubtitle(R.string.action_containers);
        } catch (NullPointerException e) {
            // Let it go
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_layout, ContainerListFragment.newInstance(item))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onContainerListFragmentInteraction(Container item) {
        try {
            getSupportActionBar().setSubtitle(item.getName());
        } catch (NullPointerException e) {
            // Let it go
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_layout, ContainerFragment.newInstance(item))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onContainerFragmentInteraction(Uri uri) {

    }

    @Override
    public void onHostsListFragmentInteraction(Host item) {
        try {
            getSupportActionBar().setSubtitle(item.getName());
        } catch (NullPointerException e) {
            // Let it go
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_layout, ContainerListFragment.newInstance(item))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onHostFragmentInteraction(Uri uri) {

    }
}
