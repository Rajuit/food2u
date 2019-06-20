package com.queerdevs.raj.food2u;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity {
    public static int count = 0;
    String menuURL;
    ListView lvMenu;
    ProgressBar progressBar;
    Context context;
    String price;
    String menuname;
    private ProgressDialog dialog;
    MySQLiteHelper mySQLiteHelper;
    List<FoodModel> foods = new ArrayList<>();
    ImageView cartImage;
    public static TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_menu);
        cartImage = (ImageView) findViewById(R.id.cartImage);
        number = (TextView) findViewById(R.id.number);

        Log.d("SID", "called");



        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        menuURL = getIntent().getStringExtra("menuURL");
        Log.d("MENUURL", menuURL);
        lvMenu = (ListView) findViewById(R.id.lvMenu);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mySQLiteHelper = new MySQLiteHelper(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        new MenuJson().execute(menuURL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MenuActivity.this, OrderdetailActivity.class);
                startActivity(in);
            }
        });
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MenuActivity.this, OrderdetailActivity.class);
                startActivity(intent1);
            }
        });


    }

    public class MenuJson extends AsyncTask<String, String, List<FoodModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<FoodModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                Log.d("JSON", finalJson);

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("restaurant");//Array name in json


                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself
                     * which is commented below
                     */
                    FoodModel foodModel = gson.fromJson(finalObject.toString(), FoodModel.class); // a single line json parsing using Gson

                    foods.add(foodModel);
                }
                return foods;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<FoodModel> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                final MenuActivity.MenuAdapter adapter = new MenuActivity.MenuAdapter(getApplicationContext(), R.layout.menurow, result);
                lvMenu.setAdapter(adapter);
                lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                        // get alertdialog.xml view

                        LayoutInflater li = LayoutInflater.from(context);
                        View alertdialogView = li.inflate(R.layout.alertdialog, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);

                        // set alertdialog.xml to alertdialog builder

                        alertDialogBuilder.setView(alertdialogView);

                        final Button plus = (Button) alertdialogView.findViewById(R.id.plus);
                        final Button minus = (Button) alertdialogView.findViewById(R.id.minus);
                        final TextView quantity = (TextView) alertdialogView.findViewById(R.id.quantity);
                        alertDialogBuilder.setTitle("Please Choose your food...");
                        plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count += 1;
                                quantity.setText(String.valueOf(count));

                            }
                        });
                        minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (count > 0) {
                                    count -= 1;
                                    quantity.setText(String.valueOf(count));

                                } else {
                                    quantity.setText("0");
                                }
                            }
                        });


                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //get user input and send kro.

                                        Log.d("Insert: ", "Inserting ..");

                                        FoodModel foodModel = foods.get(position);

                                        if (count > 0) {
                                            BaseApplication.setNumber(count);
                                            mySQLiteHelper.addOrder(new Order(foodModel.getItem(), foodModel.getPrice(), String.valueOf(count)),false);

                                        } else {
                                            Toast.makeText(MenuActivity.this, "Item Can not be zero", Toast.LENGTH_SHORT).show();
                                        }

                                        mySQLiteHelper.close();
                                        number.setText(String.valueOf(BaseApplication.getNumber()));
                                        count = 0;
                                        //       mySQLiteHelper.updateTable(); // for updating

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        count = 0;
                                        dialog.cancel();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                    }
                });
            }
        }
    }


    public class MenuAdapter extends ArrayAdapter<FoodModel> {

        private List<FoodModel> foods;
        private int resource;
        private LayoutInflater inflater;


        public MenuAdapter(Context context, int resource, List<FoodModel> objects) {
            super(context, resource, objects);
//            super(context, resource, objects);
            foods = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MenuActivity.MenuAdapter.ViewHolder holder = null;


            if (convertView == null) {
                holder = new MenuActivity.MenuAdapter.ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.ivMenuIcon = (ImageView) convertView.findViewById(R.id.ivMenuIcon);
                holder.tvMenu = (TextView) convertView.findViewById(R.id.tvMenu);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
                // holder.quantity=(TextView) convertView.findViewById(R.id.quantity);
                // holder.plus=(Button)convertView.findViewById(R.id.plus);
                // holder.minus=(Button)convertView.findViewById(R.id.minus);

                convertView.setTag(holder);
            } else {
                holder = (MenuActivity.MenuAdapter.ViewHolder) convertView.getTag();
            }

            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            //Picasso.with(context).load(menuURL).into(ivMenu);
            // Then later, when you want to display image

            final MenuActivity.MenuAdapter.ViewHolder finalHolder = holder;

//            Log.d()

            ImageLoader.getInstance().displayImage(foods.get(position).getImagemenu(), holder.ivMenuIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    finalHolder.ivMenuIcon.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.ivMenuIcon.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("Image", "Loading........");
                    finalHolder.ivMenuIcon.setVisibility(View.VISIBLE);
                    Log.d("Image", "Loaded");
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.ivMenuIcon.setVisibility(View.INVISIBLE);
                }
            });

            menuname = foods.get(position).getItem();
            price = "Price  :  ₹" + foods.get(position).getPrice();
            holder.tvMenu.setText(menuname);

            holder.tvPrice.setText(price);
            return convertView;
        }

        class ViewHolder {
            private TextView tvMenu;
            private TextView tvPrice;
            private ImageView ivMenuIcon;

        }
    }


}

