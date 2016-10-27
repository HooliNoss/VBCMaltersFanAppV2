package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers.ServerConnection;

import java.util.HashMap;
import java.util.Map;

public class AddCommentActivity extends AppCompatActivity {

    private TextView mTextViewAuthor;
    private TextView mTextViewBody;
    private FloatingActionButton mFabSend;
    private Context mContext;
    private ProgressDialog progressDialog;

    private int mNewsId;
    private String mTitle;
    private String mDate;
    private String mBody;
    private String mNewsTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        mContext = this;

        mTextViewAuthor = (TextView) findViewById(R.id.txtAuthor);
        mTextViewBody = (TextView) findViewById(R.id.txtBody);
        mFabSend = (FloatingActionButton) findViewById(R.id.fab_sendComment);
        mFabSend.setOnClickListener(fabSendNewsOnClickListener);

        mNewsId = getIntent().getIntExtra("newsId", 0);
        mTitle = getIntent().getStringExtra("title");
        mDate = getIntent().getStringExtra("date");
        mBody = getIntent().getStringExtra("body");
        mNewsTag = getIntent().getStringExtra("newsTag");
    }

    private View.OnClickListener fabSendNewsOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            String creator = mTextViewAuthor.getText().toString();
            String meldung = mTextViewBody.getText().toString();

            if (creator.equals("") && meldung.equals("")) {
                final Dialog dialog = new Dialog(AddCommentActivity.this);
                dialog.setTitle("Fehlerhafte Eingabe");
                TextView txtDescription = new TextView(AddCommentActivity.this);
                txtDescription.setPadding(10, 0, 0, 10);

                txtDescription.setText("Bitte geben Sie Ihren Namen sowie eine Meldung ein");
                dialog.setContentView(txtDescription);

                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            } else {
                sendComment(creator, meldung);
            }
        }
    };

    private void sendComment(final String creator, final String meldung) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerConnection.SERVERURL +  "SetComment.php";

        progressDialog = ProgressDialog.show(this, "", "Sende Kommentar...", false, true);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        progressDialog.dismiss();

                        Intent intent = new Intent(AddCommentActivity.this, CommentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("newsId", mNewsId);
                        intent.putExtra("title", mTitle);
                        intent.putExtra("date", mDate);
                        intent.putExtra("body", mBody);
                        intent.putExtra("newsTag", mNewsTag);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        AlertDialog.Builder b = new AlertDialog.Builder(AddCommentActivity.this);
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
                params.put("newsID", Integer.toString(mNewsId));
                params.put("creator", creator);
                params.put("body", meldung);
                return params;
            }
        };
        stringRequest.setTag(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}
