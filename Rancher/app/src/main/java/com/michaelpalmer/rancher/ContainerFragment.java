package com.michaelpalmer.rancher;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private OnContainerFragmentInteractionListener mListener;

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

        if (container == null) {
            new ContainerAPI().execute(mContainerUrl);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        // Get the TabLayout
        tabLayout = (TabLayout) view.findViewById(R.id.container_tabs);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.container_view_pager);
        viewPager.setAdapter(new ContainerPagerAdapter(getFragmentManager()));

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContainerFragmentInteraction(uri);
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

    private class ContainerPagerAdapter extends FragmentStatePagerAdapter {

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
                    return ContainerPortsFragment.newInstance(container);

                case CONTAINER_TAB_VOLUMES:
                    return ContainerPortsFragment.newInstance(container);

                case CONTAINER_TAB_NETWORKING:
                    return ContainerPortsFragment.newInstance(container);

                case CONTAINER_TAB_SECURITY:
                    return ContainerPortsFragment.newInstance(container);

                case CONTAINER_TAB_HEALTH_CHECK:
                    return ContainerPortsFragment.newInstance(container);

                case CONTAINER_TAB_LABELS:
                    return ContainerLabelsFragment.newInstance(container);

                case CONTAINER_TAB_SCHEDULING:
                    return ContainerPortsFragment.newInstance(container);

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

    public class ContainerAPI extends AsyncTask<String, Void, JSONObject> {

        private static final String TAG = "ContainerAPI";

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
                setContainer(
                        new Container(id, name, state, description, healthState, links, actions, container)
                );
            } catch (JSONException e) {
                Log.e(TAG, "JSON error encountered while processing data: " + e.getMessage());
            }
        }
    }
}
