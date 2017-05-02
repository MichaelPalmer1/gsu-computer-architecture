package com.michaelpalmer.rancher;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.michaelpalmer.rancher.schema.Container;
import com.michaelpalmer.rancher.schema.Host;
import com.michaelpalmer.rancher.schema.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnContainerListFragmentInteractionListener}
 * interface.
 */
public class ContainerListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnContainerListFragmentInteractionListener mListener;
    private static final String ARG_SERVICE_ID = "service-id", ARG_HOST_ID = "host-id",
            ARG_CONTAINERS_URL = "services-url";
    private String mHostId = null, mServiceId = null, mContainersUrl = null;
    private RecyclerView recyclerView;
    private Menu mOptionsMenu;
    private static Service service;
    private Host host;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static ContainerListFragment newInstance(Service service) {
        ContainerListFragment fragment = new ContainerListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SERVICE_ID, service.getId());
        try {
            args.putString(ARG_CONTAINERS_URL, service.getLinks().getString("instances"));
        } catch (JSONException e) {
            // Let it go
        }
        fragment.setArguments(args);
        ContainerListFragment.service = service;
        return fragment;
    }

    public static ContainerListFragment newInstance(Host host) {
        ContainerListFragment fragment = new ContainerListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HOST_ID, host.getId());
        try {
            args.putString(ARG_CONTAINERS_URL, host.getLinks().getString("instances"));
        } catch (JSONException e) {
            // Let it go
        }
        fragment.setArguments(args);
        fragment.host = host;
        return fragment;
    }

    public static ContainerListFragment newInstance(String rancherUrl, String projectId) {
        ContainerListFragment fragment = new ContainerListFragment();
        Bundle args = new Bundle();

        if (rancherUrl == null || projectId == null) {
            Log.w("ContainerListFragment", "Rancher settings are not configured.");
            return null;
        }

        String url = String.format(Locale.US, "%s/v2-beta/projects/%s/containers/", rancherUrl, projectId);
        args.putString(ARG_CONTAINERS_URL, url);

        fragment.setArguments(args);
        return fragment;
    }

    public static void setService(Service service) {
        ContainerListFragment.service = service;
    }

    public static Service getService() {
        return ContainerListFragment.service;
    }

    /**
     * Load the containers
     */
    private void loadContainers() {
        new ContainersAPI(
                getContext(),
                recyclerView,
                swipeRefreshLayout,
                mListener
        ).execute(mContainersUrl);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mServiceId = getArguments().getString(ARG_SERVICE_ID);
            mHostId = getArguments().getString(ARG_HOST_ID);
            mContainersUrl = getArguments().getString(ARG_CONTAINERS_URL);
        }

        setHasOptionsMenu(mServiceId != null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container_list, container, false);

        // Set the adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(new ContainerRecyclerViewAdapter(getContext(), Container.ITEMS, mListener));

        // Setup swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        // Load the containers
        loadContainers();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_container, menu);
        mOptionsMenu = menu;
        updateOptionsMenu();
    }

    private void updateOptionsMenu() {
        switch (service.getState()) {
            case "active":
                mOptionsMenu.findItem(R.id.action_start).setVisible(false).setEnabled(false);
                mOptionsMenu.findItem(R.id.action_restart).setVisible(true).setEnabled(true);
                mOptionsMenu.findItem(R.id.action_stop).setVisible(true).setEnabled(true);
                break;

            case "inactive":
                mOptionsMenu.findItem(R.id.action_start).setVisible(true).setEnabled(true);
                mOptionsMenu.findItem(R.id.action_restart).setVisible(false).setEnabled(false);
                mOptionsMenu.findItem(R.id.action_stop).setVisible(false).setEnabled(false);
                break;

            default:
                mOptionsMenu.findItem(R.id.action_start).setVisible(false).setEnabled(false);
                mOptionsMenu.findItem(R.id.action_restart).setVisible(false).setEnabled(false);
                mOptionsMenu.findItem(R.id.action_stop).setVisible(false).setEnabled(false);
        }

        // Actions not yet supported
        mOptionsMenu.findItem(R.id.action_edit).setVisible(false).setEnabled(false);
        mOptionsMenu.findItem(R.id.action_delete).setVisible(false).setEnabled(false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateOptionsMenu();
    }

    private RunAction.RunActionCallback runActionCallback = new RunAction.RunActionCallback() {
        @Override
        public void onActionComplete() {
            updateOptionsMenu();
            loadContainers();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ServiceActionsAPI actionsAPI = new ServiceActionsAPI(getContext());
        switch (item.getItemId()) {
            case R.id.action_start:
                Log.d("ServiceActions", "Activating service " + service.getName());
                RunAction runAction = new RunAction(
                        getContext(),
                        service,
                        RunAction.ACTION_ACTIVATE,
                        service.getLinks().optString("self"),
                        actionsAPI,
                        ServiceAPI.class,
                        runActionCallback
                );
                runAction.start();
                return true;

            case R.id.action_restart:
                new AlertDialog.Builder(getContext())
                        .setTitle("Restart Service")
                        .setMessage("Are you sure you want to restart " + service.getName() + "?")
                        .setIcon(R.drawable.ic_restart_black)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ServiceActions", "Restarting service " + service.getName());
                                RunAction runAction = new RunAction(
                                        getContext(),
                                        service,
                                        RunAction.ACTION_RESTART,
                                        service.getLinks().optString("self"),
                                        actionsAPI,
                                        ServiceAPI.class,
                                        runActionCallback
                                );
                                runAction.start();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;

            case R.id.action_stop:
                new AlertDialog.Builder(getContext())
                        .setTitle("Deactivate Service")
                        .setMessage("Are you sure you want to deactivate " + service.getName() + "?")
                        .setIcon(R.drawable.ic_stop_black)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ServiceActions", "Deactivating service " + service.getName());
                                RunAction runAction = new RunAction(
                                        getContext(),
                                        service,
                                        RunAction.ACTION_DEACTIVATE,
                                        service.getLinks().optString("self"),
                                        actionsAPI,
                                        ServiceAPI.class,
                                        runActionCallback
                                );
                                runAction.start();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContainerListFragmentInteractionListener) {
            mListener = (OnContainerListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContainerListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        loadContainers();
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
    public interface OnContainerListFragmentInteractionListener {
        void onContainerListFragmentInteraction(Container item);
    }
}

class ServiceAPI extends RunAction.ActionsAPI {

    private static final String TAG = "ServiceAPI";

    /**
     * Perform the API call
     *
     * @param params Parameters
     * @return API Response as string
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        // Fetch the data
        String jsonString = API.GET(params[0]);
        Log.i(TAG, "Received JSON: " + jsonString);

        // Parse the data
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.e(TAG, "Error processing JSON: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        parseData(jsonObject);
    }

    private void parseData(JSONObject service) {
        try {
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

            // Instantiate service
            ContainerListFragment.setService(new Service(id, name, state, description, healthState, links, service));
        } catch (JSONException e) {
            Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
        }
    }
}

class ContainersAPI extends RunAction.ActionsAPI {

    private static final String TAG = "ContainersAPI";
    private Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ContainerListFragment.OnContainerListFragmentInteractionListener mListener;

    public ContainersAPI(Context context, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout,
                         ContainerListFragment.OnContainerListFragmentInteractionListener mListener) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.mListener = mListener;
    }

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
    protected JSONObject doInBackground(String... params) {
        // Fetch the data
        String jsonString = API.GET(params[0]);
        Log.i(TAG, "Received JSON: " + jsonString);

        // Parse the data
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.e(TAG, "Error processing JSON: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        List<Container> items = new ArrayList<>();
        parseItems(items, jsonObject);
        Container.ITEMS = items;
        recyclerView.setAdapter(new ContainerRecyclerViewAdapter(context, Container.ITEMS, mListener));
        swipeRefreshLayout.setRefreshing(false);
    }

    private void parseItems(List<Container> items, JSONObject jsonBaseObject) {
        try {
            // Get the containers
            JSONArray containers = jsonBaseObject.getJSONArray("data");

            for (int i = 0; i < containers.length(); i++) {
                // Get photo object
                JSONObject container = containers.getJSONObject(i);

                // Get relevant data
                String id = container.getString("id");
                String name = container.getString("name");
                String state = container.getString("state");
                String description = container.getString("description");
                String healthState = container.getString("healthState");
                JSONObject links = container.getJSONObject("links");
                JSONObject actions = container.getJSONObject("actions");

                if (description.equals("null")) {
                    description = null;
                }

                // Instantiate stack and add to list
                Container item = new Container(id, name, state, description, healthState, links, actions,
                        container);
                items.add(item);
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
        }
    }
}

class ServiceActionsAPI extends RunAction.ActionsAPI {

    private static final String TAG = "ServiceActionsAPI";

    private String action;
    private Context context;

    ServiceActionsAPI(Context context) {
        super();
        this.context = context;
    }

    /**
     * Perform the API call
     *
     * @param params Parameters
     * @return API Response as string
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        // Save action
        action = params[1];
        JSONObject data = null;

        // If this is a restart, build the restart strategy
        if (action.equals(RunAction.ACTION_RESTART)) {
            try {
                // Build restart strategy
                JSONObject restartStrategy = new JSONObject();
                restartStrategy.put("batchSize", 1);
                restartStrategy.put("intervalMillis", 2000);

                // Add to data
                data = new JSONObject();
                data.put("rollingRestartStrategy", restartStrategy);
            } catch (JSONException e) {
                data = null;
                Log.e(TAG, "JSON error when building restart strategy: " + e.getMessage());
            }
        }

        // Fetch the data
        String jsonString = API.POST(params[0], data);
        Log.i(TAG, "Received JSON: " + jsonString);

        // Parse the data
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.e(TAG, "Error processing JSON: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject service) {
        if (service == null) {
            new AlertDialog.Builder(context)
                    .setMessage("Unable to " + action + " service")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return;
        }

        try {
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

            // Instantiate service
            ContainerListFragment.setService(new Service(id, name, state, description, healthState, links, service));
        } catch (JSONException e) {
            Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
        }
    }
}

