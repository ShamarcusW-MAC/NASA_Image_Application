package com.example.nasagery_application.viewmodel;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.nasagery_application.model.Image;
import com.example.nasagery_application.model.Response;
import com.example.nasagery_application.network.RetrofitConfig;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NasaViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<Response> image = new MutableLiveData();
    private RetrofitConfig nasaFactory = new RetrofitConfig();

    public Observable<Image> getImage(String url, int page) {
        return nasaFactory.getImage(url, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void makeCall(String editText, int page) {
        if(editText != "") {
            compositeDisposable.add(getImage("" + editText, page)
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
