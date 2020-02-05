package com.example.nasagery_application.network;

import com.example.nasagery_application.model.Image;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NASAFactory {

    String BASE_URL = "https://images-api.nasa.gov";
    public NASAService nasaService = createService(retrofitInstance());

    private Retrofit retrofitInstance(){
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private NASAService createService(Retrofit retrofit){
        return retrofit.create(NASAService.class);
    }

    public Observable<Image> getImage(String url) {
        return nasaService.getImage(url);
    }
}


