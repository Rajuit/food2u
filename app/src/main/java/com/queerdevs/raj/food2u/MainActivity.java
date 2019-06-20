package com.queerdevs.raj.food2u;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
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

//import static com.queerdevs.raj.food2u.ImageLoader.imageLoader;

public class MainActivity extends AppCompatActivity {

    private final String URL_TO_HIT = "http://queerdevs.in/images/restaurant/rest.json";
    private TextView tvData;
    private ListView lvRestaurant;
    private ProgressDialog dialog;
    String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ImageLoader.getInstance().destroy();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait..."); // showing a dialog for loading the data
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();
        ImageLoaderConfiguration configs = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .denyCacheImageMultipleSizesInMemory()
                .build();
        // ImageLoader imageLoader=new ImageLoader();
        // ImgLoader.imageLoader.destroy();
        ImageLoader.getInstance().init(configs);

        lvRestaurant = (ListView) findViewById(R.id.lvRestaurant);


        // To start fetching the data when app start, uncomment below line to start the async task.
        new JSONTask().execute(URL_TO_HIT);
    }


    public class JSONTask extends AsyncTask<String, String, List<RestaurantModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<RestaurantModel> doInBackground(String... params) {
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
                //  System.out.print(finalJson);

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("restaurant");//Array name in json

                List<RestaurantModel> restaurantModelList = new ArrayList<>();

                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself
                     * which is commented below
                     */
                    RestaurantModel restaurantModel = gson.fromJson(finalObject.toString(), RestaurantModel.class); // a single line json parsing using Gson

                    //       restaurantModel.setRestaurant(finalObject.getString("restaurant"));
//                    restaurantModel.setRating((float) finalObject.getDouble("rating"));
//                    restaurantModel.setWorkingDay(finalObject.getString("WorkingDay"));
//                    restaurantModel.setImage(finalObject.getString("image"));
//                    restaurantModel.setStory(finalObject.getString("story"));
//
//                    List<RestaurantModel.Service> serviceList = new ArrayList<>();
//                    for(int j=0; j<finalObject.getJSONArray("service").length(); j++){
//                        RestaurantModel.Service service = new RestaurantModel.Service();
//                        service.setName(finalObject.getJSONArray("service").getJSONObject(j).getString("name"));
//                        serviceList.add(service);
//                    }
//                    restaurantModel.setServiceList(serviceList);
                    // adding the final object in the list
                    restaurantModelList.add(restaurantModel);
                }
                return restaurantModelList;

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
        protected void onPostExecute(final List<RestaurantModel> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                final RestaurantAdapter adapter = new RestaurantAdapter(getApplicationContext(), R.layout.row, result);
                lvRestaurant.setAdapter(adapter);
                lvRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // list item click opens a new detailed activity
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RestaurantModel restaurantModel = result.get(position); // getting the model
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("restaurantModel", new Gson().toJson(restaurantModel)); // converting model json into string type and sending it via intent
                        startActivity(intent);

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class RestaurantAdapter extends ArrayAdapter<RestaurantModel> {

        private List<RestaurantModel> restaurantModelList;
        private int resource;
        private LayoutInflater inflater;

        public RestaurantAdapter(Context context, int resource, List<RestaurantModel> objects) {
            super(context, resource, objects);
            restaurantModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.ivRestaurantIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
                holder.tvRestaurant = (TextView) convertView.findViewById(R.id.tvRestaurant);
                holder.tvWorkingDay = (TextView) convertView.findViewById(R.id.tvWorkingDay);
                holder.tvHourOfOperation = (TextView) convertView.findViewById(R.id.tvHoursOfOperation);
                holder.rbRestaurantRating = (RatingBar) convertView.findViewById(R.id.rbRestaurant);
                holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
                holder.tvService = (TextView) convertView.findViewById(R.id.tvService);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            // Then later, when you want to display image
            final ViewHolder finalHolder = holder;
            ImageLoader.getInstance().displayImage(restaurantModelList.get(position).getImage(), holder.ivRestaurantIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    finalHolder.ivRestaurantIcon.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.ivRestaurantIcon.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.ivRestaurantIcon.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.ivRestaurantIcon.setVisibility(View.INVISIBLE);
                }
            });

            holder.tvRestaurant.setText(restaurantModelList.get(position).getRestaurant());
            holder.tvWorkingDay.setText("Working Day  :  " + restaurantModelList.get(position).getWorkingDay());
            holder.tvHourOfOperation.setText("Working Hours  : " + restaurantModelList.get(position).getHourOfOperation());

            // rating bar
            holder.rbRestaurantRating.setRating(restaurantModelList.get(position).getRating() / 2);


            StringBuffer stringBuffer = new StringBuffer();
            for (RestaurantModel.Address address : restaurantModelList.get(position).getAddress()) {
                stringBuffer.append(address.getName() + ", ");
            }

            holder.tvAddress.setText("Address  :  " + stringBuffer);
            holder.tvService.setText("Service  :  " + restaurantModelList.get(position).getService());
            return convertView;
        }


        class ViewHolder {
            private TextView tvRestaurant;
            private TextView tvWorkingDay;
            private TextView tvHourOfOperation;
            private RatingBar rbRestaurantRating;
            private TextView tvService;
            private TextView tvAddress;
            private ImageView ivRestaurantIcon;
        }


    }










}
