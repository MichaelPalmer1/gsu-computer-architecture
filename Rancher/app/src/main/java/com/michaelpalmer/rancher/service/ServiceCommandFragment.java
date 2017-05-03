package com.michaelpalmer.rancher.service;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelpalmer.rancher.R;


public class ServiceCommandFragment extends Fragment {
    private String TAG = getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_command, container, false);

//        TextView container_commands = (TextView) view.findViewById(R.id.container_command_commands);
//        TextView container_entry_point = (TextView) view.findViewById(R.id.container_command_entry_point);
//        TextView container_working_dir = (TextView) view.findViewById(R.id.container_command_working_dir);
//        TextView container_user = (TextView) view.findViewById(R.id.container_command_user);
//        TextView container_console = (TextView) view.findViewById(R.id.container_command_console);
//        TextView container_auto_restart = (TextView) view.findViewById(R.id.container_command_auto_restart);
//        TableLayout container_environment = (TableLayout) view.findViewById(R.id.container_command_environment_table);
//
//        if (ContainerFragment.getContainer() != null) {
//            try {
//                JSONArray commands = (JSONArray) ContainerFragment.getContainer().getProperty("command");
//                container_commands.setText(commands.join(", "));
//            } catch (Exception e) {
//                container_commands.setText(R.string.unknown);
//            }
//
//            try {
//                container_entry_point.setText(ContainerFragment.getContainer().getProperty("entryPoint").toString());
//            } catch (NullPointerException e) {
//                container_entry_point.setText(R.string.unknown);
//            }
//
//            try {
//                container_working_dir.setText(ContainerFragment.getContainer().getProperty("workingDir").toString());
//            } catch (NullPointerException e) {
//                container_working_dir.setText(R.string.unknown);
//            }
//
//            try {
//                container_user.setText(ContainerFragment.getContainer().getProperty("user").toString());
//            } catch (NullPointerException e) {
//                container_user.setText(R.string.unknown);
//            }
//
////            try {
////                container_console.setText(ContainerFragment.getContainer().getProperty("console").toString());
////            } catch (NullPointerException e) {
////                container_console.setText(R.string.unknown);
////            }
//
//            try {
//                container_auto_restart.setText(
//                        ContainerFragment.getContainer().getProperty("restartPolicy").toString());
//            } catch (NullPointerException e) {
//                container_auto_restart.setText(R.string.unknown);
//            }
//
//            try {
//                JSONObject environment = (JSONObject) ContainerFragment.getContainer().getProperty("environment");
//                Iterator<String> keys = environment.keys();
//
//                // Loop through the keys
//                while (keys.hasNext()) {
//                    // Get values
//                    String key = keys.next();
//                    String value = environment.optString(key);
//
//                    // Create row
//                    TableRow row = new TableRow(getContext());
//
//                    // Create text views
//                    TextView envKey = new TextView(getContext());
//                    TextView envValue = new TextView(getContext());
//
//                    // Set layout params
//                    envKey.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
//                    envValue.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
//
//                    // Set values
//                    envKey.setText(key);
//                    envValue.setText(value);
//
//                    // Add to row
//                    row.addView(envKey);
//                    row.addView(envValue);
//
//                    // Add to table
//                    container_environment.addView(row);
//                }
//            } catch (NullPointerException e) {
//                // Do nothing
//            }
//        }

        return view;
    }
}
