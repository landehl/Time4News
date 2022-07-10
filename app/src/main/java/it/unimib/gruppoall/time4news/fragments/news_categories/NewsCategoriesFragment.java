package it.unimib.gruppoall.time4news.fragments.news_categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.tabs.TabLayout;

import it.unimib.gruppoall.time4news.R;
import it.unimib.gruppoall.time4news.adapters.ViewPagerAdapter;
import it.unimib.gruppoall.time4news.fragments.news_categories.tab_fragments.business.Business;
import it.unimib.gruppoall.time4news.fragments.news_categories.tab_fragments.entertainment.Entertainment;
import it.unimib.gruppoall.time4news.fragments.news_categories.tab_fragments.health.Health;
import it.unimib.gruppoall.time4news.fragments.news_categories.tab_fragments.science.Science;
import it.unimib.gruppoall.time4news.fragments.news_categories.tab_fragments.sport.Sport;
import it.unimib.gruppoall.time4news.fragments.news_categories.tab_fragments.technology.Technology;
import it.unimib.gruppoall.time4news.shared.NewsViewModel;
import it.unimib.gruppoall.time4news.utils.Constants;


public class NewsCategoriesFragment extends Fragment {

    private int tabPos = 0;

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    NewsViewModel viewModel;


    public NewsCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_categories, container, false);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());;
        viewPager2 = view.findViewById(R.id.view_pager);
        viewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);
        viewPagerManager();

        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout = view.findViewById(R.id.include);
        tabLayoutManager();
        viewModel.setCategory(Constants.SPORTS);
        viewModel.refreshNews();
        return view;
    }

    private void tabLayoutManager(){

        tabLayout.selectTab(tabLayout.getTabAt(tabPos));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    viewModel.setCategory(Constants.SPORTS);
                    viewModel.refreshNews();
                }
                if(tab.getPosition() == 1){
                    viewModel.setCategory(Constants.HEALTH);
                    viewModel.refreshNews();
                }

                if(tab.getPosition() == 2){
                    viewModel.setCategory(Constants.BUSINESS);
                    viewModel.refreshNews();
                }

                if(tab.getPosition() == 3){
                    viewModel.setCategory(Constants.SCIENCE);
                    viewModel.refreshNews();
                }

                if(tab.getPosition() == 4){
                    viewModel.setCategory(Constants.TECHNOLOGY);
                    viewModel.refreshNews();
                }

                if(tab.getPosition() == 5){
                    viewModel.setCategory(Constants.ENTERTAINMENT);
                    viewModel.refreshNews();
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


    }

    private void viewPagerManager(){

        viewPagerAdapter.add(new Sport());
        viewPagerAdapter.add(new Health());
        viewPagerAdapter.add(new Business());
        viewPagerAdapter.add(new Science());
        viewPagerAdapter.add(new Technology());
        viewPagerAdapter.add(new Entertainment());
    }

}