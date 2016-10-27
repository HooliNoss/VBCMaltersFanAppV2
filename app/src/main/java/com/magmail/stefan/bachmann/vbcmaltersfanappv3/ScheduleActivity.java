package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Schedule;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Team;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers.SOAPConnection;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.VBCData.DataGenerator;

import org.ksoap2.serialization.SoapObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class ScheduleActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 23;
    Activity activity = this;

    private ArrayList<Schedule> mScheduleList;
    private ProgressDialog progressDialog;
    SoapObject mySoapObject;
    private Context mContext;
    private AsyncTask mTask;

    private Team team;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView mImageView;
    private FloatingActionButton mFabAddCalendar;

    private int mCalenderImportOption = DialogInterface.BUTTON_NEGATIVE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        team = DataGenerator.getTeamById(getIntent().getIntExtra("teamId", 0));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        //appBarLayout.setExpanded(false, false);

        mFabAddCalendar = (FloatingActionButton) findViewById(R.id.fab_addCalendar);
        mFabAddCalendar.setOnClickListener(fabAddCalendarOnClickListener);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "placeholder_thumb");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.theme_color_vbc));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.theme_color_vbc));

        mImageView = (ImageView) findViewById(R.id.scheduleImage);

        if (team != null) {
            collapsingToolbarLayout.setTitle("Spielplan");
            int id = getResources().getIdentifier(team.getM_ImgSrc() + MainActivity.THUMBEXTENSION, "drawable", getPackageName());
            mImageView.setImageResource(id);
            mFabAddCalendar.setVisibility(View.VISIBLE);
        } else {
            collapsingToolbarLayout.setTitle("Die nächsten Spiele");
            int id = getResources().getIdentifier("placeholder_thumb", "drawable", getPackageName());
            mImageView.setImageResource(id);
            mFabAddCalendar.setVisibility(View.INVISIBLE);
        }

        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_scheduleActivity);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getSchedule();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        if (mTask != null) {
            mTask.cancel(true);
        }

        super.onDestroy();
    }

    private void getSchedule() {
        mTask = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(ScheduleActivity.this, "", "Lade Spielpläne...", false, true);
            }

            @Override
            protected Integer doInBackground(Integer... params) {
                if (params == null) {
                    return 0;
                }
                try {
                    SOAPConnection myConnection = new SOAPConnection();

                    if (team != null) {
                        mySoapObject = myConnection.getSOAPConnection("http://myvolley.swissvolley.ch/getGamesTeam", "getGamesTeam", "team_ID", Integer.toString(team.getM_TeamId()));
                    } else {
                        mySoapObject = myConnection.getSOAPConnection("http://myvolley.swissvolley.ch/getGamesByClub", "getGamesByClub", "ID_club", Integer.toString(DataGenerator.getClub().getM_ClubId()));
                    }
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                    return 0;
                }
                return 1;
            }

            @Override
            protected void onPostExecute(Integer result) {
                progressDialog.dismiss();

                if (mySoapObject != null && result == 1) {
                    addDataToList(mySoapObject);
                    Schedule[] myDataset;

                    if (mScheduleList.size() < 20) {
                        myDataset = mScheduleList.toArray(new Schedule[mScheduleList.size()]);
                    } else {

                        ArrayList<Schedule> tmpList = new ArrayList<Schedule>();
                        for (Schedule schedule : mScheduleList) {
                            String dateString = (String) schedule.getDate();
                            String formatedDate = dateString.substring(0, 10);

                            //Date now = new Date();
                            Date now = new Date();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(now);
                            cal.add(Calendar.DAY_OF_YEAR, -1); // <--
                            Date yesterday = cal.getTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            Date convertedDate = null;
                            try {
                                convertedDate = dateFormat.parse(formatedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (yesterday.before(convertedDate)) {
                                tmpList.add(schedule);
                            }
                        }

                        myDataset = tmpList.toArray(new Schedule[tmpList.size()]);
                    }

                    // specify an adapter (see also next example)
                    mAdapter = new ScheduleAdapter(myDataset);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    AlertDialog.Builder b = new AlertDialog.Builder(ScheduleActivity.this);
                    b.setTitle(android.R.string.dialog_alert_title);
                    b.setMessage("Es konnte keine Verbindung hergestellt werden");
                    b.setPositiveButton(getString(android.R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dlg, int arg1) {
                                    dlg.dismiss();
                                }
                            });
                    b.create().show();
                }
            }
        }.execute(30000);
    }

    private void addDataToList(SoapObject data) {
        mScheduleList = new ArrayList<Schedule>();

        try {
            if (data.getProperty(0) != null) {
                SoapObject table = (SoapObject) data.getProperty(1);
                int i = 0;
                while (i < table.getPropertyCount()) {
                    SoapObject entry = (SoapObject) table.getProperty(i);

                    Schedule schedule = new Schedule();
                    schedule.setDate(entry.getProperty("PlayDate").toString());
                    String teamHome = DataGenerator.getTeamById((int) entry.getProperty("TeamHomeID")).getM_Name();
                    String teamAway = DataGenerator.getTeamById((int) entry.getProperty("TeamAwayID")).getM_Name();

                    if (teamHome == null)
                        schedule.setTeamHome(entry.getProperty("TeamHomeCaption").toString());
                    else
                        schedule.setTeamHome(teamHome);

                    if (teamAway == null)
                        schedule.setTeamAway(entry.getProperty("TeamAwayCaption").toString());
                    else
                        schedule.setTeamAway(teamAway);

                    schedule.setSetPointsHome(entry.getProperty("NumberOfWinsHome").toString());
                    schedule.setSetPointsAway(entry.getProperty("NumberOfWinsAway").toString());
                    schedule.setHall(entry.getProperty("HallCaption").toString());

                    int[] pointsTeamHome = new int[5];
                    int[] pointsTeamAway = new int[5];
                    for (int j = 0; j < 5; j++) {
                        switch (j) {
                            case 0:
                                pointsTeamHome[j] = (int) entry.getProperty("Set1PointsHome");
                                pointsTeamAway[j] = (int) entry.getProperty("Set1PointsAway");
                                break;
                            case 1:
                                pointsTeamHome[j] = (int) entry.getProperty("Set2PointsHome");
                                pointsTeamAway[j] = (int) entry.getProperty("Set2PointsAway");
                                break;
                            case 2:
                                pointsTeamHome[j] = (int) entry.getProperty("Set3PointsHome");
                                pointsTeamAway[j] = (int) entry.getProperty("Set3PointsAway");
                                break;
                            case 3:
                                pointsTeamHome[j] = (int) entry.getProperty("Set4PointsHome");
                                pointsTeamAway[j] = (int) entry.getProperty("Set4PointsAway");
                                break;
                            case 4:
                                pointsTeamHome[j] = (int) entry.getProperty("Set5PointsHome");
                                pointsTeamAway[j] = (int) entry.getProperty("Set5PointsAway");
                                break;
                        }
                    }
                    schedule.setPointsTeamHome(pointsTeamHome);
                    schedule.setPointsTeamAway(pointsTeamAway);
                    schedule.setmSoapObject(entry);
                    mScheduleList.add(schedule);

                    i++;
                }
            }
        } catch (Exception ex) {
            String message = ex.getMessage();
        }
    }


    private View.OnClickListener fabAddCalendarOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Spielplan in Kalender übertragen?");
            dialog.setMessage("Möchten Sie den Spielplan in Ihr Kalender übertragen");
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Alle Spiele", addGamesToCalendarEventListener);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Nur Heimspiele", addGamesToCalendarEventListener);
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Abbrechen", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    };


    private DialogInterface.OnClickListener addGamesToCalendarEventListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            mCalenderImportOption = which;

            //First checking if the app is already having the permission
            if (isReadStorageAllowed()) {
                addGamesToCalendar();
            }
            else{
                //If the app has not the permission then asking for the permission
                requestStoragePermission();
            }
        }
    };

    public void addGamesToCalendar() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        for (Schedule schedule : mScheduleList) {

            //Wenn nur Heimspiele, dann bei nicht Heimspiel ignorieren
            if (mCalenderImportOption == DialogInterface.BUTTON_NEGATIVE) {
                if (!schedule.getTeamHome().equals(team.getM_Name())) {
                    continue;
                }
            }

            String dateString = schedule.getDate();
            String formatedDate = dateString.substring(0, 16);

            Date convertedDate = null;
            try {
                convertedDate = dateFormat.parse(formatedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String title = schedule.getTeamHome() + " - " + schedule.getTeamAway();
            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(convertedDate); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 2); // adds two hours
            Date endTime = cal.getTime(); // returns new date object, one hour in the future

            addEvent(title, convertedDate.getTime(), endTime.getTime(), 0, schedule.getHall());
        }
    }

    private String addEvent(String title, long startTime,
                            long endTime, int allDay, String eventLocation) {
        TimeZone tz = TimeZone.getDefault();

        ContentValues event = new ContentValues();
        event.put("calendar_id", 1); // "" for insert
        event.put("title", title);
        event.put("description", "");
        event.put("eventLocation", eventLocation);
        //event.put("allDay", allDay);
        event.put("eventStatus", 1);
        //event.put("transparency", 0);
        event.put("dtstart", startTime);
        event.put("dtend", endTime);
        event.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());


        ContentResolver contentResolver = mContext.getContentResolver();
        //Uri eventsUri = Uri.parse("content://com.android.calendar/calendars");
         Uri eventsUri =  Uri.parse(String.valueOf(CalendarContract.Events.CONTENT_URI));
        //Uri url = contentResolver.insert(eventsUri, event);
        Uri uri = contentResolver.insert(eventsUri, event);
        String ret = uri.toString();
        return ret;
    }

    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_CALENDAR)){

        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CALENDAR},STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){
            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                addGamesToCalendar();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
