package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.News;

/**
 * Created by stefan.bachmann on 13.11.2015.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private News[] mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtTitle;
        public TextView txtDate;
        public TextView txtBody;
        View itemView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtTitle = (TextView) itemLayoutView.findViewById(R.id.txt_title);
            txtDate = (TextView) itemLayoutView.findViewById(R.id.txt_date_news);
            txtBody = (TextView) itemLayoutView.findViewById(R.id.txt_body);

            itemView = itemLayoutView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(Context context, News[] myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_news, parent, false);
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtTitle.setText(mDataset[position].getTitle());
        holder.txtDate.setText(mDataset[position].getDate());
        holder.txtBody.setText(mDataset[position].getBody());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("newsId", mDataset[position].getId());
                intent.putExtra("title", mDataset[position].getTitle());
                intent.putExtra("date", mDataset[position].getDate());
                intent.putExtra("body", mDataset[position].getBody());
                mContext.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

