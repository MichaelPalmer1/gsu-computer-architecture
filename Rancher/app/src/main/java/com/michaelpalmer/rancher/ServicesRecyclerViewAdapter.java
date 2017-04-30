package com.michaelpalmer.rancher;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelpalmer.rancher.ServicesListFragment.OnServiceListFragmentInteractionListener;
import com.michaelpalmer.rancher.schema.Service;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Service} and makes a call to the
 * specified {@link OnServiceListFragmentInteractionListener}.
 */
public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<ServicesRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Service> mValues;
    private final OnServiceListFragmentInteractionListener mListener;

    public ServicesRecyclerViewAdapter(Context context, List<Service> items, OnServiceListFragmentInteractionListener
            listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_service_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(holder.mItem.getName());
        if (holder.mItem.getDescription() != null) {
            holder.mDescriptionView.setText(holder.mItem.getDescription());
        } else {
            holder.mDescriptionView.setText(" ");
        }
        holder.mScaleView.setText(holder.mItem.getProperty("currentScale").toString());

        switch (holder.mItem.getHealthState()) {
            case "healthy":
                holder.mStateView.setBackgroundColor(mContext.getResources().getColor(R.color.colorActive));
                break;

            case "started-once":
                holder.mStateView.setBackgroundColor(mContext.getResources().getColor(R.color.colorActive));
                break;

            case "unhealthy":
                holder.mStateView.setBackgroundColor(mContext.getResources().getColor(R.color.colorInactive));
                break;

            case "inactive":
                holder.mStateView.setBackgroundColor(mContext.getResources().getColor(R.color.colorInactive));
                break;

            case "initializing":
                holder.mStateView.setBackgroundColor(mContext.getResources().getColor(R.color.colorUnhealthy));
                break;

            default:
                holder.mStateView.setBackgroundColor(Color.WHITE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onServiceListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final View mStateView;
        private final TextView mNameView, mDescriptionView, mScaleView;
        private Service mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mStateView = view.findViewById(R.id.service_state);
            mNameView = (TextView) view.findViewById(R.id.service_name);
            mDescriptionView = (TextView) view.findViewById(R.id.service_description);
            mScaleView = (TextView) view.findViewById(R.id.service_scale);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
