package com.michaelpalmer.rancher;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelpalmer.rancher.schema.Container;
import com.michaelpalmer.rancher.schema.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnContainerListFragmentInteractionListener}
 * interface.
 */
public class ContainerListFragment extends Fragment {

    private OnContainerListFragmentInteractionListener mListener;
    private static final String ARG_SERVICE_ID = "service-id", ARG_CONTAINERS_URL = "services-url";
    private String mServiceId = null, mContainersUrl = null;
    private RecyclerView recyclerView;

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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mServiceId = getArguments().getString(ARG_SERVICE_ID);
            mContainersUrl = getArguments().getString(ARG_CONTAINERS_URL);
        }

        new ContainersAPI().execute(mContainersUrl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new ContainerRecyclerViewAdapter(getContext(), Container.ITEMS, mListener));
        }
        return view;
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

    private class ContainersAPI extends AsyncTask<String, Void, List<Container>> {

        private static final String TAG = "ContainersAPI";

        /**
         * Perform the API call
         *
         * @param params Parameters
         * @return API Response as string
         */
        @Override
        protected List<Container> doInBackground(String... params) {
            return fetchItems(params[0]);
        }

        @Override
        protected void onPostExecute(List<Container> items) {
            Container.ITEMS = items;
            recyclerView.setAdapter(new ContainerRecyclerViewAdapter(getContext(), Container.ITEMS, mListener));
        }

        private List<Container> fetchItems(String url) {
            List<Container> items = new ArrayList<>();

            // Fetch the data
            String jsonString = API.GET(url);
            Log.i(TAG, "Received JSON: " + jsonString);

            // Parse the data
            parseItems(items, jsonString);

            return items;
        }

        private void parseItems(List<Container> items, String data) {
            try {
                // Get base object
                JSONObject jsonBaseObject = new JSONObject(data);

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

                    if (description == null) {
                        description = "";
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
}
