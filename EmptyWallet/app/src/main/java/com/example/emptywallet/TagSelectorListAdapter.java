package com.example.emptywallet;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TagSelectorListAdapter extends RecyclerView.Adapter<TagSelectorListAdapter.TagSelectorViewHolder>{

    private final LayoutInflater myInflater;
    private List<Tag> myTags; // Cached copy of Tags
    private TagSelectorListAdapter.OnTagClickListener onTagClickListener;

    TagSelectorListAdapter(Context context, TagSelectorListAdapter.OnTagClickListener pOnTagClickListener) {
        myInflater = LayoutInflater.from(context);
        this.onTagClickListener = pOnTagClickListener;
    }


    class TagSelectorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tTitle;
        private final ConstraintLayout tLayout;

        TagSelectorListAdapter.OnTagClickListener onTagClickListener;

        private TagSelectorViewHolder(View itemView, TagSelectorListAdapter.OnTagClickListener pOnTagClickListener) {
            super(itemView);

            tTitle = itemView.findViewById(R.id.single_tag_tagName);
            tLayout = itemView.findViewById(R.id.single_tag_ConstraintHolder);

            this.onTagClickListener = pOnTagClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTagClickListener.onTagClick(getAdapterPosition());
        }
    }

    @Override
    public TagSelectorListAdapter.TagSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = myInflater.inflate(R.layout.recyclerview_tags_singletag, parent, false);
        return new TagSelectorListAdapter.TagSelectorViewHolder(itemView, onTagClickListener);
    }

    @Override
    public void onBindViewHolder(TagSelectorListAdapter.TagSelectorViewHolder holder, int position) {

        if (myTags != null) {
            Tag current = myTags.get(position);

            if(current.getName().isEmpty()){
                holder.tTitle.setText("NoTitle");
            }
            else{
                holder.tTitle.setText(current.getName());
            }

            int temp= current.getColor();
            Drawable newD = ContextCompat.getDrawable(myInflater.getContext(), R.drawable.tag_shape);
            newD.setColorFilter(new PorterDuffColorFilter(temp, PorterDuff.Mode.SRC_ATOP));

            holder.tLayout.setBackground(newD);
        } else {
            // Covers the case of data not being ready yet.
            holder.tTitle.setText("No tags found");
        }
    }

    void setTags(List<Tag> tags) {
        myTags = tags;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (myTags != null)
            return myTags.size();
        else return 0;
    }

    public interface OnTagClickListener {
        void onTagClick(int position);
    }
}
