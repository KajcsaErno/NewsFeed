package com.example.android.newsfeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

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

        int categoryColor = getCategoryColor(newsPosition.getCategory());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            holder.categoryTextView.setBackgroundTintList(context.getResources().getColorStateList(categoryColor));
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

/*    private int getCategoryColor() {
        //make colors great again
        int[] colorsFromResources = context.getResources().getIntArray(R.array.rainbow);
        List<Integer> colorList = new ArrayList<>();
        for (int colorId : colorsFromResources) {
            colorList.add(colorId);
        }
        return colorList.get(new Random().nextInt(colorList.size()));
    }*/


    private int getCategoryColor(String category) {
        int categoryColor;
        switch (category) {
            case Constants.NEWS:
                categoryColor = R.color.md_red_400;
                break;
            case Constants.WORLD_NEWS:
                categoryColor = R.color.md_red_600;
                break;
            case Constants.UK_NEWS:
                categoryColor = R.color.md_red_900;
                break;
            case Constants.CITIES:
                categoryColor = R.color.md_deep_orange_600;
                break;
            case Constants.GLOBAL_DEVELOPMENT:
                categoryColor = R.color.md_deep_orange_700;
                break;
            case Constants.TECHNOLOGY:
                categoryColor = R.color.md_deep_orange_800;
                break;
            case Constants.BUSINESS:
                categoryColor = R.color.md_indigo_600;
                break;
            case Constants.ENVIRONMENT:
                categoryColor = R.color.md_teal_800;
                break;
            case Constants.EDUCATION:
                categoryColor = R.color.md_cyan_500;
                break;
            case Constants.SOCIETY:
                categoryColor = R.color.md_brown_500;
                break;
            case Constants.SCIENCE:
                categoryColor = R.color.md_amber_700;
                break;
            case Constants.OPINION:
                categoryColor = R.color.md_light_green_400;
                break;
            case Constants.SPORT:
                categoryColor = R.color.md_blue_500;
                break;
            case Constants.FOOTBALL:
                categoryColor = R.color.md_blue_700;
                break;
            case Constants.CULTURE:
                categoryColor = R.color.md_cyan_800;
                break;
            case Constants.BOOKS:
                categoryColor = R.color.md_purple_400;
                break;
            case Constants.MUSIC:
                categoryColor = R.color.md_purple_500;
                break;
            case Constants.TV_AND_RADIO:
                categoryColor = R.color.md_purple_600;
                break;
            case Constants.ART_AND_DESIGN:
                categoryColor = R.color.md_purple_700;
                break;
            case Constants.FILMS:
                categoryColor = R.color.md_green_400;
                break;
            case Constants.GAMES:
                categoryColor = R.color.md_green_400;
                break;
            case Constants.STAGE:
                categoryColor = R.color.md_deep_orange_400;
                break;
            case Constants.LIFE_AND_STYLE:
                categoryColor = R.color.md_deep_orange_400;
                break;
            case Constants.FASHION:
                categoryColor = R.color.md_deep_orange_500;
                break;
            case Constants.TRAVEL:
                categoryColor = R.color.md_lime_500;
                break;
            case Constants.MONEY:
                categoryColor = R.color.md_yellow_700;
                break;
            default:
                categoryColor = R.color.md_red_A700;
                break;
        }
        return categoryColor;
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
