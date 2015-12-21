package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Comment;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.News;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers.ServerConnection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stefan.bachmann on 13.11.2015.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Comment[] mDataset;
    private Context mContext;
    private boolean mIsAdmin;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtAuthor;
        public TextView txtDate;
        public TextView txtBody;
        public ImageView imgComment;
        View itemView;
        public ImageButton btnMoreContent;
        private ProgressDialog progressDialog;
        private int commentId;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtAuthor = (TextView) itemLayoutView.findViewById(R.id.txt_author);
            txtDate = (TextView) itemLayoutView.findViewById(R.id.txt_date_comment);
            txtBody = (TextView) itemLayoutView.findViewById(R.id.txt_body_comment);
            imgComment = (ImageView) itemLayoutView.findViewById(R.id.img_comment);
            btnMoreContent = (ImageButton) itemLayoutView.findViewById(R.id.btn_MoreContent_comment);
            btnMoreContent.setOnClickListener(btnMoreContentOnClickListener);

            itemView = itemLayoutView;
        }

        public View.OnClickListener btnMoreContentOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {

                String[] items = {"Löschen"};
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle("Bearbeiten")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 0) {
                                    DeleteComment();
                                }
                            }
                        });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };

        public void DeleteComment() {

            RequestQueue queue = Volley.newRequestQueue(itemView.getContext());
            String url = ServerConnection.SERVERURL + "DeleteComment.php";

            progressDialog = ProgressDialog.show(itemView.getContext(), "", "Lösche Kommentar...", false, true);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            progressDialog.dismiss();

                            Intent intent = new Intent(itemView.getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            itemView.getContext().startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();

                            AlertDialog.Builder b = new AlertDialog.Builder(itemView.getContext());
                            b.setTitle(android.R.string.dialog_alert_title);
                            b.setMessage("Es konnte keine Verbindung hergestellt werden");
                            b.setPositiveButton(itemView.getContext().getString(android.R.string.ok),
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
                    params.put("commentID", Integer.toString(commentId));
                    return params;
                }
            };
            queue.add(stringRequest);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentAdapter(Context context, Comment[] myDataset, boolean isAdmin) {
        mContext = context;
        mDataset = myDataset;
        mIsAdmin = isAdmin;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_comments, parent, false);
        // set the view's size, margins, paddings and layout parameters

        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat fmTime = new SimpleDateFormat("HH:mm");

            holder.txtAuthor.setText(mDataset[position].getAuthor());
            Date date = fmt.parse(mDataset[position].getDate());
            String formatedDate = fmtOut.format(date) + " " + fmTime.format(date);

            holder.txtDate.setText(formatedDate);
            holder.txtBody.setText(mDataset[position].getBody());
            holder.commentId = mDataset[position].getId();

            if (mIsAdmin) {
                holder.btnMoreContent.setVisibility(View.VISIBLE);
            } else {
                holder.btnMoreContent.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

