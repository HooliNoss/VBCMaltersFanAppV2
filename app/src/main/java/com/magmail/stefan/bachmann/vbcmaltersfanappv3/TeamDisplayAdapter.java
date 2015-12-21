package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Team;

/**
 * Created by stefan.bachmann on 13.11.2015.
 */
public class TeamDisplayAdapter extends RecyclerView.Adapter<TeamDisplayAdapter.ViewHolder> {
    private Team[] mDataset;
    private Context mContext;
    private Class mNextActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView teamName;
        TextView league;
        ImageView thumb_image;

        View itemView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            teamName = (TextView)itemLayoutView.findViewById(R.id.txt_teamName); // teamname
            league = (TextView)itemLayoutView.findViewById(R.id.txt_teamLeague); // league
            thumb_image=(ImageView)itemLayoutView.findViewById(R.id.list_image); // thumb image
            itemView = itemLayoutView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TeamDisplayAdapter(Context context, Team[] myDataset, Class nextActivity) {
        mContext = context;
        mDataset = myDataset;
        mNextActivity = nextActivity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TeamDisplayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_teamdisplay, parent, false);
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
        // Setting all values in listview
        final Context context =  holder.thumb_image.getContext();
        int id = context.getResources().getIdentifier(mDataset[position].getM_ImgSrc()+MainActivity.THUMBEXTENSION, "drawable", context.getPackageName());
        holder.thumb_image.setImageResource(id);

        holder.teamName.setText(mDataset[position].getM_Name());
        holder.league.setText(mDataset[position].getM_League());
        //holder.thumb_image.setImageURI(Uri.parse(mDataset[position].getM_ImgSrc()));
        final int teamId = mDataset[position].getM_TeamId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, mNextActivity);
                intent.putExtra("teamId", teamId);
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

