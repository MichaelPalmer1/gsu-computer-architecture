package com.michaelpalmer.rancher.service;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.michaelpalmer.rancher.R;


public class ServiceCommandFragment extends Fragment implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();

    private TableLayout tableEnvVars;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_command, container, false);

        EditText command = (EditText) view.findViewById(R.id.command);
        EditText entryPoint = (EditText) view.findViewById(R.id.entry_point);
        EditText workingDir = (EditText) view.findViewById(R.id.working_dir);
        EditText user = (EditText) view.findViewById(R.id.user);

        RadioGroup radioConsole = (RadioGroup) view.findViewById(R.id.consoleMode);
        RadioGroup radioRestartPolicy = (RadioGroup) view.findViewById(R.id.restartPolicy);

        tableEnvVars = (TableLayout) view.findViewById(R.id.tableEnvVars);

        Button btnAddEnvVar = (Button) view.findViewById(R.id.btnAddEnvVar);
        btnAddEnvVar.setOnClickListener(this);

        return view;
    }

    private void addEnvVar() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.45f);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f);
        final TableRow tableRow = new TableRow(getContext());
        EditText varName = new EditText(getContext());
        EditText varValue = new EditText(getContext());
        ImageButton btnRemoveVar = new ImageButton(getContext());
        varName.setLayoutParams(params);
        varValue.setLayoutParams(params);
        varName.setInputType(InputType.TYPE_CLASS_TEXT);
        varValue.setInputType(InputType.TYPE_CLASS_TEXT);
        btnRemoveVar.setLayoutParams(params2);
        btnRemoveVar.setImageResource(R.drawable.ic_delete_black);

        btnRemoveVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableEnvVars.removeView(tableRow);
            }
        });

        tableRow.addView(varName);
        tableRow.addView(varValue);
        tableRow.addView(btnRemoveVar);

        tableEnvVars.addView(tableRow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddEnvVar:
                addEnvVar();
                break;
        }
    }
}
