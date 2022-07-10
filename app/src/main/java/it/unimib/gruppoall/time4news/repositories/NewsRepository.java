package it.unimib.gruppoall.time4news.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.gruppoall.time4news.models.News;
import it.unimib.gruppoall.time4news.service.NewsApiService;
import it.unimib.gruppoall.time4news.models.NewsResponse;
import it.unimib.gruppoall.time4news.service.ServiceLocator;
import it.unimib.gruppoall.time4news.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository implements INewsRepository {
    private static final String TAG = "NewsRepWithLiveData";

    private final Application mApplication;
    private final MutableLiveData<NewsResponse> mNewsResponseLiveData;
    private final NewsApiService mNewsApiService;

    public NewsRepository(Application application) {
        this.mApplication = application;
        this.mNewsApiService = ServiceLocator.getInstance().getNewsApiService();
        this.mNewsResponseLiveData = new MutableLiveData<>();
    }


    @Override
    public MutableLiveData<NewsResponse> fetchNews(String country, int page, String category) {

        getNews(country, page, category);
        return mNewsResponseLiveData;
    }

    @Override
    public void refreshNews(String country, int page, String category) {
        getNews(country, page, category);
    }


    private void getNews(String country, int page , String category) {
        Call<NewsResponse> newsResponseCall = mNewsApiService.getNews(country, page, category, Constants.API_KEY);

        newsResponseCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                if (response.body() != null && response.isSuccessful() && !response.body().getStatus().equals("error")) {
                    mNewsResponseLiveData.postValue(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
            }
        });
    }



}
