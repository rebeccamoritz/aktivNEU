package com.example.rebeccaannamoritz.aktiv;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Fragment1_class listView;
    public Fragment2_class mapView;
    public Fragment3_class infoView;

    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;

    //Liste aufrufen
    List<Aktivitaet> aktivitaetenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Fragmente aufrufen
        listView = (Fragment1_class) Fragment.instantiate(this, Fragment1_class.class.getName(), null);
        mapView = (Fragment2_class) Fragment.instantiate(this, Fragment2_class.class.getName(), null);
        infoView = (Fragment3_class) Fragment.instantiate(this, Fragment3_class.class.getName(), null);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.drawer_layout, listView);
        fragmentTransaction.commit();

        //Funktion getAktiviteaten aufrufen
        getAktivitaeten();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        View view = findViewById(android.R.id.content);

        int id = item.getItemId();

        if (id == R.id.nav_activity) {

             fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.drawer_layout, listView);
            fragmentTransaction.commit();

            /*
            // Create new fragment and transaction
            android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.fragment, new MainActivityFragment());
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
            */

            Snackbar.make(view, "Aktivitäten", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else if (id == R.id.nav_map) {

            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.drawer_layout, mapView);
            fragmentTransaction.commit();

            Snackbar.make(view, "Kartenansicht", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else if (id == R.id.nav_info) {

            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.drawer_layout, infoView);
            fragmentTransaction.commit();

            Snackbar.make(view, "Informationen", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Funktion get Aktivitäten von Api
    public void getAktivitaeten(){
        // Prepare the HTTP request
        Call<List<Aktivitaet>> call = HttpApi.getInstance().getService().getAktivitaeten();

        // Asynchronously execute HTTP request
        call.enqueue(new Callback<List<Aktivitaet>>() {
            /**
             * onResponse is called when any kind of response has been received.
             */
            @Override
            public void onResponse(Response<List<Aktivitaet>> response, Retrofit retrofit) {
                // http response status code + headers
                System.out.println("Response status code: " + response.code());

                // isSuccess is true if response code => 200 and <= 300
                if (!response.isSuccess()) {
                    // print response body if unsuccessful
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        // do nothing
                    }
                    return;
                }

                // if parsing the JSON body failed, `response.body()` returns null
                aktivitaetenList = response.body();
                if (aktivitaetenList == null) return;

                for (Aktivitaet a : aktivitaetenList){
                    System.out.println ("- id:                  " + a.id);
                    System.out.println ("- name:                " + a.name);
                    System.out.println ("- lat:                 " + a.lat);
                    System.out.println ("- longi:               " + a.longi);
                    System.out.println ("- beschreibung:        " + a.beschreibung);
                    System.out.println ("- link                 " + a.link);
                    System.out.println ("- sonnig:              " + a.sonnig);
                    System.out.println ("- leicht_bewoelkt:     " + a.leicht_bewoelkt);
                    System.out.println ("- wolkig:              " + a.wolkig);
                    System.out.println ("- bedeckt:             " + a.bedeckt);
                    System.out.println ("- nebel:               " + a.nebel);
                    System.out.println ("- spruehregen:         " + a.spruehregen);
                    System.out.println ("- regen:               " + a.regen);
                    System.out.println ("- schnee:              " + a.schnee);
                    System.out.println ("- schauer:             " + a.schauer);
                    System.out.println ("- gewitter:            " + a.gewitter);
                }

            }

            /**
             * onFailure gets called when the HTTP request didn't get through.
             * For instance if the URL is invalid / host not reachable
             */
            @Override
            public void onFailure(Throwable t) {
                System.out.println("onFailure");
                System.out.println(t.getMessage());
            }
        });

    }
}