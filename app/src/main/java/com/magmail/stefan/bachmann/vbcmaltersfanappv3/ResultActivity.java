package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Result;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Team;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<Result> resultList;
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
        setContentView(R.layout.activity_result);

        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_resultActivity);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        team = DataGenerator.getTeamById(getIntent().getIntExtra("teamId", 0));

        getResults();
    }

    private void getResults()
    {
        new AsyncTask<Integer, Integer, Integer>()
        {
            @Override
            protected void onPreExecute()
            {
                progressDialog = ProgressDialog.show(ResultActivity.this, "", "Loading Results...", false, true);
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
                    result.setmRank(entry.getProperty("Rank").toString());
                    result.setmTeam(entry.getProperty("Caption").toString());
                    result.setmNumberOfGames(entry.getProperty("NumberOfGames").toString());
                    result.setmPoints(entry.getProperty("Points").toString());
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
