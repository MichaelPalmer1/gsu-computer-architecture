package com.michaelpalmer.rancher.container;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.michaelpalmer.rancher.API;
import com.michaelpalmer.rancher.R;
import com.michaelpalmer.rancher.RunAction;
import com.michaelpalmer.rancher.schema.Container;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContainerFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerFragment extends Fragment {
    private static final String ARG_CONTAINER_ID = "container-id", ARG_CONTAINER_URL = "container-url";
    private static final int
            CONTAINER_TAB_INFO = 0,
            CONTAINER_TAB_PORTS = 1,
            CONTAINER_TAB_COMMAND = 2,
            CONTAINER_TAB_VOLUMES = 3,
            CONTAINER_TAB_NETWORKING = 4,
            CONTAINER_TAB_SECURITY = 5,
            CONTAINER_TAB_HEALTH_CHECK = 6,
            CONTAINER_TAB_LABELS = 7,
            CONTAINER_TAB_SCHEDULING = 8;
    private String mContainerId, mContainerUrl;
    public static Container container = null;
    private TabLayout tabLayout;
    private Menu mOptionsMenu;
    private OnContainerFragmentInteractionListener mListener;
    private ViewPager viewPager;
    private ContainerPagerAdapter adapter;

    public interface ContainerFragmentListener {
        void onContainerUpdate();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment ContainerFragment.
     */
    public static ContainerFragment newInstance(Container container) {
        ContainerFragment fragment = new ContainerFragment();
        Bundle args = new Bundle();
        ContainerFragment.setContainer(container);
        args.putString(ARG_CONTAINER_ID, container.getId());
        try {
            args.putString(ARG_CONTAINER_URL, container.getLinks().getString("self"));
        } catch (JSONException e) {
            // Let it go
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void loadContainer() {
        new ContainerAPI().execute(mContainerUrl);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContainerId = getArguments().getString(ARG_CONTAINER_ID);
            mContainerUrl = getArguments().getString(ARG_CONTAINER_URL);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        // Get the TabLayout
        tabLayout = (TabLayout) view.findViewById(R.id.container_tabs);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.container_view_pager);
        adapter = new ContainerPagerAdapter(getFragmentManager());

        // Load container
        loadContainer();

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

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
        switch (container.getState()) {
            case "running":
                mOptionsMenu.findItem(R.id.action_start).setVisible(false).setEnabled(false);
                mOptionsMenu.findItem(R.id.action_restart).setVisible(true).setEnabled(true);
                mOptionsMenu.findItem(R.id.action_stop).setVisible(true).setEnabled(true);
                break;

            case "stopped":
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
            loadContainer();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ContainerActionsAPI actionsAPI = new ContainerActionsAPI(getContext());
        switch (item.getItemId()) {
            case R.id.action_start:
                Log.d("ContainerActions", "Starting container " + container.getName());
                RunAction runContainerAction = new RunAction(
                        getContext(),
                        container,
                        RunAction.ACTION_START,
                        mContainerUrl,
                        actionsAPI,
                        ContainerAPI.class,
                        runActionCallback
                );
                runContainerAction.start();
                return true;

            case R.id.action_restart:
                new AlertDialog.Builder(getContext())
                        .setTitle("Restart Container")
                        .setMessage("Are you sure you want to restart " + container.getName() + "?")
                        .setIcon(R.drawable.ic_restart_black)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RunAction runContainerAction = new RunAction(
                                        getContext(),
                                        container,
                                        RunAction.ACTION_RESTART,
                                        mContainerUrl,
                                        actionsAPI,
                                        ContainerAPI.class,
                                        runActionCallback
                                );
                                runContainerAction.start();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;

            case R.id.action_stop:
                new AlertDialog.Builder(getContext())
                        .setTitle("Stop Container")
                        .setMessage("Are you sure you want to stop " + container.getName() + "?")
                        .setIcon(R.drawable.ic_stop_black)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RunAction runContainerAction = new RunAction(
                                        getContext(),
                                        container,
                                        RunAction.ACTION_STOP,
                                        mContainerUrl,
                                        actionsAPI,
                                        ContainerAPI.class,
                                        runActionCallback
                                );
                                runContainerAction.start();
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
        if (context instanceof OnContainerFragmentInteractionListener) {
            mListener = (OnContainerFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContainerFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static Container getContainer() {
        return container;
    }

    public static void setContainer(Container container) {
        ContainerFragment.container = container;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnContainerFragmentInteractionListener {
        void onContainerFragmentInteraction(Uri uri);
    }

    class ContainerPagerAdapter extends FragmentStatePagerAdapter {

        ContainerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case CONTAINER_TAB_INFO:
                    return ContainerInfoFragment.newInstance(container);

                case CONTAINER_TAB_PORTS:
                    return ContainerPortsFragment.newInstance(container);

                case CONTAINER_TAB_COMMAND:
                    return ContainerCommandFragment.newInstance(container);

                case CONTAINER_TAB_VOLUMES:
                    return ContainerVolumesFragment.newInstance(container);

                case CONTAINER_TAB_NETWORKING:
                    return ContainerNetworkingFragment.newInstance(container);

                case CONTAINER_TAB_SECURITY:
                    return ContainerSecurityFragment.newInstance(container);

                case CONTAINER_TAB_HEALTH_CHECK:
                    return ContainerHealthCheckFragment.newInstance(container);

                case CONTAINER_TAB_LABELS:
                    return ContainerLabelsFragment.newInstance(container);

                case CONTAINER_TAB_SCHEDULING:
                    return ContainerSchedulingFragment.newInstance(container);

            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case CONTAINER_TAB_INFO:
                    return "Info";

                case CONTAINER_TAB_PORTS:
                    return "Ports";

                case CONTAINER_TAB_COMMAND:
                    return "Command";

                case CONTAINER_TAB_VOLUMES:
                    return "Volumes";

                case CONTAINER_TAB_NETWORKING:
                    return "Networking";

                case CONTAINER_TAB_SECURITY:
                    return "Security";

                case CONTAINER_TAB_HEALTH_CHECK:
                    return "Health Check";

                case CONTAINER_TAB_LABELS:
                    return "Labels";

                case CONTAINER_TAB_SCHEDULING:
                    return "Scheduling";

                default:
                    return null;
            }
        }
    }
}


class ContainerAPI extends RunAction.ActionsAPI {

    private static final String TAG = "ContainerAPI";

    ContainerAPI() {
        super();
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
    protected void onPostExecute(JSONObject container) {
        try {
            // Get relevant data
            String id = container.getString("id");
            String name = container.getString("name");
            String state = container.getString("state");
            String description = container.getString("description");
            String healthState = container.getString("healthState");
            JSONObject links = container.getJSONObject("links");
            JSONObject actions = container.getJSONObject("actions");

            if (description == null) {
                description = "";
            }

            // Instantiate container
            ContainerFragment.setContainer(
                    new Container(id, name, state, description, healthState, links, actions, container)
            );
        } catch (JSONException e) {
            Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
        }
    }
}

class ContainerActionsAPI extends RunAction.ActionsAPI {

    private static final String TAG = "ContainerActionsAPI";

    private String action;
    private Context context;

    ContainerActionsAPI(Context context) {
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

        // Fetch the data
        String jsonString = API.POST(params[0]);
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
    protected void onPostExecute(JSONObject container) {
        try {
            if (container == null) {
                new AlertDialog.Builder(context)
                        .setMessage("Unable to stop container")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return;
            }

            // Get relevant data
            String id = container.getString("id");
            String name = container.getString("name");
            String state = container.getString("state");
            String description = container.getString("description");
            String healthState = container.getString("healthState");
            JSONObject links = container.getJSONObject("links");
            JSONObject actions = container.getJSONObject("actions");

            if (description == null) {
                description = "";
            }

            // Instantiate container
            ContainerFragment.setContainer(
                    new Container(id, name, state, description, healthState, links, actions, container)
            );
        } catch (JSONException e) {
            Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
        }
    }
}
