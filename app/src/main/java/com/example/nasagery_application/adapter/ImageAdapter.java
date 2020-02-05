package com.example.nasagery_application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.nasagery_application.R;
import com.example.nasagery_application.databinding.ImageItemLayoutBinding;
import com.example.nasagery_application.model.Item;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Item> itemList;
    public int limit = 20;

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
        holder.binding.imagetitleTextview.setOnClickListener(v -> {
            boolean expanded = item.isNotExpand();
            item.setExpand(!expanded);
            notifyItemChanged(position);
        });
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        if(itemList != null && itemList.size() > limit) {
            return limit;
        } else {
            return itemList.size() > 0 ?
                    itemList.size() : 0;
        }
    }


    //All of the appropriate data is held within this class
    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView nasaImageView;
        ImageItemLayoutBinding binding;

        public ImageViewHolder(ImageItemLayoutBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Item item)
        {
            binding.setData(item.getData().get(0));
            binding.setLink(item.getLinks().get(0));

            boolean expanded = item.isNotExpand();
            binding.imageDescriptionTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageAuthorTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);
            binding.imageDateTextview.setVisibility(expanded ? View.VISIBLE : View.GONE);

            nasaImageView = itemView.findViewById(R.id.nasaimage_imageview);
            Glide.with(itemView.getContext())
                    .load(item.getLinks().get(0).getHref())
                .into(nasaImageView);
            binding.executePendingBindings();
        }
    }

}
