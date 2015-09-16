package com.isaac.coolweather.model;

/**
 * Created by IsaacCn on 2015/9/13.
 */
public class SavedCityDetail {
    private int cityId;
    private String cityName;
    private String country;
    private double longitude;
    private double latitude;

    public SavedCityDetail(int cityId, String cityName, String country, double longitude, double latitude) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
