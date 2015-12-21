package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Result;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Schedule;

import org.ksoap2.serialization.SoapObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stefan.bachmann on 13.11.2015.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private Schedule[] mDataset;
    private int[] colors = new int[]{0x30ffffff, 0x30000000};

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
        public RelativeLayout layout_schedule;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtDate = (TextView) itemLayoutView.findViewById(R.id.txt_date);
            txtTime = (TextView) itemLayoutView.findViewById(R.id.txt_time);
            txtTeamHome = (TextView) itemLayoutView.findViewById(R.id.txt_teamHome);
            txtTeamAway = (TextView) itemLayoutView.findViewById(R.id.txt_teamAway);
            layout_schedule = (RelativeLayout) itemLayoutView.findViewById(R.id.layout_schedule);
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
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat fmTime = new SimpleDateFormat("HH:mm");

            Date date = fmt.parse(mDataset[position].getDate());
            holder.txtDate.setText(fmtOut.format(date));

            holder.txtTime.setText(fmTime.format(date));

            holder.txtTeamHome.setText(mDataset[position].getTeamHome());
            holder.txtTeamAway.setText(mDataset[position].getTeamAway());
            holder.data = mDataset[position].getmSoapObject();

            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_YEAR, -1); // <--
            Date yesterday = cal.getTime();

            int colorPos;
            if (yesterday.after(date)) {
                colorPos = 1;
            } else {
                colorPos = 0;
            }

            holder.layout_schedule.setBackgroundColor(colors[colorPos]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

