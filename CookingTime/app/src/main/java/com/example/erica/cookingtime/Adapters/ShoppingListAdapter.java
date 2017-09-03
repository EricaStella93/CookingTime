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
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.erica.cookingtime.DataBase.DBModifiers.DeleteShoppingList;
import com.example.erica.cookingtime.DataBase.DBModifiers.ModifyShopList;
import com.example.erica.cookingtime.Dialogs.EditShopListIngredient;
import com.example.erica.cookingtime.POJO.ShoppingListIngredient;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private ArrayList<ShoppingListIngredient> ingList;
    private OnMoveIngredients mListener;
    private View gotItRow;
    private AppCompatActivity context;

    public ShoppingListAdapter(ArrayList<ShoppingListIngredient> ingList, OnMoveIngredients mListener,
                               View gotItRow, AppCompatActivity context){
        this.ingList = ingList;
        this.mListener = mListener;
        this.gotItRow = gotItRow;
        this.context = context;
        if(this.gotItRow != null && this.ingList.isEmpty()){
            this.gotItRow.setVisibility(View.GONE);
        }
        if(this.gotItRow != null && !this.ingList.isEmpty()){
            this.gotItRow.setVisibility(View.VISIBLE);
        }
    }

    public void add(int position, ShoppingListIngredient item){
        ingList.add(position, item);
        if(!ingList.isEmpty() && gotItRow != null){
            gotItRow.setVisibility(View.VISIBLE);
        }
        notifyItemInserted(position);
    }

    public void addAll(ArrayList<ShoppingListIngredient> items){
        if(items.isEmpty()){
            return;
        }
        ingList.addAll(items);
        if(!ingList.isEmpty() && gotItRow != null){
            gotItRow.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        ingList.remove(position);
        notifyItemRemoved(position);
        if(ingList.isEmpty() && gotItRow != null){
            gotItRow.setVisibility(View.GONE);
        }
    }

    public void remove(ShoppingListIngredient item){
        int position = ingList.indexOf(item);
        remove(position);
    }

    public void removeAll(){
        ingList.clear();
        notifyDataSetChanged();
        if(ingList.isEmpty() && gotItRow != null){
            gotItRow.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CheckBox gotItBox;
        public TextView recipeName;
        public TextView ingName;
        public LinearLayout rowContainer;
        public SwipeLayout swipe;
        public LinearLayout bottom;
        public ImageButton edit;
        public ImageButton delete;

        public ViewHolder(View v){
            super(v);
            gotItBox = (CheckBox) v.findViewById(R.id.got_it);
            recipeName = (TextView) v.findViewById(R.id.recipe_name);
            ingName = (TextView) v.findViewById(R.id.ing_name);
            rowContainer = (LinearLayout) v.findViewById(R.id.row_container);
            swipe = (SwipeLayout) v.findViewById(R.id.swipe);
            bottom = (LinearLayout) v.findViewById(R.id.bottom_wrapper);
            edit = (ImageButton) v.findViewById(R.id.edit_ing);
            delete = (ImageButton) v.findViewById(R.id.delete_ing);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.shopping_list_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ShoppingListAdapter.ViewHolder vh = new ShoppingListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ShoppingListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ShoppingListIngredient ing = ingList.get(position);

        holder.swipe.addDrag(SwipeLayout.DragEdge.Right, holder.bottom);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditShopListIngredient dialog = EditShopListIngredient.newInstance(ing);
                dialog.show(context.getSupportFragmentManager(), "edit_dialog");
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
                DeleteShoppingList task = new DeleteShoppingList(ConstantsDictionary.SINGLE, ing.getId());
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
        holder.gotItBox.setChecked(ing.isGotIt());
        holder.gotItBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                ing.setGotIt(checked);
                ModifyShopList task = new ModifyShopList(ing.getId(), checked);
                task.execute(v.getContext());
                remove(ing);
                mListener.moveIngredient(checked, ing);
            }
        });
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

        String recipe;
        recipe = ing.getRecipe();
        if(recipe == null || "".equals(recipe)){
            holder.recipeName.setVisibility(View.GONE);
            return;
        }else{
            recipe = recipe.toUpperCase();
            holder.recipeName.setVisibility(View.VISIBLE);
            holder.recipeName.setText(recipe);
        }
    }

    @Override
    public int getItemCount(){
        return ingList.size();
    }

    public interface OnMoveIngredients{
        void moveIngredient(boolean gotIt, ShoppingListIngredient ingredient);
        void resort(ShoppingListIngredient ing);
    }
}
