package com.example.amir.mydiabetes;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        TextView txtAbout;
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_about, container, false);
        txtAbout = view.findViewById(R.id.txtAbout);
        String str = "About: \n" +
                "The application is intended for diabetics to record blood sugar measurements.\n" +
                "Contains the following options:\n" +
                "1. Recording glucose (sugar, carbohydrate, insulin).\n" +
                "2. Measure log.\n" +
                "3. Testing the average blood glucose for different periods.\n" +
                "4. Send emergency messages if necessary containing location.\n" +
                "5. Alerts for measuring sugar.\n" +
                "\n" +
                "\n" +
                "The application was built as part of an Android development course at Azrieli College, by Amir Erez and Or Bardugo.";
        txtAbout.setText(str);
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        OnFragmentInteractionListener mListener;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
