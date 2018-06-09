package com.example.android.newsfeed;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<News> mNewsList;
    private Context context;
    private ClickListener newsClickListener;

    NewsAdapter(List<News> mNewsList, Context context, ClickListener newsClickListener) {
        this.mNewsList = mNewsList;
        this.context = context;
        this.newsClickListener = newsClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cardview_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //getting the current position
        News newsPosition = mNewsList.get(position);

        //Update the newsPosition title
        holder.webTitleTextView.setText(newsPosition.getWebTitle());

        //Getting oval background otherwise the default is rectangular
        Drawable drawable = holder.categoryTextView.getBackground();
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable)drawable).getPaint().setColor(getCategoryColor());
        } else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable)drawable).setColor(getCategoryColor());
        } else if (drawable instanceof ColorDrawable) {
            ((ColorDrawable)drawable).setColor(getCategoryColor());
        }

        //Update the categoryTextView
        holder.categoryTextView.setText(newsPosition.getCategory());

        //Update the date and time if there is any
        if (newsPosition.getPublicationDate().equals("")) {
            holder.publicationDateTextView.setText(R.string.no_date);
        } else {
            holder.publicationDateTextView.setText(newsPosition.getPublicationDate());
        }

        //Update the author name if it has any, or update with Anonymous text
        if (newsPosition.getAuthorName().equals("")) {
            holder.authorNameTextView.setText(R.string.anonymous);
        } else {
            holder.authorNameTextView.setText(newsPosition.getAuthorName());
        }

        //Update the thumbnail with glide this is first time I use it
        Glide.with(context).load(newsPosition.getThumbnail()).into(holder.webThumbnailImageView);

        //Share the news with friends
        holder.shareImageView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsClickListener.sharingWebsiteOnClickListener(holder.getAdapterPosition());

            }
        }));

        // Opening the current news web page when user clicks on the openWebPageImage
        holder.openWebPageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsClickListener.openingWebsiteOnClickListener(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public News getItemPosition(int position) {
        return mNewsList.get(position);
    }

    public void clear() {
        mNewsList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<News> newsList) {
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }

    private int getCategoryColor() {
        //make colors great again
        int[] colorsFromResources = context.getResources().getIntArray(R.array.rainbow);
        List<Integer> colorList = new ArrayList<>();
        for (int colorId : colorsFromResources) {
            colorList.add(colorId);
        }
        return colorList.get(new Random().nextInt(colorList.size()));
    }




    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_card_view)
        CardView cardView;
        @BindView(R.id.news_title_text_view)
        TextView webTitleTextView;
        @BindView(R.id.news_category_text_view)
        TextView categoryTextView;
        @BindView(R.id.publication_date_text_view)
        TextView publicationDateTextView;
        @BindView(R.id.author_name_text_view)
        TextView authorNameTextView;
        @BindView(R.id.news_thumbnail_image_view)
        ImageView webThumbnailImageView;
        @BindView(R.id.share_image_view)
        ImageView shareImageView;
        @BindView(R.id.open_web_page_image_view)
        ImageView openWebPageImageView;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
