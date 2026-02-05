package com.divy.mlbbguideoffline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.divy.mlbbguideoffline.R;
import com.divy.mlbbguideoffline.model.Hero;
import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> {

    private List<Hero> heroList;
    private Context context;

    public HeroAdapter(Context context, List<Hero> heroList) {
        this.context = context;
        this.heroList = heroList;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hero, parent, false);
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        Hero hero = heroList.get(position);
        holder.tvName.setText(hero.getHeroName());
        holder.tvClass.setText(hero.getHeroClass());

        // Load image using Glide
        // Using provided web URL if available
        if (hero.getPortraitUrl() != null && !hero.getPortraitUrl().isEmpty()) {
            Glide.with(context)
                    .load(hero.getPortraitUrl())
                    .placeholder(R.drawable.ic_launcher_foreground) // Placeholder
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache both original & resized image
                    .into(holder.ivPortrait);
        } else {
            holder.ivPortrait.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    public static class HeroViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPortrait;
        TextView tvName;
        TextView tvClass;

        public HeroViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPortrait = itemView.findViewById(R.id.iv_hero_portrait);
            tvName = itemView.findViewById(R.id.tv_hero_name);
            tvClass = itemView.findViewById(R.id.tv_hero_class);
        }

    }

    public void updateList(List<Hero> newList) {
        this.heroList = newList;
        notifyDataSetChanged();
    }
}
