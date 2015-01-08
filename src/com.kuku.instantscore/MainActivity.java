package com.kuku.instantscore;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.kuku.instantscore.communication.JsonRequest;
import com.kuku.instantscore.communication.MyVolley;
import com.kuku.instantscore.database.DBManager;
import com.kuku.instantscore.model.League;
import com.kuku.instantscore.volley.Request;
import com.kuku.instantscore.volley.RequestQueue;
import com.kuku.instantscore.volley.Response;
import com.kuku.instantscore.volley.VolleyError;
import com.newrelic.agent.android.NewRelic;

import java.util.List;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    MenuItem mMenuItem;
    boolean loadingFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        DBManager.init(getApplicationContext());

        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(mAppSectionsPagerAdapter.getCount() - 1);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        NewRelic.withApplicationToken("AA907c3b86fbc007ff0ecb385c864207f3d89b8715").start(this.getApplication());

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }

        if (firstTimeRun()) {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.putBoolean("firsttime", false);
            editor.apply();

            showPreferences();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        runDownloader(0, getString(R.string.url_data_1));
        runDownloader(1, getString(R.string.url_data_2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenuItem = menu.findItem(R.id.refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                runDownloader(0, getString(R.string.url_data_1));
                runDownloader(1, getString(R.string.url_data_2));
                return true;
            case R.id.action_settings:
                showPreferences();
                return true;
            case R.id.action_about:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        int pos = tab.getPosition();
        mViewPager.setCurrentItem(pos);
        android.support.v4.app.Fragment curFragment = mAppSectionsPagerAdapter.getItem(pos);
        if (pos == mAppSectionsPagerAdapter.getLastFragmentIndex()) {
            ((SelectedSectionFragment) curFragment).updateList();
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    private boolean firstTimeRun() {
        boolean res;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        res = prefs.getBoolean("firsttime", true);
        return res;
    }

    private void runDownloader(final int pos, final String url) {
        JsonRequest request1 = new JsonRequest(url, new Response.Listener<List<League>>() {
            @Override
            public void onResponse(List<League> list) {
                try {
                    MainSectionFragment fragment = (MainSectionFragment) mAppSectionsPagerAdapter.getItem(pos);
                    fragment.onUpdate(list);
                    fragment.removeListBackground();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                showLoading(!loadingFinished);
                loadingFinished = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                MainSectionFragment fragment = (MainSectionFragment) mAppSectionsPagerAdapter.getItem(pos);
                fragment.setListBackground();

                showLoading(!loadingFinished);
                loadingFinished = true;
            }
        });

        showLoading(true);
        loadingFinished = false;
        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(request1);
    }

    private void showLoading(boolean b){
        setProgressBarIndeterminateVisibility(b);
        if(mMenuItem != null)
            mMenuItem.setVisible(!b);
    }

    private void showPreferences() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingsActivity.class);
        startActivityForResult(intent, 0);
    }

    private void showAboutDialog(){
        FragmentManager fm = getFragmentManager();
        AboutFragment editNameDialog = new AboutFragment();
        editNameDialog.show(fm, "fragment_about");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyVolley.getInstance(getApplicationContext()).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

}
