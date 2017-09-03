package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.MyTextView;


public class PrepTimeFilterFragment extends Fragment{

    private OnCloseFilterFragment mListener;

    SharedPreferences sharedPref;

    public static PrepTimeFilterFragment newInstance(){
        return new PrepTimeFilterFragment();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnCloseFilterFragment){
            mListener = (OnCloseFilterFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        sharedPref = context.getSharedPreferences(ConstantsDictionary.SHARED_PREF_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.filter_prep_time, container, false);
        layout.findViewById(R.id.back_to_filters_prep_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCloseFilterFragment(FilterSupportFragment.PREP_TIME_OPEN);
            }
        });

        Switch mySwitch = (Switch) layout.findViewById(R.id.switch_prep_time);
        boolean on = sharedPref.getBoolean(ConstantsDictionary.SHARED_PREP_TIME_SWITCH, false);
        mySwitch.setChecked(on);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(ConstantsDictionary.SHARED_PREP_TIME_SWITCH, isChecked);
                editor.apply();
            }
        });
        final MyTextView text = (MyTextView) layout.findViewById(R.id.seekbar_progress_text);

        SeekBar mySeekBar = (SeekBar) layout.findViewById(R.id.prep_time_seekbar);
        int progress = sharedPref.getInt(ConstantsDictionary.SHARED_PREP_TIME_PROGRESS, 0);
        mySeekBar.setProgress(progress);
        text.setText(setSeekBarText(progress));
        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text.setText(setSeekBarText(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return layout;
    }

    private String setSeekBarText(int progress){
        switch (progress){
            case 0:
                return "<5 minutes";
            case 1:
                return "<10 minutes";
            case 2:
                return "<15 minutes";
            case 3:
                return "<20 minutes";
            case 4:
                return "<25 minutes";
            case 5:
                return "<30 minutes";
            case 6:
                return "<45 minutes";
            case 7:
                return "<1h";
            case 8:
                return "<1.5h";
            case 9:
                return "<2h";
            case 10:
                return "<4h";
            case 11:
                return "<8h";
            case 12:
                return "<12h";
            case 13:
                return "anytime";
            default:
                return "anytime";
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
