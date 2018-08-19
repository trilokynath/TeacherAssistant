package com.trinfosoft.teacherassistant;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TEACHER_ASSISTANT";
    private static final String DATABASE_TABLE_CLASS = "Class";
    public static final String COLID = "ClassID";
    public static final String COLCLASS = "Class";
    private static final String DATABASE_TABLE_SUBJECT = "Subjects_data";
    public static final String COL_ID = "MySubjectID";
    public static final String COLSUBJECTS = "Subjects";
    public static final String COLSTARTH = "Start_h";
    public static final String COLSTARTM = "Start_m";
    private static final String DATABASE_TABLE_MONDAY= "Monday";
    public static final String COL_MONDAY_ID = "Monday_ID";
    private static final String DATABASE_TABLE_TUESDAY= "Tuesday";
    public static final String COL_TUESDAY_ID = "Tuesday_ID";
    private static final String DATABASE_TABLE_WEDNESDAY= "Wednesday";
    public static final String COL_WEDNESDAY_ID = "Wednesday_ID";
    private static final String DATABASE_TABLE_THURSDAY= "Thursday";
    public static final String COL_THURSDAY_ID = "Thursday_ID";
    private static final String DATABASE_TABLE_FRIDAY= "Friday";
    public static final String COL_FRIDAY_ID = "Friday_ID";
    private static final String DATABASE_TABLE_SATURDAY= "Saturday";
    public static final String COL_SATURDAY_ID = "Saturday_ID";

    Context context;
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This runs once after the installation and creates a database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Here we are creating two columns in our database.
        // MyNotesID, which is the primary key and Title which will hold the
        // todo text
        db.execSQL("CREATE TABLE " + DATABASE_TABLE_CLASS + " (" + COLID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLCLASS + " TEXT)");

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_SUBJECT + " (" + COL_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLCLASS + " TEXT, " + COLSUBJECTS + " TEXT)");

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_MONDAY + " (" + COL_MONDAY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLSTARTH + " TEXT, " +
                COLSTARTM + " TEXT, " + COLSUBJECTS + " TEXT)");

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_TUESDAY + " (" + COL_TUESDAY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLSTARTH + " TEXT, " +
                COLSTARTM + " TEXT, " + COLSUBJECTS + " TEXT)");

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_WEDNESDAY + " (" + COL_WEDNESDAY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLSTARTH + " TEXT, " +
                COLSTARTM + " TEXT, " + COLSUBJECTS + " TEXT)");

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_THURSDAY + " (" + COL_THURSDAY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLSTARTH + " TEXT, " +
                COLSTARTM + " TEXT, " + COLSUBJECTS + " TEXT)");

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_FRIDAY + " (" + COL_FRIDAY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLSTARTH + " TEXT, " +
                COLSTARTM + " TEXT, " + COLSUBJECTS + " TEXT)");

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_SATURDAY + " (" + COL_SATURDAY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLSTARTH + " TEXT, " +
                COLSTARTM + " TEXT, " + COLSUBJECTS + " TEXT)");

    }

    /**
     * This would run after the user updates the app. This is in case you want
     * to modify the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    /**
     * This method adds a record to the database. All we pass in is the todo
     * text
     */
    public long addRecord(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLCLASS, title);

        return db.insert(DATABASE_TABLE_CLASS, null, cv);
    }

    /**
     * //This method returns all notes from the database
     */
    public ArrayList<Class_data> getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Class_data> listItems = new ArrayList<Class_data>();

        Cursor cursor = db.rawQuery("SELECT * from " + DATABASE_TABLE_CLASS,
                new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Class_data note = new Class_data();

                note.Id = cursor.getInt(cursor.getColumnIndex(COLID));

                note.Class = cursor.getString(cursor.getColumnIndex(COLCLASS));

                listItems.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }
    /*
     * //This method deletes a record from the database.
     */
    public void deleteNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + DATABASE_TABLE_CLASS + " WHERE " + COLID
                + "=" + id + "");
    }


    /**
     * This method adds a record to the database. All we pass in is the todo
     * text
     */
    public long addRecord_Subject(String Class, String Subject,Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLCLASS, Class);
        cv.put(COLSUBJECTS, Subject);

        Long id  = db.insert(DATABASE_TABLE_SUBJECT, null, cv);

        if(id == -1)
            Toast.makeText(context, "Error While Inserting Data", Toast.LENGTH_LONG).show();

        return id;
    }

    /**
     * //This method returns all notes from the database
     */
    public ArrayList<Subject_data> getAllNotes_Subject() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Subject_data> listItems = new ArrayList<Subject_data>();

        Cursor cursor = db.rawQuery("SELECT * from " + DATABASE_TABLE_SUBJECT,
                new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Subject_data note = new Subject_data();

                note.Id = cursor.getInt(cursor.getColumnIndex(COL_ID));

                note.Class = cursor.getString(cursor.getColumnIndex(COLCLASS));

                note.Subject = cursor.getString(cursor.getColumnIndex(COLSUBJECTS));

                listItems.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }

    /**
     * //This method deletes a record from the database.
     */
    public void deleteNote_Subject(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + DATABASE_TABLE_SUBJECT + " WHERE " + COL_ID
                + "=" + id + "");
    }


    public DatabaseHelper contextset(Context context1){
        context = context1;
        return this;
    }
    /**
     * This method adds a record to the database. All we pass in is the todo
     * text
     */
    public long addRecord_Monday(String start_hour, String start_min, String subject, Context context1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        context = context1;

        cv.put(COLSTARTH, start_hour);
        cv.put(COLSTARTM, start_min);
        cv.put(COLSUBJECTS, subject);

        Long id  = db.insert(DATABASE_TABLE_MONDAY, null, cv);

        if(id == -1)
            Toast.makeText(context, "Error While Inserting Data", Toast.LENGTH_LONG).show();

        return id;
    }

    /**
     * //This method returns all notes from the database
     */
    public ArrayList<Time_Data> getAllNotes_Monday() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Time_Data> listItems = new ArrayList<Time_Data>();

        Cursor cursor = db.rawQuery("SELECT * from " + DATABASE_TABLE_MONDAY,
                new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Time_Data note = new Time_Data();

                note.Id = cursor.getInt(cursor.getColumnIndex(COL_MONDAY_ID));

                note.start_h = cursor.getString(cursor.getColumnIndex(COLSTARTH));
                note.start_m = cursor.getString(cursor.getColumnIndex(COLSTARTM));
                note.subject = cursor.getString(cursor.getColumnIndex(COLSUBJECTS));

                listItems.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }

    /**
     * //This method deletes a record from the database.
     */
    public void deleteNote_Monday(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + DATABASE_TABLE_MONDAY + " WHERE " + COL_MONDAY_ID
                + "=" + id + "");

        int status = 0;

        SharedPreferences prefs = context.getSharedPreferences("notification", MODE_PRIVATE);

        if(prefs!=null)
            status = prefs.getInt("noti", 0);

        if(status%10000==id){
            //save the status to the database that the notification has been send to the user
            SharedPreferences.Editor editor = context.getSharedPreferences("notification", MODE_PRIVATE).edit();
            editor.putInt("noti", 0);
            editor.apply();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(1);
        }

    }

