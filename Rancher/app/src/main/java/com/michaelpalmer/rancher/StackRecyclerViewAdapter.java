package com.michaelpalmer.rancher;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelpalmer.rancher.StacksListFragment.OnStackListFragmentInteractionListener;
import com.michaelpalmer.rancher.schema.Stack;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Stack} and makes a call to the
 * specified {@link StacksListFragment.OnStackListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class StackRecyclerViewAdapter extends RecyclerView.Adapter<StackRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Stack> mValues;
    private final StacksListFragment.OnStackListFragmentInteractionListener mListener;

    public StackRecyclerViewAdapter(Context context, List<Stack> items, OnStackListFragmentInteractionListener
            listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_stack_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mDescriptionView.setText(mValues.get(position).getDescription());

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

            default:
                holder.mStateView.setBackgroundColor(Color.WHITE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onStackListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final View mStateView;
        public final TextView mNameView;
        public final TextView mDescriptionView;
        public Stack mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mStateView = view.findViewById(R.id.stack_state);
            mNameView = (TextView) view.findViewById(R.id.stack_name);
            mDescriptionView = (TextView) view.findViewById(R.id.stack_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescriptionView.getText() + "'";
        }
    }
}
