package com.michaelpalmer.rancher.container;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.michaelpalmer.rancher.R;
import com.michaelpalmer.rancher.schema.Container;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContainerVolumesFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContainerVolumesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerVolumesFragment extends Fragment {
    private static final String ARG_CONTAINER_ID = "container-id";
    private String mContainerId;
    private OnContainerVolumesFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment ContainerVolumesFragment.
     */
    public static ContainerVolumesFragment newInstance(Container container) {
        ContainerVolumesFragment fragment = new ContainerVolumesFragment();
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
        View view = inflater.inflate(R.layout.fragment_container_volumes, container, false);

        TableLayout tableLayout = (TableLayout) view;

        if (ContainerFragment.getContainer() != null) {

            JSONArray mounts;

            try {
                mounts = (JSONArray) ContainerFragment.getContainer().getProperty("mounts");

                TableRow.LayoutParams params =
                        new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);

                for (int i = 0; i < mounts.length(); i++) {
                    JSONObject mountObject = mounts.getJSONObject(i);

                    TableRow tableRow = new TableRow(getContext());
                    TextView volumeName = new TextView(getContext());
                    TextView volumeMount = new TextView(getContext());
                    volumeName.setLayoutParams(params);
                    volumeMount.setLayoutParams(params);

                    volumeName.setText(mountObject.optString("volumeName"));
                    volumeMount.setText(mountObject.optString("path"));

                    tableRow.addView(volumeName);
                    tableRow.addView(volumeMount);

                    tableLayout.addView(tableRow);
                }

            } catch (Exception e) {
                // Just return
            }
        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContainerVolumesFragmentInteraction(uri);
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
    public interface OnContainerVolumesFragmentInteractionListener {
        void onContainerVolumesFragmentInteraction(Uri uri);
    }
}
