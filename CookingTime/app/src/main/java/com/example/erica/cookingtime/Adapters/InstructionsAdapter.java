package com.example.erica.cookingtime.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.erica.cookingtime.R;


import java.util.ArrayList;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.ViewHolder>{

    private ArrayList<String> instructions;

    public InstructionsAdapter(ArrayList<String> instructions){
        this.instructions = instructions;
    }

    public void add(int position, String item) {
        instructions.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        instructions.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        public TextView instr;
        public LinearLayout container;

        public ViewHolder(View v){
            super(v);
            instr = (TextView) v.findViewById(R.id.instr_text);
            container = (LinearLayout) v.findViewById(R.id.container_step);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public InstructionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.instruction_step, parent, false);
        // set the view's size, margins, paddings and layout parameters
        InstructionsAdapter.ViewHolder vh = new InstructionsAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final InstructionsAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String instr = instructions.get(position);

        if(position % 2 == 0){
            holder.container.setBackgroundColor(holder.container.getResources().getColor(R.color.light_grey));
        }else{
            holder.container.setBackgroundColor(holder.container.getResources().getColor(R.color.lighter_grey));
        }
        holder.instr.setText(instr);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return instructions.size();
    }

}
