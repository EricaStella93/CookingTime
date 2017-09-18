package com.example.erica.cookingtime.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.erica.cookingtime.R;

public class ClearAllShoppingListDialog extends DialogFragment {

    private ClearAllDialogListener mListener;


    public Dialog onCreateDialog(Bundle savedInstanceState){

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View layout = inflater.inflate(R.layout.clear_all_shopping_list_dialog, null);

    builder.setView(layout);
    final Dialog dialog = builder.create();

    Button button = (Button) layout.findViewById(R.id.dismiss_clear_all_dialog);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();

        }
    });

    ImageButton imageButton = (ImageButton) layout.findViewById(R.id.shopping_list_exit_clear_all_dialog);
        imageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();

        }
    });

    Button clearAll = (Button) layout.findViewById(R.id.clear_all_shopping_list);
        clearAll.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.onClearAllChoice();
            dialog.dismiss();
        }
    });

        return dialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (ClearAllDialogListener) activity;
        }catch (ClassCastException e){
            Log.e("CLASSCAST", "MUST IMPLEMENT LISTENER");
        }
    }

    public interface ClearAllDialogListener {
        void onClearAllChoice();
    }
}
