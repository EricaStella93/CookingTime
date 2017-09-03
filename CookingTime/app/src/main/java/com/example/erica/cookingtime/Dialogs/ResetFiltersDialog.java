package com.example.erica.cookingtime.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.R;

public class ResetFiltersDialog extends DialogFragment{

    private ResetFilterListener mListener;

    public interface ResetFilterListener{
        void onResetFilters();
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (ResetFilterListener) activity;
        }catch (ClassCastException e){
            Log.e("CLASSCAST", "MUST IMPLEMENT LISTENER");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.reset_filter_dialog, null);

        builder.setView(layout);
        final Dialog dialog = builder.create();

        ImageButton imageButton = (ImageButton) layout.findViewById(R.id.exit_dialog);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        Button addButton = (Button) layout.findViewById(R.id.reset);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onResetFilters();
                dialog.dismiss();
            }
        });

        Button cancelButton = (Button) layout.findViewById(R.id.dismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        return dialog;
    }
}
