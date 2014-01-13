package com.groupon.mobile.frag;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
/**
 * Date picker fragment which is used by other activities and fragments.
 * Classes that wants to use this implements onDateSetListener became onDateSet listener of
 * this.
 * @author serkan
 *
 */
public class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {
	private OnDateSetListener onDateSetListener = this;
	public DatePickerFragment(){
		
	}
	/**
	 * A date picker fragment is initalized with another onDateSetListener class to
	 * make necessary steps upon on date set in that class.
	 * @param onDateSetListener onDateSetListener interface that is passed.
	 */
	public DatePickerFragment(OnDateSetListener onDateSetListener){
	
		this.onDateSetListener = onDateSetListener;
	}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
    }
}
