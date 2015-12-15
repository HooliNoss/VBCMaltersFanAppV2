package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddNewsActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mBody;
    private FloatingActionButton mFabSend;
    private Context mContext;
    private ProgressDialog progressDialog;

    private int mNewsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        mContext = this;

        mTitle = (TextView) findViewById(R.id.txtTitle);
        mBody = (TextView) findViewById(R.id.txtBody);
        mFabSend = (FloatingActionButton) findViewById(R.id.fab_sendNews);
        mFabSend.setOnClickListener(fabSendNewsOnClickListener);

        mNewsId = getIntent().getIntExtra("newsID", 0);
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");

        if (title == null)
            title = "Titel";

        if (body == null)
            body = "Nachricht";

        mTitle.setText(title);
        mBody.setText(body);
    }

    private View.OnClickListener fabSendNewsOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            String titel = mTitle.getText().toString();
            String meldung = mBody.getText().toString();

            if (titel.equals("") && meldung.equals("")) {
                final Dialog dialog = new Dialog(AddNewsActivity.this);
                dialog.setTitle("Fehlerhafte Eingabe");
                TextView txtDescription = new TextView(AddNewsActivity.this);
                txtDescription.setPadding(10, 0, 0, 10);

                txtDescription.setText("Bitte geben Sie einen Titel sowie eine Meldung ein");
                dialog.setContentView(txtDescription);

                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            } else {
                sendNews(titel, meldung);
            }
        }
    };

    private void sendNews(final String titel, final String meldung) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "";

        if (mNewsId == 0)
            url = "http://grodien.ddns.net:8080/SetNews.php";
        else
            url = "http://grodien.ddns.net:8080/UpdateNews.php";


        progressDialog = ProgressDialog.show(this, "", "Sende News...", false, true);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        progressDialog.dismiss();

                        Intent intent = new Intent(AddNewsActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        AlertDialog.Builder b = new AlertDialog.Builder(AddNewsActivity.this);
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (mNewsId != 0)
                    params.put("newsID", Integer.toString(mNewsId));

                params.put("title", titel);
                params.put("body", meldung);
                params.put("tag", "Info");
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
