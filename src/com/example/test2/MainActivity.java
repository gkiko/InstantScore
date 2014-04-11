package com.example.test2;

import com.newrelic.agent.android.NewRelic;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
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
	    	mAppSectionsPagerAdapter.onSubmit();
	        return true;
	    case R.id.refresh:
	    	mAppSectionsPagerAdapter.onRefresh();
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
		mViewPager.setCurrentItem(tab.getPosition());
		
		int pos = tab.getPosition();
		if(pos == 1){
			SelectedSectionFragment curFragment = (SelectedSectionFragment) mAppSectionsPagerAdapter.getItem(pos);
			curFragment.updateCart();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
}
