package com.example.erica.cookingtime.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.erica.cookingtime.DataBase.DBModifiers.DeleteShoppingList;
import com.example.erica.cookingtime.DataBase.DBModifiers.ModifyFridge;
import com.example.erica.cookingtime.DataBase.DBModifiers.ModifyShopList;
import com.example.erica.cookingtime.Dialogs.EditFridgeDialog;
import com.example.erica.cookingtime.Dialogs.EditShopListIngredient;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.POJO.ShoppingListIngredient;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.ArrayList;

public class FridgeAdapter extends  RecyclerView.Adapter<FridgeAdapter.ViewHolder> {

    private OnEditIngredients mListener;

    private ArrayList<FridgeIngredient> ingredients;
    private AppCompatActivity context;

    public FridgeAdapter(ArrayList<FridgeIngredient> ingredients,
                         OnEditIngredients listener, AppCompatActivity context) {
        this.ingredients = ingredients;
        this.mListener = listener;
        this.context = context;
    }

    public void add(int position, FridgeIngredient item){
        ingredients.add(position, item);
        notifyItemInserted(position);
    }

    public void addAll(ArrayList<FridgeIngredient> items){
        ingredients.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        ingredients.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(FridgeIngredient item){
        int position = ingredients.indexOf(item);
        remove(position);
    }

    public void removeAll(){
        ingredients.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ingName;
        public TextView date;
        public LinearLayout rowContainer;
        public SwipeLayout swipe;
        public LinearLayout bottom;
        public ImageButton edit;
        public ImageButton delete;

        public ViewHolder(View v){
            super(v);
            date = (TextView) v.findViewById(R.id.date_text);
            ingName = (TextView) v.findViewById(R.id.ing_text);
            rowContainer = (LinearLayout) v.findViewById(R.id.row_container);
            swipe = (SwipeLayout) v.findViewById(R.id.swipe);
            bottom = (LinearLayout) v.findViewById(R.id.bottom_wrapper);
            edit = (ImageButton) v.findViewById(R.id.edit_ing);
            delete = (ImageButton) v.findViewById(R.id.delete_ing);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FridgeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fridge_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FridgeAdapter.ViewHolder vh = new FridgeAdapter.ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final FridgeAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final FridgeIngredient ing = ingredients.get(position);

        holder.swipe.addDrag(SwipeLayout.DragEdge.Right, holder.bottom);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditFridgeDialog dialog = EditFridgeDialog.newInstance(ing);
                dialog.show(context.getSupportFragmentManager(), "edit_dialog");

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
                ModifyFridge task = new ModifyFridge(ConstantsDictionary.SINGLE, ing.getId());
                task.execute(view.getContext());
                mListener.resort(ing);
            }
        });

        holder.swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        if(position % 2 == 0){
            holder.rowContainer.setBackgroundColor(holder.rowContainer.getResources().getColor(R.color.light_grey));
        }else{
            holder.rowContainer.setBackgroundColor(holder.rowContainer.getResources().getColor(R.color.lighter_grey));
        }
        //ing name
        String name;
        String udm = ing.getUdm();
        if(udm == null){
            udm = "";
        }
        String quantity;
        if(ing.getQuantity()<=0){
            quantity = "";
        }else{
            quantity = String.valueOf(ing.getQuantity());
        }
        name = quantity + " " + udm + " " + ing.getName();
        holder.ingName.setText(name);

        holder.date.setText(IngredientUtils.fromDateToString(ing.getBestBefore()));
    }

    @Override
    public int getItemCount(){
        return ingredients.size();
    }

    public interface OnEditIngredients{
        void resort(FridgeIngredient ing);
        void onIngredientEdited(FridgeIngredient ing);
    }
}
