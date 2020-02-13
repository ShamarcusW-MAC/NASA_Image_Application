package com.example.nasagery_application.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.example.nasagery_application.R;
import com.example.nasagery_application.adapter.ImageAdapter;
import com.example.nasagery_application.databinding.ActivityMainBinding;
import com.example.nasagery_application.model.Item;
import com.example.nasagery_application.model.Utils;
import com.example.nasagery_application.viewmodel.NASAViewModel;
import com.example.nasagery_application.model.Response;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private NASAViewModel nasaViewModel;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private EditText editText;
    private Utils status;
    private int page = 1;

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        nasaViewModel = ViewModelProviders.of(this).get(NASAViewModel.class);
        activityMainBinding.setViewModel(nasaViewModel);

        //Chip group is created and ready for display once the user is ready to input
        //a search word
        ChipGroup entryChipGroup = findViewById(R.id.chip_groupof6);
        Chip entryChip1 = findViewById(R.id.chip1);
        Chip entryChip2 = findViewById(R.id.chip2);
        Chip entryChip3 = findViewById(R.id.chip3);
        Chip entryChip4 = findViewById(R.id.chip4);
        Chip entryChip5 = findViewById(R.id.chip5);
        Chip entryChip6 = findViewById(R.id.chip6);
        Chip entryChip7 = findViewById(R.id.chip7);
        Chip entryChip8 = findViewById(R.id.chip8);
        Chip entryChip9 = findViewById(R.id.chip9);
        Chip entryChip10 = findViewById(R.id.chip10);


        //Once edit text is clicked for input, the chip group appears. Once the user has
        //clicked on a chip, that said chip will be removed.
        activityMainBinding.searchEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.horizontalScrollview.setVisibility(View.VISIBLE);
                entryChipGroup.setVisibility(View.VISIBLE);
                activityMainBinding.seeMoreTextview.setVisibility(View.INVISIBLE);
                CompoundButton.OnCheckedChangeListener entryChipListener = new CompoundButton.OnCheckedChangeListener() {
                    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        //Once a chip is clicked
                        if(isChecked) {
                            activityMainBinding.searchEdittext.setText(buttonView.getText());
                            buttonView.setVisibility(View.GONE);
                        } else {
                            buttonView.setVisibility(View.VISIBLE);
                        }

                        Log.i("TAG_SHA", buttonView.getText().toString() + "");
                    }
                };
                entryChip1.setOnCheckedChangeListener(entryChipListener);
                entryChip2.setOnCheckedChangeListener(entryChipListener);
                entryChip3.setOnCheckedChangeListener(entryChipListener);
                entryChip4.setOnCheckedChangeListener(entryChipListener);
                entryChip5.setOnCheckedChangeListener(entryChipListener);
                entryChip6.setOnCheckedChangeListener(entryChipListener);
                entryChip7.setOnCheckedChangeListener(entryChipListener);
                entryChip8.setOnCheckedChangeListener(entryChipListener);
                entryChip9.setOnCheckedChangeListener(entryChipListener);
                entryChip10.setOnCheckedChangeListener(entryChipListener);


            }
        });


        //Check if network is up and running, shows green check mark if it's up, red x if it's not.
        if(status.isNetworkAvailable(this)){
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_check_green_24dp);
        }else{
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_close_red_24dp);
        }






        //Once button is clicked, the api call is made.
        activityMainBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
                activityMainBinding.rightArrowImageview.setVisibility(View.VISIBLE);
                page = 1;
                nasaViewModel.makeCall(activityMainBinding.searchEdittext.getText().toString(), page);
                entryChipGroup.setVisibility(View.INVISIBLE);
                InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(null != MainActivity.this.getCurrentFocus())
                {
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getApplicationWindowToken(), 0);
                }
            }
        });

        activityMainBinding.leftArrowImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page == 2)
                {
                    activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
                }
