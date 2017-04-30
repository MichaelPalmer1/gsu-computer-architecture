package com.michaelpalmer.rancher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnStackListFragmentInteractionListener}
 * interface.
 */
public class StacksListFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PROJECT_ID = "project-id";
    private OnStackListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private String mProjectId = "1a5";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StacksListFragment() {
    }

    public static StacksListFragment newInstance(String project) {
        StacksListFragment fragment = new StacksListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mProjectId = getArguments().getString(ARG_PROJECT_ID);
        }

        new StacksAPI().execute(mProjectId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stack_list, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_stack);
        fab.setOnClickListener(this);

        // Set the adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.stack_list_recyclerview);
        recyclerView.setAdapter(new StacksRecyclerViewAdapter(getContext(), Stack.ITEMS, mListener));

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_stack:
                // Go to create stack activity
                break;
        }
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
        void onStackListFragmentInteraction(Stack item);
    }

    private class StacksAPI extends AsyncTask<String, Void, List<Stack>> {

        private static final String TAG = "StacksAPI";

        /**
         * Perform the API call
         *
         * @param params Parameters
         * @return API Response as string
         */
        @Override
        protected List<Stack> doInBackground(String... params) {
            return fetchItems();
        }

        @Override
        protected void onPostExecute(List<Stack> items) {
            Stack.ITEMS = items;
            recyclerView.setAdapter(new StacksRecyclerViewAdapter(getContext(), Stack.ITEMS, mListener));
        }

        private List<Stack> fetchItems() {
            List<Stack> items = new ArrayList<>();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String rancherUrl = preferences.getString("rancher_url", null);

            if (rancherUrl == null) {
                Log.w(TAG, "Rancher settings are not configured.");
                return items;
            }

            // Fetch the data
            String jsonString = API.GET(
                    String.format(Locale.US, "%s/v2-beta/projects/%s/stacks/", rancherUrl, mProjectId));
            Log.i(TAG, "Received JSON: " + jsonString);

            // Parse the data
            parseItems(items, jsonString);

            return items;
        }

        private void parseItems(List<Stack> items, String data) {
            if (data == null) {
                return;
            }

            try {
                // Get base object
                JSONObject jsonBaseObject = new JSONObject(data);

                // Get the stacks
                JSONArray stacks = jsonBaseObject.optJSONArray("data");

                for (int i = 0; i < stacks.length(); i++) {
                    // Get stack object
                    JSONObject stack = stacks.optJSONObject(i);

                    // Get relevant data
                    String id = stack.optString("id");
                    String name = stack.optString("name");
                    String state = stack.optString("state");
                    String description = stack.optString("description");
                    String group = stack.optString("group");
                    String healthState = stack.optString("healthState");
                    JSONObject links = stack.optJSONObject("links");
                    JSONObject actions = stack.optJSONObject("actions");

                    if (description.equals("null")) {
                        description = null;
                    }

                    // Instantiate stack and add to list
                    Stack item = new Stack(id, name, state, description, group, healthState, links, actions, stack);
                    items.add(item);
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
            }
        }
    }
}
