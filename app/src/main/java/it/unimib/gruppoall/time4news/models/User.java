package it.unimib.gruppoall.time4news.models;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import it.unimib.gruppoall.time4news.database.FbDatabase;

@IgnoreExtraProperties
public class User {

    private String username;
    private String email;
    private List<String> news;
    private Gson gson = new Gson();

    private static final String TAG = "User";

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        news = new ArrayList<>();
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;

        news = new ArrayList<>();

    }


    @PropertyName("news")
    public List<String> getNews() {
        return news;
    }


    @PropertyName("username")
    public String getUsername() {
        return username;
    }

    @PropertyName("email")
    public String getEmail() {
        return email;
    }

    public boolean savePieceOfNews(News pon) {
        if (!checkSavedPieceOfNews(pon)) {
            String jsonPieceOfNews = gson.toJson(pon);
            List<String> userDbNews = news;
            userDbNews.add(jsonPieceOfNews);
            FbDatabase.getUserReference().child("news").setValue(userDbNews);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeSavedPieceOfNews(News pon) {
        if (checkSavedPieceOfNews(pon)) {
            for (int i = 0; i < news.size(); i++) {
                // Per evitare uno spreco computazionale, eseguo comparazione news
                // solo se i primi 50 caratteri sono uguali
                if (news.get(i).substring(0, 49).equals(gson.toJson(pon).substring(0, 49))) {
                    news.remove(i);
                }
            }

            FbDatabase.getUserReference().child("news").setValue(news);
            return true;
        } else {
            return false;
        }
    }


    public boolean checkSavedPieceOfNews(News localPon) {
        String ponToString = gson.toJson(localPon);
        for (String cloudPon : news) {
            // Per evitare uno spreco computazionale, eseguo comparazione news
            // solo se i primi 50 caratteri sono uguali
            if (cloudPon.substring(0, 49).equals(ponToString.substring(0, 49))) {
                return true;
            }
        }

        return false;
    }


    @NotNull
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    public void setUsername(String username) {
        this.username = username;
    }
}