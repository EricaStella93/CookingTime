package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.erica.cookingtime.Adapters.CourseAdapter;
import com.example.erica.cookingtime.Adapters.CuisineAdapter;
import com.example.erica.cookingtime.Dialogs.ResetCoursesDialog;
import com.example.erica.cookingtime.Dialogs.ResetCuisinesDialog;
import com.example.erica.cookingtime.POJO.Course;
import com.example.erica.cookingtime.POJO.Cuisine;
import com.example.erica.cookingtime.R;

import java.util.ArrayList;

public class CourseFilterFragment extends Fragment {

    private static final String COURSES = "cuisines";
    private ArrayList<Course> courses;

    private RecyclerView coursesRecView;

    private View dualPane;

    private OnCloseFilterFragment mListener;

    public static CourseFilterFragment newInstance(ArrayList<Course> courses){
        CourseFilterFragment fragment = new CourseFilterFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(COURSES, courses);
        fragment.setArguments(args);
        return fragment;
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courses = getArguments().getParcelableArrayList(COURSES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.filter_course, container, false);
        layout.findViewById(R.id.back_to_filters_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCloseFilterFragment(FilterSupportFragment.COURSE_OPEN);
            }
        });

        layout.findViewById(R.id.reset_filter_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ResetCoursesDialog dialog = new ResetCoursesDialog();
                dialog.show(((AppCompatActivity)getActivity()).getSupportFragmentManager(), "reset_filter");
            }
        });

        coursesRecView = (RecyclerView) layout.findViewById(R.id.courses_rec_view);
        dualPane = layout.findViewById(R.id.stub_second);
        if(courses != null){
            setCoursesRecView();
        }
        return layout;
    }

    private void setCoursesRecView(){
        RecyclerView.LayoutManager layoutManager;
        if(dualPane == null){
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                layoutManager = new GridLayoutManager(coursesRecView.getContext(), 4);
            }else{
                layoutManager = new GridLayoutManager(coursesRecView.getContext(), 2);
            }
        }else{
            layoutManager = new GridLayoutManager(coursesRecView.getContext(), 2);
        }
        coursesRecView.setLayoutManager(layoutManager);
        CourseAdapter adapter = new CourseAdapter(courses);
        coursesRecView.setAdapter(adapter);
    }

    public void setCourses(ArrayList<Course> courses){
        if(this.courses == null){
            this.courses = courses;
            setCoursesRecView();
        }
    }

    public void resetAllCourses(){
        if(courses != null){
            for(int i = 0; i < courses.size(); i++){
                courses.get(i).setChosen(false);
            }

            if(coursesRecView != null){
                coursesRecView.getAdapter().notifyDataSetChanged();
            }
        }
    }
}
