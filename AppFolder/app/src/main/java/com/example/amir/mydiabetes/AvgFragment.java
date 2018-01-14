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
        leftBtn = view.findViewById(R.id.leftBtn);
        rightBtn = view.findViewById(R.id.rightBtn);



        dbHelper = new AssignmentsDbHelper(mContext);
        db = dbHelper.getReadableDatabase();
        String [] projection = {Constants.diabetesTable.GLUCOSE , Constants.diabetesTable.DATE };
        Cursor c;

        c = db.query( //sort by ASU
                Constants.diabetesTable.TABLE_NAME,  // The table to query
                projection,               // The columns to return
                null,                // WHERE clause
                null,                // The values for the WHERE clause
                null,                //  group the rows
                null,                // filter by row groups
                null);             // The sort order

        sum=0;count=0;avg=0;range=0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = df.format(Calendar.getInstance().getTime());
        Date today=null;
        try {
            today = df.parse(todayStr);
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
        if (date.equals(today)) {
            sum += c.getInt(0);
            count++;
        }
        while(c.moveToNext()) {
            dateStr = c.getString(1);
            date=null;
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date.equals(today)) {
                sum += c.getInt(0);
                count++;
            }
        }

        avg=sum/count;
        txtAvg.setText("Average: "+avg);

        view.setOnTouchListener(new OnSwipeTouchListener(mContext) {
            @Override
            public void onSwipeLeft() {
                range--;
                avg=sum/count;
                txtAvg.setText("Average: "+avg+" Range:"+range);
            }
            public void onSwipeRight(){
                range++;
                avg=sum/count;
                txtAvg.setText("Average: "+avg+" Range:"+range);
            }
        });

        return view;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
