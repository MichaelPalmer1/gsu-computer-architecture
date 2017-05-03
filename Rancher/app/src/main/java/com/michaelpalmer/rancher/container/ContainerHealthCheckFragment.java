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

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContainerHealthCheckFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContainerHealthCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerHealthCheckFragment extends Fragment {
    private static final String ARG_CONTAINER_ID = "container-id";
    private String mContainerId;
    private OnContainerHealthCheckFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment ContainerHealthCheckFragment.
     */
    public static ContainerHealthCheckFragment newInstance(Container container) {
        ContainerHealthCheckFragment fragment = new ContainerHealthCheckFragment();
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
        View view = inflater.inflate(R.layout.fragment_container_health_check, container, false);

        TextView type = (TextView) view.findViewById(R.id.type);
        TextView port = (TextView) view.findViewById(R.id.port);
        TextView initializingTimeout = (TextView) view.findViewById(R.id.initializing_timeout);
        TextView reinitializingTimeout = (TextView) view.findViewById(R.id.reinitializing_timeout);
        TextView checkInterval = (TextView) view.findViewById(R.id.check_interval);
        TextView checkTimeout = (TextView) view.findViewById(R.id.check_timeout);
        TextView healthyAfter = (TextView) view.findViewById(R.id.healthy_after);
        TextView unhealthyAfter = (TextView) view.findViewById(R.id.unhealthy_after);

        if (ContainerFragment.getContainer() != null) {

            JSONObject healthCheck;

            try {
                healthCheck = (JSONObject) ContainerFragment.getContainer().getProperty("healthCheck");
            } catch (Exception e) {
                return view;
            }

            try {
                type.setText(healthCheck.getString("type"));
            } catch (Exception e) {
                type.setText(R.string.unknown);
            }

            try {
                port.setText(String.valueOf(healthCheck.getInt("port")));
            } catch (Exception e) {
                port.setText(R.string.unknown);
            }

            try {
                initializingTimeout.setText(String.valueOf(healthCheck.getInt("initializingTimeout")));
            } catch (Exception e) {
                initializingTimeout.setText(R.string.unknown);
            }

            try {
                reinitializingTimeout.setText(String.valueOf(healthCheck.getInt("reinitializingTimeout")));
            } catch (Exception e) {
                reinitializingTimeout.setText(R.string.unknown);
            }

            try {
                checkInterval.setText(String.valueOf(healthCheck.getInt("interval")));
            } catch (Exception e) {
                checkInterval.setText(R.string.unknown);
            }

            try {
                checkTimeout.setText(String.valueOf(healthCheck.getInt("responseTimeout")));
            } catch (Exception e) {
                checkTimeout.setText(R.string.unknown);
            }

            try {
                healthyAfter.setText(String.valueOf(healthCheck.getInt("healthyThreshold")));
            } catch (Exception e) {
                healthyAfter.setText(R.string.unknown);
            }

            try {
                unhealthyAfter.setText(String.valueOf(healthCheck.getInt("unhealthyThreshold")));
            } catch (Exception e) {
                unhealthyAfter.setText(R.string.unknown);
            }

        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContainerHealthCheckFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnContainerHealthCheckFragmentInteractionListener) {
//            mListener = (OnContainerHealthCheckFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnContainerHealthCheckFragmentInteractionListener");
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
    public interface OnContainerHealthCheckFragmentInteractionListener {
        void onContainerHealthCheckFragmentInteraction(Uri uri);
    }
}
