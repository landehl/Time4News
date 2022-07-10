package it.unimib.gruppoall.time4news.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.unimib.gruppoall.time4news.R;
import it.unimib.gruppoall.time4news.models.News;
import it.unimib.gruppoall.time4news.models.User;
import it.unimib.gruppoall.time4news.utils.DateTimeUtil;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsModelViewHolder> {

    private final FragmentActivity mContext;
    private LayoutInflater layoutInflater;
    private List<News> newsList;
    private User user;
    private byte fragmentType;
    private final static String TAG = "NewsListAdapter";

    @NotNull
    @Override
    public NewsModelViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = this.layoutInflater.inflate(R.layout.layout_singlenews_card, parent, false);
        return new NewsModelViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        if (newsList != null)
            return newsList.size();
        return 0;
    }

    public NewsListAdapter(Context mContext, List<News> newsFeedModels, User user, byte fragmentType) {
        this.mContext = (FragmentActivity) mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.newsList = newsFeedModels;
        this.user = user;
        this.fragmentType = fragmentType;
    }

    @Override
    public void onBindViewHolder(NewsModelViewHolder holder, int position) {
        holder.bind(newsList.get(position));
    }

    public class NewsModelViewHolder extends RecyclerView.ViewHolder {
        private TextView newsTitle, newsSource, newsDescription, newsDate;
        private ImageView newsImage;
        private ToggleButton favBtn;


        public NewsModelViewHolder(View view) {
            super(view);

            newsTitle = view.findViewById(R.id.newsTitle);
            newsSource = view.findViewById(R.id.newsSource);
            newsDescription = view.findViewById(R.id.newsDesc);
            newsDate = view.findViewById(R.id.newsPubDate);
            newsImage = view.findViewById(R.id.newsImage);
            favBtn = view.findViewById(R.id.saveNewsImg);
        }

        public void bind(News pieceOfNews){

            // Configurazione link
            itemView.setOnClickListener(view -> {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(view.getResources().getColor(R.color.colorPrimary));
                builder.setShowTitle(true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(view.getContext(), Uri.parse(pieceOfNews.getUrl()));
            });

            // Immagine
            String imgUrl = pieceOfNews.getUrlToImage();
            if(imgUrl.isEmpty()) {
                Picasso.get()
                        .load(R.drawable.image_not_available)
                        .fit()
                        .centerCrop()
                        .into(newsImage);
            } else {
                Picasso.get()
                        .load(imgUrl)
                        .fit()
                        .centerCrop()
                        .into(newsImage);
            }

            // Titolo
            newsTitle.setText(pieceOfNews.getTitle());

            // Provider della notizia
            newsSource.setText(pieceOfNews.getNewsSource().getName());

            // Descrizione
            String plainDesc = Html.fromHtml(pieceOfNews.getDescription().replaceAll("<img.+/(img)*>", "")).toString();
            newsDescription.setText(plainDesc);

            // Data di pubblicazione
            String osDateTime = pieceOfNews.getPublishedAt();
            newsDate.setText(DateTimeUtil.getDate(pieceOfNews.getPublishedAt()));

            // ToggleButton bookmark
            if(fragmentType!=3) {
                if (user.checkSavedPieceOfNews(pieceOfNews)) {
                    favBtn.setChecked(true);
                } else {
                    favBtn.setChecked(false);
                }
            } else {
                favBtn.setChecked(true);
            }
            favBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(favBtn.isPressed()) {
                    final News ponClicked = pieceOfNews;
                    if (isChecked) {
                        if(user.savePieceOfNews(ponClicked)) {
                            Snackbar.make(buttonView, R.string.fav_news_added, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.action_undo, v -> user.removeSavedPieceOfNews(ponClicked))
                                    .setAnchorView(R.id.nav_view)
                                    .show();
                        }
                    } else {
                        if(user.removeSavedPieceOfNews(ponClicked)) {
                            Snackbar.make(buttonView, R.string.fav_news_removed, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.action_undo, v -> user.savePieceOfNews(ponClicked))
                                    .setAnchorView(R.id.nav_view)
                                    .show();
                        }
                    }
                }
            });

        }
    }

}