/** ****************TUESDAY*********************
 *
 * This method adds a record to the database. All we pass in is the todo
 * text
 */
public long addRecord_Tuesday(String start_hour, String start_min, String subject, Context context1) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    context = context1;

    cv.put(COLSTARTH, start_hour);
    cv.put(COLSTARTM, start_min);
    cv.put(COLSUBJECTS, subject);

    Long id  = db.insert(DATABASE_TABLE_TUESDAY, null, cv);

    if(id == -1)
        Toast.makeText(context, "Error While Inserting Data", Toast.LENGTH_LONG).show();

    return id;
}

    /**
     * //This method returns all notes from the database
     */
    public ArrayList<Time_Data> getAllNotes_Tuesday() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Time_Data> listItems = new ArrayList<Time_Data>();

        Cursor cursor = db.rawQuery("SELECT * from " + DATABASE_TABLE_TUESDAY,
                new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Time_Data note = new Time_Data();

                note.Id = cursor.getInt(cursor.getColumnIndex(COL_TUESDAY_ID));

                note.start_h = cursor.getString(cursor.getColumnIndex(COLSTARTH));
                note.start_m = cursor.getString(cursor.getColumnIndex(COLSTARTM));
                note.subject = cursor.getString(cursor.getColumnIndex(COLSUBJECTS));

                listItems.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }

    /**
     * //This method deletes a record from the database.
     */
    public void deleteNote_Tuesday(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + DATABASE_TABLE_TUESDAY + " WHERE " + COL_TUESDAY_ID
                + "=" + id + "");

        int status = 0;

        SharedPreferences prefs = context.getSharedPreferences("notification", MODE_PRIVATE);

        if(prefs!=null)
            status = prefs.getInt("noti", 0);

        if(status%10000==id){
            //save the status to the database that the notification has been send to the user
            SharedPreferences.Editor editor = context.getSharedPreferences("notification", MODE_PRIVATE).edit();
            editor.putInt("noti", 0);
            editor.apply();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(1);
        }

    }

