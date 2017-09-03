package com.example.erica.cookingtime.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.erica.cookingtime.R;

public class ResetCuisinesDialog extends DialogFragment{

    private ResetCuisinesListener mListener;

    public interface ResetCuisinesListener{
        void onResetAllCuisines();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.reset_cuisine_dialog, null);

        builder.setView(layout);
        final Dialog dialog = builder.create();

        layout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        layout.findViewById(R.id.reset_cuisine_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onResetAllCuisines();
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (ResetCuisinesListener) activity;
        }catch (ClassCastException e){
            Log.e("CLASSCAST", "MUST IMPLEMENT LISTENER");
        }
    }
}
