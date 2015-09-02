package com.isaac.coolweather.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.isaac.coolweather.R;
import com.isaac.coolweather.util.LogUtil;

import java.util.List;

public class MainActivity extends Activity implements OnClickListener {

    private LocationManager locationManager;
    private String provider;
    private double currentLongitude;
    private double currentLatitude;
    TextView locationInfoText;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLongitude=location.getLongitude();
            currentLatitude=location.getLatitude();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getWeatherInfo = (Button) findViewById(R.id.get_weather_info);
        getWeatherInfo.setOnClickListener(this);
        locationInfoText = (TextView) findViewById(R.id.location_info_text);

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
        locationInfoText.setText("您所在地的坐标为：" + currentLongitude + "," + currentLatitude);
        locationManager.requestLocationUpdates(provider,5000,1,locationListener);
        //int temp = Utilities.getCityIdByLocation(this, 117.08, 36.19);
        //LogUtil.d("MainActivity", "city_id= " + temp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_weather_info:
                getWeatherInfoByLocation();
            default:
                break;
        }
    }

    private void getWeatherInfoByLocation() {

    }
}