/** ****************WEDNESDAY*********************
 *
 * This method adds a record to the database. All we pass in is the todo
 * text
 */
public long addRecord_Wednesday(String start_hour, String start_min, String subject, Context context1) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    context = context1;

    cv.put(COLSTARTH, start_hour);
    cv.put(COLSTARTM, start_min);
    cv.put(COLSUBJECTS, subject);

    Long id  = db.insert(DATABASE_TABLE_WEDNESDAY, null, cv);

    if(id == -1)
        Toast.makeText(context, "Error While Inserting Data", Toast.LENGTH_LONG).show();

    return id;
}

    /**
     * //This method returns all notes from the database
     */
    public ArrayList<Time_Data> getAllNotes_Wednesday() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Time_Data> listItems = new ArrayList<Time_Data>();

        Cursor cursor = db.rawQuery("SELECT * from " + DATABASE_TABLE_WEDNESDAY,
                new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Time_Data note = new Time_Data();

                note.Id = cursor.getInt(cursor.getColumnIndex(COL_WEDNESDAY_ID));

                note.start_h = cursor.getString(cursor.getColumnIndex(COLSTARTH));
                note.start_m = cursor.getString(cursor.getColumnIndex(COLSTARTM));
                note.subject = cursor.getString(cursor.getColumnIndex(COLSUBJECTS));

                listItems.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }

    /**
     * //This method deletes a record from the database.
     */
    public void deleteNote_Wednesday(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + DATABASE_TABLE_WEDNESDAY + " WHERE " + COL_WEDNESDAY_ID
                + "=" + id + "");

        int status = 0;

        SharedPreferences prefs = context.getSharedPreferences("notification", MODE_PRIVATE);

        if(prefs!=null)
            status = prefs.getInt("noti", 0);

        if(status%10000==id){
            //save the status to the database that the notification has been send to the user
            SharedPreferences.Editor editor = context.getSharedPreferences("notification", MODE_PRIVATE).edit();
            editor.putInt("noti", 0);
            editor.apply();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(1);
        }

    }

/** ****************THURSDAY*********************
 *
 * This method adds a record to the database. All we pass in is the todo
 * text
 */
public long addRecord_Thursday(String start_hour, String start_min, String subject, Context context1) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    context = context1;

    cv.put(COLSTARTH, start_hour);
    cv.put(COLSTARTM, start_min);
    cv.put(COLSUBJECTS, subject);

    Long id  = db.insert(DATABASE_TABLE_THURSDAY, null, cv);

    if(id == -1)
        Toast.makeText(context, "Error While Inserting Data", Toast.LENGTH_LONG).show();

    return id;
}

    /**
     * //This method returns all notes from the database
     */
    public ArrayList<Time_Data> getAllNotes_Thursday() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Time_Data> listItems = new ArrayList<Time_Data>();

        Cursor cursor = db.rawQuery("SELECT * from " + DATABASE_TABLE_THURSDAY,
                new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Time_Data note = new Time_Data();

                note.Id = cursor.getInt(cursor.getColumnIndex(COL_THURSDAY_ID));

                note.start_h = cursor.getString(cursor.getColumnIndex(COLSTARTH));
                note.start_m = cursor.getString(cursor.getColumnIndex(COLSTARTM));
                note.subject = cursor.getString(cursor.getColumnIndex(COLSUBJECTS));

                listItems.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }

    /**
     * //This method deletes a record from the database.
     */
    public void deleteNote_Thursday(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + DATABASE_TABLE_THURSDAY + " WHERE " + COL_THURSDAY_ID
                + "=" + id + "");

        int status = 0;

        SharedPreferences prefs = context.getSharedPreferences("notification", MODE_PRIVATE);

        if(prefs!=null)
            status = prefs.getInt("noti", 0);

        if(status%10000==id){
            //save the status to the database that the notification has been send to the user
            SharedPreferences.Editor editor = context.getSharedPreferences("notification", MODE_PRIVATE).edit();
            editor.putInt("noti", 0);
            editor.apply();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(1);
        }

    }

