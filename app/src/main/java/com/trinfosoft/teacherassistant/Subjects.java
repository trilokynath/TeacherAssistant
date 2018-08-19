package com.trinfosoft.teacherassistant;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Subjects extends AppCompatActivity {

    ImageButton fab;
    Spinner class_spinner;

    EditText edit_subject;
    // Out custom adapter
    MySimpleArrayAdapter adapter;

    // contains our listview items
    ArrayList<Subject_data> listItems;
    ArrayList<Class_data> listItems_1;

    // database
    DatabaseHelper DatabaseHelper;

    // list of todo titles
    ArrayList<String> data_class;
    ArrayList<String> data_subject;
    ArrayList<String> newData_1;

    // contains the id of the item we are about to delete
    public int deleteItem;

    // EditText field for adding new items to the list
    EditText editText2;
  /*  String[] classes = {
            "FE", "SE", "TE", "BE"
    };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        this.setTitle("Subjects Details");

        // We're getting our listView by the id
        final ListView listView = (ListView) findViewById(R.id.list);

        // Creating a new instance of our DatabaseHelper, which we've created
        // earlier
        DatabaseHelper = new DatabaseHelper(this);

        // This returns a list of all our current available notes
        listItems = DatabaseHelper.getAllNotes_Subject();
        listItems_1 = DatabaseHelper.getAllNotes();

        data_class = new ArrayList<String>();
        data_subject = new ArrayList<String>();
        newData_1 = new ArrayList<String>();

        // Assigning the title to our global property so we can access it
        // later after certain actions (deleting/adding)
        for (Subject_data note : listItems) {
            data_class.add(note.Class);
            data_subject.add(note.Subject);
        }
        for (Class_data note : listItems_1) {
            newData_1.add(note.Class);
        }

        // We're initialising our custom adapter with all our data from the
        // database
        adapter = new MySimpleArrayAdapter(this, data_class, data_subject);

        // Assigning the adapter to ListView
        listView.setAdapter(adapter);

        // Assigning an event to the listview
        // This event will be used to delete records
        listView.setOnItemClickListener(myClickListener);

        fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Toast.makeText(LectureTime.this, "Open PopUp",Toast.LENGTH_LONG).show();

                if (!newData_1.isEmpty()) {
                    View v = LayoutInflater.from(Subjects.this).inflate(R.layout.add_subject, null);
                    rotateAnimation(fab);

                    edit_subject = (EditText) v.findViewById(R.id.edit_subject);

                    // Creating a dynamical edittext for our alert dialog
                    edit_subject.setId((int)9999);
                    edit_subject.setHint("Enter Subject Name");

                    class_spinner = (Spinner) v.findViewById(R.id.select_subject);

                    final ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<String>(Subjects.this, android.R.layout.simple_spinner_dropdown_item, newData_1);

                    class_spinner.setAdapter(adapter);

        /*            class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            // Get select item
                            int sid = class_spinner.getSelectedItemPosition();
                            Toast.makeText(getBaseContext(), "You have selected Class: " + newData_1.get(sid),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });
        */
                AlertDialog.Builder builder = new AlertDialog.Builder(Subjects.this)
                        .setMessage("Add Subjects")
                        .setView(v)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String subject_name = edit_subject.getText().toString();
                                String  class_name = class_spinner.getSelectedItem().toString();
                                if (subject_name != null && !subject_name.isEmpty() && !subject_name.trim().isEmpty()) {
                                    // Adding the new todo text to our database

                                    long Id = DatabaseHelper.addRecord_Subject(class_name,subject_name.toUpperCase(),
                                            getApplicationContext());

                                    // Create a new MyNotes object to add it to our
                                    // global property listItems
                                    Subject_data note = new Subject_data();
                                    note.Id = (int) Id;
                                    note.Class = class_name;
                                    note.Subject = subject_name.toUpperCase();

                                    listItems.add(note);

                                    data_class.add(note.Class);
                                    data_subject.add(note.Subject);

                                    adapter.notifyDataSetChanged();

                                    Toast.makeText(Subjects.this, "Saved", Toast.LENGTH_SHORT).show();

                                    clearAnimation(fab);
                                } else {
                                    Toast.makeText(Subjects.this, "Invalid Selection", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Subjects.this, "Please Add Class Detail First", Toast.LENGTH_SHORT).show();
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
        final Animation an = new RotateAnimation(0, 225, v.getWidth()/2, v.getHeight()/2);
        an.setDuration(700);
        an.setFillAfter(true);
        v.startAnimation(an);
    }

    public static void clearAnimation(View v){
        // Create an animation instance
        final Animation an = new RotateAnimation(225, 0, v.getWidth()/2, v.getHeight()/2);
        an.setDuration(500);
        an.setFillAfter(true);
        v.startAnimation(an);
    }

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
                                    // Retrieving the note from our listItems
                                    // property, which contains all notes from
                                    // our database
                                    Subject_data note = listItems.get(deleteItem);

                                    // Deleting it from the ArrayList<string>
                                    // property which is linked to our adapter
                                    data_class.remove(deleteItem);

                                    // Deleting the note from our database
                                    DatabaseHelper.deleteNote_Subject(note.Id);

                                    // Tell the adapter to update the list view
                                    // with the latest changes
                                    adapter.notifyDataSetChanged();
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
     */
    public class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values_1, values_2;

        public MySimpleArrayAdapter(Context context, ArrayList<String> values_1, ArrayList<String> values_2) {
            super(context, R.layout.rowlayout, values_1);

            this.context = context;
            this.values_1 = values_1;
            this.values_2 = values_2;
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

            // Setting the text to display
            textView.setText((position+1)+". "+values_1.get(position)+" : "+values_2.get(position));

            return rowView;
        }
    }
}
