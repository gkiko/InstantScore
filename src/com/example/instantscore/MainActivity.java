package com.example.instantscore;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
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

import com.example.instantscore.communication.DataFetcher;
import com.example.instantscore.database.DBManager;
import com.example.instantscore.listener.CallbackListener;
import com.example.instantscore.listener.MyChangeEvent;
import com.newrelic.agent.android.NewRelic;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, CallbackListener {
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;
    DataFetcher fetcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DBManager.init(getApplicationContext());
		
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(), this);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(mAppSectionsPagerAdapter.getCount()-1);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		// For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
        
        if(firstTimeRun()){
        	showPreferences();
        }

        NewRelic.withApplicationToken("AA907c3b86fbc007ff0ecb385c864207f3d89b8715").start(this.getApplication());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.submit:
            ((MainSectionFragment)mAppSectionsPagerAdapter.getItem(0)).submitGames();
			return true;
	    case R.id.refresh:
            fetcher = new DataFetcher(this);
            fetcher.addMyChangeListener(this);
            fetcher.execute(this.getString(R.string.url_get_submit));
	        return true;
	    case R.id.action_settings:
	    	showPreferences();
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
		if(pos == mAppSectionsPagerAdapter.getLastFragmentIndex()){
			((SelectedSectionFragment) curFragment).updateList();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	private boolean firstTimeRun(){
		boolean res;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		res = prefs.getBoolean("firsttime", true);
		
		Editor editor = prefs.edit();
		editor.putBoolean("firsttime", false);
		editor.commit();
		return res;
	}
	
	private void showPreferences(){
		Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingsActivity.class);
        startActivityForResult(intent, 0);
	}
	
    @Override
    public void onUpdate(MyChangeEvent evt) {
        fetcher.removeMyChangeListener(this);
        ((MainSectionFragment)mAppSectionsPagerAdapter.getItem(0)).onUpdate((String)evt.getResult());
    }

    @Override
    public void onException(MyChangeEvent evt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("error");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
