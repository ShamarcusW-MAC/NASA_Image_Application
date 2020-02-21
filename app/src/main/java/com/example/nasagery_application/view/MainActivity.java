package com.example.nasagery_application.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.example.nasagery_application.R;
import com.example.nasagery_application.adapter.ImageAdapter;
import com.example.nasagery_application.databinding.ActivityMainBinding;
import com.example.nasagery_application.model.Item;
import com.example.nasagery_application.model.Utils;
import com.example.nasagery_application.viewmodel.NasaViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.List;
import java.util.Objects;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private NasaViewModel nasaViewModel;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    public EditText editText;
    private int page = 1;
    private ActivityMainBinding activityMainBinding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        nasaViewModel = ViewModelProviders.of(this).get(NasaViewModel.class);
        activityMainBinding.setViewModel(nasaViewModel);


        /*
        The title animation is initialized and executed here.
         */
        Animation slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        Animation slideBackLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left_back);
        Animation slideBackRight = AnimationUtils.loadAnimation(this, R.anim.slide_back_right);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        activityMainBinding.titleTextview.startAnimation(slideRight);
        slideRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                activityMainBinding.rightStar.startAnimation(shake);
                activityMainBinding.rightStar.setImageResource(R.drawable.half_star_white_24dp);
                activityMainBinding.titleTextview.startAnimation(slideBackLeft);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideBackLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                activityMainBinding.leftStar.startAnimation(shake);
                activityMainBinding.leftStar.setImageResource(R.drawable.half_star_white_24dp);
                activityMainBinding.titleTextview.startAnimation(slideBackRight);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slideBackRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                activityMainBinding.titleTextview.startAnimation(rotate);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                activityMainBinding.leftStar.startAnimation(shake);
                activityMainBinding.rightStar.startAnimation(shake);
                activityMainBinding.leftStar.setImageResource(R.drawable.solid_star_white_24dp);
                activityMainBinding.rightStar.setImageResource(R.drawable.solid_star_white_24dp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


//        String[] keywordArray = new String[10];
//
//        keywordArray = {"earth", "sun", "moon", "star"}

        /*
            Chip group is created and is displayed once the user clicks on the edit text
            and is ready to input a search word.
         */
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

        /*
            Once edit text is clicked for input, the chip group appears. Once the user has
            clicked on a chip, that said chip will be removed.
         */
        activityMainBinding.searchEdittext.setOnClickListener(v -> {

            activityMainBinding.horizontalScrollview.setVisibility(View.VISIBLE);
            entryChipGroup.setVisibility(View.VISIBLE);
            activityMainBinding.seeMoreTextview.setVisibility(View.INVISIBLE);
            CompoundButton.OnCheckedChangeListener entryChipListener = (buttonView, isChecked) -> {

                //Once a chip is clicked, its text will be entered in the edit text.
                if(isChecked) {
                    activityMainBinding.searchEdittext.setText(buttonView.getText());
                    buttonView.setVisibility(View.GONE);
                } else {
                    buttonView.setVisibility(View.VISIBLE);
                }

                Log.i("TAG_SHA", buttonView.getText().toString() + "");
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


        });

        //Check if network is up and running, shows green check mark if it's up, red x if it's not.
        if(Utils.isNetworkAvailable(this)){
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_check_green_24dp);
        }else{
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_close_red_24dp);
        }



        /*
        Once the search button is clicked, the api call is made with the page number
        and the user input text.
         */
        activityMainBinding.searchButton.setOnClickListener(v -> {
            activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
            activityMainBinding.rightArrowImageview.setVisibility(View.VISIBLE);
            activityMainBinding.pageNumberTextview.setVisibility(View.VISIBLE);
            page = 1;
            nasaViewModel.makeCall(Objects.requireNonNull(activityMainBinding.searchEdittext.getText()).toString(), page);
            activityMainBinding.pageNumberTextview.setText("" + page);
            entryChipGroup.setVisibility(View.INVISIBLE);
            InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(null != MainActivity.this.getCurrentFocus())
            {
                assert imm != null;
                imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getApplicationWindowToken(), 0);
            }
        });

        /*
        When the user clicks the left arrow button on the application, the page number decrements
        and another api call is made.
         */
        activityMainBinding.leftArrowImageview.setOnClickListener(v -> {
            if(page == 2)
            {
                activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
            }
            entryChipGroup.setVisibility(View.INVISIBLE);
            page -= 1;
            nasaViewModel.makeCall(Objects.requireNonNull(activityMainBinding.searchEdittext.getText()).toString(), page);
            activityMainBinding.pageNumberTextview.setText("" + page);
            Toast.makeText(MainActivity.this, "Page: " + page, Toast.LENGTH_SHORT).show();


        });

        /*
        When the user clicks the right arrow button on the application, the page number increments
        and another api call is made.
         */
        activityMainBinding.rightArrowImageview.setOnClickListener(v -> {
            if(page == 1)
            {
                activityMainBinding.leftArrowImageview.setVisibility(View.VISIBLE);

            }
            entryChipGroup.setVisibility(View.INVISIBLE);
            page += 1;
            nasaViewModel.makeCall(Objects.requireNonNull(activityMainBinding.searchEdittext.getText()).toString(), page);
            activityMainBinding.pageNumberTextview.setText("" + page);
            Toast.makeText(MainActivity.this, "Page: " + page, Toast.LENGTH_SHORT).show();

        });
        /*
        If the network request is successful, the item are displayed in recycler view.
        However, if the request has failed, an error message will appear.
         */
        activityMainBinding.getViewModel().image.observe(this, response -> {

            if (response.success) {
                displayImages(response.image.getCollection().getItems());
            }else{
                Toast.makeText(MainActivity.this, response.message, Toast.LENGTH_SHORT).show();
                activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
                activityMainBinding.rightArrowImageview.setVisibility(View.INVISIBLE);

            }
        });

          /*
          Initiates when the user pulls to refresh the recycler view.
          Once initiated, the recycler view refresh back to the starting value of items which is 20.
           */
        activityMainBinding.swipeRecyclerview.setOnRefreshListener(() -> {
            page = 1;
            activityMainBinding.pageNumberTextview.setText("" + page);
            compositeDisposable.add(nasaViewModel.getImage(Objects.requireNonNull(activityMainBinding.searchEdittext.getText()).toString(), page)
                    .subscribe(images -> {
                        {
                            if((Utils.isNetworkAvailable(MainActivity.this)) && (!activityMainBinding.searchEdittext.getText().toString().isEmpty())) {
                                displayImages(images.getCollection().getItems());
                                imageAdapter.notifyDataSetChanged();
                                activityMainBinding.swipeRecyclerview.setRefreshing(false);
                                activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, "Page: " + page, Toast.LENGTH_SHORT).show();

                            } else {
                                activityMainBinding.swipeRecyclerview.setRefreshing(false);
                                activityMainBinding.imageRecyclerview.setVisibility(View.GONE);
                                activityMainBinding.seeMoreTextview.setVisibility(View.GONE);
                                activityMainBinding.noresultsTextview.setText(getText(R.string.no_search_word));
                                activityMainBinding.noresultsTextview.setVisibility(View.VISIBLE);
                                activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
                                activityMainBinding.rightArrowImageview.setVisibility(View.INVISIBLE);
                                activityMainBinding.pageNumberTextview.setVisibility(View.INVISIBLE);
                            }
                        }
                        }, throwable -> {
                        Log.d("TAG_ERROR_MAIN", Objects.requireNonNull(throwable.getMessage()));
                    }

                    )

            );
        });
    }

    /*
    Adapter is initialized and recycler view is filled with items once called upon and if
    the api call is successful.
     */
    private void displayImages(List<Item> images) {
        Log.d("TAG_COUNT", "" + images.size());
        editText = findViewById(R.id.search_edittext);
        imageAdapter = new ImageAdapter(this, images);
        recyclerView = findViewById(R.id.image_recyclerview);

        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(true);

        /*
            If the search comes up empty, a message saying "No Results." will be displayed.
            If the search is initiated without a string in the edit textview, then a message saying
            "No Search Word No Images!" will be displayed.
            If the search is successful, the adapter and layout manager are initialized, and the items
            are displayed
         */
        if (images.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            activityMainBinding.seeMoreTextview.setVisibility(View.GONE);
            activityMainBinding.noresultsTextview.setText(getText(R.string.no_results));
            activityMainBinding.noresultsTextview.setVisibility(View.VISIBLE);
            activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
            activityMainBinding.pageNumberTextview.setVisibility(View.INVISIBLE);
            activityMainBinding.rightArrowImageview.setVisibility(View.INVISIBLE);

        } else if (editText.getText().toString().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            activityMainBinding.seeMoreTextview.setVisibility(View.GONE);
            activityMainBinding.noresultsTextview.setText(getText(R.string.no_search_word));
            activityMainBinding.noresultsTextview.setVisibility(View.VISIBLE);
            activityMainBinding.leftArrowImageview.setVisibility(View.INVISIBLE);
            activityMainBinding.pageNumberTextview.setVisibility(View.INVISIBLE);
            activityMainBinding.rightArrowImageview.setVisibility(View.INVISIBLE);

        } else {
            activityMainBinding.noresultsTextview.setVisibility(View.GONE);
            activityMainBinding.seeMoreTextview.setVisibility(View.VISIBLE);
            activityMainBinding.seeMoreTextview.setText(getText(R.string.see_more));
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

    /*
        When the application opened once again after being suspended, the application retains
        its previous state, and the connection is check is executed once again.
     */
    public void onResume(){
        super.onResume();
        if(Utils.isNetworkAvailable(this)){
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_check_green_24dp);
        }else{
            activityMainBinding.statusImageview.setImageResource(R.drawable.ic_close_red_24dp);

        }
    }
}
