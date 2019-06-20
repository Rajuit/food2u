package com.queerdevs.raj.food2u;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import static com.queerdevs.raj.food2u.R.id.tvHourOfOperation;

public class DetailActivity extends AppCompatActivity {

    public String y;
    ImageView ivRestaurantIcon;
    TextView tvRestaurant;
    TextView tvWorkingDay;
    RatingBar rbRestaurantRating;
    TextView tvService;
    TextView tvAddress;
    ProgressBar progressBar;
    TextView tvHourOfOperation;
    String url;
    Button btn1;
    Context context;
    private DetailActivity ImageView;
    RestaurantModel restaurantModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        ivRestaurantIcon = (ImageView) findViewById(R.id.ivRestaurantIcon);
        tvRestaurant = (TextView) findViewById(R.id.tvRestaurant);
        tvWorkingDay = (TextView) findViewById(R.id.tvWorkingDay);
        tvHourOfOperation = (TextView) findViewById(R.id.tvHourOfOperation);

        rbRestaurantRating = (RatingBar) findViewById(R.id.rbRestaurant);
        tvService = (TextView) findViewById(R.id.tvService);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn1 = (Button) findViewById(R.id.fooditem);


        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        // recovering data from MainActivity, sent via intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String json = bundle.getString("restaurantModel"); // getting the model from MainActivity send via extras

            Log.d("MODEL", json);

            restaurantModel = new Gson().fromJson(json, RestaurantModel.class);

            Log.d("URL", restaurantModel.getImage());

            //  this.ivRestaurantIcon=ivRestaurantIcon;
// url=restaurantModel.getUrl();

//                 Picasso.with(context).load(url).into((Target) ivRestaurantIcon);


            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(restaurantModel.getImage(), ivRestaurantIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            });

            tvRestaurant.setText(restaurantModel.getRestaurant());
            tvWorkingDay.setText("Working Day  :  " + restaurantModel.getWorkingDay());
            tvHourOfOperation.setText("Working Hours  :  " + restaurantModel.getHourOfOperation());


            // rating bar
            rbRestaurantRating.setRating(restaurantModel.getRating() / 2);

            StringBuffer stringBuffer = new StringBuffer();
            for (RestaurantModel.Address address : restaurantModel.getAddress()) {
                stringBuffer.append(address.getName() + ", ");
            }

            tvAddress.setText("Address  :  " + stringBuffer);
            tvService.setText("Service  :  " + restaurantModel.getService());

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MenuActivity.class);
                intent.putExtra("menuURL", restaurantModel.getMenuURL());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
