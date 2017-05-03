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
 * {@link OnContainerInfoFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContainerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerInfoFragment extends Fragment implements ContainerFragment.ContainerFragmentListener {
    private static final String ARG_CONTAINER_ID = "container-id";
    private String mContainerId;
    private OnContainerInfoFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();
    private TextView container_external_id, container_state, container_image, container_ip;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment ContainerInfoFragment.
     */
    public static ContainerInfoFragment newInstance(Container container) {
        ContainerInfoFragment fragment = new ContainerInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_container_info, container, false);

        container_external_id = (TextView) view.findViewById(R.id.container_info_external_id);
        container_state = (TextView) view.findViewById(R.id.container_info_state);
        container_image = (TextView) view.findViewById(R.id.container_image);
        container_ip = (TextView) view.findViewById(R.id.container_ip);

        if (ContainerFragment.getContainer() != null) {
            onContainerUpdate();
        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContainerInfoFragmentInteraction(uri);
        }
    }

    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnContainerLabelsFragmentInteractionListener) {
//            mListener = (OnContainerLabelsFragmentInteractionListener) context;
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

    @Override
    public void onContainerUpdate() {
        try {
            container_state.setText(ContainerFragment.getContainer().getProperty("state").toString());
        } catch (NullPointerException e) {
            container_state.setText(R.string.unknown);
        }

        try {
            container_external_id.setText(ContainerFragment.getContainer().getProperty("externalId").toString());
        } catch (NullPointerException e) {
            container_external_id.setText(R.string.unknown);
        }

        try {
            container_image.setText(ContainerFragment.getContainer().getProperty("imageUuid").toString());
        } catch (NullPointerException e) {
            container_image.setText(R.string.unknown);
        }

        try {
            container_ip.setText(ContainerFragment.getContainer().getProperty("primaryIpAddress").toString());
        } catch (NullPointerException e) {
            container_ip.setText(R.string.unknown);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnContainerInfoFragmentInteractionListener {
        void onContainerInfoFragmentInteraction(Uri uri);
    }
}
