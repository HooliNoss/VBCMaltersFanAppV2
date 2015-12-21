package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers.ServerConnection;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers.XMLParser;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.PushNotification.RegistrationIntentService;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.VBCData.DataGenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MainActivity extends AppCompatActivity implements NewsFragment.OnFragmentInteractionListener, ResultFragment.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener, PropertiesFragment.OnFragmentInteractionListener {

    public static final String THUMBEXTENSION = "_thumb";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

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

        DataGenerator.getAllTeams();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(AppPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    int asdf = 0;
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    int asdf = 0;
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

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
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
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

        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = NewsFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = ResultFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = ScheduleFragment.class;
                break;
            case R.id.nav_fourth_fragment:
                Intent intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_properties_fragment:
                fragmentClass = PropertiesFragment.class;
                break;
            case R.id.nav_login_fragment:
                fragmentClass = LoginFragment.class;
                break;
            default:
                fragmentClass = NewsFragment.class;
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();


                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                // Highlight the selected item, update the title, and close the drawer
                menuItem.setChecked(true);
                setTitle(menuItem.getTitle());
                mDrawer.closeDrawers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onNewsFragmentInteraction(Uri string) {

    }

    @Override
    public void onResultFragmentInteraction(Uri string) {

    }

    @Override
    public void onScheduleFragmentInteraction(Uri string) {

    }

    @Override
    public void onLoginFragmentInteraction(Uri string) {

    }

    @Override
    public void onPropertiesFragmentInteraction(Uri uri) {

    }

    private void checkPermissions() {
        SharedPreferences settings = getSharedPreferences(AppPreferences.PREFS_NAME, 0);
        String loginString = settings.getString(AppPreferences.LOGIN_ADRESS, "");

        checkAdminPermission(loginString);
        checkAuthorPermission(loginString);
    }

    private void checkAdminPermission(String name) {
        final XMLParser parser = new XMLParser();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerConnection.SERVERURL + "CheckAdminPermission.php?name=" + "'" + name + "'";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Document doc = parser.getDomElement(response); // getting DOM element
                        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

                        // looping through all item nodes <item>
                        for (int i = 0; i < nl.getLength(); i++) {
                            Element e = (Element) nl.item(i);

                            String isAdmin = parser.getValue(e, AppPreferences.IS_ADMIN);

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

    private void checkAuthorPermission(String name) {
        final XMLParser parser = new XMLParser();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerConnection.SERVERURL + "CheckAuthorPermission.php?name=" + "'" + name + "'";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Document doc = parser.getDomElement(response); // getting DOM element
                        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

                        // looping through all item nodes <item>
                        for (int i = 0; i < nl.getLength(); i++) {
                            Element e = (Element) nl.item(i);

                            String isAuthor = parser.getValue(e, AppPreferences.IS_AUTHOR);

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

    private void setAdminPermissions(boolean isAdmin) {
        final SharedPreferences settings = getSharedPreferences(AppPreferences.PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(AppPreferences.IS_ADMIN, isAdmin);
        editor.commit();
    }

    private void setAuthorPermissions(boolean isAuthor) {
        final SharedPreferences settings = getSharedPreferences(AppPreferences.PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(AppPreferences.IS_AUTHOR, isAuthor);
        editor.commit();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
