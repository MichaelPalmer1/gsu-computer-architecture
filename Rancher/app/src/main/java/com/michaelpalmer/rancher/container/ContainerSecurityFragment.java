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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContainerSecurityFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContainerSecurityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerSecurityFragment extends Fragment {
    private static final String ARG_CONTAINER_ID = "container-id";
    private String mContainerId;
    private OnContainerSecurityFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment ContainerInfoFragment.
     */
    public static ContainerSecurityFragment newInstance(Container container) {
        ContainerSecurityFragment fragment = new ContainerSecurityFragment();
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
        View view = inflater.inflate(R.layout.fragment_container_security, container, false);

        TextView privileged = (TextView) view.findViewById(R.id.privileged);
        TextView memoryLimit = (TextView) view.findViewById(R.id.memory_limit);
        TextView memoryReservation = (TextView) view.findViewById(R.id.memory_reservation);
        TextView cpuPinning = (TextView) view.findViewById(R.id.cpu_pinning);
        TextView pidMode = (TextView) view.findViewById(R.id.pid_mode);
        TextView swapLimit = (TextView) view.findViewById(R.id.swap_limit);
        TextView mcpuReservation = (TextView) view.findViewById(R.id.mcpu_reservation);
        TextView cpuShares = (TextView) view.findViewById(R.id.cpu_shares);

        if (ContainerFragment.getContainer() != null) {

            try {
                privileged.setText(ContainerFragment.getContainer().getProperty("privileged").toString());
            } catch (NullPointerException e) {
                privileged.setText(R.string.unknown);
            }

            try {
                memoryLimit.setText(ContainerFragment.getContainer().getProperty("memory").toString());
            } catch (NullPointerException e) {
                memoryLimit.setText(R.string.unknown);
            }

            try {
                memoryReservation.setText(ContainerFragment.getContainer().getProperty("memoryReservation").toString());
            } catch (NullPointerException e) {
                memoryReservation.setText(R.string.unknown);
            }

            try {
                cpuPinning.setText(ContainerFragment.getContainer().getProperty("cpuSet").toString());
            } catch (NullPointerException e) {
                cpuPinning.setText(R.string.unknown);
            }

            try {
                pidMode.setText(ContainerFragment.getContainer().getProperty("pidMode").toString());
            } catch (NullPointerException e) {
                pidMode.setText(R.string.unknown);
            }

            try {
                swapLimit.setText(ContainerFragment.getContainer().getProperty("memorySwap").toString());
            } catch (NullPointerException e) {
                swapLimit.setText(R.string.unknown);
            }

            try {
                mcpuReservation.setText(ContainerFragment.getContainer().getProperty("milliCpuReservation").toString());
            } catch (NullPointerException e) {
                mcpuReservation.setText(R.string.unknown);
            }

            try {
                cpuShares.setText(ContainerFragment.getContainer().getProperty("cpuShares").toString());
            } catch (NullPointerException e) {
                cpuShares.setText(R.string.unknown);
            }

        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContainerSecurityFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnContainerSchedulingFragmentInteractionListener) {
//            mListener = (OnContainerSchedulingFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnContainerSchedulingFragmentInteractionListener");
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
    public interface OnContainerSecurityFragmentInteractionListener {
        void onContainerSecurityFragmentInteraction(Uri uri);
    }
}
