package com.trinfosoft.teacherassistant;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class Saturday extends AppCompatActivity {

    ImageButton fab;
    TextView pick_hour;
    TextView pick_min;
    Spinner class_spinner;

    // Out custom adapter
    MySimpleArrayAdapter adapter;

    // contains our listview items
    ArrayList<Subject_data> listItems;
    ArrayList<Time_Data> time_data;

    // database
    DatabaseHelper DatabaseHelper;

    // list of todo titles
    ArrayList<Integer> ID;
    ArrayList<String> start_hour;
    ArrayList<String> stop_min;
    ArrayList<String> data_subject;
    ArrayList<String> spinner_subject;

    // contains the id of the item we are about to delete
    public int deleteItem;

    // EditText field for adding new items to the list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saturday);
        this.setTitle("Saturday");

        // We're getting our listView by the id
        final ListView listView = (ListView) findViewById(R.id.list);

        /**
        * Creating a new instance of our DatabaseHelper, which we've created
        * earlier
        */
        DatabaseHelper = new DatabaseHelper(this);

        // This returns a list of all our current available notes
        listItems = DatabaseHelper.getAllNotes_Subject();
        time_data = DatabaseHelper.getAllNotes_Saturday();

        ID = new ArrayList<Integer>();
        start_hour = new ArrayList<String>();
        stop_min = new ArrayList<String>();
        data_subject = new ArrayList<String>();
        spinner_subject = new ArrayList<String>();


        // Assigning the title to our global property so we can access it
        // later after certain actions (deleting/adding)
        for (Time_Data note : time_data) {
            ID.add(note.Id);
            start_hour.add(note.start_h);
            stop_min.add(note.start_m);
            data_subject.add(note.subject);
        }


        for(int j=0;j<start_hour.size()-1;j++) {
            for (int i = 0; i < start_hour.size() - 1; i++) {
                int h = Integer.parseInt(start_hour.get(i));
                int m = Integer.parseInt(stop_min.get(i));
                int nh = Integer.parseInt(start_hour.get(i + 1));
                int nm = Integer.parseInt(stop_min.get(i + 1));
                int id = ID.get(i);
                int nid = ID.get(i + 1);
                String sub = data_subject.get(i);
                String nsub = data_subject.get(i + 1);

                if (h > nh) {
                    ID.set(i, nid);
                    start_hour.set(i, nh + "");
                    stop_min.set(i, nm + "");
                    data_subject.set(i, nsub);

                    ID.set(i + 1, id);
                    start_hour.set(i + 1, h + "");
                    stop_min.set(i + 1, m + "");
                    data_subject.set(i + 1, sub);
                }
                if (h == nh) {
                    if (m > nm) {
                        ID.set(i, nid);
                        start_hour.set(i, nh + "");
                        stop_min.set(i, nm + "");
                        data_subject.set(i, nsub);

                        ID.set(i + 1, id);
                        start_hour.set(i + 1, h + "");
                        stop_min.set(i + 1, m + "");
                        data_subject.set(i + 1, sub);
                    }
                }
            }
        }

        time_data.clear();

        for(int i=0;i<start_hour.size();i++) {
            Time_Data time = new Time_Data();
            time.Id = ID.get(i);
            time.start_h = start_hour.get(i);
            time.start_m = stop_min.get(i);
            time.subject = data_subject.get(i);
            time_data.add(time);
        }

        for (Subject_data note : listItems) {
            spinner_subject.add(note.Subject);
        }

        // We're initialising our custom adapter with all our data from the
        // database
        adapter = new MySimpleArrayAdapter(this, start_hour, stop_min, data_subject);

        // Assigning the adapter to ListView
        listView.setAdapter(adapter);

        // Assigning an event to the listview
        // This event will be used to delete records
        listView.setOnItemClickListener(myClickListener);

        /**
         * After clicking on the fab button the pop up will opens for the user
         * so that user can edit the details of lectures
         */
        fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Toast.makeText(LectureTime.this, "Open PopUp",Toast.LENGTH_LONG).show();
                LinearLayout layout1, layout2;

                final TextView starthour,startmin;

                View view1 = LayoutInflater.from(Saturday.this)
                        .inflate(R.layout.add_lectures, null);

                layout1 = (LinearLayout) view1.findViewById(R.id.start_t);
                starthour = (TextView) view1.findViewById(R.id.s_hour);
                startmin = (TextView) view1.findViewById(R.id.s_min);

                layout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pick_hour = starthour;
                        pick_min = startmin;
                        showTimePickerDialog1(view);
                 //    Toast.makeText(LectureTime.this, "Starting Time is Selected",
                        // Toast.LENGTH_LONG).show();
                    }
                });

                if (!spinner_subject.isEmpty()) {
                    /**
                     * check for subjects are defined in the settings or not
                     * if not then reject the request to open popup
                     */
                    rotateAnimation(fab);

                    class_spinner = (Spinner) view1.findViewById(R.id.select_subject);

                    final ArrayAdapter<String> adapter;

                    adapter = new ArrayAdapter<String>(Saturday.this,
                            android.R.layout.simple_spinner_dropdown_item, spinner_subject);

                    class_spinner.setAdapter(adapter);

                    AlertDialog.Builder builder = new AlertDialog.Builder(Saturday.this)
                            .setMessage("Add Lecture Time and Subject")
                            .setView(view1)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    /**
                                     * Opening of pop up that is use to select lecture time
                                     * and subjects that are defined in the setting
                                     * all subjects are in spinner retrieved from the database
                                     */
                                    if(Integer.parseInt(starthour.getText().toString())<21&&
                                            Integer.parseInt(starthour.getText().toString())>3) {
                                        /**
                                         * if the time is valid then it store to the database
                                         * otherwise it will rejected
                                         */
                                        String selected_subject = spinner_subject.
                                                get(class_spinner.getSelectedItemPosition());

                                        long Id = DatabaseHelper.addRecord_Saturday(
                                                starthour.getText().toString(),
                                                startmin.getText().toString(),
                                                selected_subject, getApplicationContext());

                                        // Create a new MyNotes object to add it to our
                                        // global property listItems
                                        Time_Data note = new Time_Data();
                                        //     note.Id = (int) Id;
                                        note.start_h = starthour.getText().toString();
                                        note.start_m = startmin.getText().toString();
                                        note.subject = selected_subject;

                                        time_data.add(note);

                                        start_hour.add(note.start_h);
                                        stop_min.add(note.start_m);
                                        data_subject.add(note.subject);

                                        adapter.notifyDataSetChanged();

                                        /**
                                         * After storing the data into the databse we have to
                                         * reset the list view and again retrieve that items
                                         * in to the list view so that finish the current activity
                                         * and open again it by using getIntent
                                         */
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);

                                        //Display the toast that which data is to be stored into
                                        //the database
                                        Toast.makeText(Saturday.this,
                                                starthour.getText().toString() + " : " +
                                                        startmin.getText().toString() + " " +
                                                        class_spinner.getSelectedItem().toString(),
                                                Toast.LENGTH_LONG).show();

                                        clearAnimation(fab);

                                    }else{
                                        Toast.makeText(Saturday.this, "Invalid Time",
                                                Toast.LENGTH_LONG).show();

                                        clearAnimation(fab);
                                    }

                                }
                            })
                            .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    clearAnimation(fab);
                                }})
                            .setCancelable(false);

                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    /**
                     * Show the message that the subjects are not defined in the setting
                     */
                    Toast.makeText(Saturday.this, "Please Add Subject Detail First",
                            Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    /**
     * When user clicks on the fab button then it will rotated by using rotateAnimation
     * and if user clicks on ok button or cancel button then rotation will be reset
     * @param v
     */
    public static void rotateAnimation(View v){
        // Create an animation instance
        final Animation an = new RotateAnimation(0, 225,
                v.getWidth()/2, v.getHeight()/2);
        an.setDuration(700);
        an.setFillAfter(true);
        v.startAnimation(an);
    }

    public static void clearAnimation(View v){
        // Create an animation instance
        final Animation an = new RotateAnimation(225, 0,
                v.getWidth()/2, v.getHeight()/2);
        an.setDuration(500);
        an.setFillAfter(true);
        v.startAnimation(an);
    }


    /**
     * showTimePickerDialog1 method can be opens the timepicker popup and allow the user
     * to pick the time using system time picker module
     */

    public void showTimePickerDialog1(View v) {
        DialogFragment newFragment = new TimePickerFragment(pick_hour, pick_min);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTimePickerDialog2(View v) {
        DialogFragment newFragment = new TimePickerFragment(pick_hour, pick_min);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    /**
     * in this class when user clicks on the list items then the choice will
     * open for the users that do you want to delete or not
     * in that the view of the list view plays main roll to find which item number
     * is to be clicked by the user
     */

    public AdapterView.OnItemClickListener myClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1,
                                int arg2, long arg3) {
            // Assigning the item position to our global variable
            // So we can access it within our AlertDialog below
            deleteItem = arg2;

            // Creating a new alert dialog to confirm the delete
            android.app.AlertDialog alert = new android.app.AlertDialog.Builder(arg1.getContext())
                    .setTitle("Are you sure to Delete?")
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                    /**
                                    * Retrieving the note from our listItems
                                    * property, which contains all notes from
                                    * our database
                                    */
                                    Time_Data note =time_data.get(deleteItem);

                                    // Deleting it from the ArrayList<string>
                                    // property which is linked to our adapter
                                    start_hour.remove(deleteItem);

                                    // Deleting the note from our database
                                    DatabaseHelper.contextset(getApplicationContext()).deleteNote_Saturday(note.Id);

                                    // Tell the adapter to update the list view
                                    // with the latest changes
                                    adapter.notifyDataSetChanged();

                                    /**
                                     * After storing the data into the databse we have to
                                     * reset the list view and again retrieve that items
                                     * in to the list view so that finish the current activity
                                     * and open again it by using getIntent
                                     */
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);

                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // When you press cancel, just close the
                                    // dialog
                                    dialog.cancel();
                                }
                            }).show();

        }
    };

    /**
     * This adapter will create your list view row by row
     * In this adapter wi gets the view of rowlayout and edit it's items
     * then it will pushed to the list adapter
     */
    public class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values_1;
        private final ArrayList<String> values_2;
        private final ArrayList<String> values_3;

        public MySimpleArrayAdapter(Context context, ArrayList<String> values_1,
                                    ArrayList<String> values_2, ArrayList<String> values_3) {
            super(context, R.layout.rowlayout, values_1);

            this.context = context;
            this.values_1 = values_1;
            this.values_2 = values_2;
            this.values_3 = values_3;
        }

        /**
         * Here we go and get our rowlayout.xml file and set the textview text.
         * This happens for every row in your listview.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int cnt = 0;
            View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

            TextView textView = (TextView) rowView.findViewById(R.id.label);

            /**
             * Convert the time to AM and PM format so that user can easily understand
             * the timing of lectures in there own time format
             */
            int hourOfDay = Integer.parseInt(values_1.get(position));
            String aMpM = "AM";

            if(hourOfDay >11) {
                aMpM = "PM";
            }

            //Make the 24 hour time format to 12 hour time format
            int currentHour;

            if(hourOfDay>11) {
                currentHour = hourOfDay - 12;
            } else {
                currentHour = hourOfDay;
            }

            if(currentHour==0){
                currentHour = 12;
            }

            // Setting the text to display
            textView.setText((position+1)+". "+String.format("%02d", currentHour)+" : "+
                    values_2.get(position)+" "+aMpM+"  -> "+values_3.get(position));

            return rowView;
        }
    }
}
