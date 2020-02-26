package com.biz.naver.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private final static String naver_movie_url = "https://openapi.naver.com/v1/";

    public static RetrofitService getInstance() {
        return getRetrofit().create(RetrofitService.class);
    }

    // baseUrl은 문자열 부분이 꼭 "/"로 끝나야 한다.
    private static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new  Retrofit.Builder()
                .baseUrl(naver_movie_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

}
