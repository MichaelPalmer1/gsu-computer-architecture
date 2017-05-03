package com.michaelpalmer.rancher;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class NewStackDialog extends DialogFragment implements DialogInterface.OnClickListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add new stack")
                .setView(R.layout.dialog_add_stack)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:

                break;
        }
    }
}
