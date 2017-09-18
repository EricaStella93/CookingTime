package com.example.erica.cookingtime.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.erica.cookingtime.Adapters.IngredientAdapter;
import com.example.erica.cookingtime.Adapters.InstructionsAdapter;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.MyTextView;

import java.util.ArrayList;

public class InstructionsFragment extends Fragment {

    private static final String INSTR = "instr";
    private String instructions;

    private RecyclerView recyclerView;

    public InstructionsFragment(){

    }

    public static InstructionsFragment newInstance(String instructions){
        InstructionsFragment fragment = new InstructionsFragment();
        Bundle args = new Bundle();
        args.putString(INSTR, instructions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            instructions = getArguments().getString(INSTR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.instructions_layout, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.instr_rec_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(layout.getContext());
        recyclerView.setLayoutManager(layoutManager);
        if(instructions != null){
            String[] spli = instructions.split("\\.");
            ArrayList<String> instr = new ArrayList<>();
            for(String spl : spli){
                instr.add(spl.trim());
            }
            InstructionsAdapter adapter = new InstructionsAdapter(instr);
            recyclerView.setAdapter(adapter);
        }
        return layout;
    }
}
