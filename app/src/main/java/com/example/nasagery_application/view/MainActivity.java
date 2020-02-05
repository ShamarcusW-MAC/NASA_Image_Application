package com.example.nasagery_application.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.nasagery_application.R;
import com.example.nasagery_application.adapter.ImageAdapter;
import com.example.nasagery_application.databinding.ActivityMainBinding;
import com.example.nasagery_application.model.Item;
import com.example.nasagery_application.model.Status;
import com.example.nasagery_application.viewmodel.NASAViewModel;
import com.example.nasagery_application.model.Response;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private NASAViewModel nasaViewModel;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private EditText editText;
    private int pageSize = 20;
    private Status status;

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        nasaViewModel = ViewModelProviders.of(this).get(NASAViewModel.class);
        activityMainBinding.setViewModel(nasaViewModel);
        status = new Status();

        if(status.isNetworkAvailable(this)){
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_check_green_24dp);
        }else{
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_close_red_24dp);
        }

        activityMainBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nasaViewModel.makeCall(activityMainBinding.searchEdittext.getText().toString());
            }
        });

        activityMainBinding.getViewModel().image.observe(this, new Observer<Response>() {
            @Override
            public void onChanged(Response response) {

                if (response.success) {
                    displayImages(response.image.getCollection().getItems());
                }else{
                    Toast.makeText(MainActivity.this, response.message, Toast.LENGTH_SHORT).show();
                }
            }
        });
//      Initiates when the user pulls to refresh the recycler view.
//      Once initiated, 20 more photos will be added to the view.
        activityMainBinding.swipeRecyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                compositeDisposable.add(nasaViewModel.getImage(activityMainBinding.searchEdittext.getText().toString())
                        .subscribe(images -> {
                            {
                                displayImages(images.getCollection().getItems());
                                imageAdapter.notifyDataSetChanged();
                                activityMainBinding.swipeRecyclerview.setRefreshing(false);
                                pageSize = 20;
                                imageAdapter.limit = pageSize;
                                Toast.makeText(MainActivity.this, "Number of photos: " + imageAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
                            }
                            }, throwable -> {
                            Log.d("TAG_ERROR", throwable.getMessage());
                        }

                        )

                );
            }
        });

    }

    //Adapter is initialized and recycler view is filled with items
    private void displayImages(List<Item> images) {
        editText = findViewById(R.id.search_edittext);
        imageAdapter = new ImageAdapter(this, images);
        recyclerView = findViewById(R.id.image_recyclerview);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        if (images.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            activityMainBinding.seeMoreTextview.setVisibility(View.GONE);
            activityMainBinding.noresultsTextview.setText("No Results.");
            activityMainBinding.noresultsTextview.setVisibility(View.VISIBLE);

        } else if (editText.getText().toString().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            activityMainBinding.seeMoreTextview.setVisibility(View.GONE);
            activityMainBinding.noresultsTextview.setText("No Search Word! No Images!");
            activityMainBinding.noresultsTextview.setVisibility(View.VISIBLE);
        } else {
            activityMainBinding.noresultsTextview.setVisibility(View.GONE);
            activityMainBinding.seeMoreTextview.setVisibility(View.VISIBLE);
            activityMainBinding.seeMoreTextview.setText("(Click title to see more!)");
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(imageAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                        pageSize += 20;
                        imageAdapter.limit = pageSize;
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageAdapter.notifyDataSetChanged();
                            }
                        });
                        Toast.makeText(MainActivity.this, "Number of photos: " + imageAdapter.limit, Toast.LENGTH_SHORT).show();
                        Log.d("TAG_NUMBER", "" + pageSize);
                    }
                }
            });

        }

    }
    public void onResume(){
        super.onResume();
        status = new Status();
        if(status.isNetworkAvailable(this)){
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_check_green_24dp);
        }else{
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_close_red_24dp);

        }
    }
}
