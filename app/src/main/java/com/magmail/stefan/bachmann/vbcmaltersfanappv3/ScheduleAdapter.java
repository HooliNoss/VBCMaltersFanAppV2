package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Result;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Schedule;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by stefan.bachmann on 13.11.2015.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private Schedule[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtDate;
        public TextView txtTime;
        public TextView txtTeamHome;
        public TextView txtTeamAway;
        public SoapObject data;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtDate = (TextView) itemLayoutView.findViewById(R.id.txt_date);
            txtTime = (TextView) itemLayoutView.findViewById(R.id.txt_time);
            txtTeamHome = (TextView) itemLayoutView.findViewById(R.id.txt_teamHome);
            txtTeamAway = (TextView) itemLayoutView.findViewById(R.id.txt_teamAway);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleAdapter(Schedule[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_schedule, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.txtDate.setText(mDataset[position].getDate());
        holder.txtTime.setText(mDataset[position].getTime());
        holder.txtTeamHome.setText(mDataset[position].getTeamHome());
        holder.txtTeamAway.setText(mDataset[position].getTeamAway());
        holder.data = mDataset[position].getmSoapObject();

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

