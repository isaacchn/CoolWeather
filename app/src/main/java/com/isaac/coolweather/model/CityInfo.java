package com.isaac.coolweather.model;

/**
 * Name: CityInfo
 * Usage: Each city is an object.
 * Created by IsaacCn on 2015/8/30.
 */
public class CityInfo {
    private int cityId;
    private String cityName;
    private String country;
    private float cityLongitude;
    private float cityLatitude;

    public CityInfo() {
    }

    public CityInfo(int cityId, String cityName, String country, float cityLongitude, float cityLatitude) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.country = country;
        this.cityLongitude = cityLongitude;
        this.cityLatitude = cityLatitude;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public float getCityLongitude() {
        return cityLongitude;
    }

    public float getCityLatitude() {
        return cityLatitude;
    }
}
