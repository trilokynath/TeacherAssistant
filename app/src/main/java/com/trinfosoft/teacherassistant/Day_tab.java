package com.trinfosoft.teacherassistant;

//import android.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Trilokynath Wagh on 08/02/2018.
 */

public class Day_tab extends Fragment {

    String switching_day;
    public void select_day(String switching_day){
        this.switching_day = switching_day;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        switch (switching_day){
            case "mon":
                return getLayoutInflater().inflate(R.layout.activity_monday, container, false);
            case "tue":
                return inflater.inflate(R.layout.activity_tuesday, container, false);
            case "wed":
                return inflater.inflate(R.layout.activity_wednesday, container, false);
            case "thu":
                return inflater.inflate(R.layout.activity_thursday, container, false);
            case "fri":
                return inflater.inflate(R.layout.activity_friday, container, false);
            case "sat":
                return inflater.inflate(R.layout.activity_saturday, container, false);
            default:
                return null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
