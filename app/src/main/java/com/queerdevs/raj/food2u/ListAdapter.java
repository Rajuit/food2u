package com.queerdevs.raj.food2u;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by RAJ on 3/2/2017.
 */


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    static Context context;
    String tp;
    String tprice;
    int x, y, z, a, b, c;
    String ip, iq, name;
    MySQLiteHelper mySQLiteHelper;


    private List<Order> orderList;


    public ListAdapter() {
        //dbHelper = new MySQLiteHelper(context);

    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView name;
        public TextView price;
        public TextView qty;
        public TextView pricetotal;
        public Button delet;
        public Button plus;
        public Button minus;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            pricetotal = (TextView) view.findViewById(R.id.pricetotal);
            qty = (TextView) view.findViewById(R.id.qtybox);
            plus = (Button) view.findViewById(R.id.plus);
            minus = (Button) view.findViewById(R.id.minus);
            delet = (Button) view.findViewById(R.id.delete);
            //dbHelper = new MySQLiteHelper(context);


        }


    }


    public ListAdapter(List<Order> orderList, Context laContext) {
        this.orderList = orderList;
        this.context = laContext;
        mySQLiteHelper = new MySQLiteHelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.finallist, parent, false);
        return new ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Order order = orderList.get(position);
        holder.name.setText(name = order.getName());
        holder.price.setText(ip = order.getPrice());
        holder.qty.setText(iq = order.getQuantity());
        holder.pricetotal.setText(String.valueOf(Integer.parseInt(ip) * Integer.parseInt(iq)));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = orderList.get(position);// getting the item at particular position

                a = (Integer.parseInt(order.getQuantity()) + 1);// incrementing item quantity by 1
                b = Integer.parseInt(order.getPrice());
                c = a * b;
                BaseApplication.setNumber(1);
                MenuActivity.number.setText(String.valueOf(BaseApplication.getNumber()));
                order.setQuantity(String.valueOf(a));


                mySQLiteHelper.addOrder(new Order(name, ip, String.valueOf(1)), false);

                tprice = " ₹ " + String.valueOf(c);
                holder.pricetotal.setText(tprice);

                orderList.set(position, order);// updating the itemList to that new item with incremented quantity
                notifyDataSetChanged();//notifying the adapter
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = orderList.get(position);// getting the item at particular position


                MenuActivity.number.setText(String.valueOf(BaseApplication.getNumber()));

                x = Integer.parseInt(order.getQuantity());

                if (x > 1) {
                    x = x - 1;
                    BaseApplication.delNumber(1);

                    Order nOrder = new Order(name, ip, String.valueOf(x));

                    y = Integer.parseInt(order.getPrice());
                    z = x * y;

                    nOrder.setQuantity(String.valueOf(x));

                    mySQLiteHelper.addOrder(nOrder, true);
//                    order.setQuantity(String.valueOf(x));// decrementing item quantity by 1
                    tp = " ₹ " + String.valueOf(z);
                    holder.pricetotal.setText(tp);
                    orderList.set(position, nOrder);// updating the orderList to that new item with incremented quantity
                    notifyDataSetChanged();//notifying the adapter


                }


            }
        });
        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = orderList.get(position);
                orderList.remove(position);
                notifyDataSetChanged();
                BaseApplication.setZero(0);
                MenuActivity.number.setText(String.valueOf(BaseApplication.getNumber()));
                mySQLiteHelper.deleteOrder(name);
                //dbHelper=new MySQLiteHelper();
//                dbHelper.deleteOrder(order.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


}
