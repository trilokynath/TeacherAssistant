package com.trinfosoft.teacherassistant;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    TextView result_hour,result_min;

    public TimePickerFragment(){}
    @SuppressLint("ValidFragment")
    public TimePickerFragment(TextView hour, TextView min){
        result_hour = hour;
        result_min = min;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        String str_hour = String.format("%02d", hourOfDay);
        String str_min = String.format("%02d", minute);
        result_hour.setText(str_hour);
        result_min.setText(str_min);
    }
}