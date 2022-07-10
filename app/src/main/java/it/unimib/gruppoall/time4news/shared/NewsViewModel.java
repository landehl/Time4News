package it.unimib.gruppoall.time4news.shared;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.gruppoall.time4news.models.NewsResponse;
import it.unimib.gruppoall.time4news.repositories.INewsRepository;
import it.unimib.gruppoall.time4news.repositories.NewsRepository;

public class NewsViewModel extends AndroidViewModel {
    private static final String TAG = "CountryNewsViewModel";

    private final INewsRepository mINewsRepository;
    private MutableLiveData<NewsResponse> mNewsResponseLiveData;
    private String country = "it";
    private String category = "";
    private int page = 100;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public MutableLiveData<NewsResponse> getNewsResponseLiveData() {
        return mNewsResponseLiveData;
    }

    public NewsViewModel(Application application) {
        super(application);
        // You can choose which type of Repository to use
        mINewsRepository = new NewsRepository(application);
    }

    public MutableLiveData<NewsResponse> getNews() {
        fetchNews();
        return mNewsResponseLiveData;
    }


    public void refreshNews() {
        mINewsRepository.refreshNews(country, 100, category);
    }

    /**
     * It uses the Repository to download the news list
     * and to associate it with the LiveData object.
     */
    private void fetchNews() {
        mNewsResponseLiveData = mINewsRepository.fetchNews("it",
               page, category);
    }

}
