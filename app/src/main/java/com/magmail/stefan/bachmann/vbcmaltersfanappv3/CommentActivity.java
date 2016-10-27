package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Comment;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers.ServerConnection;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers.XMLParser;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.VBCData.DataGenerator;

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
    private ImageView imageViewComment;
    private int mNewsId;
    private String mTitle;
    private String mDate;
    private String mBody;
    private String mNewsTag;
    private boolean mIsAdmin;
    private boolean mIsAuthor;

    private FloatingActionButton mFabComment;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        //appBarLayout.setExpanded(false, false);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "placeholder_thumb");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.theme_color_vbc));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.theme_color_vbc));
        collapsingToolbarLayout.setTitle("Kommentare");

        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_commentActivity);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        txtTitle = (TextView) findViewById(R.id.txt_title_original_comment);
        txtDate = (TextView) findViewById(R.id.txt_date_original_comment);
        txtBody = (TextView) findViewById(R.id.txt_body_original_comment);
        imageViewComment = (ImageView) findViewById(R.id.CommentImage);

        mFabComment = (FloatingActionButton) findViewById(R.id.fab_comments);
        mFabComment.setOnClickListener(fabCommentOnClickListener);

        mNewsId = getIntent().getIntExtra("newsId", 0);
        mTitle = getIntent().getStringExtra("title");
        mDate = getIntent().getStringExtra("date");
        mBody = getIntent().getStringExtra("body");
        mNewsTag = getIntent().getStringExtra("newsTag");

        txtTitle.setText(mTitle);
        txtDate.setText(mDate);
        txtBody.setText(mBody);

        final Context context =  imageViewComment.getContext();
        String imageName = DataGenerator.getImageStringByNewsTag(mNewsTag);
        int id = context.getResources().getIdentifier(imageName+MainActivity.THUMBEXTENSION, "drawable", context.getPackageName());
        imageViewComment.setImageResource(id);

        checkPermissions();

        parser = new XMLParser();
        getComments(mNewsId);
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

    private void getComments(int newsId)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerConnection.SERVERURL + "GetComments.php?commentID=" + Integer.toString(newsId);

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
                            comment.setId(Integer.parseInt(parser.getValue(e, "CommentID").toString()));
                            comment.setAuthor(parser.getValue(e, "CommentCreator"));

                            String date = parser.getValue(e, "CommentDate");
                            String formatedDate = date.substring(0, date.length() - 7);
                            comment.setDate(formatedDate);
                            comment.setBody(parser.getValue(e, "CommentBody"));
                            comment.setCommentObject(e);
                            commentList.add(comment);
                        }

                        Comment[] myDataset = commentList.toArray(new Comment[commentList.size()]);
                        mAdapter = new CommentAdapter(CommentActivity.this, myDataset, mIsAdmin);
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
        stringRequest.setTag(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private View.OnClickListener fabCommentOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(CommentActivity.this, AddCommentActivity.class);
            intent.putExtra("newsId", mNewsId);
            intent.putExtra("title", mTitle);
            intent.putExtra("date", mDate);
            intent.putExtra("body", mBody);
            intent.putExtra("newsTag", mNewsTag);
            startActivity(intent);
        }
    };

    void checkPermissions()
    {
        SharedPreferences settings = getSharedPreferences(AppPreferences.PREFS_NAME, 0);
        mIsAdmin = settings.getBoolean(AppPreferences.IS_ADMIN, false);
        mIsAuthor = settings.getBoolean(AppPreferences.IS_AUTHOR, false);
    }
}
