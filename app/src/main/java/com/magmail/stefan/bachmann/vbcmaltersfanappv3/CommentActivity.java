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
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Comment;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.News;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Result;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Team;

import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private final String KEY_ITEM = "entry"; // parent node

    private ArrayList<Comment> commentList;
    private Context mContext;

    private ProgressDialog progressDialog;
    XMLParser parser;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView txtTitle;
    private TextView txtDate;
    private TextView txtBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_commentActivity);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        txtTitle = (TextView) findViewById(R.id.txt_title_original_comment);
        txtDate = (TextView) findViewById(R.id.txt_date_original_comment);
        txtBody = (TextView) findViewById(R.id.txt_body_original_comment);

        int newsId = getIntent().getIntExtra("newsId", 0);
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String body = getIntent().getStringExtra("body");

        txtTitle.setText(title);
        txtDate.setText(date);
        txtBody.setText(body);

        parser = new XMLParser();
        getComments(newsId);
    }

    private void getComments(int newsId)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://grodien.ddns.net:8080/GetComments.php?commentID=" + Integer.toString(newsId);

        progressDialog = ProgressDialog.show(this, "", "Lade Kommentare...", false, true);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        progressDialog.dismiss();

                        Document doc = parser.getDomElement(response); // getting DOM element
                        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

                        commentList = new ArrayList<Comment>();
                        // looping through all item nodes <item>
                        for (int i = 0; i < nl.getLength(); i++)
                        {
                            Element e = (Element) nl.item(i);

                            Comment comment = new Comment();
                            comment.setAuthor(parser.getValue(e, "CommentCreator"));

                            String date = parser.getValue(e, "CommentDate");
                            String formatedDate = date.substring(0, date.length() - 7);
                            comment.setDate(formatedDate);
                            comment.setBody(parser.getValue(e, "CommentBody"));
                            comment.setCommentObject(e);
                            commentList.add(comment);
                        }

                        Comment[] myDataset = commentList.toArray(new Comment[commentList.size()]);
                        mAdapter = new CommentAdapter(CommentActivity.this, myDataset);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                AlertDialog.Builder b = new AlertDialog.Builder(CommentActivity.this);
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
        });
        queue.add(stringRequest);
    }
}
