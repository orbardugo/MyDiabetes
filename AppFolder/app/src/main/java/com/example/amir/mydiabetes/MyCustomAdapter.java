package com.example.amir.mydiabetes;

/**
 * Created by or on 08/01/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyViewHolder> {

    private final Context mContext;
    private Cursor mData;

    MyCustomAdapter(Context context, Cursor cursor ) {
        this.mData = cursor;
        this.mContext = context;
    }

    @Override
    public MyCustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.single_row, parent, false);

        return new MyViewHolder(contactView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView rowGluc;
        private TextView rowIns;
        private TextView rowCarbs;
        private TextView rowDate;
        private RelativeLayout layout;

        MyViewHolder(View view) {
            super(view);
            AssignmentsDbHelper dbHelper;
            dbHelper = new AssignmentsDbHelper(mContext);
            rowGluc = view.findViewById(R.id.textGluc);
            rowIns = view.findViewById(R.id.textIns);
            rowCarbs = view.findViewById(R.id.textCarbs);
            rowDate = view.findViewById(R.id.textDate);
            layout = view.findViewById(R.id.rowLayout);
        }

    }

    @Override
    public void onBindViewHolder(MyCustomAdapter.MyViewHolder holder, int position) {
        mData.moveToPosition(position);

        holder.rowGluc.setText(mData.getString(0));
        holder.rowIns.setText("Ins:"+ mData.getString(1));
        holder.rowCarbs.setText("Carbs:"+ mData.getString(2));
        holder.rowDate.setText(mData.getString(3));
    }

    @Override
    public int getItemCount() {
        return mData.getCount();
    }


}
