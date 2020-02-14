package com.example.nasagery_application.network;

import com.example.nasagery_application.model.Image;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NASAFactory {

    private String BASE_URL = "https://images-api.nasa.gov";
    private NASAService nasaService = createService(retrofitInstance());

    private Retrofit retrofitInstance(){
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    private NASAService createService(Retrofit retrofit){
        return retrofit.create(NASAService.class);
    }

    public Observable<Image> getImage(String url, int page) {
        return nasaService.getImage(url, page);
    }
}


