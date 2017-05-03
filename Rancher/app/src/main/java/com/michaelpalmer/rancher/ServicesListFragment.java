package com.michaelpalmer.rancher;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.michaelpalmer.rancher.schema.Service;
import com.michaelpalmer.rancher.schema.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnServiceListFragmentInteractionListener}
 * interface.
 */
public class ServicesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_STACK_ID = "stack-id", ARG_SERVICES_URL = "services-url";
    private String mStackId = null, mServicesUrl = null;
    private OnServiceListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ServicesListFragment() {
    }

    public static ServicesListFragment newInstance(Stack stack) {
        ServicesListFragment fragment = new ServicesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STACK_ID, stack.getId());
        try {
            args.putString(ARG_SERVICES_URL, stack.getLinks().getString("services"));
        } catch (JSONException e) {
            // Let it go
        }
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Load the services
     */
    private void loadServices() {
        new ServicesAPI().execute(mServicesUrl);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mStackId = getArguments().getString(ARG_STACK_ID);
            mServicesUrl = getArguments().getString(ARG_SERVICES_URL);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);

        // Set the adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(new ServicesRecyclerViewAdapter(getContext(), Service.ITEMS, mListener));

        // Setup swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        // Load the services
        loadServices();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_stack_service, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                // Open add activity
                Intent intent = new Intent(getContext(), AddServiceActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnServiceListFragmentInteractionListener) {
            mListener = (OnServiceListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnServiceListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        loadServices();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnServiceListFragmentInteractionListener {
        void onServiceListFragmentInteraction(Service item);
    }

    private class ServicesAPI extends AsyncTask<String, Void, List<Service>> {

        private static final String TAG = "ServicesAPI";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        }

        /**
         * Perform the API call
         *
         * @param params Parameters
         * @return API Response as string
         */
        @Override
        protected List<Service> doInBackground(String... params) {
            return fetchItems(params[0]);
        }

        @Override
        protected void onPostExecute(List<Service> items) {
            Service.ITEMS = items;
            recyclerView.setAdapter(new ServicesRecyclerViewAdapter(getContext(), Service.ITEMS, mListener));
            swipeRefreshLayout.setRefreshing(false);
        }

        private List<Service> fetchItems(String url) {
            List<Service> items = new ArrayList<>();

            // Fetch the data
            String jsonString = API.GET(url);
            Log.i(TAG, "Received JSON: " + jsonString);

            // Parse the data
            parseItems(items, jsonString);

            return items;
        }

        private void parseItems(List<Service> items, String data) {
            try {
                // Get base object
                JSONObject jsonBaseObject = new JSONObject(data);

                // Get the services
                JSONArray services = jsonBaseObject.getJSONArray("data");

                for (int i = 0; i < services.length(); i++) {
                    // Get service object
                    JSONObject service = services.getJSONObject(i);

                    // Get relevant data
                    String id = service.getString("id");
                    String name = service.getString("name");
                    String state = service.getString("state");
                    String description = service.getString("description");
                    String healthState = service.getString("healthState");
                    JSONObject links = service.getJSONObject("links");

                    if (description.equals("null")) {
                        description = null;
                    }

                    // Instantiate service and add to list
                    Service item = new Service(id, name, state, description, healthState, links, service);
                    items.add(item);
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
            }
        }
    }
}
