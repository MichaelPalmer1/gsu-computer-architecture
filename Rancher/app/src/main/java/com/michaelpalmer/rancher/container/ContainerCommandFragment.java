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

import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContainerCommandFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContainerCommandFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerCommandFragment extends Fragment {
    private static final String ARG_CONTAINER_ID = "container-id";
    private String mContainerId;
    private OnContainerCommandFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment ContainerCommandFragment.
     */
    public static ContainerCommandFragment newInstance(Container container) {
        ContainerCommandFragment fragment = new ContainerCommandFragment();
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
        View view = inflater.inflate(R.layout.fragment_container_command, container, false);

        TextView container_commands = (TextView) view.findViewById(R.id.container_command_commands);
        TextView container_entry_point = (TextView) view.findViewById(R.id.container_command_entry_point);
        TextView container_working_dir = (TextView) view.findViewById(R.id.container_command_working_dir);
        TextView container_user = (TextView) view.findViewById(R.id.container_command_user);
        TextView container_console = (TextView) view.findViewById(R.id.container_command_console);
        TextView container_auto_restart = (TextView) view.findViewById(R.id.container_command_auto_restart);
        TableLayout container_environment = (TableLayout) view.findViewById(R.id.container_command_environment_table);

        if (ContainerFragment.getContainer() != null) {
            try {
                JSONArray commands = (JSONArray) ContainerFragment.getContainer().getProperty("command");
                container_commands.setText(commands.join(", "));
            } catch (Exception e) {
                container_commands.setText(R.string.unknown);
            }

            try {
                container_entry_point.setText(ContainerFragment.getContainer().getProperty("entryPoint").toString());
            } catch (NullPointerException e) {
                container_entry_point.setText(R.string.unknown);
            }

            try {
                container_working_dir.setText(ContainerFragment.getContainer().getProperty("workingDir").toString());
            } catch (NullPointerException e) {
                container_working_dir.setText(R.string.unknown);
            }

            try {
                container_user.setText(ContainerFragment.getContainer().getProperty("user").toString());
            } catch (NullPointerException e) {
                container_user.setText(R.string.unknown);
            }

//            try {
//                container_console.setText(ContainerFragment.getContainer().getProperty("console").toString());
//            } catch (NullPointerException e) {
//                container_console.setText(R.string.unknown);
//            }

            try {
                container_auto_restart.setText(
                        ContainerFragment.getContainer().getProperty("restartPolicy").toString());
            } catch (NullPointerException e) {
                container_auto_restart.setText(R.string.unknown);
            }

            try {
                JSONObject environment = (JSONObject) ContainerFragment.getContainer().getProperty("environment");
                Iterator<String> keys = environment.keys();

                // Loop through the keys
                while (keys.hasNext()) {
                    // Get values
                    String key = keys.next();
                    String value = environment.optString(key);

                    // Create row
                    TableRow row = new TableRow(getContext());

                    // Create text views
                    TextView envKey = new TextView(getContext());
                    TextView envValue = new TextView(getContext());

                    // Set layout params
                    envKey.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
                    envValue.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));

                    // Set values
                    envKey.setText(key);
                    envValue.setText(value);

                    // Add to row
                    row.addView(envKey);
                    row.addView(envValue);

                    // Add to table
                    container_environment.addView(row);
                }
            } catch (NullPointerException e) {
                // Do nothing
            }
        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContainerCommandFragmentInteraction(uri);
        }
    }

    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnContainerCommandFragmentInteractionListener) {
//            mListener = (OnContainerCommandFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnContainerCommandFragmentInteractionListener");
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
    public interface OnContainerCommandFragmentInteractionListener {
        void onContainerCommandFragmentInteraction(Uri uri);
    }
}
