package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Result;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Schedule;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Team;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    private ArrayList<Schedule> scheduleList;
    private ProgressDialog progressDialog;
    SoapObject mySoapObject;
    private Context mContext;

    private Team team;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_scheduleActivity);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        team = DataGenerator.getTeamById(getIntent().getIntExtra("teamId", 0));

        getSchedule();
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

                    mySoapObject = myConnection.getSOAPConnection("http://myvolley.swissvolley.ch/getGamesTeam", "getGamesTeam", "team_ID", Integer.toString(team.getM_TeamId()));
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
                    Schedule[] myDataset = scheduleList.toArray(new Schedule[scheduleList.size()]);

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
