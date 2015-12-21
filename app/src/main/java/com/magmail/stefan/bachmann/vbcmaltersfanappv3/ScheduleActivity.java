package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

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

public class ScheduleActivity extends AppCompatActivity {

    private ArrayList<Schedule> scheduleList;
    private ProgressDialog progressDialog;
    SoapObject mySoapObject;
    private Context mContext;

    private Team team;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView mImageView;

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

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "placeholder_thumb");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.theme_color_vbc));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.theme_color_vbc));

        mImageView = (ImageView) findViewById(R.id.scheduleImage);

        if (team != null) {
            collapsingToolbarLayout.setTitle("Spielplan");
            int id = getResources().getIdentifier(team.getM_ImgSrc()+MainActivity.THUMBEXTENSION, "drawable", getPackageName());
            mImageView.setImageResource(id);
        }
        else{
            collapsingToolbarLayout.setTitle("Die nächsten Spiele");
            int id = getResources().getIdentifier("placeholder_thumb", "drawable", getPackageName());
            mImageView.setImageResource(id);
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

    private void getSchedule()
    {
        new AsyncTask<Integer, Integer, Integer>()
        {
            @Override
            protected void onPreExecute()
            {
                progressDialog = ProgressDialog.show(ScheduleActivity.this, "", "Lade Spielpläne...", false, true);
            }

            @Override
            protected Integer doInBackground(Integer... params)
            {
                if (params == null)
                {
                    return 0;
                }
                try
                {
                    SOAPConnection myConnection = new SOAPConnection();

                    if (team != null) {
                        mySoapObject = myConnection.getSOAPConnection("http://myvolley.swissvolley.ch/getGamesTeam", "getGamesTeam", "team_ID", Integer.toString(team.getM_TeamId()));
                    }
                    else{
                        mySoapObject = myConnection.getSOAPConnection("http://myvolley.swissvolley.ch/getGamesByClub", "getGamesByClub", "ID_club", Integer.toString(DataGenerator.getClub().getM_ClubId()));
                    }
                }
                catch (Exception e)
                {
                    Log.e("tag", e.getMessage());
                    return 0;
                }
                return 1;
            }

            @Override
            protected void onPostExecute(Integer result)
            {
                progressDialog.dismiss();

                if (mySoapObject != null && result == 1)
                {
                    addDataToList(mySoapObject);
                    Schedule[] myDataset;

                    if (scheduleList.size() < 20){
                        myDataset = scheduleList.toArray(new Schedule[scheduleList.size()]);
                    }
                    else {

                        ArrayList<Schedule> tmpList = new ArrayList<Schedule>();
                        for(Schedule schedule: scheduleList){
                            String dateString = (String)schedule.getDate();
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


                    // Listener
//                    _mainListView.setOnItemClickListener(new OnItemClickListener() {
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            // Get the HashMap of the clicked item
//                            HashMap<String, Object> user = resultList.get(position);
//
//                            // Get Attribute name of the HashMap
//
//
//
//                            SoapObject entry = (SoapObject)user.get("object");
//
//                            String teamName = (String)entry.getProperty("Caption");
//                            String setsWon = entry.getProperty("SetsWon").toString();
//                            String setsLost = entry.getProperty("SetsLost").toString();
//                            String setsQuotient = entry.getProperty("SetQuotient").toString();
//                            String ballsWon = entry.getProperty("BallsWon").toString();
//                            String ballsLost = entry.getProperty("BallsLost").toString();
//                            String ballsQuotient = entry.getProperty("BallsQuotient").toString();
//
//
//                            // Create new Dialog
//                            final Dialog dialog = new Dialog(ResultActivity.this);
//                            dialog.setTitle("Detail of " + teamName);
//                            TextView txtDescription = new TextView(ResultActivity.this);
//                            txtDescription.setPadding(10, 0, 0, 10);
//                            txtDescription.setText("Sätze:   " + setsWon + " : " + setsLost + " \r\n" +
//                                            "Satzquotient:     " + setsQuotient +  "\r\n" +
//                                            "Punkte:   " + ballsWon + " : " + ballsLost  + " \r\n" +
//                                            "Ballquotient:     " + ballsQuotient
//
//                            );
//                            dialog.setContentView(txtDescription);
//
//                            dialog.setCanceledOnTouchOutside(true);
//                            dialog.show();
//                        }
//                    });
                }
                else {
                    AlertDialog.Builder b = new AlertDialog.Builder(ScheduleActivity.this);
                    b.setTitle(android.R.string.dialog_alert_title);
                    b.setMessage("Es konnte keine Verbindung hergestellt werden");
                    b.setPositiveButton(getString(android.R.string.ok),
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dlg, int arg1){
                                    dlg.dismiss();
                                }
                            });
                    b.create().show();
                }
            }
        }.execute(30000);
    }

    private void addDataToList(SoapObject data){
        scheduleList = new ArrayList<Schedule>();

        try {
            if (data.getProperty(0) != null) {
                SoapObject table = (SoapObject) data.getProperty(1);
                int i = 0;
                while(i < table.getPropertyCount()) {
                    SoapObject entry = (SoapObject)table.getProperty(i);

                    Schedule schedule = new Schedule();
                    schedule.setDate(entry.getProperty("PlayDate").toString());
                    schedule.setTeamHome(entry.getProperty("TeamHomeCaption").toString());
                    schedule.setTeamAway(entry.getProperty("TeamAwayCaption").toString());
                    schedule.setmSoapObject(entry);

                    scheduleList.add(schedule);

                    i++;
                }
            }
        }
        catch(Exception ex) {
            String message = ex.getMessage();
        }
    }
}
