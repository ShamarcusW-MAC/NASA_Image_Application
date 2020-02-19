package com.example.nasagery_application.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.nasagery_application.R;
import com.example.nasagery_application.databinding.ImageItemLayoutBinding;
import com.example.nasagery_application.model.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Item> itemList;

    public ImageAdapter(Context context, List<Item> itemList){

        this.context = context;
        this.itemList = itemList;
    }


    //This method sets the view for our content to be displayed.
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ImageItemLayoutBinding binding = ImageItemLayoutBinding.inflate(layoutInflater, parent, false);
        return new ImageViewHolder(binding);
    }

    //This method binds the initialized values with the view. Fetches the appropriate data and fills
    //the view's layout with the fetched data.
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Item item = itemList.get(position);

        boolean expanded = item.isNotExpand();
        Animation animUp = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_up);
        Animation animDown = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_down);

        holder.binding.imagetitleTextview.setOnClickListener(v -> {
            Log.d("TAG_EXPAND", expanded + "");

            item.setExpand(!expanded);

            if(!expanded) {
                holder.binding.nasaimageImageview.startAnimation(animDown);
            }
            else {
                holder.binding.nasaimageImageview.startAnimation(animUp);
            }
            holder.binding.itemCardview.animate().translationY(holder.binding.nasaimageImageview.getHeight());
            holder.binding.imagetitleTextview.setEnabled(false);
            notifyItemChanged(position);
            holder.binding.imagetitleTextview.setEnabled(true);


        });
        holder.bind(item);
    }

    @Override
    public int getItemCount() {

        return itemList == null ? 0 : itemList.size();

    }


    //All of the appropriate data is held within this class
    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView nasaImageView;
        ImageItemLayoutBinding binding;
        FirebaseVisionImage image;
        Animation fade = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.fade);


        private ImageViewHolder(ImageItemLayoutBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        /*

        #item
         */
        @SuppressLint("ClickableViewAccessibility")
        private void bind(Item item)
        {

            binding.setData(item.getData().get(0));
            binding.setLink(item.getLinks().get(0));

            if(binding.getData().getSecondaryCreator() == null)
            {
                binding.getData().setSecondaryCreator("Unknown");
            }

            //Here are the views in which are visible depending on the boolean variable.
            //These view turn visible if the boolean is set to true, gone if set to false.
            boolean expanded = item.isNotExpand();
            binding.titleLabelTextview.setText("Title : ");
            binding.authorLabelTextview.setText("Author : ");
            binding.dateLabelTextview.setText("Date/Time: ");
            binding.descriptionLabelTextview.setText("Description: ");
            binding.labelsLabelTextview.setText("Labels: ");
            binding.descriptionLabelTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.authorLabelTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.dateLabelTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageDescriptionTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageAuthorTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageDateTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.itemHorizontalScrollview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.tellScrollTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.labelsLabelTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageLabelLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageDescriptionTextview.setMovementMethod(new ScrollingMovementMethod());
            binding.itemCardview.setStrokeColor(expanded ? Color.WHITE : 0);
            binding.itemCardview.setStrokeWidth(expanded ? 2 : 0);
            binding.tellScrollTextview.startAnimation(fade);

            //Card view will not scroll once the description view is touched upon.
            binding.imageDescriptionTextview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });


            //Each image is loaded in each item here,
            nasaImageView = itemView.findViewById(R.id.nasaimage_imageview);
            Glide.with(itemView.getContext())
                    .load(item.getLinks().get(0).getHref())
                .into(nasaImageView);

            //Here is where the chips are generated. Each chip contains a key word of each image.
            binding.itemChipGroup.removeAllViews();
            if(binding.getData().getKeywords() != null) {
                for (int i = 0; i < binding.getData().getKeywords().size(); i++) {
                    Chip chip = new Chip(binding.itemChipGroup.getContext());
                    chip.setText(binding.getData().getKeywords().get(i));
                    chip.setTextColor(Color.RED);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#420101")));
                    chip.setChipStrokeColor(ColorStateList.valueOf(Color.WHITE));
                    chip.setChipStrokeWidth((float) 2.0);
                    binding.itemChipGroup.addView(chip);
                }

            }



            //Convert image into a bitmap
            try{

                Glide.with(itemView.getContext())
                        .asBitmap()
                        .load(Uri.parse(item.getLinks().get(0).getHref()))
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                image = FirebaseVisionImage.fromBitmap(resource);

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });

                FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                        .getOnDeviceImageLabeler();

                binding.imageLabelLayout.removeAllViews();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,4,0,4);
                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                                //Task successful
                                for (FirebaseVisionImageLabel label: firebaseVisionImageLabels) {
                                    String text = label.getText();
                                    String entityId = label.getEntityId();
                                    float confidence = label.getConfidence();
                                    TextView labelTextView = new TextView(itemView.getContext());
                                    labelTextView.setText(text + " : " + (int) (confidence * 100) + "%");
                                    labelTextView.setLayoutParams(params);
                                    labelTextView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.textview_border));
                                    labelTextView.setPadding(10,10,10,10);
                                    labelTextView.setTextColor(Color.parseColor("#420101"));
                                    binding.imageLabelLayout.addView(labelTextView);
                                    binding.imageLabelLayout.invalidate();

                                    if(labelTextView.getVisibility() == View.INVISIBLE)
                                    {
                                        binding.labelsLabelTextview.setVisibility(View.GONE);
                                    }

                                    Log.d("TAG_TEXT", text);
                                    Log.d("TAG_ID", entityId);
                                    Log.d("TAG_CON", "" + confidence);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Task failed
                                Log.d("TAG_ERROR", "It didn't work");
                                e.printStackTrace();

                            }
                        });

            }catch (Exception e) {

                e.printStackTrace();

            }

            binding.executePendingBindings();
        }
    }

}
