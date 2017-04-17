package com.michaelpalmer.rancher;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelpalmer.rancher.schema.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnStackListFragmentInteractionListener}
 * interface.
 */
public class StacksFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnStackListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StacksFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StacksFragment newInstance(int columnCount) {
        StacksFragment fragment = new StacksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new StacksAPI().execute();

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stack_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new StackRecyclerViewAdapter(getContext(), Stack.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStackListFragmentInteractionListener) {
            mListener = (OnStackListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStackListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStackListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onStackListFragmentInteraction(Stack item);
    }

    private class StacksAPI extends AsyncTask<Void, Void, List<Stack>> {

        private static final String TAG = "StacksAPI";

        /**
         * Perform the API call
         *
         * @param params Parameters
         * @return API Response as string
         */
        @Override
        protected List<Stack> doInBackground(Void... params) {
            return fetchItems();
        }

        @Override
        protected void onPostExecute(List<Stack> items) {
            Stack.ITEMS = items;
            recyclerView.setAdapter(new StackRecyclerViewAdapter(getContext(), Stack.ITEMS, mListener));
        }

        private List<Stack> fetchItems() {
            List<Stack> items = new ArrayList<>();

            // Fetch the data
            String jsonString = API.GET(
                    "http://rancher.aws/v2-beta/projects/1a5/stacks/",
                    "4C78A5C44C14140A6576",
                    "FUetQLpzdCYZmvYWVVa9KSzck6xgGKgzWUnzgBsJ"
            );
            Log.i(TAG, "Received JSON: " + jsonString);

            // Parse the data
            parseItems(items, jsonString);

            return items;
        }

        private void parseItems(List<Stack> items, String data) {
            try {
                // Get base object
                JSONObject jsonBaseObject = new JSONObject(data);

                // Get the stacks
                JSONArray stacks = jsonBaseObject.getJSONArray("data");

                for (int i = 0; i < stacks.length(); i++) {
                    // Get photo object
                    JSONObject stack = stacks.getJSONObject(i);

                    // Get relevant data
                    String id = stack.getString("id");
                    String name = stack.getString("name");
                    String state = stack.getString("state");
                    String description = stack.getString("description");
                    String group = stack.getString("group");
                    String healthState = stack.getString("healthState");
                    JSONObject links = stack.getJSONObject("links");
                    JSONObject actions = stack.getJSONObject("actions");

                    if (description == null) {
                        description = "";
                    }

                    // Instantiate stack and add to list
                    Stack item = new Stack(id, name, state, description, group, healthState, links, actions);
                    items.add(item);
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
            }
        }
    }
}
