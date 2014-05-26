package com.example.instantscore;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.instantscore.database.DBManager;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragmentList;
	private MainSectionFragment fragmentLive, fragmentComing;
	private SelectedSectionFragment fragmentChosen;
	private transient Context c;
	
	public AppSectionsPagerAdapter(FragmentManager fm, Context c) {
		super(fm);
		this.c = c;

		Bundle b1 = new Bundle();
		b1.putString("url", c.getResources().getString(R.string.url_get_submit));
		Bundle b2 = new Bundle();
		b2.putString("url", c.getResources().getString(R.string.url_get_coming));
		
		fragmentChosen = new SelectedSectionFragment();
		fragmentComing = new MainSectionFragment();
		fragmentComing.setArguments(b2);
		fragmentLive = new MainSectionFragment();
		fragmentLive.setArguments(b1);
		
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fragmentLive);
		fragmentList.add(fragmentComing);
		fragmentList.add(fragmentChosen);
		
		DBManager.init(c);
	}
	
	@Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
    	String[] titles = c.getResources().getStringArray(R.array.titles);
        return titles[position];
    }
    
    public int getLastFragmentIndex(){
    	return fragmentList.indexOf(fragmentChosen);
    }
    
    public void onRefresh(){
    	MainSectionFragment fragment = (MainSectionFragment)fragmentList.get(0);
    	fragment.fetchList();
    }
    
    public void onSubmit(){
    	MainSectionFragment fragment = (MainSectionFragment)fragmentList.get(0);
    	fragment.submitGames();
    }
    
}
