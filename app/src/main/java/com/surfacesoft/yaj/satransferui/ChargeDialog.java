package com.surfacesoft.yaj.satransferui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;

import java.security.AccessController;

/**
 * Created by Yulio on 04/08/2015.
 */
public class ChargeDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View chargeCodeView = inflater.inflate(R.layout.charge_dialog, null);

        builder.setTitle("Código de Recarga")
                .setView(chargeCodeView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String chargeCode = ((EditText) chargeCodeView.findViewById(R.id.charge_code)).getText().toString();
                        ((MainActivity) getActivity()).Service_Charge_Action(chargeCode);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChargeDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
