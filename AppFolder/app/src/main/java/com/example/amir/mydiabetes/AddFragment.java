package com.example.amir.mydiabetes;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddFragment extends Fragment implements View.OnClickListener{

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    AssignmentsDbHelper dbHelper;
    SQLiteDatabase db;
    EditText inputGluc , inputIns , inputCarbs;
    Button submitBtn;
    Snackbar errorSnackbar;
    public AddFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_add, container, false);
        inputGluc = view.findViewById(R.id.txtGlucose);
        inputIns = view.findViewById(R.id.txtInsulin);
        inputCarbs = view.findViewById(R.id.txtCarbs);
        submitBtn = view.findViewById(R.id.btnSubmit);
        submitBtn.setOnClickListener(this);
        String stringId  = "Please enter glucose level";
        errorSnackbar = Snackbar.make(view, stringId,  Snackbar.LENGTH_SHORT);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            // NOTE: This is the part that usually gives you the error
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(inputGluc.getText().length() == 0) {
            errorSnackbar.show();
            return;
        }
        mContext = this.getActivity();
        dbHelper = new AssignmentsDbHelper(mContext);
        db = dbHelper.getWritableDatabase();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy 'At' HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        ContentValues values = new ContentValues();

        values.put(Constants.diabetesTable.GLUCOSE,""+inputGluc.getText());
        values.put(Constants.diabetesTable.INSULIN,""+inputIns.getText());
        values.put(Constants.diabetesTable.CARBO,""+inputCarbs.getText());
        values.put(Constants.diabetesTable.DATE, date);

        long id;
        id = db.insert(Constants.diabetesTable.TABLE_NAME,null,values);
        db.close();
        Bundle bundle = new Bundle();
        bundle.putInt("glucose",Integer.parseInt(inputGluc.getText().toString()));
        //bundle.putInt("carbo",Integer.parseInt(""+inputCarbs.getText()));
        //bundle.putInt("insulin",Integer.parseInt(""+inputIns.getText()));
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment submitFragment = new SubmitFragment();
        submitFragment.setArguments(bundle);
        transaction.replace(R.id.mainFrame, submitFragment);
       // transaction.addToBackStack(null);
        transaction.commit();
    }


    public interface OnFragmentInteractionListener {
        // NOTE : We changed the Uri to String.
        void onFragmentInteraction(String title);
    }
}
