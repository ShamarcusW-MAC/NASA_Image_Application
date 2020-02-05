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


        //Check if network is up and running
        if(status.isNetworkAvailable(this)){
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_check_green_24dp);
        }else{
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_close_red_24dp);
        }

        //Once button is clicked, the api call is made.
        activityMainBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nasaViewModel.makeCall(activityMainBinding.searchEdittext.getText().toString());
            }
        });

        //If the network request is successful, the item are displayed in recycler view.
        //However, if the request has failed, an error message will appear.
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
//      Once initiated, the recycler view refresh back to the starting value of items which is 20.
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

    //Adapter is initialized and recycler view is filled with items once called upon and if
    //api call is success
    private void displayImages(List<Item> images) {
        editText = findViewById(R.id.search_edittext);
        imageAdapter = new ImageAdapter(this, images);
        recyclerView = findViewById(R.id.image_recyclerview);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        //If the search comes up empty, a message saying "No Results." will be displayed.
        //If the search is initiated without a string in the edit textview, then a message saying
        //"No Search Word No Images!" will be displayed.
        //If the search is successful, the adapter and layout manager are initialized, and the items
        //are displayed
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

            //Whenever the user reaches to the bottom of the recycler view, the number of items
            //is increased by 20 each time.
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
