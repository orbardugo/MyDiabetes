package com.example.amir.mydiabetes;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitFragment extends Fragment implements View.OnClickListener {

    TextView result;
    Button smsBtn;
    View view;
    SmsManager smsManager;
    String name;
    String phoneNum;
    String lat,lng;
    public SubmitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_submit, container, false);
        result = view.findViewById(R.id.sugarLvlTxt);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        name = prefs.getString("txt_name", "");
        phoneNum = prefs.getString("edit_text_emergency","");
        smsBtn = view.findViewById(R.id.smsBtn);
        smsBtn.setOnClickListener(this);
        lat = "31.769524";
        lng = "35.193667";
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
        else if(gluc>=250) {
            res = "Your Blood Sugar Level " + gluc + " is high!\n" +
                    "We'll recommend to take insulin injection and  Drink lots of water\n" +
                    "If your glucose level does not go down, contact your doctor and consider a hospital evacuation.";
            smsBtn.setVisibility(View.VISIBLE);

        }
        else
            res="Your Blood Sugar Level "+gluc+" is good!";
        result.setText(res);
        return view;
    }

    @Override
    public void onClick(View v) {
        smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, null, name+"'s blood sugar is very high! glucose:" + getArguments().getInt("glucose") + " \nhis location is at: " + "http://maps.google.com/?q="+lat+","+lng , null, null);

    }
}
