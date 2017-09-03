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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.R;

public class AddShoppingListIngredientDialog extends DialogFragment{

    private AddShopListIngListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.add_ing_shop_list_dialog, null);

        builder.setView(layout);
        final Dialog dialog = builder.create();

        ImageButton imageButton = (ImageButton) layout.findViewById(R.id.shop_list_exit_add_ing_dialog);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        Button button = (Button) layout.findViewById(R.id.cancel_add_ingredient_shop_list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        Button addButton = (Button) layout.findViewById(R.id.add_ingredient_shop_list);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //prendo il nome e controllo che non sia una stringa vuota
                EditText nameInput = (EditText) layout.findViewById(R.id.input_ing_name);
                String name = nameInput.getText().toString().trim();
                if(name.isEmpty()){
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

                EditText recipe = (EditText) layout.findViewById(R.id.input_recipe);
                String recipeName = recipe.getText().toString().trim();

                mListener.onAddIngredient(name, finalQuant, udm, aisle, recipeName);
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

        return dialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (AddShopListIngListener) activity;
        }catch (ClassCastException e){
            Log.e("CLASSCAST", "MUST IMPLEMENT LISTENER");
        }
    }

    public interface AddShopListIngListener{
        void onAddIngredient(String name, float quantity, String udm, String aisle, String recipe);
    }
}
