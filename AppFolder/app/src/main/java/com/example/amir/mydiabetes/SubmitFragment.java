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
        int gluc = getArguments().getInt("glucose");
        String res = null;
        if(gluc<=70)
            res = "Your Blood Sugar Level "+gluc+" is low!\n You should eat or drink something to raise it. \n" +
                    "We'll recommend one of the following:\n" +
                    "* 3 – 4 glucose tablets or gels\n" +
                    "* 4 ounces of fruit juice\n" +
                    "* 6 ounces of regular soda\n" +
                    "* 1 tablespoon of honey\n" +
                    "* 1 tablespoon of table sugar";
        if(gluc>=250)
            res = "Your Blood Sugar Level "+gluc+" is high!\n" +
                    "We'll recommend to take insulin injection and  Drink lots of water\n" +
                    "If your glucose level does not go down, contact your doctor and consider a hospital evacuation.";

        else
            res="Your Blood Sugar Level "+gluc+" is good!";
        result.setText(res);
        return view;
    }

}
