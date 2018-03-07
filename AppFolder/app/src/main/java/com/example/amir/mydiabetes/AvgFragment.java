package com.example.amir.mydiabetes;


import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AvgFragment extends Fragment implements View.OnTouchListener,View.OnClickListener {
    private Context mContext;
    TextView txtRange, txtAvg;
    private AddFragment.OnFragmentInteractionListener mListener;
    AssignmentsDbHelper dbHelper;
    SQLiteDatabase db;
    int sum,count,avg,range;
    Cursor c;
    Button graphBtn;
    public AvgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = this.getActivity();
        View view= inflater.inflate(R.layout.fragment_avg, container, false);
        graphBtn = view.findViewById(R.id.graphBtn);
        graphBtn.setOnClickListener(this);
        txtAvg = view.findViewById(R.id.textAvg);
        txtRange = view.findViewById(R.id.rangeTxt);

        dbHelper = new AssignmentsDbHelper(mContext);
        db = dbHelper.getReadableDatabase();
        String [] projection = {Constants.diabetesTable.GLUCOSE , Constants.diabetesTable.DATE };

        c = db.query( //sort by ASU
                Constants.diabetesTable.TABLE_NAME,  // The table to query
                projection,               // The columns to return
                null,                // WHERE clause
                null,                // The values for the WHERE clause
                null,                //  group the rows
                null,                // filter by row groups
                null);             // The sort order
        if(c.getCount()<=0)
            return view;
        range=0;
        setAvg();

        view.setOnTouchListener(new OnSwipeTouchListener(mContext) {
            @Override
            public void onSwipeLeft() {
                range++;
                if(range==4)
                    range=0;
                avg=sum/count;
                txtAvg.setText("Average: "+avg+" Range:"+range);
                setAvg();
            }
            public void onSwipeRight(){
                range--;
                if(range==-1)
                    range=3;
                avg=sum/count;
                txtAvg.setText("Average: "+avg+" Range:"+range);
                setAvg();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddFragment.OnFragmentInteractionListener) {
            mListener = (AddFragment.OnFragmentInteractionListener) context;
        } else {
            // NOTE: This is the part that usually gives you the error
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }
    public void setAvg(){
        sum=0;count=0;avg=0;

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String todayStr = df.format(Calendar.getInstance().getTime());
        Date today = null;
        Date rangeDate=null;
        try {
            today = df.parse(todayStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            if(range==1)    // week
                calendar.add(Calendar.DAY_OF_YEAR, -7);
            else if(range==2)        // month
                calendar.add(Calendar.DAY_OF_YEAR, -30);
            rangeDate = calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.moveToFirst();
        String dateStr = c.getString(1);
        Date date=null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(range==0) {      // today
            if (date.equals(rangeDate)) {
                sum += c.getInt(0);
                count++;
            }
        }
        else {
            if (date.after(rangeDate) || range == 3) {   // range=3 means allways
                sum += c.getInt(0);
                count++;
            }
        }


        while(c.moveToNext()) {
            dateStr = c.getString(1);
            date=null;
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(range==0) {
                if (date.equals(rangeDate)) {
                    sum += c.getInt(0);
                    count++;
                }
            }
            else {
                if (date.after(rangeDate) || range == 3) {   // range=3 means allways
                    sum += c.getInt(0);
                    count++;
                }
            }
        }
        avg=sum/count;
        txtAvg.setText(""+avg);
        if(range==0)
            txtRange.setText("Today");
        else if(range==1)
            txtRange.setText("Week");
        else if(range==2)
            txtRange.setText("Month");
        else
            txtRange.setText("Allways");
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onClick(View v) {
        int sumM=0;int sumN=0;int sumE=0;int countM=0;int countN=0;int countE=0;
        c.moveToFirst();
        Bundle bundle = new Bundle();
        int morningAvg,noonAvg,eveningAvg;
        Date fromMorning,toMorning;

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int t = cal.get(Calendar.HOUR_OF_DAY) * 100 + cal.get(Calendar.MINUTE);
        Date dateR=null;
        DateFormat df = new SimpleDateFormat("HHmm");
        fromMorning =Calendar.getInstance().getTime();
        toMorning =Calendar.getInstance().getTime();
        String dateStr = c.getString(1);
        try {
            dateR = df.parse(dateStr);
            fromMorning = df.parse("0600");
            toMorning = df.parse("1200");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if( dateR.after(fromMorning)&& dateR.after(toMorning))
        {
            sumM += c.getInt(0);
            countM++;
        }



        while(c.moveToNext()) {
            sumM += c.getInt(0);
            countM++;
        }
        morningAvg = sumM/countM;
        bundle.putInt("MorningAvg",morningAvg);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment graphFragment = new graphFragment();
        graphFragment.setArguments(bundle);
        transaction.replace(R.id.mainFrame, graphFragment);
        // transaction.addToBackStack(null);
        transaction.commit();
    }
    public interface OnFragmentInteractionListener {
        // NOTE : We changed the Uri to String.
        void onFragmentInteraction(String title);
    }
}
