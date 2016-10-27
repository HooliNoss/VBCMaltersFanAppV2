package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Result;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Team;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers.SOAPConnection;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.VBCData.DataGenerator;

import org.ksoap2.serialization.SoapObject;


import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<Result> resultList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        team = DataGenerator.getTeamById(getIntent().getIntExtra("teamId", 0));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        //appBarLayout.setExpanded(false, false);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "placeholder_thumb");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Rangliste");
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.theme_color_vbc));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.theme_color_vbc));

        mImageView = (ImageView) findViewById(R.id.resultImage);

        int id = getResources().getIdentifier(team.getM_ImgSrc()+MainActivity.THUMBEXTENSION, "drawable", getPackageName());
        mImageView.setImageResource(id);

        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_resultActivity);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getResults();
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

    private void getResults()
    {
        mTask = new AsyncTask<Integer, Integer, Integer>()
        {
            @Override
            protected void onPreExecute()
            {
                progressDialog = ProgressDialog.show(ResultActivity.this, "", "Lade Rangliste...", false, true);
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
                    //String teamID = Integer.toString(team.get_Team_ID());
                    String groupID = Integer.toString(team.getM_GroupId());

                    mySoapObject = myConnection.getSOAPConnection("http://myvolley.swissvolley.ch/getTable", "getTable", "group_ID", groupID );
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
                    Result[] myDataset = resultList.toArray(new Result[resultList.size()]);

                    // specify an adapter (see also next example)
                    mAdapter = new ResultAdapter(myDataset);
                    mRecyclerView.setAdapter(mAdapter);
                }
                else {
                    AlertDialog.Builder b = new AlertDialog.Builder(ResultActivity.this);
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
        resultList = new ArrayList<Result>();

        Result header = new Result();
        header.setmTeam("Team");
        header.setmNumberOfGames("Anzahl Spiele");
        header.setmPoints("Punkte");
        header.setmRank("#");
        resultList.add(header);

        try {
            if (data.getProperty(0) != null) {
                SoapObject table = (SoapObject) data.getProperty(1);
                int i = 0;
                while(i < table.getPropertyCount()) {
                    SoapObject entry = (SoapObject)table.getProperty(i);

                    Result result = new Result();
                    String rank = entry.getProperty("Rank").toString();
                    if (rank.equals("&amp;nbsp;"))
                        rank = " ";

                    result.setmRank(rank);
                    result.setmTeam(entry.getProperty("Caption").toString());
                    result.setmNumberOfGames(entry.getProperty("NumberOfGames").toString());
                    result.setmPoints(entry.getProperty("Points").toString());

                    String setProportion = entry.getProperty("SetsWon").toString() + " : " + entry.getProperty("SetsLost").toString();
                    String setQuotient = entry.getProperty("SetQuotient").toString();
                    String pointProportion = entry.getProperty("BallsWon").toString() + " : " + entry.getProperty("BallsLost").toString();
                    String pointQuotient = entry.getProperty("BallsQuotient").toString();

                    result.setmSetProportion(setProportion);
                    result.setmSetQuotient(setQuotient);
                    result.setmPointProportion(pointProportion);
                    result.setmPointQuotient(pointQuotient);
                    result.setmSoapObject(entry);

                    resultList.add(result);

                    i++;
                }
            }
        }
        catch(Exception ex) {
            String message = ex.getMessage();
        }
    }
}
