package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Result;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by stefan.bachmann on 13.11.2015.
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private Result[] mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtRank;
        public TextView txtTeam;
        public TextView txtNumberOfGames;
        public TextView txtPoints;
        public SoapObject data;
        public RelativeLayout layout_result;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtRank = (TextView) itemLayoutView.findViewById(R.id.txt_rank);
            txtTeam = (TextView) itemLayoutView.findViewById(R.id.txt_team);
            txtNumberOfGames = (TextView) itemLayoutView.findViewById(R.id.txt_numberOfGames);
            txtPoints = (TextView) itemLayoutView.findViewById(R.id.txt_points);
            layout_result = (RelativeLayout) itemLayoutView.findViewById(R.id.layout_result);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ResultAdapter(Result[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_results, parent, false);
        // set the view's size, margins, paddings and layout parameters
        mContext = parent.getContext();
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

        final int finalPosition = position;
        holder.txtTeam.setText(mDataset[position].getmTeam());
        holder.txtNumberOfGames.setText(mDataset[position].getmNumberOfGames());
        holder.txtPoints.setText(mDataset[position].getmPoints());
        holder.txtRank.setText(mDataset[position].getmRank());
        holder.data = mDataset[position].getmSoapObject();

        holder.layout_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
                dialog.setTitle(mDataset[finalPosition].getmTeam());
                dialog.setMessage("Satzverhältnis:  " + mDataset[finalPosition].getmSetProportion() + " \r\n" +
                                  "Satzquotient:    " + mDataset[finalPosition].getmSetQuotient() + "\r\n" +
                                  "Punkteverhältnis: " + mDataset[finalPosition].getmPointProportion() + "\r\n" +
                                  "Punktequotient:   " + mDataset[finalPosition].getmPointQuotient()
                );
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

