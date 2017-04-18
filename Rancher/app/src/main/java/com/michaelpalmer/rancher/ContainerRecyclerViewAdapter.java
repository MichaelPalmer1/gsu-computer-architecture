package com.michaelpalmer.rancher;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelpalmer.rancher.schema.Container;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Container} and makes a call to the
 * specified {@link ContainerListFragment.OnContainerListFragmentInteractionListener}.
 */
public class ContainerRecyclerViewAdapter extends RecyclerView.Adapter<ContainerRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private final List<Container> mValues;
    private final ContainerListFragment.OnContainerListFragmentInteractionListener mListener;

    public ContainerRecyclerViewAdapter(Context context, List<Container> items, ContainerListFragment
            .OnContainerListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_container_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mDescriptionView.setText(mValues.get(position).getDescription());

        switch (holder.mItem.getState()) {
            case "running":
                holder.mStateView.setBackgroundColor(mContext.getResources().getColor(R.color.colorActive));
                break;

            case "inactive":
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
                    mListener.onContainerListFragmentInteraction(holder.mItem);
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
        public Container mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mStateView = view.findViewById(R.id.container_state);
            mNameView = (TextView) view.findViewById(R.id.container_name);
            mDescriptionView = (TextView) view.findViewById(R.id.container_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescriptionView.getText() + "'";
        }
    }
}
