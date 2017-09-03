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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.erica.cookingtime.DataBase.DBModifiers.ModifyShopListIngredient;
import com.example.erica.cookingtime.POJO.ShoppingListIngredient;
import com.example.erica.cookingtime.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class EditShopListIngredient extends DialogFragment{

    private static final String INGREDIENT = "ingredient";
    private ShoppingListIngredient ingredient;

    private OnEditIngredient mListener;

    public interface OnEditIngredient{
        void onIngredientEdited(ShoppingListIngredient ing);
    }

    public EditShopListIngredient(){

    }

    public static EditShopListIngredient newInstance(ShoppingListIngredient ingredient){
        EditShopListIngredient fragment = new EditShopListIngredient();
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
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.edit_shop_ing, null);

        builder.setView(layout);
        final Dialog dialog = builder.create();

        //recipe
        String recipe = ingredient.getRecipe();
        if(recipe == null){
            layout.findViewById(R.id.recipe_name).setVisibility(View.GONE);
        }else{
            if(recipe.equals("")){
                layout.findViewById(R.id.recipe_name).setVisibility(View.GONE);
            }else{
                ((TextView) layout.findViewById(R.id.recipe_name)).setText(recipe);

            }
        }
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
        //TODO selezionare quello dell'ing

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

                ingredient.setQuantity(q);
                ingredient.setUdm(udm);

                ModifyShopListIngredient task = new ModifyShopListIngredient(ingredient.getId(), q, udm);
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
            mListener = (OnEditIngredient) activity;
        }catch (ClassCastException e){
            Log.e("CLASSCAST", "MUST IMPLEMENT LISTENER");
        }
    }

}