//                activityMainBinding.seeMoreTextview.setVisibility(View.GONE);
                entryChipGroup.setVisibility(View.INVISIBLE);
                page -= 1;
                nasaViewModel.makeCall(activityMainBinding.searchEdittext.getText().toString(), page);
                Toast.makeText(MainActivity.this, "Page: " + page, Toast.LENGTH_SHORT).show();

            }
        });



        activityMainBinding.rightArrowImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page == 1)
                {
                    activityMainBinding.leftArrowImageview.setVisibility(View.VISIBLE);

                }
//                activityMainBinding.seeMoreTextview.setVisibility(View.GONE);
                entryChipGroup.setVisibility(View.INVISIBLE);
                page += 1;
                nasaViewModel.makeCall(activityMainBinding.searchEdittext.getText().toString(), page);
                Toast.makeText(MainActivity.this, "Page: " + page, Toast.LENGTH_SHORT).show();

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
                    activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
                    activityMainBinding.rightArrowImageview.setVisibility(View.INVISIBLE);

                }
            }
        });

//      Initiates when the user pulls to refresh the recycler view.
//      Once initiated, the recycler view refresh back to the starting value of items which is 20.
        activityMainBinding.swipeRecyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                compositeDisposable.add(nasaViewModel.getImage(activityMainBinding.searchEdittext.getText().toString(), page)
                        .subscribe(images -> {
                            {
                                if((status.isNetworkAvailable(MainActivity.this)) && (!activityMainBinding.searchEdittext.getText().toString().isEmpty())) {
                                    displayImages(images.getCollection().getItems());
                                    imageAdapter.notifyDataSetChanged();
                                    activityMainBinding.swipeRecyclerview.setRefreshing(false);
                                    activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
//                                    pageSize = 20;
//                                    imageAdapter.limit = pageSize;
//                                    Toast.makeText(MainActivity.this, "Number of photos: " + imageAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "Page: " + page, Toast.LENGTH_SHORT).show();

                                } else {
                                    activityMainBinding.swipeRecyclerview.setRefreshing(false);
                                    activityMainBinding.imageRecyclerview.setVisibility(View.GONE);
                                    activityMainBinding.seeMoreTextview.setVisibility(View.GONE);
                                    activityMainBinding.noresultsTextview.setText("No Search Word! No Images!");
                                    activityMainBinding.noresultsTextview.setVisibility(View.VISIBLE);
                                    activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
                                    activityMainBinding.rightArrowImageview.setVisibility(View.INVISIBLE);

                                }
                            }
                            }, throwable -> {
                            Log.d("TAG_ERROR_MAIN", throwable.getMessage());
                        }

                        )

                );
            }
        });



    }

    //Adapter is initialized and recycler view is filled with items once called upon and if
    //api call is success
    private void displayImages(List<Item> images) {
        Log.d("TAG_COUNT", "" + images.size());


        editText = findViewById(R.id.search_edittext);
        imageAdapter = new ImageAdapter(this, images);
        recyclerView = findViewById(R.id.image_recyclerview);
//        int keywordsLength = images.get(0).getData().get(0).getKeywords().size();


        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(true);

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
            activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
            activityMainBinding.rightArrowImageview.setVisibility(View.INVISIBLE);

        } else {
            activityMainBinding.noresultsTextview.setVisibility(View.GONE);
            activityMainBinding.seeMoreTextview.setVisibility(View.VISIBLE);
            activityMainBinding.seeMoreTextview.setText("(Click title to see more!)");
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(imageAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            if(images.size() < 100)
            {
                activityMainBinding.rightArrowImageview.setVisibility(View.INVISIBLE);
            } else {
                activityMainBinding.rightArrowImageview.setVisibility(View.VISIBLE);
            }

        }

    }
    public void onResume(){
        super.onResume();
        status = new Utils();
        if(status.isNetworkAvailable(this)){
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_check_green_24dp);
        }else{
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_close_red_24dp);

        }
    }
}
