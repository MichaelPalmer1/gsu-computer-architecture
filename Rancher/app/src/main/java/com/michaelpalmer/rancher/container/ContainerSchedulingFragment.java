package com.michaelpalmer.rancher.container;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelpalmer.rancher.R;
import com.michaelpalmer.rancher.schema.Container;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContainerSchedulingFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContainerSchedulingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerSchedulingFragment extends Fragment {
    private static final String ARG_CONTAINER_ID = "container-id";
    private String mContainerId;
    private OnContainerSchedulingFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment ContainerSchedulingFragment.
     */
    public static ContainerSchedulingFragment newInstance(Container container) {
        ContainerSchedulingFragment fragment = new ContainerSchedulingFragment();
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
        View view = inflater.inflate(R.layout.fragment_container_scheduling, container, false);

        if (ContainerFragment.getContainer() != null) {

        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContainerSchedulingFragmentInteraction(uri);
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
    public interface OnContainerSchedulingFragmentInteractionListener {
        void onContainerSchedulingFragmentInteraction(Uri uri);
    }
}
