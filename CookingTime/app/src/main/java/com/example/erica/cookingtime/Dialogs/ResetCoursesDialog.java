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
import com.example.erica.cookingtime.Utils.MyTextView;

public class ResetCoursesDialog extends DialogFragment{

    private ResetCoursesListener mListener;

    public interface ResetCoursesListener{
        void onResetAllCourses();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.reset_cuisine_dialog, null);

        builder.setView(layout);
        final Dialog dialog = builder.create();

        ((MyTextView) layout.findViewById(R.id.reset_text)).setText(getString(R.string.reset_courses_text));

        layout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        layout.findViewById(R.id.reset_cuisine_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onResetAllCourses();
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (ResetCoursesListener) activity;
        }catch (ClassCastException e){
            Log.e("CLASSCAST", "MUST IMPLEMENT LISTENER");
        }
    }
}