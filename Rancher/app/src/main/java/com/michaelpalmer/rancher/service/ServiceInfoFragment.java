package com.michaelpalmer.rancher.service;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelpalmer.rancher.R;
import com.michaelpalmer.rancher.schema.Container;
import com.michaelpalmer.rancher.schema.Stack;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnServiceInfoFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiceInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceInfoFragment extends Fragment implements ContainerFragment.ContainerFragmentListener {
    private static final String ARG_STACK_ID = "stack-id";
    private String mStackId;
    private OnServiceInfoFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();
    private TextView container_external_id, container_state;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stack Stack
     * @return A new instance of fragment ServiceInfoFragment.
     */
    public static ServiceInfoFragment newInstance(Stack stack) {
        ServiceInfoFragment fragment = new ServiceInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STACK_ID, stack.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStackId = getArguments().getString(ARG_STACK_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_container_info, container, false);
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
    public interface OnServiceInfoFragmentInteractionListener {
        void onContainerInfoFragmentInteraction(Uri uri);
    }
}
