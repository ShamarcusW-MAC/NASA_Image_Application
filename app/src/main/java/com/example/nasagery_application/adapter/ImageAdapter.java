package com.example.nasagery_application.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.IOException;
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
//        Animation animRotate = AnimationUtils.loadAnimation(this.context, R.anim.rotate);

        boolean expanded = item.isNotExpand();
        holder.binding.imagetitleTextview.setOnClickListener(v -> {
//            Log.d("TAG_EXPAND", expanded + "");
            item.setExpand(!expanded);

//            holder.binding.itemCardview.startAnimation(animRotate);
//            chip.setChipStrokeColor(ColorStateList.valueOf(Color.WHITE));
//            chip.setChipStrokeWidth((float) 2.0);

            holder.binding.itemCardview.animate().translationY(holder.binding.nasaimageImageview.getHeight());
//            holder.binding.nasaimageImageview.animate().translationY(holder.binding.nasaimageImageview.getHeight());
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


        private ImageViewHolder(ImageItemLayoutBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("ClickableViewAccessibility")
        private void bind(Item item)
        {

            binding.setData(item.getData().get(0));
            binding.setLink(item.getLinks().get(0));


            if(binding.getData().getSecondaryCreator() == null)
            {
                binding.getData().setSecondaryCreator("Unknown");
            }



            boolean expanded = item.isNotExpand();
            binding.titleLabelTextview.setText("Title : ");
            binding.authorLabelTextview.setText("Author : ");
            binding.dateLabelTextview.setText("Date/Time: ");
            binding.descriptionLabelTextview.setText("Description: ");
            binding.descriptionLabelTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.authorLabelTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.dateLabelTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
//            binding.imageTextLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageDescriptionTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageAuthorTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageDateTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.itemHorizontalScrollview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.labelsLabelTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageLabelLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
//            binding.textResultsTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageDescriptionTextview.setMovementMethod(new ScrollingMovementMethod());
            binding.itemCardview.setStrokeColor(expanded ? Color.WHITE : 0);
            binding.itemCardview.setStrokeWidth(expanded ? 2 : 0);


            binding.imageDescriptionTextview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });



            nasaImageView = itemView.findViewById(R.id.nasaimage_imageview);
            Glide.with(itemView.getContext())
                    .load(item.getLinks().get(0).getHref())
                .into(nasaImageView);

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
//                    Log.d("TAG_KEY", binding.getData().getKeywords().get(i));
//                    ObjectAnimator forwardAnimator = ObjectAnimator.ofFloat(binding.itemHorizontalScrollview, "translationX", 1200f);
//                    forwardAnimator.setDuration(20000);
//                    forwardAnimator.start();
//
//                    forwardAnimator.setRepeatCount(Animation.INFINITE);

//
                }

//                Log.d("TAG_KEY", binding.getData().getKeywords().size() + "");
            }


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


                FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

//                 Or, to set the minimum confidence required:
//                 FirebaseVisionOnDeviceImageLabelerOptions options =
//                     new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
//                         .setConfidenceThreshold(0.7f)
//                         .build();
//                 FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
//                     .getOnDeviceImageLabeler(options);

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
//                                    labelTextView.setBackgroundColor(Color.WHITE);
                                    labelTextView.setTextColor(Color.parseColor("#420101"));
                                    binding.imageLabelLayout.addView(labelTextView);

                                    if(labelTextView.getText().toString() == "")
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


//                Task<FirebaseVisionText> result =
//                        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//                            @Override
//                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
//
//                                for(FirebaseVisionText.TextBlock textBlock : firebaseVisionText.getTextBlocks())
//                                {
//                                    Rect boundingBox = textBlock.getBoundingBox();
//                                    Point[] cornerPoints = textBlock.getCornerPoints();
//                                    String resultText = textBlock.getText();
//                                    Log.d("TAG_BOUND", boundingBox + "");
//                                    Log.d("TAG_CORNER", cornerPoints + "");
//                                    Log.d("TAG_RESULT", resultText);
//
//
//                    String blockText = textBlock.getText();
//                    Float blockConfidence = textBlock.getConfidence();
//                    List<RecognizedLanguage> blockLanguages = textBlock.getRecognizedLanguages();
//                    Point[] blockCornerPoints = textBlock.getCornerPoints();
//                    Rect blockFrame = textBlock.getBoundingBox();
//                    for (FirebaseVisionText.Line line: textBlock.getLines()) {
//                        String lineText = line.getText();
//                        Float lineConfidence = line.getConfidence();
//                        List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
//                        Point[] lineCornerPoints = line.getCornerPoints();
//                        Rect lineFrame = line.getBoundingBox();
//                        for (FirebaseVisionText.Element element: line.getElements()) {
//                            String elementText = element.getText();
//                            Float elementConfidence = element.getConfidence();
//                            List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
//                            Point[] elementCornerPoints = element.getCornerPoints();
//                            Rect elementFrame = element.getBoundingBox();
//
//                            //                                    binding.textResultsTextview.setText(resultText);
//                                    if(lineText == null)
//                                    {
//                                        binding.imageTextLayout.setVisibility(View.GONE);
//
//                                    }
//                                    else {
////                                        binding.textResultsTextview.setText(resultText);
//                                        TextView textTextView = new TextView(itemView.getContext());
//                                        textTextView.setText(lineText);
//                                        textTextView.setTextColor(Color.parseColor("#420101"));
//                                        binding.imageTextLayout.addView(textTextView);
//                                    }
//                        }
//                    }
//                                }
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });
//---------------------------------_---------------___---_---_-----------_-_-_------
//                String resultText = result.toString();
//                for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
//                    String blockText = block.getText();
//                    Float blockConfidence = block.getConfidence();
//                    List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
//                    Point[] blockCornerPoints = block.getCornerPoints();
//                    Rect blockFrame = block.getBoundingBox();
//                    for (FirebaseVisionText.Line line: block.getLines()) {
//                        String lineText = line.getText();
//                        Float lineConfidence = line.getConfidence();
//                        List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
//                        Point[] lineCornerPoints = line.getCornerPoints();
//                        Rect lineFrame = line.getBoundingBox();
//                        for (FirebaseVisionText.Element element: line.getElements()) {
//                            String elementText = element.getText();
//                            Float elementConfidence = element.getConfidence();
//                            List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
//                            Point[] elementCornerPoints = element.getCornerPoints();
//                            Rect elementFrame = element.getBoundingBox();
//                        }
//                    }
//                }

            }catch (Exception e) {

                e.printStackTrace();


            }




//
//            labeler.processImage(image)
//                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
//                        @Override
//                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
//                            // Task completed successfully
//                            // ...
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // Task failed with an exception
//                            // ...
//                        }
//                    });


            binding.executePendingBindings();
        }
    }

}
