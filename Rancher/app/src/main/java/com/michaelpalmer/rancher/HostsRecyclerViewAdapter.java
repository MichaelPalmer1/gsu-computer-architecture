package com.michaelpalmer.rancher;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelpalmer.rancher.schema.Host;
import com.michaelpalmer.rancher.schema.Stack;

import org.json.JSONArray;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Host} and makes a call to the
 * specified {@link HostsListFragment.OnHostsListFragmentInteractionListener}.
 */
public class HostsRecyclerViewAdapter extends RecyclerView.Adapter<HostsRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Host> mValues;
    private final HostsListFragment.OnHostsListFragmentInteractionListener mListener;

    public HostsRecyclerViewAdapter(Context context, List<Host> items,
                                    HostsListFragment.OnHostsListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_host_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(holder.mItem.getName());

        try {
            if (holder.mItem.getProperty("description") != null) {
                holder.mDescriptionView.setText(holder.mItem.getProperty("description").toString());
            } else {
                holder.mDescriptionView.setText(" ");
            }
        } catch (NullPointerException e) {
            holder.mDescriptionView.setText(" ");
        }

        try {
            JSONArray instanceIds = (JSONArray) holder.mItem.getProperty("instanceIds");
            holder.mContainerCountView.setText(String.valueOf(instanceIds.length()));
        } catch (NullPointerException e) {
            holder.mContainerCountView.setText("N/A");
        }

        try {
            switch (holder.mItem.getProperty("state").toString()) {
                case "active":
                    holder.mStateView.setBackgroundColor(mContext.getResources().getColor(R.color.colorActive));
                    break;

                case "inactive":
                    holder.mStateView.setBackgroundColor(mContext.getResources().getColor(R.color.colorInactive));
                    break;

                default:
                    holder.mStateView.setBackgroundColor(Color.WHITE);
            }
        } catch (NullPointerException e) {
            holder.mStateView.setBackgroundColor(Color.WHITE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onHostsListFragmentInteraction(holder.mItem);
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
        private final TextView mNameView, mDescriptionView, mContainerCountView;
        private Host mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mStateView = view.findViewById(R.id.host_state);
            mNameView = (TextView) view.findViewById(R.id.host_name);
            mDescriptionView = (TextView) view.findViewById(R.id.host_description);
            mContainerCountView = (TextView) view.findViewById(R.id.host_container_count);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
