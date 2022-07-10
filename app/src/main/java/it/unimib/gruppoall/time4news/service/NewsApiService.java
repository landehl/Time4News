package it.unimib.gruppoall.time4news.service;


import it.unimib.gruppoall.time4news.models.NewsResponse;
import it.unimib.gruppoall.time4news.utils.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET(Constants.TOP_HEADLINES_ENDPOINT)
    Call<NewsResponse> getNews(
            @Query(Constants.TOP_HEADLINES_COUNTRY_PARAMETER) String country,
            @Query(Constants.PAGE_SIZE) int pageSize,
            @Query(Constants.TOP_HEADLINES_CATEGORY_PARAMETER) String category,
            @Header("Authorization") String apiKey);
}
