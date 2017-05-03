package com.michaelpalmer.rancher.container;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelpalmer.rancher.R;
import com.michaelpalmer.rancher.schema.Container;

import org.json.JSONArray;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContainerNetworkingFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContainerNetworkingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerNetworkingFragment extends Fragment {
    private static final String ARG_CONTAINER_ID = "container-id";
    private String mContainerId;
    private OnContainerNetworkingFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment ContainerInfoFragment.
     */
    public static ContainerNetworkingFragment newInstance(Container container) {
        ContainerNetworkingFragment fragment = new ContainerNetworkingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTAINER_ID, container.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContainerId = getArguments().getString(ARG_CONTAINER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_container_networking, container, false);

        TextView networkMode = (TextView) view.findViewById(R.id.network_mode);
        TextView requestedIp = (TextView) view.findViewById(R.id.requested_ip);
        TextView hostname = (TextView) view.findViewById(R.id.hostname);
        TextView domainName = (TextView) view.findViewById(R.id.domain_name);
        TextView resolvingServers = (TextView) view.findViewById(R.id.resolving_servers);
        TextView searchDomains = (TextView) view.findViewById(R.id.search_domains);

        if (ContainerFragment.getContainer() != null) {

            try {
                networkMode.setText(ContainerFragment.getContainer().getProperty("networkMode").toString());
            } catch (NullPointerException e) {
                networkMode.setText(R.string.unknown);
            }

            try {
                requestedIp.setText(ContainerFragment.getContainer().getProperty("ip").toString());
            } catch (NullPointerException e) {
                requestedIp.setText(R.string.unknown);
            }

            try {
                hostname.setText(ContainerFragment.getContainer().getProperty("hostname").toString());
            } catch (NullPointerException e) {
                hostname.setText(R.string.unknown);
            }

            try {
                domainName.setText(ContainerFragment.getContainer().getProperty("domainName").toString());
            } catch (NullPointerException e) {
                domainName.setText(R.string.unknown);
            }

            try {
                JSONArray servers = (JSONArray) ContainerFragment.getContainer().getProperty("dns");
                resolvingServers.setText(servers.join(", "));
            } catch (Exception e) {
                resolvingServers.setText(R.string.unknown);
            }

            try {
                JSONArray domains = (JSONArray) ContainerFragment.getContainer().getProperty("dnsSearch");
                searchDomains.setText(domains.join(", "));
            } catch (Exception e) {
                searchDomains.setText(R.string.unknown);
            }

        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContainerNetworkingFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnContainerNetworkingFragmentInteractionListener) {
//            mListener = (OnContainerNetworkingFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnContainerNetworkingFragmentInteractionListener");
//        }
//    }

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
    public interface OnContainerNetworkingFragmentInteractionListener {
        void onContainerNetworkingFragmentInteraction(Uri uri);
    }
}
