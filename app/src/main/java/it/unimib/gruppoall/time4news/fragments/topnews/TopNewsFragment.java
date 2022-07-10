package it.unimib.gruppoall.time4news.fragments.topnews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.gruppoall.time4news.R;
import it.unimib.gruppoall.time4news.models.News;
import it.unimib.gruppoall.time4news.models.NewsResponse;
import it.unimib.gruppoall.time4news.shared.NewsViewModel;
import it.unimib.gruppoall.time4news.adapters.NewsListAdapter;
import it.unimib.gruppoall.time4news.database.FbDatabase;

import it.unimib.gruppoall.time4news.models.User;


public class TopNewsFragment extends Fragment {

    private List<News> mNewsList;
    private NewsViewModel mNewsViewModel;
    private User user;
    private NewsListAdapter mNewsListAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean feedInitializedSentinel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedInitializedSentinel = false;
        mNewsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);
        mSwipeRefreshLayout = view.findViewById(R.id.feed_swipe_refresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = view.findViewById(R.id.feed_recycler_view);
        // Recupero dati database
        user = null;
        // Collego un listener all'utente
        FbDatabase.getUserReference().addValueEventListener(postListenerUserData);


    }

    public void initializeFeed() {
        mNewsList = new ArrayList<>();
        mNewsViewModel.setCategory("");
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mNewsListAdapter = new NewsListAdapter(getActivity(), mNewsList, user, (byte) 1);
        mRecyclerView.setAdapter(mNewsListAdapter);
        final Observer<NewsResponse> observer = new Observer<NewsResponse>() {
            @Override
            public void onChanged(NewsResponse newsResponse) {
                if (newsResponse.isError()) {
                    showError(newsResponse.getStatus());
                }

                if (newsResponse.getArticles() != null) {
                    mNewsList.clear();
                    mNewsList.addAll(newsResponse.getArticles());
                    mNewsListAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        };

        mNewsViewModel.getNews().observe(getViewLifecycleOwner(),
                observer);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);

            mNewsViewModel.refreshNews();
        });
    }


    private ValueEventListener postListenerUserData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            user = dataSnapshot.getValue(User.class);
            if (!FbDatabase.getUserDeleting()) {
                if (!feedInitializedSentinel && user != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    initializeFeed();
                    feedInitializedSentinel = true;
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            //  throw databaseError.toException();
        }
    };

    private void showError(String errorMessage) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();


    }
}