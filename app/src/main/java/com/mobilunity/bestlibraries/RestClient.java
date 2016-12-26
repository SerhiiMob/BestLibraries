package com.mobilunity.bestlibraries;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestClient {
    @GET ("/emojis")
    Call<Map<String, String>> getEmojis();
}