/** ****************FRIDAY*********************
 *
 * This method adds a record to the database. All we pass in is the todo
 * text
 */
public long addRecord_Friday(String start_hour, String start_min, String subject, Context context1) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    context = context1;

    cv.put(COLSTARTH, start_hour);
    cv.put(COLSTARTM, start_min);
    cv.put(COLSUBJECTS, subject);

    Long id  = db.insert(DATABASE_TABLE_FRIDAY, null, cv);

    if(id == -1)
        Toast.makeText(context, "Error While Inserting Data", Toast.LENGTH_LONG).show();

    return id;
}

    /**
     * //This method returns all notes from the database
     */
    public ArrayList<Time_Data> getAllNotes_Friday() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Time_Data> listItems = new ArrayList<Time_Data>();

        Cursor cursor = db.rawQuery("SELECT * from " + DATABASE_TABLE_FRIDAY,
                new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Time_Data note = new Time_Data();

                note.Id = cursor.getInt(cursor.getColumnIndex(COL_FRIDAY_ID));

                note.start_h = cursor.getString(cursor.getColumnIndex(COLSTARTH));
                note.start_m = cursor.getString(cursor.getColumnIndex(COLSTARTM));
                note.subject = cursor.getString(cursor.getColumnIndex(COLSUBJECTS));

                listItems.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }

    /**
     * //This method deletes a record from the database.
     */
    public void deleteNote_Friday(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + DATABASE_TABLE_FRIDAY + " WHERE " + COL_FRIDAY_ID
                + "=" + id + "");

        int status = 0;

        SharedPreferences prefs = context.getSharedPreferences("notification", MODE_PRIVATE);

        if(prefs!=null)
            status = prefs.getInt("noti", 0);

        if(status%10000==id){
            //save the status to the database that the notification has been send to the user
            SharedPreferences.Editor editor = context.getSharedPreferences("notification", MODE_PRIVATE).edit();
            editor.putInt("noti", 0);
            editor.apply();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(1);
        }

    }

/** ****************SATURDAY*********************
 *
 * This method adds a record to the database. All we pass in is the todo
 * text
 */
public long addRecord_Saturday(String start_hour, String start_min, String subject, Context context1) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    context = context1;

    cv.put(COLSTARTH, start_hour);
    cv.put(COLSTARTM, start_min);
    cv.put(COLSUBJECTS, subject);

    Long id  = db.insert(DATABASE_TABLE_SATURDAY, null, cv);

    if(id == -1)
        Toast.makeText(context, "Error While Inserting Data", Toast.LENGTH_LONG).show();

    return id;
}

    /**
     * //This method returns all notes from the database
     */
    public ArrayList<Time_Data> getAllNotes_Saturday() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Time_Data> listItems = new ArrayList<Time_Data>();

        Cursor cursor = db.rawQuery("SELECT * from " + DATABASE_TABLE_SATURDAY,
                new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Time_Data note = new Time_Data();

                note.Id = cursor.getInt(cursor.getColumnIndex(COL_SATURDAY_ID));

                note.start_h = cursor.getString(cursor.getColumnIndex(COLSTARTH));
                note.start_m = cursor.getString(cursor.getColumnIndex(COLSTARTM));
                note.subject = cursor.getString(cursor.getColumnIndex(COLSUBJECTS));

                listItems.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }

    /**
     * //This method deletes a record from the database.
     */
    public void deleteNote_Saturday(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + DATABASE_TABLE_SATURDAY + " WHERE " + COL_SATURDAY_ID
                + "=" + id + "");

        int status = 0;

        SharedPreferences prefs = context.getSharedPreferences("notification", MODE_PRIVATE);

        if(prefs!=null)
            status = prefs.getInt("noti", 0);

        if(status%10000==id){
            //save the status to the database that the notification has been send to the user
            SharedPreferences.Editor editor = context.getSharedPreferences("notification", MODE_PRIVATE).edit();
            editor.putInt("noti", 0);
            editor.apply();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(1);
        }

    }
}