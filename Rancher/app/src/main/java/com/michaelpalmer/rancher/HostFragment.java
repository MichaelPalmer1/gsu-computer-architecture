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
import android.widget.TextView;

import com.michaelpalmer.rancher.container.ContainerCommandFragment;
import com.michaelpalmer.rancher.container.ContainerHealthCheckFragment;
import com.michaelpalmer.rancher.container.ContainerInfoFragment;
import com.michaelpalmer.rancher.container.ContainerLabelsFragment;
import com.michaelpalmer.rancher.container.ContainerNetworkingFragment;
import com.michaelpalmer.rancher.container.ContainerPortsFragment;
import com.michaelpalmer.rancher.container.ContainerSchedulingFragment;
import com.michaelpalmer.rancher.container.ContainerSecurityFragment;
import com.michaelpalmer.rancher.container.ContainerVolumesFragment;
import com.michaelpalmer.rancher.schema.Container;
import com.michaelpalmer.rancher.schema.Host;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHostFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostFragment extends Fragment {
    private static final String ARG_HOST_ID = "host-id", ARG_HOST_URL = "host-url";
    private String mHostId, mHostUrl;
    private OnHostFragmentInteractionListener mListener;
    private static Host currentHost;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param host Host
     * @return A new instance of fragment HostFragment.
     */
    public static HostFragment newInstance(Host host) {
        HostFragment fragment = new HostFragment();
        setCurrentHost(host);
        Bundle args = new Bundle();
        args.putString(ARG_HOST_ID, host.getId());
        try {
            args.putString(ARG_HOST_URL, host.getLinks().getString("self"));
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
            mHostId = getArguments().getString(ARG_HOST_ID);
            mHostUrl = getArguments().getString(ARG_HOST_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_host, container, false);

        // Set hostname
//        TextView hostName = (TextView) view.findViewById(R.id.host_host_name);
//        hostName.setText(currentHost.getName());

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHostFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHostFragmentInteractionListener) {
            mListener = (OnHostFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHostFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static void setCurrentHost(Host currentHost) {
        HostFragment.currentHost = currentHost;
    }

    public static Host getCurrentHost() {
        return currentHost;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnHostFragmentInteractionListener {
        void onHostFragmentInteraction(Uri uri);
    }
}
