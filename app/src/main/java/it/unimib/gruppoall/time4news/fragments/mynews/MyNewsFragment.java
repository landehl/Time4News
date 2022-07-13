package it.unimib.gruppoall.time4news.fragments.mynews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import it.unimib.gruppoall.time4news.R;
import it.unimib.gruppoall.time4news.models.News;
import it.unimib.gruppoall.time4news.adapters.NewsListAdapter;
import it.unimib.gruppoall.time4news.database.FbDatabase;

import it.unimib.gruppoall.time4news.models.User;

public class MyNewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TextView mEmptyTV;
    private NewsListAdapter adapter;
    private List<News> locallySavedNews;

    // Firebase
    private User user;

    public MyNewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_news, container, false);
        // Inizializzo lista news mantenute salvate localmente
        locallySavedNews = new ArrayList<>();

        // Binding elementi visuali
        mRecyclerView = view.findViewById(R.id.tabsavednews_recycler_view);
        mEmptyTV = view.findViewById(R.id.tabsavednews_empty_view);

        // Recupero dati database
        FbDatabase.getUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);


                locallySavedNews.clear();
                Gson gson = new Gson();
                for (String jsonPON : user.getNews()) {
                    locallySavedNews.add(gson.fromJson(jsonPON, News.class));
                }

                adapter = new NewsListAdapter(getActivity(), locallySavedNews, user, (byte) 3);

                // Controllo la presenza o meno di informazioni per mostrare un messaggio di stato
                if (locallySavedNews.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyTV.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyTV.setVisibility(View.GONE);
                }

                // Recupero il recyclerview dal layout xml e imposto l'adapter
                LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(manager);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FbDatabase.getUserReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                if(user!=null) {

                    locallySavedNews.clear();
                    Gson gson = new Gson();
                    for (String jsonPON : user.getNews()) {
                        locallySavedNews.add(gson.fromJson(jsonPON, News.class));
                    }

                    // Controllo la presenza o meno di informazioni per mostrare un messaggio di stato
                    if (locallySavedNews.isEmpty()) {
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyTV.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyTV.setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }


}