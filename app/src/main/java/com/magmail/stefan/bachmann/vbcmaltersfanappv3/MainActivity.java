package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.News;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewsFragment.OnFragmentInteractionListener, ResultFragment.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener{

    public static final String PREFS_NAME = "VBCMaltersFanAppPreferences";

    private DrawerLayout mDrawer;
    private DrawerLayout dlDrawer;
    private NavigationView nvDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    private final String KEY_ITEM = "entry"; // parent node

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Find our drawer view
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        dlDrawer.setDrawerListener(drawerToggle);

        checkPermissions();
        nvDrawer.getMenu().performIdentifierAction(R.id.nav_first_fragment, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
//        // The action bar home/up action should open or close the drawer.
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                mDrawer.openDrawer(GravityCompat.START);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.

        drawerToggle.syncState();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = NewsFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = ResultFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = ScheduleFragment.class;
                break;
            case R.id.nav_login_fragment:
                fragmentClass = LoginFragment.class;
                break;
            default:
                fragmentClass = NewsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onNewsFragmentInteraction(Uri string)
    {

    }

    @Override
    public void onResultFragmentInteraction(Uri string)
    {

    }

    @Override
    public void onScheduleFragmentInteraction(Uri string)
    {

    }

    @Override
    public void onLoginFragmentInteraction(Uri string)
    {

    }

    private void checkPermissions()
    {
        setAdminPermissions(false);
        setAuthorPermissions(false);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String loginString = settings.getString("loginAdress", "");

        checkAdminPermission(loginString);
        checkAuthorPermission(loginString);
    }

    private void checkAdminPermission(String name)
    {
        final XMLParser parser = new XMLParser();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://grodien.ddns.net:8080/CheckAdminPermission.php?name=" + "'" + name + "'";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Document doc = parser.getDomElement(response); // getting DOM element
                        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

                        // looping through all item nodes <item>
                        for (int i = 0; i < nl.getLength(); i++)
                        {
                            Element e = (Element) nl.item(i);

                            String isAdmin = parser.getValue(e, "IsAdmin");

                            if (!isAdmin.isEmpty()) {
                                setAdminPermissions(Boolean.parseBoolean(isAdmin));
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setAdminPermissions(false);
            }
        });
        queue.add(stringRequest);
    }

    private void checkAuthorPermission(String name)
    {
        final XMLParser parser = new XMLParser();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://grodien.ddns.net:8080/CheckAuthorPermission.php?name=" + "'" + name + "'";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Document doc = parser.getDomElement(response); // getting DOM element
                        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

                        // looping through all item nodes <item>
                        for (int i = 0; i < nl.getLength(); i++)
                        {
                            Element e = (Element) nl.item(i);

                            String isAuthor = parser.getValue(e, "IsAuthor");

                            if (!isAuthor.isEmpty()) {
                                setAuthorPermissions(Boolean.parseBoolean(isAuthor));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setAuthorPermissions(false);
            }
        });
        queue.add(stringRequest);
    }

    private void setAdminPermissions(boolean isAdmin)
    {
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("IsAdmin", isAdmin);
        editor.commit();
    }

    private void setAuthorPermissions(boolean isAuthor)
    {
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("IsAuthor", isAuthor);
        editor.commit();
    }
}
