package com.example.amir.mydiabetes;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;



public class graphFragment extends Fragment {



    public graphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_graph, container, false);


        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, getArguments().getInt("MorningAvg")),//morning bar
                new DataPoint(1, getArguments().getInt("NoonAvg")),//noon bar
                new DataPoint(2, getArguments().getInt("EveningAvg"))//evening bar
        });
        graph.addSeries(series);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"Morning" , "Noon" , "Evening"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });
        series.setSpacing(30);
        series.setDrawValuesOnTop(true);//value on top of each bar
        series.setValuesOnTopColor(Color.RED);
        graph.setTitle("All time Glucose level by day time");//graph title
        graph.setTitleTextSize(60);//title size
        graph.setTitleColor(Color.rgb(255,0,0));
        return view;
    }


}
