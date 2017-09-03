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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.erica.cookingtime.Adapters.FridgeAdapter;
import com.example.erica.cookingtime.Adapters.ShoppingListAdapter;
import com.example.erica.cookingtime.DataBase.DBModifiers.UpdateFridge;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.POJO.ShoppingListIngredient;
import com.example.erica.cookingtime.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

public class EditFridgeDialog extends DialogFragment{

    private static final String INGREDIENT = "ingredient";
    private FridgeIngredient ingredient;

    private FridgeAdapter.OnEditIngredients mListener;

    public EditFridgeDialog(){

    }

    public static EditFridgeDialog newInstance(FridgeIngredient ingredient){
        EditFridgeDialog fragment = new EditFridgeDialog();
        Bundle args = new Bundle();
        args.putParcelable(INGREDIENT, ingredient);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.ingredient = getArguments().getParcelable(INGREDIENT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.edit_fridge_ing, null);

        builder.setView(layout);
        final Dialog dialog = builder.create();

        //ingredient name
        ((TextView) layout.findViewById(R.id.ing_name)).setText(ingredient.getName());

        //quantity
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        final String quantity = df.format(ingredient.getQuantity());
        final EditText quantText = (EditText) layout.findViewById(R.id.input_ing_quantity);
        quantText.setText(quantity);

        //udm spinner
        final Spinner udmSpinner = (Spinner) layout.findViewById(R.id.udm_spinner);
        ArrayAdapter<CharSequence> udmAdapter = ArrayAdapter.createFromResource(layout.getContext(),
                R.array.udm_array, android.R.layout.simple_spinner_item);
        udmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        udmSpinner.setAdapter(udmAdapter);

        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);
        Date today = new Date();
        datePicker.setMinDate(today.getTime()-100);
        Date bestBefore = ingredient.getBestBefore();
        datePicker.init(bestBefore.getYear(), bestBefore.getMonth(), bestBefore.getDay(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });

        layout.findViewById(R.id.save_changes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quant = quantText.getText().toString();
                if(quant.trim().equals("")){
                    TextInputLayout input = (TextInputLayout) layout.findViewById(R.id.input_layout_ing_quantity);
                    input.setError(getResources().getString(R.string.quantity_error));
                    quantText.setText("");
                    return;
                }

                float q = Float.valueOf(quant);
                String udm = udmSpinner.getSelectedItem().toString();
                Date bb = new Date(datePicker.getYear()-1900, datePicker.getMonth(), datePicker.getDayOfMonth());

                ingredient.setQuantity(q);
                ingredient.setUdm(udm);
                ingredient.setBestBefore(bb);

                UpdateFridge task = new UpdateFridge(ingredient.getId(), q, udm, bb);
                task.execute(view.getContext());

                mListener.onIngredientEdited(ingredient);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (FridgeAdapter.OnEditIngredients) activity;
        }catch (ClassCastException e){
            Log.e("CLASSCAST", "MUST IMPLEMENT LISTENER");
        }
    }
}
