package com.michaelpalmer.rancher;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.michaelpalmer.rancher.schema.Container;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContainerLabelsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContainerLabelsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerLabelsFragment extends Fragment {
    private static final String ARG_CONTAINER_ID = "container-id";
    private String mContainerId;
    private OnContainerLabelsFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment ContainerInfoFragment.
     */
    public static ContainerLabelsFragment newInstance(Container container) {
        ContainerLabelsFragment fragment = new ContainerLabelsFragment();
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
        View view = inflater.inflate(R.layout.fragment_container_labels, container, false);

        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.container_labels_table);

        if (ContainerFragment.getContainer() != null) {
            JSONObject labels = (JSONObject) ContainerFragment.getContainer().getProperty("labels");
            Iterator<String> keys = labels.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = labels.optString(key);


                TextView txtKey = new TextView(getContext());
                TextView txtValue = new TextView(getContext());
                txtKey.setText(key);
                txtValue.setText(value);


                txtKey.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f));
                txtValue.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f));

                TableRow row = new TableRow(getContext());
                row.addView(txtKey);
                row.addView(txtValue);

                tableLayout.addView(row);
            }
        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContainerLabelsFragmentInteraction(uri);
        }
    }

    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnContainerLabelsFragmentInteractionListener) {
//            mListener = (OnContainerLabelsFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnContainerLabelsFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnContainerLabelsFragmentInteractionListener {
        void onContainerLabelsFragmentInteraction(Uri uri);
    }
}
