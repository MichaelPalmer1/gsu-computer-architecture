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

import com.michaelpalmer.rancher.schema.Host;
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
 * Activities containing this fragment MUST implement the {@link OnHostsListFragmentInteractionListener}
 * interface.
 */
public class HostsListFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "project-id";
    private OnHostsListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private String mProjectId = "1a5";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HostsListFragment() {
    }

    public static HostsListFragment newInstance(String project) {
        HostsListFragment fragment = new HostsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HostsAPI().execute();

        if (getArguments() != null) {
            mProjectId = getArguments().getString(ARG_PROJECT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new HostsRecyclerViewAdapter(getContext(), Host.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHostsListFragmentInteractionListener) {
            mListener = (OnHostsListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHostsListFragmentInteractionListener");
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
     */
    public interface OnHostsListFragmentInteractionListener {
        void onHostsListFragmentInteraction(Host item);
    }

    private class HostsAPI extends AsyncTask<Void, Void, List<Host>> {

        private static final String TAG = "HostsAPI";

        /**
         * Perform the API call
         *
         * @param params Parameters
         * @return API Response as string
         */
        @Override
        protected List<Host> doInBackground(Void... params) {
            return fetchItems();
        }

        @Override
        protected void onPostExecute(List<Host> items) {
            Host.ITEMS = items;
            recyclerView.setAdapter(new HostsRecyclerViewAdapter(getContext(), Host.ITEMS, mListener));
        }

        private List<Host> fetchItems() {
            List<Host> items = new ArrayList<>();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String rancherUrl = preferences.getString("rancher_url", null);

            if (rancherUrl == null) {
                Log.w(TAG, "Rancher settings are not configured.");
                return items;
            }

            // Fetch the data
            String jsonString = API.GET(
                    String.format(Locale.US, "%s/v2-beta/projects/%s/hosts/", rancherUrl, mProjectId));
            Log.i(TAG, "Received JSON: " + jsonString);

            // Parse the data
            parseItems(items, jsonString);

            return items;
        }

        private void parseItems(List<Host> items, String data) {
            if (data == null) {
                return;
            }

            try {
                // Get base object
                JSONObject jsonBaseObject = new JSONObject(data);

                // Get the hosts
                JSONArray hosts = jsonBaseObject.optJSONArray("data");

                for (int i = 0; i < hosts.length(); i++) {
                    // Get host object
                    JSONObject host = hosts.optJSONObject(i);

                    // Get relevant data
                    String id = host.optString("id");
                    String name = host.optString("name");
                    JSONObject links = host.optJSONObject("links");

                    // Instantiate stack and add to list
                    Host item = new Host(id, name, links, host);
                    items.add(item);
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
            }
        }
    }
}
