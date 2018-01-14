package com.example.amir.mydiabetes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalendarFragment extends Fragment {

    private Context mContext;
    RecyclerView myRecyclerView;
    AssignmentsDbHelper dbHelper;
    SQLiteDatabase db;
    MyCustomAdapter myAdapter;

    private OnFragmentInteractionListener mListener;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        myRecyclerView = view.findViewById(R.id.recyclerView);
        myRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mContext));
        dbHelper = new AssignmentsDbHelper(mContext);
        db = dbHelper.getReadableDatabase();
        String [] projection = {Constants.diabetesTable.GLUCOSE ,Constants.diabetesTable.INSULIN,
                Constants.diabetesTable.CARBO , Constants.diabetesTable.DATE };
        Cursor c;

            c = db.query( //sort by ASU
                    Constants.diabetesTable.TABLE_NAME,  // The table to query
                    projection,               // The columns to return
                    null,                // WHERE clause
                    null,                // The values for the WHERE clause
                    null,                //  group the rows
                    null,                // filter by row groups
                    null);             // The sort order

        myAdapter = new MyCustomAdapter(mContext, c);
        myRecyclerView.setAdapter(myAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }
}
