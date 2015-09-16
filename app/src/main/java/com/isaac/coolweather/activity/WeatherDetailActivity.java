package com.isaac.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.isaac.coolweather.R;
import com.isaac.coolweather.db.CoolWeatherDBOpenHelper;
import com.isaac.coolweather.model.WeatherDetail;
import com.isaac.coolweather.util.LogUtil;
import com.isaac.coolweather.util.UpdateUIListener;
import com.isaac.coolweather.util.Utilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.List;

public class WeatherDetailActivity extends Activity implements OnClickListener {

    Button homeButton;
    TextView cityName;
    Button refreshButton;
    ImageView weatherImage;
    TextView weatherMain;
    TextView weatherDetail;
    ProgressDialog progressDialog;
    ProgressBar iconProgressBar;

    private final static int UPDATE_ON_CREATE = 1000;
    private final static int REFRESH_CURRENT_CITY = 1001;
    private static final String CURRENT_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?id=";
    private static final String WEATHER_ICON_URL = "http://openweathermap.org/img/w/";  //eg:http://openweathermap.org/img/w/09d.png
    //public static final double KELVIN_ZERO_DEGREE = 273.15;

    private LocationManager locationManager;
    private String locationProvider;
    private int currentCityId;
    private String currentWeatherIcon;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_ON_CREATE:
                    LogUtil.d("WeatherDetailActivity", (String) msg.obj);
                    Gson gson = new Gson();
                    WeatherDetail weatherDetailObj = gson.fromJson((String) msg.obj, WeatherDetail.class);
                    //update title text view
                    cityName.setText(weatherDetailObj.getName() + "," + weatherDetailObj.getSys().getCountry());
                    //update main weather text view
                    StringBuilder mainBuilder = new StringBuilder();
                    mainBuilder.append(weatherDetailObj.getWeather().get(0).getMain() + "\n");
                    mainBuilder.append(Math.round(weatherDetailObj.getMain().getTemp_min() - Utilities.KELVIN_ZERO_DEGREE) + "/" //minus absolute zero degree
                            + Math.round(weatherDetailObj.getMain().getTemp_max() - Utilities.KELVIN_ZERO_DEGREE) + " ℃\n");
                    weatherMain.setText(mainBuilder.toString());
                    //update weather detail text view
                    StringBuilder detailBuilder = new StringBuilder();
                    detailBuilder.append("Humidity: " + weatherDetailObj.getMain().getHumidity() + "%\n");
                    detailBuilder.append("Pressure: " + weatherDetailObj.getMain().getPressure() + "hPa\n");
                    detailBuilder.append("Wind Speed: " + weatherDetailObj.getWind().getSpeed() + "m/s\n");
                    weatherDetail.setText(detailBuilder.toString());
                    //update currentCityId & currentWeatherIcon
                    currentCityId = weatherDetailObj.getId();
                    currentWeatherIcon=weatherDetailObj.getWeather().get(0).getIcon();
                    //dismiss progress dialog
                    progressDialog.dismiss();
                    //update weather icon
                    WeatherIconLoader weatherIconLoader=new WeatherIconLoader();
                    weatherIconLoader.execute(WEATHER_ICON_URL, currentWeatherIcon, ".png");
                    break;
                case REFRESH_CURRENT_CITY:
                    LogUtil.d("WeatherDetailActivity", "Refresh current city");
                    LogUtil.d("WeatherDetailActivity", (String) msg.obj);
                    Gson gson2 = new Gson();
                    WeatherDetail weatherDetail2 = gson2.fromJson((String) msg.obj, WeatherDetail.class);
                    //refresh main weather text view
                    StringBuilder mainBuilder2 = new StringBuilder();
                    mainBuilder2.append(weatherDetail2.getWeather().get(0).getMain() + "\n");
                    mainBuilder2.append(Math.round(weatherDetail2.getMain().getTemp_min() - Utilities.KELVIN_ZERO_DEGREE) + "/" //minus absolute zero degree
                            + Math.round(weatherDetail2.getMain().getTemp_max() - Utilities.KELVIN_ZERO_DEGREE) + " ℃\n");
                    weatherMain.setText(mainBuilder2.toString());
                    //refresh weather detail text view
                    StringBuilder detailBuilder2 = new StringBuilder();
                    detailBuilder2.append("Humidity: " + weatherDetail2.getMain().getHumidity() + "%\n");
                    detailBuilder2.append("Pressure: " + weatherDetail2.getMain().getPressure() + "hPa\n");
                    detailBuilder2.append("Wind Speed: " + weatherDetail2.getWind().getSpeed() + "m/s\n");
                    weatherDetail.setText(detailBuilder2.toString());
                    //update currentCityId & currentWeatherIcon
                    currentCityId = weatherDetail2.getId();
                    currentWeatherIcon=weatherDetail2.getWeather().get(0).getIcon();
                    //dismiss progress dialog
                    progressDialog.dismiss();
                    //update weather icon
                    WeatherIconLoader weatherIconLoader2=new WeatherIconLoader();
                    weatherIconLoader2.execute(WEATHER_ICON_URL,currentWeatherIcon,".png");
                default:
                    break;
            }
        }
    };

    //Load weather icon from server.
    class WeatherIconLoader extends AsyncTask<String, Void, BitmapDrawable> {
        @Override
        protected void onPreExecute() {
            iconProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * @param strings
         * @return null when error occurred.
         */
        @Override
        protected BitmapDrawable doInBackground(String... strings) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                LogUtil.d("WeatherIconLoader",strings[0]+strings[1]+strings[2]);
                HttpGet httpGet = new HttpGet(strings[0]+strings[1]+strings[2]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    InputStream inputStream = entity.getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return new BitmapDrawable(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
            if (bitmapDrawable != null) {
                iconProgressBar.setVisibility(View.GONE);
                weatherImage.setImageDrawable(bitmapDrawable);
            } else {
                Toast.makeText(WeatherDetailActivity.this, "获取天气图标失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        homeButton = (Button) findViewById(R.id.home);
        cityName = (TextView) findViewById(R.id.city_name);
        refreshButton = (Button) findViewById(R.id.refresh);
        weatherImage = (ImageView) findViewById(R.id.weather_image);
        weatherMain = (TextView) findViewById(R.id.weather_main);
        weatherDetail = (TextView) findViewById(R.id.weather_detail);
        iconProgressBar = (ProgressBar) findViewById(R.id.weather_icon_progress);

        homeButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在获取天气信息");
        progressDialog.setMessage("请稍后");
        progressDialog.setCancelable(true);
        progressDialog.show();

        /******************************************************************************************
         1 Update UI.
         2 Change current city id, so you can refresh current place weather info without geographic info.
         3 Dismiss progress dialog.
         ******************************************************************************************/
        Location location = getLocation();
        updateUIOnCreate(location); //Update current city ID in handler.
}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refresh:
                refreshUI(currentCityId);
                //tempWritePreferences();
                //tempWriteDatabase();
                break;
            case R.id.home:
                Intent intent = new Intent(WeatherDetailActivity.this,CitySelectionActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //Refresh user interface on button clicked
    private void refreshUI(int currentCityId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在获取天气信息");
        progressDialog.setMessage("请稍后");
        progressDialog.setCancelable(true);
        progressDialog.show();

        Utilities.updateCurrentCityWeatherInfo(CURRENT_WEATHER_URL + currentCityId, new UpdateUIListener() {
            @Override
            public void onFinish(String weatherJson) {
                Message message = new Message();
                message.what = REFRESH_CURRENT_CITY;
                message.obj = weatherJson;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onUpdateCurrentCityId(int cityId) {
            }
        });
    }

    //Update user interface when activity created
    private void updateUIOnCreate(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LogUtil.d("WeatherDetailActivity", "Location: " + longitude + "," + latitude);
        Utilities.updateWeatherInfoByLocation(this, longitude, latitude, new UpdateUIListener() {
            @Override
            public void onFinish(String weatherJson) {
                Message message = new Message();
                message.what = UPDATE_ON_CREATE;
                message.obj = weatherJson;
                handler.sendMessage(message);
            }

            @Override
            public void onUpdateCurrentCityId(int cityId) {
                currentCityId = cityId;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    //Get current location
    private Location getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "无法获取位置信息", Toast.LENGTH_SHORT).show();
            return null;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        return location;
    }

//    private void tempWritePreferences(){
//        SharedPreferences.Editor editor=getSharedPreferences("savedCityList",MODE_PRIVATE).edit();
//        editor.putInt("cityId",12345);
//        editor.putString("cityName","Utuobang");
//        editor.putString("country","Earth");
//        editor.putFloat("longitude",12.11f);
//        editor.putFloat("latitude",11.12f);
//        editor.apply();
//    }
//    private void tempWriteDatabase(){
//        CoolWeatherDBOpenHelper openHelper = new CoolWeatherDBOpenHelper(this,"OpenWeather.db",null,1);
//        SQLiteDatabase db = openHelper.getWritableDatabase();
//        //db.execSQL("insert into saved_city_detail (city_id,city_name,country,lon,lat) values ('12345,'Sishui','CN','11.22','22.11');");
//        //db.execSQL("insert into saved_city_detail (city_id,city_name,country,lon,lat) values ('12346,'Jining','CN','33.22','22.11');");
//    }
}
