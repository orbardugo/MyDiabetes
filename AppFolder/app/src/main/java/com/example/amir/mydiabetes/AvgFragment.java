package com.example.amir.mydiabetes;


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
public class AvgFragment extends Fragment implements View.OnTouchListener {
    private Context mContext;
    TextView txtRange, txtAvg;
    Button leftBtn,rightBtn;
    AssignmentsDbHelper dbHelper;
    SQLiteDatabase db;
    int sum,count,avg,range;
    Cursor c;
    public AvgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = this.getActivity();
        View view= inflater.inflate(R.layout.fragment_avg, container, false);
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

    public void setAvg(){
        sum=0;count=0;avg=0;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = df.format(Calendar.getInstance().getTime());
        Date today = null;
        Date rangeDate=null;
        try {
            today = df.parse(todayStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            if(range==1)
                calendar.add(Calendar.DAY_OF_YEAR, -7);
            else if(range==2)        // range = 2
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
}
