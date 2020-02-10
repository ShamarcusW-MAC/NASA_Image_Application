package com.example.nasagery_application.adapter;

import android.animation.LayoutTransition;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.example.nasagery_application.R;
import com.example.nasagery_application.databinding.ImageItemLayoutBinding;
import com.example.nasagery_application.model.Item;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

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
        holder.binding.imagetitleTextview.setOnClickListener(v -> {
//            Log.d("TAG_EXPAND", expanded + "");
            item.setExpand(!expanded);

//            holder.binding.itemCardview.addView(holder.binding.imageConstraintLayout);

//            TransitionManager.beginDelayedTransition(holder.binding.itemCardview, new AutoTransition());
            holder.binding.itemCardview.animate().translationY(holder.binding.nasaimageImageview.getHeight());
            holder.binding.nasaimageImageview.animate().translationY(holder.binding.nasaimageImageview.getHeight());
            notifyItemChanged(position);

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
//        ConstraintLayout itemLayout;
        MaterialCardView materialCardView;

        public ImageViewHolder(ImageItemLayoutBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Item item)
        {

//            imageLayout = itemView.findViewById(R.id.image_ConstraintLayout);

//            materialCardView = itemView.findViewById(R.id.item_cardview);
//            itemLayout = itemView.findViewById(R.id.item_ConstraintLayout);
//            itemLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
//            materialCardView.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            binding.setData(item.getData().get(0));
            binding.setLink(item.getLinks().get(0));
//            binding.itemCardview.removeView(binding.imageAuthorTextview);
//            binding.itemCardview.removeView(binding.imageDateTextview);

            if(binding.getData().getSecondaryCreator() == null)
            {
                binding.getData().setSecondaryCreator("Unknown");
            }


            boolean expanded = item.isNotExpand();
            binding.imageDescriptionTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
//            binding.itemCardview.addView(binding.imageAuthorTextview);
//            binding.itemCardview.addView(binding.imageDateTextview);
            binding.imageAuthorTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageDateTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.itemHorizontalScrollview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageDescriptionTextview.setMovementMethod(new ScrollingMovementMethod());
//            binding.imageConstraintLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);


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
                    binding.itemChipGroup.addView(chip);
                    Log.d("TAG_KEY", binding.getData().getKeywords().get(i));

                }
                Log.d("TAG_KEY", binding.getData().getKeywords().size() + "");
            }


            binding.executePendingBindings();
        }
    }

}
