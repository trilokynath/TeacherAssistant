package com.trinfosoft.teacherassistant;

import android.app.ActionBar;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Classes extends AppCompatActivity {

    ImageButton fab;
    EditText edit_class;
    // Out custom adapter
    MySimpleArrayAdapter adapter;

    // contains our listview items
    ArrayList<Class_data> listItems;

    // database
    DatabaseHelper DatabaseHelper;

    // list of todo titles
    ArrayList<String> newData;

    // contains the id of the item we are about to delete
    public int deleteItem;

    // EditText field for adding new items to the list
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        this.setTitle("Class Detail");


        // We're getting our listView by the id
        ListView listView = (ListView) findViewById(R.id.list);

        // Creating a new instance of our DatabaseHelper, which we've created
        // earlier
        DatabaseHelper = new DatabaseHelper(this);

        // This returns a list of all our current available notes
        listItems = DatabaseHelper.getAllNotes();

        newData = new ArrayList<String>();

        // Assigning the title to our global property so we can access it
        // later after certain actions (deleting/adding)
        for (Class_data note : listItems) {
            newData.add(note.Class);
        }

        // We're initialising our custom adapter with all our data from the
        // database
        adapter = new MySimpleArrayAdapter(this, newData);

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

                View v = LayoutInflater.from(Classes.this).inflate(R.layout.add_class, null);
                edit_class = (EditText) v.findViewById(R.id.edit_class);

                // Creating a dynamical edittext for our alert dialog
                edit_class.setId((int)9999);
                edit_class.setHint("Enter Class Name");

                AlertDialog.Builder builder = new AlertDialog.Builder(Classes.this)
                        .setMessage("Add Class")
                        .setView(v)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String class_name = edit_class.getText().toString();
                                if (class_name != null && !class_name.isEmpty() && !class_name.trim().isEmpty()) {
                                    // Adding the new todo text to our database

                                    long Id = DatabaseHelper.addRecord(edit_class
                                            .getText().toString().toUpperCase());

                                    // Create a new MyNotes object to add it to our
                                    // global property listItems
                                    Class_data note = new Class_data();
                                    note.Id = (int) Id;
                                    note.Class = edit_class.getText().toString().toUpperCase();

                                        listItems.add(note);

                                        newData.add(note.Class);

                                        adapter.notifyDataSetChanged();

                                    Toast.makeText(Classes.this, "Saved", Toast.LENGTH_SHORT).show();

                                    rotateAnimation(fab);
                                } else {
                                    Toast.makeText(Classes.this, "Invalid Selection", Toast.LENGTH_SHORT).show();

                                    rotateAnimation(fab);
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
                                    Class_data note = listItems.get(deleteItem);

                                    // Deleting it from the ArrayList<string>
                                    // property which is linked to our adapter
                                    newData.remove(deleteItem);

                                    // Deleting the note from our database
                                    DatabaseHelper.deleteNote(note.Id);

                                    // Tell the adapter to update the list view
                                    // with the latest changes
                                    adapter.notifyDataSetChanged();

                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // When you press cancel, just close the dialog
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
        private final ArrayList<String> values;

        public MySimpleArrayAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.rowlayout, values);

            this.context = context;
            this.values = values;
        }

        /**
         * Here we go and get our rowlayout.xml file and set the textview text.
         * This happens for every row in your listview.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

            TextView textView = (TextView) rowView.findViewById(R.id.label);

            // Setting the text to display
            textView.setText((position+1)+". "+values.get(position));

            return rowView;
        }
    }

}
