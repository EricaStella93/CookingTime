package com.example.erica.cookingtime.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.R;


public class AddDislikedIngredientDialog extends DialogFragment {

    private AddDislIngredientDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.add_disl_ing_dialog, null);

        builder.setView(layout);
        final Dialog dialog = builder.create();

        ImageButton imageButton = (ImageButton) layout.findViewById(R.id.exit_add_disl_ing_dialog);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        Button addButton = (Button) layout.findViewById(R.id.add_disl_ing);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameInput = (EditText) layout.findViewById(R.id.input_disl_ing_name);
                String name = nameInput.getText().toString().trim();

                if(name.isEmpty()){
                    TextInputLayout inputLay = (TextInputLayout) layout.findViewById(R.id.input_layout_disl_ing_name);
                    inputLay.setError(getResources().getString(R.string.name_error));
                    nameInput.setText("");
                    return;
                }

                mListener.onAddIngredient(new DislikedIng(-1, name));
                dialog.dismiss();
            }
        });

        Button cancelButton = (Button) layout.findViewById(R.id.cancel_add_disl_ing);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (AddDislIngredientDialogListener) activity;
        }catch (ClassCastException e){
            Log.e("CLASSCAST", "MUST IMPLEMENT LISTENER");
        }
    }

    public interface AddDislIngredientDialogListener{
        void onAddIngredient(DislikedIng ing);
    }
}
