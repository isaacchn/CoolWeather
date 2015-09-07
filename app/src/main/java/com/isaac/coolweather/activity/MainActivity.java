package com.isaac.coolweather.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.isaac.coolweather.R;
import com.isaac.coolweather.model.WeatherDetail;
import com.isaac.coolweather.util.GetCityIdListener;
import com.isaac.coolweather.util.HttpCallbackListener;
import com.isaac.coolweather.util.LogUtil;
import com.isaac.coolweather.util.Utilities;

import java.util.List;

public class MainActivity extends Activity implements OnClickListener {

    private LocationManager locationManager;
    private String provider;
    private double currentLongitude;
    private double currentLatitude;
    private static final String SERVER_URL = "http://api.openweathermap.org/data/2.5/weather?id=";
    private static int selectCityId = 0;
    TextView locationInfoText;
    TextView locationInfoText2;
    TextView locationInfoText3;

    private static final int CHANGE_CITY_INFO_TEXT = 1;
    private static final int CHANGE_WEATHER_INFO = 2;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLongitude = location.getLongitude();
            currentLatitude = location.getLatitude();
            locationInfoText.setText("您所在地的坐标为：" + currentLongitude + "," + currentLatitude);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_CITY_INFO_TEXT:
                    //locationInfoText3.setText((String) msg.obj);
                    LogUtil.d("MainActivity",(String)msg.obj);
                    Gson gson = new Gson();
                    WeatherDetail weatherDetail = gson.fromJson((String)msg.obj,WeatherDetail.class);
                    StringBuilder builder = new StringBuilder();
                    builder.append("City: "+weatherDetail.getName()+"\n");
                    builder.append("Weather: "+weatherDetail.getWeather().get(0).getMain()+"\n");
                    builder.append("Temp: "+weatherDetail.getMain().getTemp_min()+"/"+weatherDetail.getMain().getTemp_max()+" ℉\n");
                    builder.append("Humidity: "+weatherDetail.getMain().getHumidity()+"\n");
                    locationInfoText3.setText(builder.toString());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getWeatherInfo = (Button) findViewById(R.id.get_weather_info);
        getWeatherInfo.setOnClickListener(this);
        locationInfoText = (TextView) findViewById(R.id.location_info_text);
        locationInfoText2 = (TextView) findViewById(R.id.location_info_text2);
        locationInfoText3 = (TextView) findViewById(R.id.location_info_text3);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "无法获取位置信息", Toast.LENGTH_SHORT).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            currentLongitude = location.getLongitude();
            currentLatitude = location.getLatitude();
        }
        locationInfoText.setText("您所在地的坐标为：\n" + currentLongitude + "," + currentLatitude);
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_weather_info:
                //此处显示正在加载
                getCityInfoByLocation(currentLongitude, currentLatitude);
//                //加载天气信息
//                switch (selectCityId) {
//                    case 0:
//                        //获取城市信息错误
//                        locationInfoText3.setText("0");
//                    default:
//                        Utilities.sendHttpRequest(SERVER_URL + selectCityId, new HttpCallbackListener() {
//                            @Override
//                            public void onFinish(String response) {
//                                Message msg = new Message();
//                                msg.what = CHANGE_WEATHER_INFO;
//                                msg.obj=response;
//                                handler.sendMessage(msg);
//                            }
//
//                            @Override
//                            public void onError(Exception e) {
//                            }
//                        });
//                }
                //正在加载结束
            default:
                break;
        }
    }

    private void getCityInfoByLocation(double longitude, double latitude) {
        Utilities.getCityIdByLocation(this, SERVER_URL, longitude, latitude, new GetCityIdListener() {
            @Override
            public void onFinish(String cityId) {
                Message msg = new Message();
                msg.what = CHANGE_CITY_INFO_TEXT;
                msg.obj = cityId;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
