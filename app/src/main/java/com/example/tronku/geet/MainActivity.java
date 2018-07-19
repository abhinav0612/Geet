package com.example.tronku.geet;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0f);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragments(new SongList());
        adapter.AddFragments(new Favorites());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_audiotrack);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_favorite);
    }
}
