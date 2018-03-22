package com.example.amir.mydiabetes;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddFragment extends Fragment implements View.OnClickListener{

    // NOTE: Removed Some unwanted Boiler Plate Codes

    private Context mContext;
    private View view;
    AssignmentsDbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText inputGluc , inputIns , inputCarbs;
    ImageButton imgCarbs,imgInsulin;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_add, container, false);
        Button submitBtn;
        inputGluc = view.findViewById(R.id.txtGlucose);
        inputIns = view.findViewById(R.id.txtInsulin);
        inputCarbs = view.findViewById(R.id.txtCarbs);
        submitBtn = view.findViewById(R.id.btnSubmit);
        imgCarbs = view.findViewById(R.id.imgCarbs);
        imgInsulin = view.findViewById(R.id.imgInsulin);
        submitBtn.setOnClickListener(this);
        imgCarbs.setOnClickListener(this);
        imgInsulin.setOnClickListener(this);




        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated


        // Here we will can create click listners etc for all the gui elements on the fragment.
        // For eg: Button btn1= (Button) view.findViewById(R.id.frag1_btn1);
        // btn1.setOnclickListener(...

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        OnFragmentInteractionListener mListener;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            // NOTE: This is the part that usually gives you the error
            String excep = context.toString() + " must implement OnFragmentInteractionListener";
            throw new RuntimeException(excep);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSubmit) {
            if (inputGluc.getText().length() == 0) {
                Snackbar errorSnackbar;
                String stringId  = "Please enter glucose level";
                errorSnackbar = Snackbar.make(view, stringId,  Snackbar.LENGTH_SHORT);
                errorSnackbar.show();
                return;
            }
            mContext = this.getActivity();
            dbHelper = new AssignmentsDbHelper(mContext);
            db = dbHelper.getWritableDatabase();

            DateFormat df = new SimpleDateFormat("dd-MM-yyyy 'At' HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            ContentValues values = new ContentValues();

            values.put(Constants.diabetesTable.GLUCOSE, "" + inputGluc.getText());
            values.put(Constants.diabetesTable.INSULIN, "" + inputIns.getText());
            values.put(Constants.diabetesTable.CARBO, "" + inputCarbs.getText());
            values.put(Constants.diabetesTable.DATE, date);

            db.insert(Constants.diabetesTable.TABLE_NAME, null, values);
            db.close();
            Bundle bundle = new Bundle();
            bundle.putInt("glucose", Integer.parseInt(inputGluc.getText().toString()));
            //bundle.putInt("carbo",Integer.parseInt(""+inputCarbs.getText()));
            //bundle.putInt("insulin",Integer.parseInt(""+inputIns.getText()));
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Fragment submitFragment = new SubmitFragment();
            submitFragment.setArguments(bundle);
            transaction.replace(R.id.mainFrame, submitFragment);
            // transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId()==R.id.imgCarbs){
            Uri uriUrl = Uri.parse("https://www.webmd.com/diet/healthtool-food-calorie-counter");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
        else{
            if((inputGluc.getText().toString()).compareTo("")==0)
                return;
            int x=0;
            int gluc_now = Integer.parseInt(inputGluc.getText().toString());
            prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            int insulin_sensitivity = Integer.parseInt(prefs.getString("edit_text_insulin_sensitivity", ""));
            int carbo_ratio = Integer.parseInt(prefs.getString("edit_text_carbo_ratio", ""));
            int gluc_target = Integer.parseInt(prefs.getString("edit_text_gluc_target", ""));
            x = (gluc_now-gluc_target)/insulin_sensitivity;
            if((inputCarbs.getText().toString()).compareTo("")!=0)
            {
                int carbs = Integer.parseInt(inputCarbs.getText().toString());
                x+= carbs/carbo_ratio;
            }
            if(x<0)
                x=0;
            inputIns.setHint("Recommended :"+x);
        }
    }


    public interface OnFragmentInteractionListener {
        // NOTE : We changed the Uri to String.
        void onFragmentInteraction(String title);
    }
}
