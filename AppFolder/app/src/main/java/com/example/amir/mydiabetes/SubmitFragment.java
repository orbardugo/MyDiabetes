package com.example.amir.mydiabetes;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitFragment extends Fragment {

    TextView result;
    Button smsBtn;
    View view;

    public SubmitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_submit, container, false);
        result = view.findViewById(R.id.sugarLvlTxt);
        smsBtn = view.findViewById(R.id.smsBtn);
        String res = "your gluc is: "+ getArguments().getString("glucose");
        result.setText(res);
        return view;
    }

}
