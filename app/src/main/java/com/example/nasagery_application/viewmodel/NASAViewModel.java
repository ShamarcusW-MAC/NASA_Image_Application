package com.example.nasagery_application.viewmodel;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nasagery_application.model.Image;
import com.example.nasagery_application.model.Response;
import com.example.nasagery_application.network.NASAFactory;
import com.example.nasagery_application.view.MainActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NASAViewModel extends ViewModel {

    public MainActivity mainActivity;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<Response> image = new MutableLiveData();




    private NASAFactory nasaFactory = new NASAFactory();


    public Observable<Image> getImage(String url) {
        return nasaFactory.getImage(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    public void makeCall(String editText) {

        if(editText != "") {
            compositeDisposable.add(getImage("" + editText)
                    .subscribe(images -> {

                        {
                            Response response = new Response();
                            response.image = images;
                            response.success = true;
                            image.postValue(response);
                        }

                    }, throwable -> {
                        Response response = new Response();
                        response.message = throwable.getMessage();
                        response.success = false;
                        image.postValue(response);

                        Log.d("TAG_ERROR", throwable.getMessage());
                    }));
        }

    }

}
