package com.example.amir.mydiabetes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;


public class CalendarFragment extends Fragment {


    private OnFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context mContext;
        SQLiteDatabase db;
        AssignmentsDbHelper dbHelper;
        RecyclerView myRecyclerView;
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
        MyCustomAdapter myAdapter;
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
            String excep = context.toString() + " must implement OnFragmentInteractionListener";
            throw new RuntimeException(excep);
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
