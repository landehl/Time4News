package it.unimib.gruppoall.time4news.repositories;

import androidx.lifecycle.MutableLiveData;

import it.unimib.gruppoall.time4news.models.NewsResponse;

public interface INewsRepository {

    enum JsonParser {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };

    MutableLiveData<NewsResponse> fetchNews(String country, int page, String category);
    void refreshNews(String country, int page, String category);

}
