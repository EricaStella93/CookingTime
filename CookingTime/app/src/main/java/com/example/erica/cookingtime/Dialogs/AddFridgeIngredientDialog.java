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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFridgeIngredientDialog extends DialogFragment {

    private AddIngredientDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.add_ingredient_fridge_dialog, null);

        builder.setView(layout);
        final Dialog dialog = builder.create();

        ImageButton imageButton = (ImageButton) layout.findViewById(R.id.fridge_exit_add_ing_dialog);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);
        Date today = new Date();
        datePicker.setMinDate(today.getTime()-100);
        datePicker.init(today.getYear(), today.getMonth(), today.getDay(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });

        Button button = (Button) layout.findViewById(R.id.cancel_add_ingredient_fridge);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        Button addIngButton = (Button) layout.findViewById(R.id.add_ingredient_fridge);
        addIngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prendo il nome e controllo che non sia una stringa vuota
                EditText nameInput = (EditText) layout.findViewById(R.id.input_ing_name);
                String name = nameInput.getText().toString();
                String trimmedName = name.trim();
                if(trimmedName.isEmpty()){
                    TextInputLayout inputLay = (TextInputLayout) layout.findViewById(R.id.input_layout_ing_name);
                    inputLay.setError(getResources().getString(R.string.name_error));
                    nameInput.setText("");
                    return;
                }
                //prendo la quantità e controllo che non sia vuota
                EditText quantityInput = (EditText) layout.findViewById(R.id.input_ing_quantity);
                String quantity = quantityInput.getText().toString();
                float finalQuant;
                if(quantity == null | quantity.equals("")){
                    finalQuant = 0;
                }else{
                    finalQuant = Float.parseFloat(quantity);
                }
                //prendo le udm
                Spinner udmSpinner = (Spinner) layout.findViewById(R.id.udm_spinner);
                String udm = udmSpinner.getSelectedItem().toString();

                //prendo aisle
                Spinner aisleSpinner = (Spinner) layout.findViewById(R.id.aisle_spinner);
                String aisle = aisleSpinner.getSelectedItem().toString();

                Date bb = new Date(datePicker.getYear()-1900, datePicker.getMonth(), datePicker.getDayOfMonth());
                mListener.onAddIngredient(trimmedName, finalQuant, aisle, IngredientUtils.fromDateToString(bb), udm);
                dialog.dismiss();
            }
        });

        Spinner udmSpinner = (Spinner) layout.findViewById(R.id.udm_spinner);
        ArrayAdapter<CharSequence> udmAdapter = ArrayAdapter.createFromResource(layout.getContext(),
                R.array.udm_array, android.R.layout.simple_spinner_item);
        udmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        udmSpinner.setAdapter(udmAdapter);

        Spinner aisleSpinner = (Spinner) layout.findViewById(R.id.aisle_spinner);
        ArrayAdapter<CharSequence> aisleAdapter = ArrayAdapter.createFromResource(layout.getContext(),
                R.array.aisle_array, android.R.layout.simple_spinner_item);
        aisleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aisleSpinner.setAdapter(aisleAdapter);

        //setto la data corrente come scadenza iniziale
        /*SimpleDateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format));
        TextView bestBefore = (TextView) layout.findViewById(R.id.best_before_date);
        bestBefore.setText(df.format(new Date()));*/

        return dialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (AddIngredientDialogListener) activity;
        }catch (ClassCastException e){
            Log.e("CLASSCAST", "MUST IMPLEMENT LISTENER");
        }
    }

    public interface AddIngredientDialogListener{
        void onAddIngredient(String name, float quant, String aisle, String date, String udm);
    }
}
