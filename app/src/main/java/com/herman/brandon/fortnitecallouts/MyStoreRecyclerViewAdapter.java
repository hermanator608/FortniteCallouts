package com.herman.brandon.fortnitecallouts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.herman.brandon.fortnitecallouts.Helpers.StoreContent.StoreItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link StoreItem}
 */
public class MyStoreRecyclerViewAdapter extends RecyclerView.Adapter<MyStoreRecyclerViewAdapter.ViewHolder> {

    private final List<StoreItem> mValues;
    private final StoreFragment parent;

    public MyStoreRecyclerViewAdapter(List<StoreItem> items, StoreFragment fragment) {
        mValues = items;
        parent = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_store_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Create store card
        holder.mItem = mValues.get(position);
        holder.title.setText(holder.mItem.title);
        holder.rarity.setText("Rarity: " + holder.mItem.rarity);
        holder.cost.setText("Price (V-Bucks): " + String.valueOf(holder.mItem.price));
        holder.type.setText("Type: " + holder.mItem.type);
        Glide.with(parent)
                .load(holder.mItem.imgUrl)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final TextView rarity;
        public final TextView cost;
        public final TextView type;
        public final ImageView image;
        public StoreItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.store_title);
            rarity = view.findViewById(R.id.store_rarity);
            cost = view.findViewById(R.id.store_price);
            type = view.findViewById(R.id.store_type);
            image = view.findViewById(R.id.store_image);
        }
    }
}
