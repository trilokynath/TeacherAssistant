package com.trinfosoft.teacherassistant;

import android.content.*;
import android.net.Uri;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;

import java.util.*;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    MySimpleArrayAdapter adapter;

    // contains our listview items
    ArrayList<Time_Data> time_data;

    // database
    DatabaseHelper DatabaseHelper;

    // list of todo titles
    ArrayList<Integer> ID;
    ArrayList<String> start_hour;
    ArrayList<String> stop_min;
    ArrayList<String> data_subject;

    ImageView sync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * If the application will start for first time then to start services we have to set
         * share preferences then we can be work for that
         */
        int status = 0;
        SharedPreferences prefs = getSharedPreferences("status", MODE_PRIVATE);

        if(prefs!=null)
            status = prefs.getInt("st", 2);

        if (status ==2) {
            SharedPreferences.Editor editor =
                    getSharedPreferences("status", MODE_PRIVATE).edit();
            editor.putInt("st", 1);
            editor.apply();
        }

        Intent intent = new Intent(this, Notification_Service.class);
        startService(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(false);


        // We're getting our listView by the id
        final ListView listView = (ListView) findViewById(R.id.list);
        TextView day_name = (TextView) findViewById(R.id.day);
        TextView date_name = (TextView) findViewById(R.id.date);

        // Creating a new instance of our DatabaseHelper, which we've created
        // earlier
        DatabaseHelper = new DatabaseHelper(this);

        // This returns a list of all our current available notes
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(Calendar.DAY_OF_WEEK);
        int date = rightNow.get(Calendar.DATE);
        String month = rightNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        date_name.setText(date+" "+month);

        switch (day) {
            case Calendar.MONDAY:
                time_data = DatabaseHelper.getAllNotes_Monday();
                day_name.setText("MONDAY");
                break;

            case Calendar.TUESDAY:
                time_data = DatabaseHelper.getAllNotes_Tuesday();
                day_name.setText("TUESDAY");
                break;

            case Calendar.WEDNESDAY:
                time_data = DatabaseHelper.getAllNotes_Wednesday();
                day_name.setText("WEDNESDAY");
                break;

            case Calendar.THURSDAY:
                time_data = DatabaseHelper.getAllNotes_Thursday();
                day_name.setText("THURSDAY");
                break;

            case Calendar.FRIDAY:
                time_data = DatabaseHelper.getAllNotes_Friday();
                day_name.setText("FRIDAY");
                break;

            case Calendar.SATURDAY:
                time_data = DatabaseHelper.getAllNotes_Saturday();
                day_name.setText("SATURDAY");
                break;

            case Calendar.SUNDAY:
                time_data.add(0,null);
                day_name.setText("SUNDAY");
                break;

        }

        ID = new ArrayList<Integer>();
        start_hour = new ArrayList<String>();
        stop_min = new ArrayList<String>();
        data_subject = new ArrayList<String>();


        // Assigning the title to our global property so we can access it
        // later after certain actions (deleting/adding)
        for (Time_Data note : time_data) {
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
                String sub = data_subject.get(i);
                String nsub = data_subject.get(i + 1);

                if (h > nh) {
                    start_hour.set(i, nh + "");
                    stop_min.set(i, nm + "");
                    data_subject.set(i, nsub);

                    start_hour.set(i + 1, h + "");
                    stop_min.set(i + 1, m + "");
                    data_subject.set(i + 1, sub);
                }
                if (h == nh) {
                    if (m > nm) {
                        start_hour.set(i, nh + "");
                        stop_min.set(i, nm + "");
                        data_subject.set(i, nsub);

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
            time.start_h = start_hour.get(i);
            time.start_m = stop_min.get(i);
            time.subject = data_subject.get(i);
            time_data.add(time);
        }


        // We're initialising our custom adapter with all our data from the
        // database
        adapter = new MySimpleArrayAdapter(this, start_hour, stop_min, data_subject);

        // Assigning the adapter to ListView
        listView.setAdapter(adapter);

        sync = (ImageView) findViewById(R.id.sync);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        });
    }


    /**
     * This adapter will create your list view row by row
     */
    public class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values_1, values_2, values_3;

        public MySimpleArrayAdapter(Context context, ArrayList<String> values_1, ArrayList<String> values_2, ArrayList<String> values_3) {
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
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int cnt = 0;
            View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

            TextView textView = (TextView) rowView.findViewById(R.id.label);

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


            String subject = values_3.get(position);
            String sub;
            if(subject.length()>10) {
                sub = subject.substring(0, 10)+"..";
            }else{
                sub = subject;
            }

            // Setting the text to display
            textView.setText((position+1)+". "+String.format("%02d", currentHour)+
                    " : "+values_2.get(position)+" "+aMpM+"  -> "+ sub);

            return rowView;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent2 = new Intent(this, Setting.class);
            startActivity(intent2);
            return true;
        }

        if (id == R.id.action_about) {

            Intent intent2 = new Intent(this, About.class);
            startActivity(intent2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.time_table) {
            // Handle the camera action
            Intent intent = new Intent(this, TimeTable.class);
        //    intent.putExtra(EXTRA_MESSAGE, "");
            startActivity(intent);

        } else if (id == R.id.setting) {
            Intent intent2 = new Intent(this, Setting.class);
            startActivity(intent2);

        } else if (id == R.id.about_us) {
            Intent intent2 = new Intent(this, About.class);
            startActivity(intent2);

        } else if (id == R.id.share) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.teacherassistant.trinfosoft.com"));
            startActivity(browserIntent);

        } else if (id == R.id.feedback) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.teacherassistant.trinfosoft.com"));
            startActivity(browserIntent);
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(false);

        super.onResume();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                for (int i = 0; i < navigationView.getMenu().size(); i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
            }
        }, 500);
    }

}
