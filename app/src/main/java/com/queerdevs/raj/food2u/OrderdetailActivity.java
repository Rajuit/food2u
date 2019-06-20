package com.queerdevs.raj.food2u;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class OrderdetailActivity extends AppCompatActivity {

    SQLiteDatabase db;
    MySQLiteHelper dbHelper;
    List<Order> orderList = new ArrayList<>();
    RecyclerView myRv;
    ListAdapter listAdapter;
    private String table = "orders";
    Order order1 = new Order();
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        dbHelper = new MySQLiteHelper(this);
        context = this;
        listAdapter = new ListAdapter(orderList, context);

        myRv = (RecyclerView) findViewById(R.id.finallist);
        myRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        myRv.setLayoutManager(layoutManager);
        myRv.setItemAnimator(new DefaultItemAnimator());
        myRv.setAdapter(listAdapter);

        boolean checkTable = dbHelper.checkTable();
        if (checkTable == true) {
            Log.d("table", "present");

            Cursor cursor = dbHelper.getData();
            if (cursor.getColumnCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex(dbHelper.C_NAME));
                        String price = cursor.getString(cursor.getColumnIndex(dbHelper.C_PRICE));
                        String quantity = cursor.getString(cursor.getColumnIndex(dbHelper.C_QTY));
                        String totalprice=cursor.getString(cursor.getColumnIndex(dbHelper.C_TOTALPRICE));
                        orderList.add(new Order(name, price, quantity,totalprice));
                        listAdapter.notifyDataSetChanged();
                    } while (cursor.moveToNext());
                }


            }
        }
    }

    private void AddDataOnOrderList(String name, String price, String quantity) {
        order1 = new Order(name, price, quantity);
        orderList.add(order1);

    }
}