package it.unimib.gruppoall.time4news.models;

import java.util.List;

public class NewsResponse {
    private String status;
    private int totalResults;
    private List<News> articles;
    private boolean isError;
    private boolean isLoading;

    public NewsResponse(String status, int totalResults, List<News> articles, boolean isLoading) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
        this.isLoading = isLoading;
    }

    public NewsResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }


    @Override
    public String toString() {
        return "NewsResponse{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", articles=" + articles +
                '}';
    }
}
