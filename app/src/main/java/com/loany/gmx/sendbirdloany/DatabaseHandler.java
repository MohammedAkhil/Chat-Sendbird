package com.loany.gmx.sendbirdloany;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows on 1/17/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "User Message";
    private static final String TABLE_MESSAGES = "Messages";


    private static final String KEY_ID = "id";
    private static final String KEY_MESSAGE = "message";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_ID + " TEXT," + KEY_MESSAGE + " TEXT" + ")";
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        // Create tables again
        onCreate(db);
    }

    void addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, message.getID());
        values.put(KEY_MESSAGE, message.getMessage());

        db.insert(TABLE_MESSAGES, null, values);
        db.close();
    }

    public List<Message> getAllMessages() {
        List<Message> messagelist = new ArrayList<Message>();
        String selectquery = "SELECT * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setID(cursor.getString(0));
                message.set_Message(cursor.getString(1));
                messagelist.add(message);
            } while (cursor.moveToNext());
        }

        return messagelist;
    }


}