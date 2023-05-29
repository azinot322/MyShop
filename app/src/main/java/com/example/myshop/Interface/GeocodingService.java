package com.example.myshop.Interface;

import com.example.myshop.model.LocationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingService {
    @GET("maps/api/geocode/json")
    Call<LocationResponse> getLocationFromPostalCode(@Query("address") String postalCode, @Query("key") String apiKey);
}