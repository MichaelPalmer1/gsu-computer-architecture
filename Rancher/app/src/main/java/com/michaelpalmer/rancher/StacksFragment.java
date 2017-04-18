package com.michaelpalmer.rancher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnStackListFragmentInteractionListener}
 * interface.
 */
public class StacksFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "project-id";
    private OnStackListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private String mProjectId = "1a5";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StacksFragment() {
    }

    public static StacksFragment newInstance(String project) {
        StacksFragment fragment = new StacksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new StacksAPI().execute();

        if (getArguments() != null) {
            mProjectId = getArguments().getString(ARG_PROJECT_ID);
        }
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
