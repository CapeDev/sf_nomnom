package com.thoughtworks.android.capedev.domain;

public class SearchResult {
    private String resultName;
    private String resultLocation;
    private String googlePlacesPictureUrl;
    private String distance;

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getResultLocation() {
        return resultLocation;
    }

    public void setResultLocation(String resultLocation) {
        this.resultLocation = resultLocation;
    }

    public String getGooglePlacesPictureUrl() {
        return googlePlacesPictureUrl;
    }

    public void setGooglePlacesPictureUrl(String googlePlacesPictureUrl) {
        this.googlePlacesPictureUrl = googlePlacesPictureUrl;
    }

    public SearchResult(String name, String location, String pictureUrl, String distance){
        this.resultName = name;
        this.resultLocation = location;
        this.googlePlacesPictureUrl = pictureUrl;
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
