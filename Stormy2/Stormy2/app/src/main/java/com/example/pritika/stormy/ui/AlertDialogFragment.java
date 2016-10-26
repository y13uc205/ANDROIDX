package com.example.pritika.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.example.pritika.stormy.R;

/**
 * Created by Pritika on 19-01-2016.
 */
public class AlertDialogFragment extends  DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context =getActivity();
        AlertDialog.Builder builder  = new AlertDialog.Builder(context)
                 .setTitle(context.getString(R.string.error_title))
                .setMessage(context.getString(R.string.error_message))
                        .setPositiveButton(context.getString(R.string.error_ok_button_text), null);// null onclick listener dismiss it
       // A subclass of Dialog that can display one, two or three buttons. If you only want to display a String in this dialog box, use the setMessage() method
        // null onclick listener will close the dialog
        AlertDialog  dialog = builder.create();
                return dialog;
    }
}