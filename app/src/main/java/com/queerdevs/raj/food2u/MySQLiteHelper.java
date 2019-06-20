package com.queerdevs.raj.food2u;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by RAJ on 3/1/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {


    public void MySQLiteHelper() {

    }

    public MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // TODO Auto-generated constructor stub
//        File file = new File("/data/data/com.queerdevs.raj.food2u/databases/" + DB_NAME);
    }

    public static final String DB_NAME = "food2u.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "orders";
    public static final String C_ID = BaseColumns._ID;
    public static final String C_NAME = "t_name";
    public static final String C_PRICE = "t_price";
    public static final String C_QTY = "t_qty";
    public static final String C_TOTALPRICE = "t_totalprice";


    Context context;

    //name is the database file name
    //database version number they are constant


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE " + TABLE + " (" +
                C_ID + " TEXT PRIMARY KEY, " +
                C_NAME + " TEXT NOT NULL, " +
                C_PRICE + " INTEGER NOT NULL, " +
                C_QTY + " INTEGER NOT NULL, " +
                C_TOTALPRICE + " INTEGER NOT NULL);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + TABLE);
        this.onCreate(db);


    }


    void addOrder(Order order, boolean check) {
        SQLiteDatabase db = this.getWritableDatabase();

        String menuName, noOfp, inNo;

        boolean checkTable = false;
        Cursor cursor = null;
        int no = 0;


        Log.d("MENUORDER", menuName = order.getName());
        Log.d("NUMBER", noOfp = order.getQuantity());
        Log.d("PRICE", inNo = order.getPrice());
//        Log.d("TOTALPRICE",order.getTotalprice());

        try {
            cursor = db.rawQuery("select * from " + TABLE + " where " + C_NAME + "='" + menuName + "';", null);

            if (cursor.getColumnCount() > 0) {
                cursor.moveToFirst();
                no = cursor.getInt(cursor.getColumnIndex(C_QTY));
                checkTable = true;
            } else {
                checkTable = false;
            }

        } catch (Exception e) {
            Log.d("ERRORIS", e.getMessage());

        } finally {
            if (cursor != null)
                cursor.close();
        }

        Log.d("BOOL", String.valueOf(checkTable));

        if (checkTable && !check) {

            int totalNo = Integer.parseInt(noOfp) + no;
            double totalprice = totalNo * Double.parseDouble(inNo);

            String sql = "update " + TABLE + " set " + C_QTY + "='" + totalNo + "'," + C_TOTALPRICE + "='" + totalprice + "';";
            Log.d("UPDATE", "updating...");
            db.execSQL(sql);

        } else if (checkTable && check) {

            int totalNo = Integer.parseInt(noOfp) - 1;
            double totalprice = totalNo * Double.parseDouble(inNo);

            String sql = "update " + TABLE + " set " + C_QTY + "='" + totalNo + "'," + C_TOTALPRICE + "='" + totalprice + "';";
            Log.d("UPDATE", "updating...");
            db.execSQL(sql);

        } else {

            ContentValues values = new ContentValues();
            values.put(C_NAME, order.getName());
            Log.d(C_NAME, order.getName());
            values.put(C_PRICE, order.getPrice());
            values.put(C_QTY, order.getQuantity());
            values.put(C_TOTALPRICE, order.getTotalprice());
            // values.put(C_TOTALPRICE,order.getTotalprice());
//        Log.d("C_TOTALPRICE",order.getTotalprice());

            // Inserting Row
            db.insert(TABLE, null, values);  // yaha bhaiya se puchna hai order of insertion
            db.close(); // Closing database connection
        }
    }

    public void deleteOrder(String order) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from " + TABLE + " where " + C_NAME + "='" + order + "';";
        Log.d("sql", sql);
        db.execSQL(sql);
        db.close();
    }

    public Cursor getData() {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String sql = "select * from " + TABLE;
        Cursor c = sqldb.rawQuery(sql, null);
        return c;
    }

    public boolean checkTable() {
        Cursor cursor = null;
        SQLiteDatabase sqldb = this.getReadableDatabase();
        try {
            cursor = sqldb.rawQuery("select * from " + TABLE + ";", null);
            if (cursor.getColumnCount() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Log.d("ERRORIS", e.getMessage());
            return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


    void updateTable() {
        SQLiteDatabase sqldb = this.getWritableDatabase();

        String sql = "update " + TABLE + " set " + C_TOTALPRICE + "='" + C_QTY + "*" + C_PRICE + "';";
        Log.d("UPDATE", "updating...");
        sqldb.execSQL(sql);
        sqldb.close();
    }
}
